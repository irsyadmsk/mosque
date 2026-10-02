#!/usr/bin/env python3
"""
MasjidKU TV - Localhost Web Remote Server
Menjalankan Web Remote Server secara mandiri di localhost:8080
"""

import http.server
import socketserver
import json
import os
import sys
import hashlib
import urllib.parse
from datetime import datetime

PORT = 8080
BASE_DIR = os.path.dirname(os.path.abspath(__file__))
INDEX_HTML = os.path.join(BASE_DIR, "index.html")
DB_FILE = os.path.join(BASE_DIR, "db.json")
UPLOADS_DIR = os.path.join(BASE_DIR, "uploads")

os.makedirs(UPLOADS_DIR, exist_ok=True)

# Default data matching MasjidDatabase Room seed
DEFAULT_DB = {
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
        "calculationMethod": "KEMENAG",
        "offsetImsak": 0,
        "offsetSubuh": 2,
        "offsetTerbit": -2,
        "offsetDhuha": 2,
        "offsetDzuhur": 2,
        "offsetAshar": 2,
        "offsetMaghrib": 2,
        "offsetIsya": 2,
        "iqomahSubuh": 10,
        "iqomahDzuhur": 10,
        "iqomahJumat": 15,
        "iqomahAshar": 10,
        "iqomahMaghrib": 7,
        "iqomahIsya": 10,
        "idulFitriEnabled": False,
        "idulFitriDate": "",
        "idulFitriTime": "06:30",
        "idulFitriIqomahMinutes": 15,
        "idulFitriSholatMinutes": 20,
        "idulAdhaEnabled": False,
        "idulAdhaDate": "",
        "idulAdhaTime": "06:30",
        "idulAdhaIqomahMinutes": 15,
        "idulAdhaSholatMinutes": 20,
        "tarawihEnabled": False,
        "tarawihAutoDetectNight": True,
        "tarawihManualNight": 1,
        "tarawihShowSlide": True,
        "tarawihKultumMinutes": 15,
        "tarawihSholatMinutes": 45,
        "tarawihTitleText": "JADWAL PETUGAS SHOLAT TARAWIH & KULTUM",
        "tarawihTitleColor": "#FFD700",
        "tarawihOfficerNameColor": "#FFFFFF",
        "tarawihOfficerLabelColor": "#38BDF8",
        "tarawihBgPreset": "PRESET_EMERALD_MIHRAB",
        "customTarawihBgPath": "",
        "sholatDurationMinutes": 10,
        "hijriAdjustmentDays": 0,
        "activeTheme": "EMERALD_GOLD",
        "displayLayoutModel": "CAROUSEL_BOTTOM",
        "carouselIntervalSeconds": 15,
        "soundAlertEnabled": True,
        "showImsak": True,
        "showSubuh": True,
        "showTerbit": True,
        "showDhuha": True,
        "showDzuhur": True,
        "showAshar": True,
        "showMaghrib": True,
        "showIsya": True,
        "showFinancialReport": True,
        "showFridayOfficers": True,
        "showActivities": True,
        "showDailyMaklumat": True,
        "customBackgroundImagePath": "",
        "customBackgroundDim": 0.5,
        "mainScreenBgPreset": "PRESET_EMERALD_MIHRAB",
        "financeBgPreset": "PRESET_WHITE_PEARL_GOLD",
        "fridayOfficerBgPreset": "PRESET_WHITE_PEARL_GOLD",
        "countdownBgPreset": "PRESET_EMERALD_MIHRAB",
        "customFinanceBgPath": "",
        "customFridayOfficerBgPath": "",
        "customCountdownBgPath": "",
        "centerCardTransparency": 0.85,
        "qrisImagePath": "",
        "qrisLabel": "INFAQ & SHODAQOH VIA QRIS",
        "donationProgramTitle": "Renovasi Kubah & Menara Masjid",
        "donationTargetAmount": 150000000,
        "donationCollectedAmount": 67500000,
        "showQrisCard": True,
        "showDailyHadith": True,
        "webAdminPassword": "admin",
        "enablePreAdhanCountdown": True,
        "preAdhanCountdownSeconds": 30,
        "imamSubuh": "Ust. H. Ahmad Dahlan, Lc.",
        "imamDzuhur": "Ust. Ridwan Kamil",
        "imamAshar": "Ust. Dr. Muhammad Iqbal",
        "imamMaghrib": "Ust. H. Abdul Somad, Lc.",
        "imamIsya": "Ust. Farhan Al-Hafizh",
        "muadzinSubuh": "Ustadz Bilal Ramadhan",
        "muadzinDzuhur": "Ustadz Salman Al-Farisi",
        "muadzinAshar": "Ustadz Ahmad Fauzi",
        "muadzinMaghrib": "Ustadz Zaidan Mubarok",
        "muadzinIsya": "Ustadz Wildan Hakim",
        "runningTextFontSize": 26,
        "murottalEnabled": True,
        "murottalSubuh": True,
        "murottalDzuhur": True,
        "murottalAshar": True,
        "murottalMaghrib": True,
        "murottalIsya": True,
        "murottalJumat": True,
        "murottalDurationMinutes": 10,
        "murottalVolume": 0.8,
        "murottalSourceType": "PRESET",
        "murottalSelectedPresetId": "mishary_ar_rahman",
        "murottalCustomAudioPath": "",
        "customLogoImagePath": "",
        "youtubeLiveUrl": "",
        "youtubeLiveEnabled": False,
        "youtubeLiveTitle": "Live Streaming Masjid Endan Andansih",
        "showYoutubeLiveSlide": False,
        "youtubeLiveMuted": True,
        "cctvStreamUrl": "",
        "cctvStreamEnabled": False,
        "cctvStreamTitle": "Kamera Utama Masjid",
        "showCctvSlide": False,
        "cctvStreamMuted": True,
        "liveStreamType": "NONE",
        "cctvCamerasJson": "[]",
        "cctvActiveCameraId": "",
        "cctvAutoRotateEnabled": False,
        "cctvAutoRotateIntervalSeconds": 15,
        "cctvDisplayLayout": "CAROUSEL",
        "cctvAutoPlayAfterPrayer": False,
        "cctvAfterPrayerDurationMinutes": 15,
        "cctvAfterPrayerWaktu": "SUBUH,DZUHUR,ASHAR,MAGHRIB,ISYA",
        "cctvAfterPrayerCameraId": "",
        "cctvScheduleEnabled": False,
        "cctvScheduleStartTime": "",
        "cctvScheduleEndTime": "",
        "cctvScheduleDays": "ALL",
        "cctvScheduleCameraId": "",
        "backgroundType": "DEFAULT",
        "backgroundVideoPath": "",
        "backgroundCctvCameraId": "",
        "fullscreenVideoPath": "",
        "fullscreenVideoTitle": "",
        "fullscreenVideoMuted": False,
        "fullscreenVideoPlaying": False
    },
    "friday": {
        "id": 1,
        "date": "Jum'at Ke-1",
        "hijriDate": "Jum'at Barakah",
        "khotib": "Prof. Dr. KH. Ahmad Syakir, M.A.",
        "khotibPhone": "081234567890",
        "imam": "Ustadz H. M. Firdaus Al-Hafidz",
        "imamPhone": "081234567891",
        "muadzin": "Ustadz Bilal Ramadhan",
        "muadzinPhone": "081234567892",
        "bilal": "Akhi Muhammad Syahril",
        "bilalPhone": "081234567893",
        "khutbahTopic": "Menjaga Keistiqomahan Ibadah & Keikhlasan Hati di Era Digital",
        "notes": "Jadwal Petugas Sholat Jum'at Pekan Pertama"
    },
    "allFridaySchedules": [
        {
            "id": 1,
            "date": "Jum'at Ke-1",
            "hijriDate": "Jum'at Barakah",
            "khotib": "Prof. Dr. KH. Ahmad Syakir, M.A.",
            "khotibPhone": "081234567890",
            "imam": "Ustadz H. M. Firdaus Al-Hafidz",
            "imamPhone": "081234567891",
            "muadzin": "Ustadz Bilal Ramadhan",
            "muadzinPhone": "081234567892",
            "bilal": "Akhi Muhammad Syahril",
            "bilalPhone": "081234567893",
            "khutbahTopic": "Menjaga Keistiqomahan Ibadah & Keikhlasan Hati di Era Digital",
            "notes": "Jadwal Petugas Sholat Jum'at Pekan Pertama"
        },
        {
            "id": 2,
            "date": "Jum'at Ke-2",
            "hijriDate": "Jum'at Barakah",
            "khotib": "Dr. H. Muchlis Muhammad, Lc., M.A.",
            "khotibPhone": "081234567894",
            "imam": "Ustadz Fahmi Idris Al-Hafidz",
            "imamPhone": "081234567895",
            "muadzin": "Ustadz Salman Al-Farisi",
            "muadzinPhone": "081234567896",
            "bilal": "Akhi Rizky Ramadhan",
            "bilalPhone": "081234567897",
            "khutbahTopic": "Membangun Generasi Rabbani Berakhlak Qur'ani",
            "notes": "Jadwal Petugas Sholat Jum'at Pekan Kedua"
        }
    ],
    "finances": [
        {
            "id": 1,
            "title": "Infaq Kotak Amal Jum'at",
            "amount": 14850000,
            "type": "INCOME",
            "category": "Kotak Amal Jum'at",
            "date": "Jum'at Pekan Ini",
            "notes": "Alhamdulillah perolehan infaq"
        },
        {
            "id": 2,
            "title": "Biaya Listrik PLN & Air PDAM",
            "amount": 2350000,
            "type": "EXPENSE",
            "category": "Operasional Masjid",
            "date": "Awal Bulan Ini",
            "notes": "Pembayaran utilitas rutin"
        }
    ],
    "runningTexts": [
        {"id": 1, "text": "Selamat Datang di Masjid Endan Andansih. Harap menonaktifkan nada dering HP selama ibadah berlangsung.", "isActive": True, "orderIndex": 1},
        {"id": 2, "text": "Lurus dan rapatkan shaf, sesungguhnya lurus dan rapatnya shaf merupakan kesempurnaan sholat.", "isActive": True, "orderIndex": 2},
        {"id": 3, "text": "Infaq & Shodaqoh pembangunan dapat disalurkan melalui Rekening BSI 7123-4567-89 a.n DKM Masjid Endan Andansih atau scan QRIS di layar.", "isActive": True, "orderIndex": 3}
    ],
    "activities": [
        {
            "id": 1,
            "title": "Kajian Rutin Ahad Pagi (Tafsir Jalalain)",
            "speaker": "KH. Ahmad Dahlan, M.A.",
            "date": "Setiap Ahad Ba'da Subuh",
            "time": "05:15 - 06:30 WIB",
            "description": "Terbuka untuk jamaah ikhwan & akhwat",
            "location": "Ruang Utama Masjid",
            "isActive": True
        }
    ],
    "imamSchedules": [],
    "mediaSlides": [],
    "tarawihSchedules": [],
    "videos": []
}

