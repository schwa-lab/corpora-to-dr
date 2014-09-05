#!/usr/bin/env python
# vim: set et nosi ai ts=2 sts=2 sw=2:
# coding: utf-8
from __future__ import absolute_import, print_function, unicode_literals
import codecs
import collections
import multiprocessing
import os
import random
import re
import signal

from schwa import dr

from models_dr import Doc

DEFAULT_NPROCESSORS = 16
DEFAULT_ONTONOTES_FILES_DIR = '/n/schwafs/home/schwa/corpora/raw/OntoNotes5/ontonotes-release-5.0/data/files/data/'

LANG_TO_FOLDER = {
    'ar': 'arabic',
    'ch': 'chinese',
    'en': 'english',
}
TOKEN_ESCAPES = {
    '(': '-LRB-',
    ')': '-RRB-',
    '{': '-LCB-',
    '}': '-RCB-',
    '[': '-LSB-',
    ']': '-RSB-',
    '<': '-LAB-',
    '>': '-RAB-',
    '&': '-AMP-',
}

SKIP_VERIFY_COREF = (
    'bc/cnn/00/cnn_0006@0006@cnn@bc@en@on',  # Mention of "73" doesn't match what's in the DB.
    'bc/msnbc/00/msnbc_0003@0003@msnbc@bc@en@on',  # A whole bunch of mentions don't match what's in the DB.
    'bc/phoenix/00/phoenix_0003@0003@phoenix@bc@en@on',  # Mention of "76" doesn't match what's in the DB.
    'bn/cnn/03/cnn_0377@0377@cnn@bn@en@on',  # Mention of "22" doesn't match waht's in the DB.
    'bn/cnn/03/cnn_0319@0319@cnn@bn@en@on',  # Mention of "25" doesn't match what's in the DB.
    'bn/cnn/03/cnn_0329@0329@cnn@bn@en@on',  # Mention of "46" doesn't match what's in the DB.
    'mz/sinorama/10/ectb_1011@1011@sinorama@mz@en@on',  # Mention of "46" doesn't match what's in the DB.
    'nw/ann/00/ann_0010@0010@ann@nw@ar@on',  # Mention of "117" doesn't match what's in the DB.
    'nw/ann/00/ann_0038@0038@ann@nw@ar@on',  # Order of nested mentions isn't sorted.
    'nw/ann/00/ann_0063@0063@ann@nw@ar@on',  # Order of nested mentions isn't sorted.
    'nw/ann/00/ann_0069@0069@ann@nw@ar@on',  # Mention of "103" doesn't match what's in the DB.
    'nw/ann/01/ann_0120@0120@ann@nw@ar@on',  # Mention of "26" doesn't match what's in the DB.
    'nw/ann/01/ann_0183@0183@ann@nw@ar@on',  # Mention of "31330" doesn't match what's in the DB.
    'nw/ann/02/ann_0295@0295@ann@nw@ar@on',  # Mention of "72872" doesn't match what's in the DB.
    'nw/ann/03/ann_0318@0318@ann@nw@ar@on',  # Mention of "2686" doesn't match what's in the DB.
    'nw/wsj/13/wsj_1312@1312@wsj@nw@en@on',  # None of the coref mentions in sentence 12 are in the DB.
    'nw/wsj/24/wsj_2402@2402@wsj@nw@en@on',  # Order of nested mentions isn't sorted.
)
SKIP_VERIFY_NAME = (
    'tc/ch/00/ch_0011@0011@ch@tc@en@on',  # What's in the files looks wrong and is different to what's in the DB.
    'tc/ch/00/ch_0021@0021@ch@tc@en@on',  # There are a number of entities in the file that are not in the DB.
)
SKIP_VERIFY_PARSE = (
    'tc/ch/00/ch_0011@0011@ch@tc@en@on',
    'tc/ch/00/ch_0021@0021@ch@tc@en@on',
)
SKIP_VERIFY_PROP = (
    'bn/p2.5_c2e/00/p2.5_c2e_0001@0001@p2.5_c2e@bn@en@on',
)

# Global constants setup at init.
ONTONOTES_DATA_DIR = None
NPROCESSORS = None

