#!/usr/bin/env python3
# -*- coding: utf-8 -*-
# vim: set ts=4 sw=4 et ai:

"""
Model for WARC data.
"""

from schwa import dr

ENC = 'utf-8'

chunk_on_tokens = dr.decorators.reverse_slices('chunks', 'tokens', 'span', 'chunk')
tokens_on_chunks = dr.decorators.materialize_slices('chunks', 'tokens', 'span', 'tokens')
sent_on_tokens = dr.decorators.reverse_slices('sents', 'tokens', 'span', 'sent')
tokens_on_sents = dr.decorators.materialize_slices('sents', 'tokens', 'span', 'tokens')
prev_next_tokens = dr.decorators.add_prev_next('tokens', index_attr='index')

@dr.requires_decoration(chunk_on_tokens, tokens_on_chunks, sent_on_tokens, tokens_on_sents, prev_next_tokens)
def decorate(doc):
    return doc

@dr.requires_decoration(chunk_on_tokens, tokens_on_chunks, sent_on_tokens, tokens_on_sents, prev_next_tokens)
def decorate_sent(doc):
    return doc


class Token(dr.Ann):
    raw = dr.Text(help='Raw string form')
    norm = dr.Text(help='Normalised string form')
    pos = dr.Field(help='Part-of-speech')
    span = dr.Slice()
    lemma = dr.Field()

    def __unicode__(self):
        return self.norm

    def __str__(self):
        return unicode(self).encode(ENC)

    def iter_prev(self, n, same_sentence=True):
        return reversed(list(self._iter_context(n, same_sentence, 'prev')))

    def iter_next(self, n, same_sentence=True):
        return self._iter_context(n, same_sentence, 'next')

    def str(self):
      return self.norm or self.raw or ''

class Sentence(dr.Ann):
    span = dr.Slice(Token)

class Facc(dr.Ann):
    entity = dr.Text()
    freebase = dr.Text()
    posterior_mention = dr.Text(help='Posterior of Freebase entity given mention and context')
    posterior_context = dr.Text(help='Posterior of Freebase entity given context')
    span = dr.Slice(Token)

class Chunk(dr.Ann):
    tag = dr.Field()
    span = dr.Slice(Token)

class Paragraph(dr.Ann):
    span = dr.Slice(Sentence)

class Doc(dr.Doc):
    tokens = dr.Store(Token)
    chunks = dr.Store(Chunk)
    sents = dr.Store(Sentence)
    paras = dr.Store(Paragraph)
    facc = dr.Store(Facc)

    # WARC fields
    # Sample: {'Content-Length': '22103', 'WARC-Date': '2009-03-65T08:45:51-0800', 'WARC-Warcinfo-ID': '993d3969-9643-4934-b1c6-68d4dbe55b83', 'WARC-Identified-Payload-Type': '', 'WARC-Type': 'response', '_status': 'WARC/0.18', 'WARC-Target-URI': 'http://acne-care-today.com/ketchup/', 'Content-Type': 'application/http;msgtype=response', 'WARC-TREC-ID': 'clueweb09-en0000-00-02622', 'WARC-Record-ID': '<urn:uuid:0b556f31-4027-4174-b8ad-46a4819e4d83>'}
    # WARC_Date = dr.Text()
    # WARC_Warcinfo_ID = dr.Text()
    WARC_Target_URI = dr.Text()
    WARC_TREC_ID = dr.Text()
    # WARC_Record_ID = dr.Text()
    enc = dr.Text()

    def __str__(self):
        return '<Doc %s>' % self.doc_id