def load_db():
    if not os.path.exists(DB_FILE):
        save_db(DEFAULT_DB)
        return DEFAULT_DB
    try:
        with open(DB_FILE, "r", encoding="utf-8") as f:
            return json.load(f)
    except Exception:
        return DEFAULT_DB

def save_db(data):
    with open(DB_FILE, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)

class MasjidRemoteHandler(http.server.BaseHTTPRequestHandler):

    def send_cors_headers(self):
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Auth-Token")

    def do_OPTIONS(self):
        self.send_response(200)
        self.send_cors_headers()
        self.end_headers()

    def send_json(self, status_code, obj):
        body = json.dumps(obj, ensure_ascii=False).encode("utf-8")
        self.send_response(status_code)
        self.send_header("Content-Type", "application/json; charset=UTF-8")
        self.send_header("Content-Length", str(len(body)))
        self.send_cors_headers()
        self.end_headers()
        self.wfile.write(body)

    def do_GET(self):
        parsed = urllib.parse.urlparse(self.path)
        path = parsed.path

        if path in ("/", "/index.html"):
            if not os.path.exists(INDEX_HTML):
                self.send_response(404)
                self.end_headers()
                self.wfile.write(b"index.html not found")
                return
            with open(INDEX_HTML, "rb") as f:
                content = f.read()
            self.send_response(200)
            self.send_header("Content-Type", "text/html; charset=UTF-8")
            self.send_header("Content-Length", str(len(content)))
            self.send_cors_headers()
            self.end_headers()
            self.wfile.write(content)
            return

        if path == "/api/status":
            db = load_db()
            response_data = {
                "config": db.get("config", {}),
                "finances": db.get("finances", []),
                "friday": db.get("friday", {}),
                "allFridaySchedules": db.get("allFridaySchedules", []),
                "activities": db.get("activities", []),
                "runningTexts": db.get("runningTexts", []),
                "mediaSlides": db.get("mediaSlides", []),
                "imamSchedules": db.get("imamSchedules", []),
                "tarawihSchedules": db.get("tarawihSchedules", []),
                "localIpAddress": "127.0.0.1",
                "serverUrl": f"http://localhost:{PORT}"
            }
            self.send_json(200, response_data)
            return

        if path == "/api/check-session":
            self.send_json(200, {"status": "ok", "valid": True})
            return

        if path == "/api/videos":
            db = load_db()
            self.send_json(200, {"status": "ok", "videos": db.get("videos", [])})
            return

        # Static uploads serving
        if path.startswith("/uploads/"):
            filename = os.path.basename(path)
            filepath = os.path.join(UPLOADS_DIR, filename)
            if os.path.exists(filepath):
                with open(filepath, "rb") as f:
                    content = f.read()
                self.send_response(200)
                self.send_header("Content-Type", "application/octet-stream")
                self.send_header("Content-Length", str(len(content)))
                self.send_cors_headers()
                self.end_headers()
                self.wfile.write(content)
                return

        self.send_response(404)
        self.end_headers()

    def do_POST(self):
        parsed = urllib.parse.urlparse(self.path)
        path = parsed.path
        length = int(self.headers.get("Content-Length", 0))
        body_bytes = self.rfile.read(length) if length > 0 else b""
        
        try:
            body = json.loads(body_bytes.decode("utf-8")) if body_bytes else {}
        except Exception:
            body = {}

        db = load_db()

        # 1. Login (Permissive for localhost development)
        if path == "/api/login":
            password = str(body.get("password", "")).strip() or "123456"
            token = hashlib.md5(f"masjidku_secret_{password}".encode("utf-8")).hexdigest()
            self.send_json(200, {
                "status": "ok",
                "token": token,
                "message": "Login berhasil! Selamat datang di Web Remote MasjidKU TV."
            })
            return

        if path == "/api/logout":
            self.send_json(200, {"status": "ok", "message": "Berhasil logout"})
            return

        if path == "/api/change-password":
            new_pw = body.get("newPassword", "")
            if new_pw:
                db["config"]["webAdminPassword"] = new_pw
                save_db(db)
                self.send_json(200, {"status": "ok", "message": "Password admin berhasil diperbarui!"})
            else:
                self.send_json(400, {"status": "error", "message": "Password baru tidak boleh kosong"})
            return

        # 2. Save Config
        if path == "/api/save-config":
            for k, v in body.items():
                db["config"][k] = v
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Pengaturan masjid berhasil disimpan!"})
            return

        # 3. Save Friday Schedule
        if path == "/api/save-friday":
            for k, v in body.items():
                db["friday"][k] = v
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Petugas Jum'at berhasil disimpan!"})
            return

        # 4. Finances CRUD
        if path == "/api/add-finance":
            finances = db.setdefault("finances", [])
            new_id = (max([f["id"] for f in finances] or [0])) + 1
            body["id"] = new_id
            finances.append(body)
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Transaksi keuangan berhasil ditambahkan!"})
            return

        if path == "/api/update-finance":
            fid = body.get("id")
            finances = db.setdefault("finances", [])
            for i, f in enumerate(finances):
                if f.get("id") == fid:
                    finances[i] = body
                    break
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Transaksi keuangan berhasil diperbarui!"})
            return

        if path == "/api/delete-finance":
            fid = body.get("id")
            db["finances"] = [f for f in db.get("finances", []) if f.get("id") != fid]
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Transaksi keuangan berhasil dihapus!"})
            return

        # 5. Running Text CRUD
        if path == "/api/add-running-text":
            texts = db.setdefault("runningTexts", [])
            new_id = (max([t["id"] for t in texts] or [0])) + 1
            body["id"] = new_id
            texts.append(body)
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Teks berjalan berhasil ditambahkan!"})
            return

        if path == "/api/update-running-text":
            tid = body.get("id")
            texts = db.setdefault("runningTexts", [])
            for i, t in enumerate(texts):
                if t.get("id") == tid:
                    texts[i] = body
                    break
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Teks berjalan berhasil diperbarui!"})
            return

        if path == "/api/delete-running-text":
            tid = body.get("id")
            db["runningTexts"] = [t for t in db.get("runningTexts", []) if t.get("id") != tid]
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Teks berjalan berhasil dihapus!"})
            return

        # 6. Activities CRUD
        if path == "/api/add-activity":
            acts = db.setdefault("activities", [])
            new_id = (max([a["id"] for a in acts] or [0])) + 1
            body["id"] = new_id
            acts.append(body)
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Agenda kegiatan berhasil ditambahkan!"})
            return

        if path == "/api/update-activity":
            aid = body.get("id")
            acts = db.setdefault("activities", [])
            for i, a in enumerate(acts):
                if a.get("id") == aid:
                    acts[i] = body
                    break
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Agenda kegiatan berhasil diperbarui!"})
            return

        if path == "/api/delete-activity":
            aid = body.get("id")
            db["activities"] = [a for a in db.get("activities", []) if a.get("id") != aid]
            save_db(db)
            self.send_json(200, {"status": "ok", "message": "Agenda kegiatan berhasil dihapus!"})
            return

        # 7. Remote Action Triggers (Adzan, Iqomah, Sholat, Pre-Adzan, Murottal, Reset)
        if path.startswith("/api/trigger-") or path in (
            "/api/preadhan", "/api/adhan", "/api/iqomah", "/api/sholat",
            "/api/play_murottal", "/api/stop_murottal", "/api/reset",
            "/api/idul_fitri_sim", "/api/idul_adha_sim", "/api/tarawih_sim"
        ):
            action_name = path.replace("/api/trigger-", "").replace("/api/", "")
            print(f"[ACTION TRIGGERED] ==> {action_name.upper()} di layar TV!")
            self.send_json(200, {
                "status": "ok",
                "message": f"Simulasi {action_name.upper()} berhasil dikirim ke layar TV!"
            })
            return

        # 8. Murottal Player Controls
        if path in ("/api/play-murottal", "/api/stop-murottal", "/api/save-murottal", "/api/select-murottal-preset"):
            self.send_json(200, {"status": "ok", "message": "Pengaturan murottal berhasil diproses!"})
            return

        # 9. CCTV & Video Controls
        if path in ("/api/save-cctv-config", "/api/play-fullscreen-cctv", "/api/stop-fullscreen-cctv", "/api/switch-cctv-camera"):
            self.send_json(200, {"status": "ok", "message": "Pengaturan CCTV berhasil diproses!"})
            return

        if path in ("/api/play-fullscreen-video", "/api/stop-fullscreen-video", "/api/set-background-video"):
            self.send_json(200, {"status": "ok", "message": "Perintah video berhasil diproses!"})
            return

        # 10. Fallback OK for any other API
        self.send_json(200, {"status": "ok", "message": "Aksi berhasil diproses!"})

def run_server():
    if sys.platform == "win32":
        try:
            sys.stdout.reconfigure(encoding="utf-8")
        except Exception:
            pass
    socketserver.TCPServer.allow_reuse_address = True
    with socketserver.TCPServer(("", PORT), MasjidRemoteHandler) as httpd:
        print("=" * 70)
        print("[*] MASJIDKU TV - LOCALHOST WEB REMOTE SERVER")
        print("=" * 70)
        print(f" URL Web Remote : http://localhost:{PORT}")
        print(f" Network URL    : http://127.0.0.1:{PORT}")
        print(" Password Admin : admin (atau 123456)")
        print(f" Database Lokal : {DB_FILE}")
        print(f" Template HTML  : {INDEX_HTML}")
        print("=" * 70)
        print(" Server aktif dan siap melayani browser.")
        print(" Silakan buka http://localhost:8080 di browser Anda.")
        print(" Tekan Ctrl+C untuk menghentikan server.")
        print("=" * 70)
        sys.stdout.flush()
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nServer dihentikan.")

if __name__ == "__main__":
    run_server()