# Define our decorators.
reverse_chain_onto_mention = dr.decorators.reverse_pointers('coref_chains', 'coref_mentions', 'mentions', 'chain', mark_outside=True)
reverse_mentions_onto_token = dr.decorators.reverse_slices('coref_mentions', 'tokens', 'span', 'mentions', mark_outside=True, mutex=False)
reverse_nes_onto_token = dr.decorators.reverse_slices('named_entities', 'tokens', 'span', 'nes', mark_outside=True, mutex=False)
reverse_parse_onto_token = dr.decorators.reverse_pointers('parse_nodes', 'tokens', 'token', 'parse', mark_outside=True)
DECORATORS = (
    reverse_chain_onto_mention,
    reverse_mentions_onto_token,
    reverse_nes_onto_token,
    reverse_parse_onto_token,
)


class Taggable(object):
  """
  A class to facilitate the generated of the SGML version of a document, with nested SGML tags
  around tokens. This wrapper handles the nesting output order.
  """
  __slots__ = ('doc_id', 'ignore_traces', 'lines', 'token_index')

  def __init__(self, doc, ignore_traces, sentence_filter=None):
    self.doc_id = doc.doc_id
    self.ignore_traces = ignore_traces
    self.lines = []
    self.token_index = {}
    i = 0
    for sentence in doc.sentences:
      if sentence_filter is not None and not sentence_filter(sentence):
        continue
      self.lines.append([])
      j = 0
      for token in doc.tokens[sentence.span]:
        if not (token.parse.is_trace() and ignore_traces):
          place = ([], token.raw, [])
          self.lines[-1].append(place)
          self.token_index[token] = (i, j)
          j += 1
      i += 1

  def add_tag(self, tokens, open_tag, close_tag):
    # Find the start and end token in the range.
    length = len(tokens)
    for t in xrange(length):
      start_token = tokens[t]
      if start_token.parse.is_trace() and self.ignore_traces:
        continue
      break
    for t in xrange(length - 1, -1, -1):
      end_token = tokens[t]
      if end_token.parse.is_trace() and self.ignore_traces:
        continue
      break
    # Insert the start and end tags in the appropriate places.
    i, j = self.token_index[start_token]
    self.lines[i][j][0].append((length, open_tag))
    i, j = self.token_index[end_token]
    self.lines[i][j][2].append((length, close_tag))

  def to_sgml(self):
    sgml = ['<DOC DOCNO="{}">'.format(self.doc_id)]
    for line in self.lines:
      out = []
      for prefix, raw, suffix in line:
        prefix.sort(key=lambda t: (-t[0], t[1]))
        prefix = ''.join(x[1] for x in prefix)
        suffix.sort()
        suffix = ''.join(x[1] for x in suffix)
        out.append(prefix + raw + suffix)
      sgml.append(' '.join(out))
    sgml.append('</DOC>')
    return sgml


def hack_escape_characters(data):
  for unescaped, escaped in TOKEN_ESCAPES.iteritems():
    data = data.replace(escaped, unescaped)
  return data


def save_tmp_files(expected, actual, suffix):
  with codecs.open('/tmp/expected' + suffix, 'w', encoding='utf-8') as f:
    f.write(expected)
  with codecs.open('/tmp/actual' + suffix, 'w', encoding='utf-8') as f:
    f.write(actual)


# =============================================================================
# .coref
# =============================================================================
RE_COREF_COREF_OPEN = re.compile(r'<COREF ID="([^"]+)"[^>]*>')
RE_COREF_COREF_OPEN_CLOSE = re.compile(r'<[/]?COREF[^>]*>')
RE_COREF_DOC_OPEN_CLOSE = re.compile(r'<[/]?DOC')
RE_COREF_TEXT_OPEN = re.compile(r'<TEXT PARTNO="(\d+)">')


class CorefExpectedSub(object):
  __slots__ = ('chains', 'nopen', 'section')

  def __init__(self, chains):
    self.chains = chains
    self.nopen = 0
    self.section = None

  def __call__(self, m):
    text = m.group(0)
    m = RE_COREF_COREF_OPEN.match(text)
    if m is None:
      if self.nopen == 0:
        return ''
      else:
        self.nopen -= 1
        return text
    else:
      ident = m.group(1)
      if self.chains[self.section][ident] == 1:
        return ''
      else:
        self.nopen += 1
        return m.group(0)


