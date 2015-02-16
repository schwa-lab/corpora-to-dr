#!/usr/bin/env python3
# vim: set et nosi ai ts=2 sts=2 sw=2:
# coding: utf-8
from __future__ import print_function, unicode_literals
import gzip
import io
import os

from schwa import dr


INPUT_DIR = '/n/schwafs/home/schwa/data/raw'
OUTPUT_DIR = '/n/schwafs/home/schwa/data/processed'

FILES = (
    ('CONLL2003/ner/eng.train', 'CONLL2003/eng.train.dr', 'en', 'ascii'),
    ('CONLL2003/ner/eng.testa', 'CONLL2003/eng.testa.dr', 'en', 'ascii'),
    ('CONLL2003/ner/eng.testb', 'CONLL2003/eng.testb.dr', 'en', 'ascii'),
    ('CONLL2003/ner/deu.train', 'CONLL2003/deu.train.dr', 'de', 'latin1'),
    ('CONLL2003/ner/deu.testa', 'CONLL2003/deu.testa.dr', 'de', 'latin1'),
    ('CONLL2003/ner/deu.testb', 'CONLL2003/deu.testb.dr', 'de', 'latin1'),
)


class Token(dr.Ann):
  span = dr.Slice()
  raw = dr.Text()
  lemma = dr.Text()
  pos = dr.Text()


class Chunk(dr.Ann):
  span = dr.Slice(Token)
  label = dr.Text()


class NamedEntity(dr.Ann):
  span = dr.Slice(Token)
  label = dr.Text()


class Sentence(dr.Ann):
  span = dr.Slice(Token)


class Doc(dr.Doc):
  doc_id = dr.Text()
  lang = dr.Text()

  tokens = dr.Store(Token)
  chunks = dr.Store(Chunk)
  named_entities = dr.Store(NamedEntity)
  sentences = dr.Store(Sentence)


def process(in_file, encoding):
  if in_file.endswith('.gz'):
    with gzip.open(os.path.join(INPUT_DIR, in_file)) as f:
      encoded = f.read()
  else:
    with open(os.path.join(INPUT_DIR, in_file), 'rb') as f:
      encoded = f.read()

  raw = encoded.decode(encoding)
  docs = []
  sentences = []
  sentence = []

  for line in raw.split('\n'):
    raw = lemma = pos = chunk = ne = None
    cols = line.split(' ')
    if not line:
      if sentence:
        sentences.append(sentence)
        sentence = []
      continue
    elif cols[0] == '-DOCSTART-':
      if sentences:
        docs.append(sentences)
        sentences = []
      continue

    if len(cols) == 3:
      raw, pos, ne = cols
    elif len(cols) == 4:
      raw, pos, chunk, ne = cols
    elif len(cols) == 5:
      raw, lemma, pos, chunk, ne = cols
    else:
      raise ValueError('Unknown number of columns: {}'.format(cols))
    sentence.append((raw, lemma, pos, chunk, ne))

  if sentences:
    docs.append(sentences)
    sentences = []
  assert len(sentence) == 0, sentence
  assert len(sentences) == 0, sentences
  assert len(docs) > 0

  return docs


def decode_iob1(doc, sentence, fake_sentence, fake_sentence_label_index, span_label_attr, span_store_attr):
  start_token_index = sentence.span.start
  start_span_i = None
  prev_span_place = prev_span_label = 'O'

  def create_slice(i):
    store = getattr(doc, span_store_attr)
    obj = store.create(span=slice(start_token_index + start_span_i, start_token_index + i))
    setattr(obj, span_label_attr, prev_span_label)

  for i, token in enumerate(doc.tokens[sentence.span]):
    orig_label = fake_sentence[i][fake_sentence_label_index]
    if orig_label == 'O':
      span_place = span_label = 'O'
    else:
      span_place, span_label = orig_label.split('-')

    if span_place == prev_span_place and span_label == prev_span_label and span_place != 'B':
      pass
    elif span_label == 'O':
      create_slice(i)
      start_span_i = None
    else:
      if prev_span_label == 'O':
        start_span_i = i
      elif prev_span_label == span_label:
        if prev_span_place == 'B' and span_place == 'I':
          pass
        else:
          create_slice(i)
          start_span_i = i
      else:
        create_slice(i)
        start_span_i = i

    prev_span_place, prev_span_label = span_place, span_label

  if start_span_i is not None:
    create_slice(i + 1)


