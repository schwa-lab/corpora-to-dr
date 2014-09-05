#!/usr/bin/env python
# vim: set et nosi ai ts=2 sts=2 sw=2:
# coding: utf-8
from __future__ import absolute_import, print_function, unicode_literals
import multiprocessing
import os
import random
import signal
import time

import MySQLdb
from schwa import dr

from models_dr import Doc
from models_ontonotes import ODocument


DB_DATABASE = 'ontonotes5'
DB_HOST = '129.78.xxx.xx'
DB_USER = 'ontonotes'
DB_PASSWD = 'xxxxxxxxx'

DEFAULT_NPROCESSORS = 16
DEFAULT_OUTPUT_DIR = '/n/ch3/data1/tim/ontonotes-5'


def format_time(seconds):
  m, s = map(int, divmod(seconds, 60))
  return '{:3d}m{:02d}s'.format(m, s)


def get_conn(host=DB_HOST, user=DB_USER, passwd=DB_PASSWD, db=DB_DATABASE):
  return MySQLdb.connect(host=host, user=user, passwd=passwd, db=db, charset='utf8', use_unicode=True)


def split_into_n(seq, n):
  div, mod = divmod(len(seq), n)
  if mod != 0:
    div += 1
  for i in xrange(0, len(seq), div):
    yield seq[i:i + div]


def process_document(output_dir, schema, conn, doc_id, doc_num_upto, doc_num_total):
  print('Processing {:5d}/{}: {}'.format(doc_num_upto, doc_num_total, doc_id))

  # Load the OntoNotes document.
  ontonotes_time_before = time.time()
  odoc = ODocument(conn, doc_id)
  ontonotes_time_after = time.time()

  # Convert it to a docrep document.
  convert_time_before = time.time()
  doc = Doc.from_ontonotes(odoc)
  convert_time_after = time.time()

  # Write it out to disk.
  write_time_before = time.time()
  filename = os.path.basename(doc.doc_id) + '.dr'
  with open(os.path.join(output_dir, doc.lang, filename), 'wb') as f:
    writer = dr.Writer(f, schema)
    writer.write(doc)
  write_time_after = time.time()

  return ontonotes_time_after - ontonotes_time_before, convert_time_after - convert_time_before, write_time_after - write_time_before


def process_document_multiprocessing(args):
  output_dir, doc_ids = args
  signal.signal(signal.SIGINT, signal.SIG_IGN)

  schema = Doc.schema()
  conn = get_conn()
  total_times = [0.0] * 3
  try:
    for i, doc_id in enumerate(doc_ids):
      times = process_document(output_dir, schema, conn, doc_id, i, len(doc_ids))
      for j in xrange(3):
        total_times[j] += times[j]
  finally:
    conn.close()
  return total_times


def main(conn, nprocessors, output_dir):
  cur = conn.cursor()

  # Fetch out the documents we are after.
  cur.execute('SELECT id, lang_id FROM document')
  #cur.execute('SELECT id, lang_id FROM document WHERE id = "wb/a2e/00/a2e_0000@0000@a2e@wb@en@on"')
  #cur.execute('SELECT id, lang_id FROM document WHERE lang_id != "en"')
  #cur.execute('SELECT id, lang_id, genre, source FROM document WHERE id = "bn/abc/00/abc_0011@0011@abc@bn@en@on"')

  # Extract the document ids and the path to write the resultant file to.
  doc_ids, folders = [], set()
  for doc_id, lang in cur:
    doc_ids.append(doc_id)
    folder = os.path.join(output_dir, lang)
    if folder not in folders:
      folders.add(folder)
      if not os.path.exists(folder):
        os.makedirs(folder)
  print('ndocs={}'.format(len(doc_ids)))

  # Randomly shuffle the document ids so that we get a better workload split when multiprocessing.
  random.shuffle(doc_ids)

  # Load the documents, possibly in parallel.
  total_times = [0.0] * 3
  if nprocessors < 2:
    schema = Doc.schema()
    for i, doc_id in enumerate(doc_ids):
      times = process_document(output_dir, schema, conn, doc_id, i, len(doc_ids))
      for j in xrange(3):
        total_times[j] += times[j]
      if i >= 3:
        break
  else:
    args = ((output_dir, ids) for ids in split_into_n(doc_ids, nprocessors))
    pool = multiprocessing.Pool(nprocessors)
    try:
      for times in pool.imap_unordered(process_document_multiprocessing, args):
        for i in xrange(3):
          total_times[i] += times[i]
    except KeyboardInterrupt:
      print('Ctrl-C received. Waiting for pool to terminate...')
      pool.terminate()
      pool.join()

  print(' Documents processed: {}'.format(len(doc_ids)))
  print('   Loading OntoNotes: {}'.format(format_time(times[0])))
  print('Converting to docrep: {}'.format(format_time(times[1])))
  print('      Writing docrep: {}'.format(format_time(times[2])))


if __name__ == '__main__':
  import argparse

  parser = argparse.ArgumentParser(description='OntoNotes 5 to docrep converter')
  parser.add_argument('-n', '--nprocessors', type=int, default=DEFAULT_NPROCESSORS, help='The number of processors to use.')
  parser.add_argument('-o', '--output-dir', type=str, default=DEFAULT_OUTPUT_DIR, help='The output direectory to store the docrep files.')
  args = parser.parse_args()

  conn = get_conn()
  try:
    main(conn, args.nprocessors, args.output_dir)
  finally:
    conn.close()