def open_coref_tag(mention):
  chain = mention.chain
  attrs = ['COREF', 'ID="{}"'.format(chain.id), 'TYPE="{}"'.format(chain.type)]
  if mention.type != chain.type:
    attrs.append('SUBTYPE="{}"'.format(mention.type))
  if mention.start_offset:
    attrs.append('S_OFF="{}"'.format(mention.start_offset))
  if mention.end_offset:
    attrs.append('E_OFF="{}"'.format(mention.end_offset))
  if chain.speaker:
    attrs.append('SPEAKER="{}"'.format(chain.speaker.name))
  return '<{}>'.format(' '.join(attrs))


def close_coref_tag(mention):
  return '</COREF>'


def verify_coref(doc, path_prefix):
  # Ensure the expected output file exists, and if it doesn't, assert that this document doesn't have any coref chains or mentions.
  SUFFIX = '.coref'
  path = path_prefix + SUFFIX
  if not os.path.exists(path):
    assert len(doc.coref_chains) == 0, (doc.id, path, len(doc.coref_chains))
    assert len(doc.coref_mentions) == 0, (doc.id, path, len(doc.coref_mentions))
    return True

  # Should we skip this verification because the expected file is broken?
  if doc.doc_id in SKIP_VERIFY_COREF:
    return True

  # Generate the actual file.
  if doc.coref_chains:
    sections = collections.defaultdict(list)
    for chain in doc.coref_chains:
      sections[chain.section].append(chain)
    actual, last_line = [], None
    for section in xrange(max(sections) + 1):
      chains = sections[section]
      taggable = Taggable(doc, ignore_traces=False, sentence_filter=lambda s: s.parse.coref_section == section)
      for chain in chains:
        for mention in chain.mentions:
          taggable.add_tag(doc.tokens[mention.span], open_coref_tag(mention), close_coref_tag(mention))
      sgml = taggable.to_sgml()
      if not actual:
        actual.append(sgml[0])
        last_line = sgml[-1]
      sgml[0] = '<TEXT PARTNO="{:03d}">'.format(section)
      sgml[-1] = '</TEXT>'
      actual += sgml
    actual.append(last_line)
  else:
    taggable = Taggable(doc, ignore_traces=False)
    actual = taggable.to_sgml()
    actual.insert(1, '<TEXT PARTNO="000">')
    actual.insert(-1, '</TEXT>')
  actual = hack_escape_characters('\n'.join(actual))

  # Read in the expected file.
  with codecs.open(path, 'rU', encoding='utf-8') as f:
    expected = f.read().strip().split('\n')

  # Process the expected file to work out which of the expected chains have been removed in the DB import process.
  sections = {}
  chains = section = None
  for i, line in enumerate(expected):
    if doc.doc_id == 'nw/wsj/13/wsj_1312@1312@wsj@nw@en@on' and i == 14:
      continue
    m = RE_COREF_TEXT_OPEN.match(line)
    if m is not None:
      if section is not None:
        sections[section] = chains
      chains = {}
      section = int(m.group(1))
    elif RE_COREF_DOC_OPEN_CLOSE.match(line) or line == '</TEXT>':
      pass
    else:
      for ident in RE_COREF_COREF_OPEN.findall(line):
        chains[ident] = chains.get(ident, 0) + 1
  sections[section] = chains

  sub = CorefExpectedSub(sections)
  new_expected = []
  for i, line in enumerate(expected):
    m = RE_COREF_TEXT_OPEN.match(line)
    if m is not None:
      sub.section = int(m.group(1))
    elif RE_COREF_DOC_OPEN_CLOSE.match(line) or line == '</TEXT>':
      pass
    elif doc.doc_id == 'nw/wsj/13/wsj_1312@1312@wsj@nw@en@on' and i == 14:
     pass
    else:
      line = RE_COREF_COREF_OPEN_CLOSE.sub(sub, line)
    new_expected.append(line)

  expected = hack_escape_characters('\n'.join(new_expected))

  # Manually fix broken files.
  if doc.doc_id == 'wb/c2e/00/c2e_0015@0015@c2e@wb@en@on':
    expected = expected.replace(' -RSB ', ' ] ')
  elif doc.doc_id == 'tc/ch/00/ch_0035@0035@ch@tc@en@on':
    expected = expected.replace('[background_noise_', '')

  # If they differ, write them out for comparison.
  match = actual == expected
  if not match:
    save_tmp_files(expected, actual, SUFFIX)
  return match


