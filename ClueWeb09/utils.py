import gzip

ENC = 'UTF-8'
BACKUP = 'iso-8859-1'
FMT_HEADER = b'WARC/0.18\n'
LENGTH_HEADER = 'Content-Length: '
LENGTH_LENGTH_HEADER = len(LENGTH_HEADER)

def iter_warc(fname):
    total_bytes = 0
    _open = open

    if fname.endswith(".gz"):
        _open = gzip.open

    with _open(fname, "rb") as f:
        while True:
            start_bytes = total_bytes
            headers = {}
            len_header = 0

            head = f.readline()
            len_header += len(head)
            while head != FMT_HEADER:
                if not head:
                    raise StopIteration
                if head.decode(ENC).strip():
                    raise ValueError(head)
                head = f.readline()
                len_header += len(head)
            length = None
            line = f.readline()
            while line != b'\n' or length is None:
                len_header += len(line)
                if not line:
                    raise StopIteration
                try:
                    _line = line.decode(ENC)
                except UnicodeDecodeError:
                    _line = line.decode(BACKUP)
                if _line.startswith(LENGTH_HEADER):
                    length = int(_line[LENGTH_LENGTH_HEADER:-1])
                if ": " in _line:
                    key, val = _line.split(": ", 1)
                    headers[key] = val.strip()
                elif _line.startswith("HTTP"):
                    raise ValueError("Incomplete WARC headers")
                line = f.readline()

            len_header += len(line)
            total_bytes += len_header + length
            payload = f.read(length)

            while len(payload) < length:
                payload += f.read(length - len(payload))
            yield headers, payload, len_header, start_bytes

