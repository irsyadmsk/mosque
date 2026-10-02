import http.server
import socketserver
import os
import sys
import json

PORT = 8080
DIRECTORY = os.path.dirname(os.path.abspath(__file__))

class MasjidHttpHandler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

    def do_GET(self):
        # Route root to preview_masjidku.html
        if self.path in ('/', '/index.html'):
            self.path = '/preview_masjidku.html'
            return super().do_GET()
        
        # Real Status API matching Masjid Endan Andansih, Purwakarta
        if self.path.startswith('/api/status'):
            self.send_response(200)
            self.send_header('Content-type', 'application/json; charset=UTF-8')
            self.send_header('Access-Control-Allow-Origin', '*')
            self.end_headers()
            data = {
                "status": "ok",
                "config": {
                    "mosqueName": "MASJID ENDAN ANDANSIH",
                    "tagline": "Pusat Ibadah, Pembinaan Umat, & Dakwah Islam",
                    "address": "Neglasari, Kec. Darangdan, Kabupaten Purwakarta, Jawa Barat 41163",
                    "city": "Purwakarta, Jawa Barat",
                    "phone": "0812-2214-534",
                    "bankName": "Bank Syariah Indonesia (BSI)",
                    "bankAccount": "7123 4567 89",
                    "bankAccountName": "DKM MASJID ENDAN ANDANSIH",
                    "latitude": -6.6961,
                    "longitude": 107.4239,
                    "timezone": 7.0,
                    "activeTheme": "EMERALD_GOLD",
                    "displayLayoutModel": "STANDARD_KLASIK",
                    "runningText": "Selamat Datang di MASJID ENDAN ANDANSIH, Neglasari, Kec. Darangdan, Kab. Purwakarta. Mohon luruskan dan rapatkan shaf sholat demi kesempurnaan sholat berjamaah."
                }
            }
            self.wfile.write(json.dumps(data).encode('utf-8'))
            return
            
        return super().do_GET()

    def do_POST(self):
        self.send_response(200)
        self.send_header('Content-type', 'application/json; charset=UTF-8')
        self.send_header('Access-Control-Allow-Origin', '*')
        self.end_headers()
        res = {"status": "ok", "message": "Berhasil disimpan untuk Masjid Endan Andansih!"}
        self.wfile.write(json.dumps(res).encode('utf-8'))

print(f"MasjidKU TV Server — MASJID ENDAN ANDANSIH (Purwakarta)")
print(f"Serving at http://localhost:{PORT}/")
with socketserver.TCPServer(("", PORT), MasjidHttpHandler) as httpd:
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