# =============================================================================
# .name
# =============================================================================
RE_EMBEDDED_TAG = re.compile(r'(<(?:ENA|TI|NU)MEX TYPE="[^"]+">)([^<]*)(<(?:ENA|TI|NU)MEX TYPE="[^"]*?">)([^<]*)(</(?:ENA|TI|NU)MEX>)([^<]*)(</(?:ENA|TI|NU)MEX>)')


def open_enamex_tag(ne):
  attrs = ['ENAMEX', 'TYPE="{}"'.format(ne.tag)]
  if ne.start_offset:
    attrs.append('S_OFF="{}"'.format(ne.start_offset))
  if ne.end_offset:
    attrs.append('E_OFF="{}"'.format(ne.end_offset))
  return '<{}>'.format(' '.join(attrs))


def close_enamex_tag(ne):
  return '</ENAMEX>'


def verify_name(doc, path_prefix):
  # Ensure the expected output file exists, and if it doesn't, assert that this document doesn't have any NEs.
  SUFFIX = '.name'
  path = path_prefix + SUFFIX
  if not os.path.exists(path):
    assert len(doc.named_entities) == 0, (doc.id, path, len(doc.named_entities))
    return True

  # Skip entirely broken files.
  if doc.doc_id in SKIP_VERIFY_NAME:
    return True

  # Generate the actual file.
  taggable = Taggable(doc, ignore_traces=True)
  for ne in doc.named_entities:
    taggable.add_tag(doc.tokens[ne.span], open_enamex_tag(ne), close_enamex_tag(ne))
  actual = hack_escape_characters('\n'.join(taggable.to_sgml()))

  # Read in the expected file, removing embedded NEs as per the OntoNotes API.
  with codecs.open(path, 'rU', encoding='utf-8') as f:
    expected = hack_escape_characters(f.read().strip())
  #print(RE_EMBEDDED_TAG.search(expected))
  expected = RE_EMBEDDED_TAG.sub(r'\1\2\4\6\7', expected)

  # Manually fix broken files.
  if doc.doc_id == 'bn/cnn/04/cnn_0425@0425@cnn@bn@en@on':
    expected = expected.replace(r'<ENAMEX TYPE="PERSON"><ENAMEX TYPE="PERSON" E_OFF="1">Paula</ENAMEX> Zahn</ENAMEX>', r'<ENAMEX TYPE="PERSON" E_OFF="1">Paula</ENAMEX> Zahn')  # Embedded removal of a slightly different form.
  elif doc.doc_id == 'tc/ch/00/ch_0005@0005@ch@tc@en@on':
    expected = expected.replace(' drain</ENAMEX> ', '</ENAMEX> drain ')
  elif doc.doc_id == 'tc/ch/00/ch_0035@0035@ch@tc@en@on':
    expected = expected.replace('[background_noise_', '')
  elif doc.doc_id == 'wb/c2e/00/c2e_0015@0015@c2e@wb@en@on':
    expected = expected.replace(' -RSB ', ' ] ')

  # If they differ, write them out for comparison.
  match = actual == expected
  if not match:
    save_tmp_files(expected, actual, SUFFIX)
  return match


# =============================================================================
# .parse
# =============================================================================
def write_parse(lines, node, line_num=0, child_num=0, prefix=''):
  if child_num != 0:
    lines[line_num] += prefix
  if node.is_leaf():
    lines[line_num] += '({} {})'.format(node.tag, node.token.raw)
  else:
    lines[line_num] += '({} '.format(node.tag)
    prefix = ' ' * len(lines[line_num])
    for i, child in enumerate(node.children):
      if i != 0:
        line_num += 1
      line_num = write_parse(lines, child, line_num, i, prefix)
    lines[line_num] += ')'
  return line_num


