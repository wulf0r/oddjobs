#!/usr/bin/env python3
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
import os

LOG_FILE = Path(os.environ.get("ODDJOBS_DEV_LOG", "/var/log/oddjobs/dev.log"))

class LogHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        LOG_FILE.parent.mkdir(parents=True, exist_ok=True)
        if not LOG_FILE.exists():
            LOG_FILE.write_text("Oddjobs dev log is not available yet.\n", encoding="utf-8")
        content = LOG_FILE.read_bytes()
        self.send_response(200)
        self.send_header("Content-Type", "text/plain; charset=utf-8")
        self.send_header("Cache-Control", "no-store")
        self.send_header("Content-Length", str(len(content)))
        self.end_headers()
        self.wfile.write(content)

    def log_message(self, fmt, *args):
        with LOG_FILE.open("a", encoding="utf-8") as log:
            log.write("[log-server] " + (fmt % args) + "\n")

if __name__ == "__main__":
    port = int(os.environ.get("ODDJOBS_LOG_PORT", "9994"))
    ThreadingHTTPServer(("0.0.0.0", port), LogHandler).serve_forever()
