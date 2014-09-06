#!/usr/bin/env python3
# vim: set et nosi ai ts=2 sts=2 sw=2:
# coding: utf-8
import io
import os
import re

from schwa import dr


INPUT_DIR = '/n/schwafs/home/schwa/data/raw'
OUTPUT_DIR = '/n/schwafs/home/schwa/data/processed'

PTB3 = 'PTB3'
FILES = (
    ('CONLL2000/train', 15, 18),
    ('CONLL2000/test', 20, 20),
)

UNESCAPE_TOKEN = {
    '-LCB-': '{',
    '-RCB-': '}',
    '-LRB-': '(',
    '-RRB-': ')',
}
ESCAPE_TOKEN = {v: k for k, v in UNESCAPE_TOKEN.items()}

RE_MRG_LEAF = re.compile(r'\(([^ ]+) ([^ )]+)\)')


class Token(dr.Ann):
  span = dr.Slice()
  raw = dr.Text()
  pos = dr.Text()


class Chunk(dr.Ann):
  span = dr.Slice(Token)
  type = dr.Text()


class Sentence(dr.Ann):
  span = dr.Slice(Token)


class Doc(dr.Doc):
  lang = dr.Text()
  name = dr.Text()

  tokens = dr.Store(Token)
  chunks = dr.Store(Chunk)
  sentences = dr.Store(Sentence)


def load_ptb_doc(path, section, file):
  path = os.path.join(path, file)
  doc = Doc(lang='en', name=file[:-len('.mrg')])
  nbytes = 0
  in_sent = False
  ntokens_before = ntokens_after = None
  with open(path, encoding='ascii') as f:
    for l, line in enumerate(f):
      line = line.rstrip()
      if not line:
        continue
      if line[0] == '(':
        if in_sent:
          ntokens_after = len(doc.tokens)
          assert ntokens_before is not None
          assert ntokens_after > ntokens_before
          doc.sentences.create(span=slice(ntokens_before, ntokens_after))
        in_sent = True
        ntokens_before = len(doc.tokens)
      for pos, token in RE_MRG_LEAF.findall(line):
        if pos == '-NONE-':
          continue
        token = UNESCAPE_TOKEN.get(token, token)
        doc.tokens.create(span=slice(nbytes, nbytes + len(token)), raw=token)
        nbytes += len(token) + 1
  if in_sent:
    ntokens_after = len(doc.tokens)
    assert ntokens_before is not None
    assert ntokens_after > ntokens_before
    doc.sentences.create(span=slice(ntokens_before, ntokens_after))
  return doc


def load_ptb_sections(section_start, section_end):
  for section in range(section_start, section_end + 1):
    path = os.path.join(INPUT_DIR, PTB3, 'parsed', 'mrg', 'wsj', str(section))
    for file in os.listdir(path):
      if file.endswith('.mrg'):
        doc = load_ptb_doc(path, section, file)
        yield doc


def process(path, section_start, section_end):
  with open(os.path.join(INPUT_DIR, path + '.txt'), encoding='ascii') as f:
    raw_lines = f.read().split('\n')

  docs = load_ptb_sections(section_start, section_end)

  with open(os.path.join(OUTPUT_DIR, path + '.dr'), 'wb') as f:
    writer = dr.Writer(f, Doc)

    raw_upto = 0
    for doc in docs:
      for sent in doc.sentences:
        chunk_start = chunk_end = prev_kind = None
        for i, token in enumerate(doc.tokens[sent.span]):
          line = raw_lines[raw_upto]
          raw, pos, chunk = line.split(' ')
          raw = UNESCAPE_TOKEN.get(raw, raw)
          assert raw == token.raw, (raw, token.raw)
          token.pos = pos

          if chunk == 'O':
            prefix = kind = 'O'
          else:
            prefix, kind = chunk.split('-')

          if kind != prev_kind or prefix == 'B':
            if prev_kind is not None and prev_kind != 'O':
              chunk_end = sent.span.start + i
              assert chunk_start is not None
              assert chunk_end > chunk_start
              doc.chunks.create(span=slice(chunk_start, chunk_end), type=prev_kind)
            prev_kind = kind
            if kind == 'O':
              chunk_start = None
            else:
              chunk_start = sent.span.start + i

          raw_upto += 1

        if prev_kind is not None and prev_kind != 'O':
          chunk_end = sent.span.stop
          assert chunk_start is not None
          assert chunk_end > chunk_start
          doc.chunks.create(span=slice(chunk_start, chunk_end), type=prev_kind)

        line = raw_lines[raw_upto]
        assert line == '', line
        raw_upto += 1

      writer.write(doc)

  if raw_upto != len(raw_lines):
    if raw_lines[raw_upto] == '':
      raw_upto += 1
  assert raw_upto == len(raw_lines)


def verify(file):
  print('Verifying', file)
  buf = io.StringIO()
  with open(os.path.join(OUTPUT_DIR, file + '.dr'), 'rb') as f:
    reader = dr.Reader(f, Doc)
    for doc in reader:
      for chunk in doc.chunks:
        for i, token in enumerate(doc.tokens[chunk.span]):
          token.chunk_tag = ('B' if i == 0 else 'I') + '-' + chunk.type

      for sent in doc.sentences:
        for token in doc.tokens[sent.span]:
          raw = ESCAPE_TOKEN.get(token.raw, token.raw)
          pos = token.pos
          chunk = getattr(token, 'chunk_tag', 'O')
          print(raw, pos, chunk, file=buf)
        print(file=buf)
  actual = buf.getvalue()

  with open(os.path.join(INPUT_DIR, file + '.txt'), encoding='ascii') as f:
    expected = f.read()

  if actual != expected:
    prefix = '/tmp/' + file.split('/')[-1]
    with open(prefix + '.expected', 'w') as f:
      f.write(expected)
    with open(prefix + '.actual', 'w') as f:
      f.write(actual)
    print('Failed. Files written to {}.{{expected,actual}}'.format(prefix))
  else:
    print('Success.')


if __name__ == '__main__':
  import argparse

  parser = argparse.ArgumentParser(description='CONLL2002/2003 to docrep')
  parser.add_argument('-i', '--input-dir', default=INPUT_DIR, help='The input corpora/raw directory')
  parser.add_argument('-o', '--output-dir', default=OUTPUT_DIR, help='The output corpora/processed directory')
  parser.add_argument('-v', '--verify', action='store_true', help='Verify the generated docrep files can reproduce the original files.')
  args = parser.parse_args()

  INPUT_DIR = args.input_dir
  OUTPUT_DIR = args.output_dir

  if args.verify:
    for file, _, _ in FILES:
      verify(file)
  else:
    for file, section_start, section_end in FILES:
      process(file, section_start, section_end)
