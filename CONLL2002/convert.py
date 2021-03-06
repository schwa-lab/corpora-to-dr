#!/usr/bin/env python3
# vim: set et nosi ai ts=2 sts=2 sw=2:
# coding: utf-8
import gzip
import io
import os

from schwa import dr


INPUT_DIR = '/n/schwafs/home/schwa/data/raw'
OUTPUT_DIR = '/n/schwafs/home/schwa/data/processed'

FILES = (
    # The Spanish data does not contain document boundaries :(
    # ('CONLL2002/ner/data/esp.train.gz', 'CONLL2002/esp.train.dr', 'es', 'windows-1251', 'iob2'),
    # ('CONLL2002/ner/data/esp.testa.gz', 'CONLL2002/esp.testa.dr', 'es', 'windows-1251', 'iob2'),
    # ('CONLL2002/ner/data/esp.testb.gz', 'CONLL2002/esp.testb.dr', 'es', 'windows-1251', 'iob2'),
    ('CONLL2002/ner/data/ned.train.gz', 'CONLL2002/ned.train.dr', 'nl', 'latin1', 'iob2'),
    ('CONLL2002/ner/data/ned.testa.gz', 'CONLL2002/ned.testa.dr', 'nl', 'latin1', 'iob2'),
    ('CONLL2002/ner/data/ned.testb.gz', 'CONLL2002/ned.testb.dr', 'nl', 'latin1', 'iob2'),
)


class Token(dr.Ann):
  span = dr.Slice()
  raw = dr.Text()
  lemma = dr.Text()
  pos = dr.Text()
  chunk = dr.Text()


class NamedEntity(dr.Ann):
  span = dr.Slice(Token)
  type = dr.Text()


class Sentence(dr.Ann):
  span = dr.Slice(Token)


class Doc(dr.Doc):
  lang = dr.Text()
  path = dr.Text()

  tokens = dr.Store(Token)
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


def convert_to_docrep(fake_docs, in_file, out_file, lang, iob):
  print('Processing {}'.format(in_file))
  with open(os.path.join(OUTPUT_DIR, out_file), 'wb') as f:
    writer = dr.Writer(f, Doc)

    for fake_doc in fake_docs:
      doc = Doc(lang=lang, path=in_file)

      bytes_upto = 0
      for fake_sent in fake_doc:
        ntokens_before = len(doc.tokens)
        for raw, lemma, pos, chunk, ne in fake_sent:
          encoded = raw.encode('utf-8')
          span = slice(bytes_upto, bytes_upto + len(encoded) + 1)
          doc.tokens.create(span=span, raw=raw, lemma=lemma, pos=pos, chunk=chunk, ne=ne)
          bytes_upto += len(encoded) + 1
        ntokens_after = len(doc.tokens)

        sentence = doc.sentences.create(span=slice(ntokens_before, ntokens_after))

        prev_ne_place = prev_ne_type = 'O'
        start_ne = None
        for i, token in enumerate(doc.tokens[sentence.span]):
          if token.ne == 'O':
            token.ne = 'O-O'

          ne_place, ne_type = token.ne.split('-')
          if ne_place == prev_ne_place and ne_type == prev_ne_type and ne_place != 'B':
            pass
          elif ne_type == 'O':
            doc.named_entities.create(span=slice(ntokens_before + start_ne, ntokens_before + i), type=prev_ne_type)
            start_ne = None
          else:
            if prev_ne_type == 'O':
              start_ne = i
            elif prev_ne_type == ne_type:
              if prev_ne_place == 'B' and ne_place == 'I':
                pass
              else:
                doc.named_entities.create(span=slice(ntokens_before + start_ne, ntokens_before + i), type=prev_ne_type)
                start_ne = i
            else:
              doc.named_entities.create(span=slice(ntokens_before + start_ne, ntokens_before + i), type=prev_ne_type)
              start_ne = i

          prev_ne_place, prev_ne_type = ne_place, ne_type

        if start_ne is not None:
          doc.named_entities.create(span=slice(ntokens_before + start_ne, ntokens_before + i + 1), type=prev_ne_type)
      writer.write(doc)


def verify_docrep(in_file, out_file, encoding, iob):
  print('Verifying {} ... '.format(out_file), end='')
  LANGS = {'nl', 'en', 'de'}
  buf = io.StringIO()
  with open(os.path.join(OUTPUT_DIR, out_file), 'rb') as f:
    reader = dr.Reader(f, Doc)
    for doc in reader:
      assert doc.lang in LANGS

      # Extract the entity data.
      entity_data = {}
      for ne in doc.named_entities:
        for i, token in enumerate(doc.tokens[ne.span]):
          entity_data[token] = (i == 0, ne)

      if doc.lang == 'nl':
        cols = ['-DOCSTART-', '-DOCSTART-', 'O']
      elif doc.lang == 'en':
        cols = ['-DOCSTART-', '-X-', 'O', 'O']
        if 'testb' in doc.path:
          cols[-2] = '-X-'
      else:
        cols = ['-DOCSTART-', '-X-', '-X-', '-X-', 'O']
      print(' '.join(cols), file=buf)
      if doc.lang != 'nl':
        print(file=buf)
      for sent in doc.sentences:
        prev_ne_end = -1
        prev_ne_type = None
        for token in doc.tokens[sent.span]:
          if doc.lang == 'nl':
            cols = [token.raw, token.pos, None]
          elif doc.lang == 'en':
            cols = [token.raw, token.pos, token.chunk, None]
          else:
            cols = [token.raw, token.lemma, token.pos, token.chunk, None]
          if token not in entity_data:
            cols[-1] = 'O'
          else:
            is_first, ne = entity_data[token]
            if (is_first or prev_ne_end == ne.span.start) and iob == 'iob2':
              prefix = 'B'
            elif is_first and prev_ne_end == ne.span.start and prev_ne_type == ne.type:
              prefix = 'B'
            else:
              prefix = 'I'
            cols[-1] = prefix + '-' + ne.type
            prev_ne_end = ne.span.stop
            prev_ne_type = ne.type
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

  parser = argparse.ArgumentParser(description='CONLL2002/2003 to docrep')
  parser.add_argument('-v', '--verify', action='store_true', help='Verify the generated docrep files can reproduce the original files.')
  args = parser.parse_args()

  if args.verify:
    for in_file, out_file, lang, encoding, iob in FILES:
      verify_docrep(in_file, out_file, encoding, iob)
  else:
    for in_file, out_file, lang, encoding, iob in FILES:
      fake_docs = process(in_file, encoding)
      convert_to_docrep(fake_docs, in_file, out_file, lang, iob)
