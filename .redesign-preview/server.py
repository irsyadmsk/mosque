"""Serve only the design preview and bundled wallpaper assets on loopback."""
from http.server import BaseHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.parse import urlsplit
import mimetypes
import json
from io import BytesIO
from functools import lru_cache
from PIL import Image, ImageOps
from photos import PHOTO_FILES, catalogue


@lru_cache(maxsize=30)
def photo_bytes(key, thumbnail, modified):
    """Resize for delivery in memory. Originals and their metadata remain untouched."""
    with Image.open(PHOTO_FILES[key]) as image:
        # JPEG draft avoids decoding the largest original at full resolution.
        image.draft("RGB", (320, 480) if thumbnail else (1920, 2400))
        image = ImageOps.exif_transpose(image)
        image.thumbnail((320, 480) if thumbnail else (1920, 2400))
        output = BytesIO()
        image.convert("RGB").save(output, format="JPEG", quality=80 if thumbnail else 88)
        return output.getvalue()

PREVIEW = Path(__file__).resolve().parent
DRAWABLE = PREVIEW.parent / "app/src/main/res/drawable"
FILES = {
    "/.redesign-preview/": PREVIEW / "index.html",
    "/.redesign-preview/index.html": PREVIEW / "index.html",
    "/.redesign-preview/style.css": PREVIEW / "style.css",
    "/.redesign-preview/preview.js": PREVIEW / "preview.js",
}
for image in DRAWABLE.glob("bg_preset_*.jpg"):
    FILES[f"/app/src/main/res/drawable/{image.name}"] = image


class PreviewHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        path = urlsplit(self.path).path
        if path == "/.redesign-preview/photos.json":
            self.send_data(json.dumps(catalogue(), ensure_ascii=False).encode("utf-8"), "application/json; charset=utf-8")
            return
        for prefix, thumbnail in [("/.redesign-preview/photos/", False), ("/.redesign-preview/thumbnails/", True)]:
            if path.startswith(prefix):
                key = path[len(prefix):].removesuffix(".jpg")
                photo = PHOTO_FILES.get(key)
                if photo is None or not photo.is_file():
                    self.send_error(404)
                    return
                try:
                    self.send_data(photo_bytes(key, thumbnail, photo.stat().st_mtime_ns), "image/jpeg")
                except (OSError, ValueError):
                    self.send_error(503, "Photo cannot be loaded")
                return
        if path == "/":
            self.send_response(302)
            self.send_header("Location", "/.redesign-preview/")
            self.end_headers()
            return
        file = FILES.get(path)
        if file is None or not file.is_file():
            self.send_error(404)
            return
        data = file.read_bytes()
        self.send_data(data, mimetypes.guess_type(file.name)[0] or "application/octet-stream")

    def send_data(self, data, content_type):
        self.send_response(200)
        self.send_header("Content-Type", content_type)
        self.send_header("Content-Length", str(len(data)))
        self.send_header("X-Content-Type-Options", "nosniff")
        self.send_header("Cache-Control", "no-cache")
        self.end_headers()
        self.wfile.write(data)


if __name__ == "__main__":
    print("MasjidKU preview: http://127.0.0.1:8091/.redesign-preview/", flush=True)
    ThreadingHTTPServer(("127.0.0.1", 8091), PreviewHandler).serve_forever()
