#!/usr/bin/env python3
# vim: set et nosi ai ts=2 sts=2 sw=2:
# coding: utf-8

"""Tools for processing W/ARC format used in ClueWeb09 and archive.org

Optionally applies FACC1 annotations"""

import logging

from model import Doc
from utils import iter_warc, ENC, BACKUP
from schwa.dr import Writer

from schwa import tokenizer as tok


tokenizer = tok.Tokenizer()
logging.basicConfig(level=logging.INFO, format='%(levelname)s - %(asctime)s: %(message)s')
log = logging.getLogger(__file__)

def matches(raw, annotation):
    if raw == annotation or raw.startswith(annotation) \
            or annotation.startswith(raw) or \
            annotation.startswith(raw.replace(".", "")):
        return True
    _raw = raw.lower().replace('&amp;', '&')
    _annotation = annotation.lower()
    return _raw == _annotation or _raw.startswith(_annotation) \
            or _annotation.startswith(_raw) or \
            _annotation.startswith(_raw.replace(".", ""))

def iter_annotations(fname):
    with open(fname, "r") as f:
        annotations = []
        prev_id = None
        for line in f:
            id, enc, token, start, end, posterior_mention, posterior_context, freebase = line.strip().split("\t")
            if prev_id is None or prev_id and prev_id == id:
                annotations.append({
                    "id": id,
                    "enc": enc,
                    "token": token,
                    "start": int(start),
                    "end": int(end),
                    "posterior_mention": posterior_mention,
                    "posterior_context": posterior_context,
                    "freebase": freebase,
                })
            else:
                annotations.sort(key=lambda x: x["start"])
                yield prev_id, annotations
                annotations = []
            prev_id = id
    if annotations:
        annotations.sort(key=lambda x: x["start"])
        yield prev_id, annotations

def process(fname, annotations, debug=None):
    if annotations is not None:
        anns_iter = iter_annotations(annotations)
        id, anns = next(anns_iter)
    else:
        id, anns = None, None

    for headers, payload, len_header, start_bytes in iter_warc(fname):
        if headers["WARC-Type"] == 'warcinfo':
            continue
        if headers.get("WARC-TREC-ID", "") == id:
            doc = convert(headers, payload, len_header, start_bytes, anns, debug)
            try:
                id, anns = next(anns_iter)
            except StopIteration:
                print("End of anns", file=sys.stderr)
                id = None
        else:
            doc = convert(headers, payload, len_header, start_bytes, None, debug)
        yield doc


