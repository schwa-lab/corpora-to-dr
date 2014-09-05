import sys
from utils import iter_warc

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("Usage: {0} <warc_file> <clueweb_id>".format(sys.argv[0]), file=sys.stderr)
        sys.exit(1)
    target = sys.argv[2]
    for headers, payload, len_headers, start_bytes in iter_warc(sys.argv[1]):
        if headers.get("WARC-TREC-ID") != target:
            continue
        break