def verify_parse(doc, path_prefix):
  SUFFIX = '.parse'
  path = path_prefix + SUFFIX
  assert os.path.exists(path)

  if doc.doc_id in SKIP_VERIFY_PARSE:
    return True

  # Generate the actual parse file contents.
  actual = []
  for sentence in doc.sentences:
    lines = [''] * sentence.parse.get_width()
    write_parse(lines, sentence.parse)
    actual += lines
    actual.append('')
  actual = '\n'.join(actual).strip()

  # Read in the expected file.
  with codecs.open(path, 'rU', encoding='utf-8') as f:
    expected = f.read().strip()

  # If they differ, write them out for comparison.
  match = actual == expected
  if not match:
    save_tmp_files(expected, actual, SUFFIX)
  return match


# =============================================================================
# .prop
# =============================================================================
RE_STRIP_SLC = re.compile(r':\d+-LINK-SLC')


def verify_prop(doc, path_prefix):
  SUFFIX = '.prop'
  path = path_prefix + SUFFIX
  if not os.path.exists(path):
    assert len(doc.propositions) == 0, ('Missing .prop file', path, len(doc.propositions))
    return True

  if doc.doc_id in SKIP_VERIFY_PROP:
    return True

  # Generate the actual prop file contents.
  seen = set()
  actual = []
  for sentence_index, sentence in enumerate(doc.sentences):
    leaf_to_index = {token: i for i, token in enumerate(doc.tokens[sentence.span])}
    ich_trace_ids = sentence.parse.get_ich_trace_ids()
    for proposition in sentence.propositions:
      token_index = leaf_to_index[proposition.leaf.token]
      seen.add((sentence_index, token_index))
      line = [doc.doc_id, unicode(sentence_index), unicode(token_index), proposition.quality]
      line.append(proposition.lemma + '-' + proposition.type)
      line.append(proposition.lemma + '.' + proposition.pb_sense_num)
      line.append('-----')
      buf = [part.encoded for part in proposition.pred_parts]
      line.append(','.join(buf) + '-rel')
      for groups_attr, group_attr in (('arg_groups', 'args'), ('link_groups', 'links')):
        for group in getattr(proposition, groups_attr):
          buf = []
          for arg in getattr(group, group_attr):
            local = [arg.parts[0].encoded]
            for i in xrange(len(arg.parts) - 1):
              a = arg.parts[i + 0].node.has_ich_in_subtree() or arg.parts[i + 0].node.has_trace_id(ich_trace_ids)
              b = arg.parts[i + 1].node.has_ich_in_subtree() or arg.parts[i + 1].node.has_trace_id(ich_trace_ids)
              local.append(';' if a or b else ',')
              local.append(arg.parts[i + 1].encoded)
            buf.append(''.join(local))
          line.append('*'.join(buf) + '-' + group.type)
      actual.append(' '.join(line))
  actual = '\n'.join(actual).strip()

  # Read in the expected file.
  with codecs.open(path, 'rU', encoding='utf-8') as f:
    expected = f.read().strip().split('\n')

  # The Arabic newswire data hasn't put a whole lot of the propositions into the DB. Remove them from the expected lines.
  if doc.doc_id.endswith('@ar@on'):
    i = 0
    while i != len(expected):
      parts = expected[i].split()
      key = tuple(map(int, parts[1:3]))
      if key in seen:
        i += 1
      else:
        del expected[i]
  expected = '\n'.join(expected)

  # It is impossible to know what the original SLC indices were due to the import transformations.
  actual = RE_STRIP_SLC.sub('', actual)
  expected = RE_STRIP_SLC.sub('', expected)

  # If they differ, write them out for comparison.
  match = actual == expected
  if not match:
    save_tmp_files(expected, actual, SUFFIX)
  return match


