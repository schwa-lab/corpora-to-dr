#!/usr/bin/env python3
# vim: set et nosi ai ts=2 sts=2 sw=2:
# coding: utf-8
import glob
import multiprocessing
import os

from schwa import dr


INPUT_DIR = '/n/schwafs/home/schwa/corpora/raw'
OUTPUT_DIR = '/n/schwafs/home/schwa/corpora/processed'
INPUT_DIR = '/Users/tim/corpora/raw'
OUTPUT_DIR = '/Users/tim/corpora/processed'
CCGBANK = 'CCGbank1.2'

UNESCAPE_TOKEN = {
    '-LCB-': '{',
    '-RCB-': '}',
    '-LRB-': '(',
    '-RRB-': ')',
    '-LSB-': '[',
    '-RSB-': ']',
}
ESCAPE_TOKEN = {v: k for k, v in UNESCAPE_TOKEN.items()}

BROKEN_SENTENCE_CORRECTIONS = {
    'wsj_0595.15': (r'((S[b]\NP)/NP)/', r'(S[b]\\NP)/NP'),
}


class Token(dr.Ann):
  span = dr.Slice()
  raw = dr.Text()
  pos = dr.Text()


class CCGNode(dr.Ann):
  token = dr.Pointer(Token)
  left = dr.SelfPointer()
  right = dr.SelfPointer()
  cat = dr.Field()
  pred_arg_cat = dr.Field()
  head_index = dr.Field()

  def is_leaf(self):
    return self.nchildren() == 0

  def nchildren(self):
    n = 0
    if self.left is not None:
      n = 1
      if self.right is not None:
        n = 2
    return n

  def correct_cat(self, find, replace):
    self.cat = self.cat.replace(find, replace)
    if self.left is not None:
      self.left.correct_cat(find, replace)
      if self.right is not None:
        self.right.correct_cat(find, replace)


class Sentence(dr.Ann):
  id = dr.Field()
  span = dr.Slice(Token)
  root = dr.Pointer(CCGNode)


class Doc(dr.Doc):
  lang = dr.Text()
  name = dr.Text()

  tokens = dr.Store(Token)
  ccgnodes = dr.Store(CCGNode)
  sentences = dr.Store(Sentence)


def find_auto_files():
  return sorted(glob.glob(os.path.join(INPUT_DIR, CCGBANK, 'data', 'AUTO', '*', '*.auto')))


def process_deriv(doc, deriv, bytes_offset, index, parent):
  assert deriv[index] == '(', (deriv, index, deriv[index])
  index += 1
  assert deriv[index] == '<', (deriv, index, deriv[index])
  index += 1
  kind = deriv[index]
  assert kind in 'LT', (deriv, index, deriv[index])
  index += 1

  items = []
  for i in range(3 if kind == 'T' else 5):
    assert deriv[index] == ' ', (deriv, index, deriv[index])
    index += 1
    start_index = index
    while deriv[index] not in ' >':
      index += 1
    items.append(deriv[start_index:index])

  assert deriv[index] == '>', (deriv, index, deriv[index])
  index += 1

  if kind == 'L':
    cat, mod_pos, orig_pos, raw, pred_arg_cat = items

    unescaped_raw = UNESCAPE_TOKEN.get(raw, raw)
    bytes_start = bytes_offset + index
    token = doc.tokens.create(slice=(bytes_start, bytes_start + len(raw)), raw=unescaped_raw, pos=mod_pos)

    node = doc.ccgnodes.create(token=token, cat=cat, pred_arg_cat=pred_arg_cat)
    if parent is None:
      doc.sentences[-1].root = node
    elif parent.left is None:
      parent.left = node
    elif parent.right is None:
      parent.right = node
    else:
      assert False, parent
  else:
    assert deriv[index] == ' ', (deriv, index, deriv[index])
    index += 1

    cat = items[0]
    head_index = int(items[1])
    nchildren = int(items[2])

    node = doc.ccgnodes.create(cat=cat, head_index=head_index)
    if parent is None:
      doc.sentences[-1].root = node
    elif parent.left is None:
      parent.left = node
    elif parent.right is None:
      parent.right = node
    else:
      assert False, parent

    for i in range(nchildren):
      index = process_deriv(doc, deriv, bytes_offset, index, node)

  assert deriv[index] == ')', (deriv, index, deriv[index])
  index += 1
  assert deriv[index] == ' ', (deriv, index, deriv[index])
  index += 1

  return index


def process_auto_file(path):
  with open(path, encoding='ascii') as f:
    lines = f.readlines()
  assert len(lines) % 2 == 0, (path, len(lines))

  doc = Doc(lang='en', name=os.path.basename(path))

  nbytes_read = 0
  for l in range(0, len(lines), 2):
    header = lines[l].strip('\n')
    deriv = lines[l + 1].strip('\n')
    sentence_id = header.split()[0].split('=')[1]

    # Read past the header line.
    nbytes_read += len(header) + 1

    # Recursively process the derivation.
    ntokens_before = len(doc.tokens)
    sentence = doc.sentences.create(id=sentence_id)
    process_deriv(doc, deriv, nbytes_read, 0, None)
    ntokens_after = len(doc.tokens)
    assert sentence.root is not None

    # Correct the sentence's span now that the token objects exist.
    sentence.span = slice(ntokens_before, ntokens_after)

    # Perform any broken category corrections.
    if sentence_id in BROKEN_SENTENCE_CORRECTIONS:
      sentence.root.correct_cat(*BROKEN_SENTENCE_CORRECTIONS[sentence_id])

    # Read past the derivation line.
    nbytes_read += len(deriv) + 1

  # Write out the docrep.
  dir_number = os.path.basename(os.path.dirname(path))
  output_dir = os.path.join(OUTPUT_DIR, CCGBANK, dir_number)
  with open(os.path.join(output_dir, os.path.basename(path).replace('.auto', '.dr')), 'wb') as f:
    writer = dr.Writer(f, Doc)
    writer.write(doc)


def process(ncores):
  # Find the AUTO files.
  paths = find_auto_files()

  # Create the output directories non-parallel.
  for path in paths:
    dir_number = os.path.basename(os.path.dirname(path))
    output_dir = os.path.join(OUTPUT_DIR, CCGBANK, dir_number)
    if not os.path.exists(output_dir):
      os.makedirs(output_dir)

  with multiprocessing.Pool(ncores) as pool:
    for ret in pool.imap_unordered(process_auto_file, paths):
      pass


if __name__ == '__main__':
  import argparse

  parser = argparse.ArgumentParser(description='CCGbank1.2 to docrep')
  parser.add_argument('-i', '--input-dir', default=INPUT_DIR, help='The input corpora/raw directory')
  parser.add_argument('-o', '--output-dir', default=OUTPUT_DIR, help='The output corpora/processed directory')
  parser.add_argument('-j', '--num-cores', default=4, help='The number of cores to utilise')
  args = parser.parse_args()

  INPUT_DIR = args.input_dir
  OUTPUT_DIR = args.output_dir
  find_auto_files()

  process(args.num_cores)