def convert(headers, payload, len_header, start_bytes, annotations=None, debug=None):
    try:
        http_headers, content = payload.split(b'\n\n', 1)
        start_offset = start_bytes + len_header + 2 # for newlines
    except ValueError:
        http_headers, content = payload, b''
        start_offset = start_bytes + len_header

    len_http = len(http_headers) + 2 # 2 newlines
    start_offset += len_http
    nanns = 0

    # paragraph hack
    content = content.replace(b'<P>', b'<p>').replace(b'</P>', b'</p>')
    _enc = ENC
    if annotations:
        _enc = annotations[0]["enc"]
        if _enc != ENC:
            #deal with stupid encoding conversions for non-UTF-8 text
            #e.g. dumb quotes
            try:
                content = content.decode(_enc, errors='replace').encode(ENC)
            except:
                log.error("Could not convert {} from {} to UTF-8".format(headers.get('WARC-TREC-ID'), _enc))
                raise

    doc = Doc(WARC_Target_URI=headers['WARC-Target-URI'],
              WARC_TREC_ID=headers['WARC-TREC-ID'], enc=_enc)

    if content:
        ntokens = 0
        nsents = 0
        freebase_start = freebase_end = None
        prev_raw = ""
        for paragraph in tokenizer.tokenize(content, dest=list):
            paragraph_start = nsents
            for sent in paragraph:
                sent_start = ntokens
                for offset, encoded, norm in sent:
                    ntokens += 1
                    if not norm:
                        norm = encoded

                    try:
                      norm = norm.decode(ENC)
                    except UnicodeDecodeError:
                        try:
                            norm = norm.decode(BACKUP)
                        except UnicodeDecodeError:
                            sys.stderr.write("norm fail: {}\n".format(encoded, norm))
                        norm = ","

                    try:
                        raw = encoded.decode(_enc)
                    except UnicodeDecodeError:
                        try:
                            raw = encoded.decode(BACKUP)
                        except UnicodeDecodeError:
                            sys.stderr.write("raw fail: {}\n".format(encoded, norm))
                        raw = norm

                    if annotations is not None and nanns < len(annotations):
                        current_ann = annotations[nanns]
                        offset_start = offset + len_http
                        offset_end = offset + len_http + len(raw)
                        # print(offset_start, offset_end, current_ann["start"], current_ann["end"], file=sys.stderr)
                        if abs(offset_start - current_ann["start"]) <= 1:
                            if matches(raw, current_ann["token"]):
                                # account for off by one tokenisation errors
                                freebase_start = ntokens
                                if debug is not None and abs(offset_start - current_ann["start"]) != 0:
                                    print("off_by_one", headers['WARC-TREC-ID'], prev_raw, raw, current_ann["token"], offset_start, current_ann["start"], file=debug, sep="\t")
                            elif debug is not None and offset_start == current_ann["start"]:
                                print("mismatch", headers['WARC-TREC-ID'], prev_raw, raw, current_ann["token"], offset_start, current_ann["start"], file=debug, sep="\t")

                        if offset_end == current_ann["end"]:
                            freebase_end = ntokens + 1
                            # print(offset_start, offset_end, raw, freebase_start, freebase_end, file=sys.stderr)
                            if freebase_start is None:
                                # assume tokenisation doesn't match
                                # assume this is a single token entity
                                if debug is not None:
                                    print("missed_start", headers['WARC-TREC-ID'], prev_raw, raw, current_ann["token"], offset_end, current_ann["end"], file=debug, sep="\t")
                                    # print(raw, current_ann["token"], offset_start, current_ann["start"], file=sys.stderr)
                                freebase_start = ntokens
                            assert freebase_start != freebase_end
                            doc.facc.create(span=slice(freebase_start,
                                            freebase_end),
                                            entity=current_ann["token"],
                                            freebase=current_ann["freebase"],
                                            posterior_mention=current_ann["posterior_mention"],
                                            posterior_context=current_ann["posterior_context"])
                            nanns += 1
                            freebase_start = freebase_end = None
                        elif freebase_start is None:
                            while offset_start > annotations[nanns]["start"] and nanns < len(annotations) - 1:
                                if debug is not None:
                                    print("missed_ann", headers['WARC-TREC-ID'], prev_raw, raw, current_ann["token"], offset_start, current_ann["start"], file=debug, sep="\t")
                                    # print(headers['WARC-TREC-ID'], offset_start, ntokens, raw, annotations[nanns]["start"], annotations[nanns]["token"], file=sys.stderr)
                                nanns += 1
                            freebase_start = freebase_end = None

                    _offset = start_offset + offset
                    span = slice(_offset, _offset+len(raw) + 1)
                    if raw == norm:
                        token = doc.tokens.create(span=span, raw=raw)
                    else:
                        token = doc.tokens.create(span=span, norm=norm, raw=raw)
                    ntokens += 1
                    prev_raw = raw
                sentence = doc.sents.create(span=slice(sent_start, ntokens))
                nsents += 1
            paragraph = doc.paras.create(span=slice(paragraph_start, nsents))
    return doc

if __name__ == '__main__':
    # grep-like over HTTP response
    import argparse
    import sys
    sys.path.append('.')
    ap = argparse.ArgumentParser(description="Reads an input warc file, and converts it to docrep on stdout")
    ap.add_argument('input_warc')
    ap.add_argument('-a', '--annotations', default=None, help='FACC annotation file')
    ap.add_argument('-d', '--debug', default=None, help='Print debug log information')
    args = ap.parse_args()
    sys.stdout = sys.stdout.detach() # make stdout binary (for py3)
    writer = Writer(sys.stdout, Doc)
    log.info("Processing {}".format(args.input_warc))
    debug = None
    if args.debug is not None:
        debug = open(args.debug, "a")
    try:
        for doc in process(args.input_warc, args.annotations, debug):
            writer.write(doc)
    except Exception as e:
        print(str(e), file=sys.stderr)
        sys.exit(1)
    log.info("Finished processing {}".format(args.input_warc))