# =============================================================================
# .speaker
# =============================================================================
def verify_speaker(doc, path_prefix):
  SUFFIX = '.speaker'
  path = path_prefix + SUFFIX
  if not os.path.exists(path):
    return True
  elif doc.doc_id.startswith('pt/nt/'):
    return True  # The times here appear to be versus, which do not appear to have been transferred into the DB.

  # Generate the actual speaker file contents.
  actual, has_speakers = [], False
  for sentence in doc.sentences:
    if sentence.start_time is not None or sentence.end_time is not None:
      has_speakers = True
      line = []
      line.append(str(sentence.start_time))
      line.append(str(sentence.end_time))
      line.append(','.join(s.name for s in sentence.speakers))
      values = tuple(set(s.gender for s in sentence.speakers))
      if len(values) == 1:
        line.append(values[0])
      else:
        line.append(','.join(s.gender for s in sentence.speakers))
      values = tuple(set(s.competence for s in sentence.speakers))
      if len(values) == 1:
        line.append(values[0])
      else:
        line.append(','.join(s.competence for s in sentence.speakers))
      actual.append(' '.join(line))
    else:
      actual.append('-')
  if has_speakers:
    while actual[-1] == '-':
      actual.pop()
  actual = '\n'.join(actual).strip()

  # Read in the expected file.
  with codecs.open(path, 'rU', encoding='utf-8') as f:
    expected = f.read().strip().replace('\t', ' ')

  # If they differ, write them out for comparison.
  match = actual == expected
  if not match:
    save_tmp_files(expected, actual, SUFFIX)
  return match


# =============================================================================
# Main
# =============================================================================
VERIFY_LAYERS = (
    ('coref', verify_coref),
    ('name', verify_name),
    ('parse', verify_parse),
    ('prop', verify_prop),
    #('speaker', verify_speaker),
)


def process_docrep_file(schema, docrep_file):
  print(docrep_file)
  # Open the docrep file and setup a reader.
  with open(docrep_file, 'rb') as f:
    # For each document in the docrep file...
    reader = dr.Reader(f, schema)
    for doc in reader:
      # Apply the decorators.
      for d in DECORATORS:
        d(doc)

      # Work out where the ontonotes data for this document is stored.
      data_dir = os.path.join(ONTONOTES_DATA_DIR, LANG_TO_FOLDER[doc.lang], 'annotations', os.path.dirname(doc.doc_id))
      assert os.path.exists(data_dir), ('OntoNotes data directory exists', doc.doc_id, data_dir)
      file_prefix = os.path.basename(doc.doc_id).split('@')[0]
      path_prefix = os.path.join(data_dir, file_prefix)

      # Verify each of the annotation layer files.
      for layer, fn in VERIFY_LAYERS:
        if not fn(doc, path_prefix):
          return False, docrep_file, doc.doc_id, layer

  return True, None, None, None


def process_docrep_file_multiprocess(docrep_file):
  signal.signal(signal.SIGINT, signal.SIG_IGN)
  schema = Doc.schema()
  return process_docrep_file(schema, docrep_file)


def main(docrep_files):
  if NPROCESSORS < 2:
    schema = Doc.schema()
    for docrep_file in docrep_files:
      success, _, doc_id, layer = process_docrep_file(schema, docrep_file)
      if not success:
        print('Verification of "{}" failed for "{}" ({})'.format(layer, doc_id, docrep_file))
        break
  else:
    # Randomly shuffle the document ids so that we get a better workload split when multiprocessing.
    random.shuffle(docrep_files)

    # Split the docrep files between each of the processors.
    pool = multiprocessing.Pool(NPROCESSORS)
    try:
      for success, docrep_file, doc_id, layer in pool.imap_unordered(process_docrep_file_multiprocess, docrep_files):
        if not success:
          print('Verification of "{}" failed for "{}" ({})'.format(layer, doc_id, docrep_file))
          break
      if not success:
        pool.terminate()
        pool.join()
    except KeyboardInterrupt:
      print('Ctrl-C received. Waiting for pool to terminate...')
      pool.terminate()
      pool.join()


if __name__ == '__main__':
  import argparse

  parser = argparse.ArgumentParser(description='OntoNotes 5 to docrep converter')
  parser.add_argument('-d', '--data-dir', type=str, default=DEFAULT_ONTONOTES_FILES_DIR, help='The OntoNotes 5 files data directory.')
  parser.add_argument('-i', '--input-file', type=str, help='An input docrep file to process')
  parser.add_argument('-n', '--nprocessors', type=int, default=DEFAULT_NPROCESSORS, help='The number of processors to use.')
  parser.add_argument('input_files', metavar='docrep-file', type=str, nargs='*')
  args = parser.parse_args()

  NPROCESSORS = args.nprocessors
  ONTONOTES_DATA_DIR = args.data_dir

  if args.input_files:
    docrep_files = args.input_files
  elif args.input_file:
    docrep_files = [args.input_file]
  else:
    docrep_files = ['/dev/stdin']

  main(docrep_files)