def encode_iob1(doc, encoded_label_attr, span_store_attr, span_label_attr):
  sentence_starters = set()
  for sentence in doc.sentences:
    sentence_starters.add(sentence.span.start)

  encodings = {}

  store = getattr(doc, span_store_attr)
  objs = sorted(store, key=lambda obj: obj.span)
  for obj in objs:
    for i, index in enumerate(range(obj.span.start, obj.span.stop)):
      label = getattr(obj, span_label_attr)
      prev_encoding = encodings.get(index - 1)
      if i == 0 and index not in sentence_starters and prev_encoding is not None and prev_encoding[1] == label:
        encodings[index] = ('B', label)
      else:
        encodings[index] = ('I', label)

  for i, token in enumerate(doc.tokens):
    encoding = encodings.get(i)
    if encoding is None:
      encoding = 'O'
    else:
      encoding = encoding[0] + '-' + encoding[1]
    setattr(token, encoded_label_attr, encoding)


def convert_to_docrep(fake_docs, in_file, out_file, lang):
  print('Processing {}'.format(in_file))
  with open(os.path.join(OUTPUT_DIR, out_file), 'wb') as f:
    writer = dr.Writer(f, Doc)

    for fake_doc in fake_docs:
      doc = Doc(lang=lang, doc_id=os.path.basename(in_file))

      bytes_upto = 0
      for fake_sentence in fake_doc:
        ntokens_before = len(doc.tokens)
        for raw, lemma, pos, chunk, ne in fake_sentence:
          encoded = raw.encode('utf-8')
          span = slice(bytes_upto, bytes_upto + len(encoded) + 1)
          doc.tokens.create(span=span, raw=raw, lemma=lemma, pos=pos)
          bytes_upto += len(encoded) + 1
        ntokens_after = len(doc.tokens)

        sentence = doc.sentences.create(span=slice(ntokens_before, ntokens_after))
        decode_iob1(doc, sentence, fake_sentence, 3, 'label', 'chunks')
        decode_iob1(doc, sentence, fake_sentence, 4, 'label', 'named_entities')

      writer.write(doc)


def verify_docrep(in_file, out_file, encoding):
  print('Verifying {} ... '.format(out_file), end='')
  LANGS = {'en', 'de'}
  buf = io.StringIO()
  with open(os.path.join(OUTPUT_DIR, out_file), 'rb') as f:
    reader = dr.Reader(f, Doc)
    for doc in reader:
      assert doc.lang in LANGS

      if doc.lang == 'en':
        cols = ['-DOCSTART-', '-X-', 'O', 'O']
        if 'testb' in doc.doc_id:
          cols[-2] = '-X-'
      else:
        cols = ['-DOCSTART-', '-X-', '-X-', '-X-', 'O']
      print(' '.join(cols), file=buf)
      print(file=buf)

      encode_iob1(doc, 'chunk', 'chunks', 'label')
      encode_iob1(doc, 'ne', 'named_entities', 'label')

      for sent in doc.sentences:
        for token in doc.tokens[sent.span]:
          if doc.lang == 'en':
            cols = [token.raw, token.pos, token.chunk, token.ne]
          else:
            cols = [token.raw, token.lemma, token.pos, token.chunk, token.ne]
          print(' '.join(cols), file=buf)
        print(file=buf)
  actual = buf.getvalue()

  if in_file.endswith('.gz'):
    with gzip.open(os.path.join(INPUT_DIR, in_file)) as f:
      expected = f.read()
  else:
    with open(os.path.join(INPUT_DIR, in_file), 'rb') as f:
      expected = f.read()
  expected = expected.decode(encoding)

  actual = actual.encode('utf-8')
  expected = expected.encode('utf-8')
  if actual != expected:
    prefix = '/tmp/' + in_file.split('/')[-1].replace('.gz', '')
    with open(prefix + '.expected', 'wb') as f:
      f.write(expected)
    with open(prefix + '.actual', 'wb') as f:
      f.write(actual)
    print('Failed. Files written to {}.{{expected,actual}}'.format(prefix))
  else:
    print('Success.')


if __name__ == '__main__':
  import argparse

  parser = argparse.ArgumentParser(description='CoNLL2003 to docrep')
  parser.add_argument('-i', '--input-dir', default=INPUT_DIR, help='Input raw corpora directory.')
  parser.add_argument('-o', '--output-dir', default=OUTPUT_DIR, help='Output processed corpora directory.')
  parser.add_argument('-v', '--verify', action='store_true', help='Verify the generated docrep files can reproduce the original files.')
  args = parser.parse_args()

  INPUT_DIR = args.input_dir
  OUTPUT_DIR = args.output_dir

  if args.verify:
    for in_file, out_file, lang, encoding in FILES:
      verify_docrep(in_file, out_file, encoding)
  else:
    for in_file, out_file, lang, encoding in FILES:
      fake_docs = process(in_file, encoding)
      convert_to_docrep(fake_docs, in_file, out_file, lang)
