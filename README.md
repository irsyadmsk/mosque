# MasjidKU TV — by Irsyads

Layar informasi masjid untuk Android TV/STB, dengan jadwal salat offline dan pengaturan melalui HP pada jaringan lokal.

**Credit by Muhammad Irsyad Sirojul Khoeir (irsyads.com)** · [irsyads.com](https://irsyads.com)

## Unduh aplikasi

[Buka GitHub Releases](https://github.com/irsyadmsk/masjidku-downloads/releases) · [Web MasjidKU](https://mosque.irsyads.com/)

Versi terbaru: **1.0.7-beta.1 — versi pengujian**.

[Unduh APK beta untuk perangkat uji](https://github.com/irsyadmsk/masjidku-downloads/releases/download/v1.0.7-beta.1/MasjidKU-TV-v1.0.7-beta.1-debug.apk)

APK ini menggunakan build debug. Belum diuji pada TV/STB nyata dan belum merupakan rilis stabil atau rilis Play Store. APK tidak dapat menimpa instalasi yang ditandatangani dengan kunci berbeda; pertahankan instalasi dan data masjid lama, lalu gunakan perangkat uji.

## Pemasangan

1. Unduh APK dari Releases, lalu pindahkan ke Android TV/STB melalui USB atau cara transfer perangkat Anda.
2. Izinkan instalasi dari sumber yang digunakan untuk membuka APK, lalu pasang pada perangkat uji Android 7 atau lebih baru.
3. Jalankan MasjidKU, buka menu Sambung HP, dan lihat alamat web admin yang ditampilkan TV.
4. Sambungkan HP ke jaringan yang sama, buka `http://IP-TV:8080`, lalu masuk dengan PIN awal `123456`.
5. Ganti PIN awal, atur nama/logo masjid, lokasi, zona waktu, koreksi jadwal, dan alur ibadah. Periksa jadwal bersama pengurus.

## APK dan web

- **APK:** menjalankan layar masjid dan menghitung jadwal secara offline.
- **Web admin lokal:** tertanam di APK; mengatur TV melalui jaringan lokal dengan PIN pengurus.
- **Web publik:** informasi produk, panduan, kredit, dan unduhan. Tidak meminta PIN pengurus atau mengontrol TV dari internet.

Streaming, layanan WhatsApp, peta daring, dan unduhan audio membutuhkan koneksi sesuai layanan. Jadwal dan data lokal tidak memerlukan akun cloud.

Domain publik adalah `mosque.irsyads.com`, menggunakan GitHub Pages dengan CNAME DNS menuju `irsyadmsk.github.io`. Source pengembangan disimpan pada repo privat terpisah.

## Fitur beta

- Lokasi dan zona waktu untuk perhitungan jadwal salat.
- Adzan, iqomah, salat, serta mode fokus khutbah Jumat.
- Sembilan komponen layar yang bisa disembunyikan atau diubah warna, kepekatan, ukuran dan teksnya pada model Papan Jadwal Editable.
- Logo/wallpaper unggahan, kas, petugas, agenda, media dan audio.

[Panduan komponen layar](KOMPONEN_LAYAR.md) · [Panduan alur ibadah](MASJIDKU_IBADAH.md) · [Laporkan masalah](https://github.com/irsyadmsk/masjidku-downloads/issues)

Kredit proyek ini tidak menggantikan lisensi library pihak ketiga atau hak foto milik fotografer.


## Source dan build

Repository ini menyimpan source pengembangan privat. Repo distribusi publik hanya berisi web, panduan dan rilis APK.

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
```

Kotlin / Jetpack Compose / Room schema 24. Identitas paket `com.alijayanet.masjidkutv` dipertahankan agar pembaruan kompatibel. Signed release membutuhkan kunci asli pada `keystore.properties` lokal; jangan commit kunci/password.

- [Editor komponen layar](docs/KOMPONEN_LAYAR.md)
- [Alur ibadah](docs/MASJIDKU_IBADAH.md)
- [Distribusi](docs/DISTRIBUSI.md)
- [Kredit](CREDITS.md)
- [Audit redesign dan batas verifikasi](ANALISIS_REDESIGN.md)

Pratinjau desain lokal: `npm ci` dan `npm run preview:build` pada `web_remote`, lalu `python .redesign-preview/server.py` dari root. Buka `http://127.0.0.1:8091/.redesign-preview/`. Simulasi ini tidak mengirim perubahan ke TV; foto lokal pengguna tidak dibundel ke APK atau repository publik.

## Fondasi web Irsyads v1 — kandidat lokal 2 Oktober 2026

`public-site` memakai hasil ekspor `irsyads/packages/design-tokens/standalone/v1`, adapter `ecosystem.css` dan `ecosystem.js`. Kontrol Tampilan berbagi tema/palet dan tautan kembali mengikuti bahasa pusat. Halaman tetap Indonesia; TV/admin/APK tidak diubah. Sesudah ekspor pusat berubah, salin ulang aset v1. Belum dipublikasikan ke repo distribusi.

## Panduan untuk AI dan pengembang

**Tiga bagian, tiga tempat:**

| Bagian | Lokasi | Catatan |
|---|---|---|
| APK Android TV/STB | `app/` | Kotlin + Jetpack Compose + Room (schema 24) + Media3. Rincian di `app/README.md` |
| Web admin lokal (`http://IP-TV:8080`) | Tertanam di `app/src/main/java/com/example/server/LocalHttpServer.kt` (±9.600 baris, HTML/CSS/JS di dalam string Kotlin) | Diakses pengurus dengan PIN di jaringan lokal |
| Web publik mosque.irsyads.com | `public-site/` → disalin manual ke repo publik `../masjidku-downloads-release` | GitHub Pages; detail di `public-site/README.md` |

**Alat pratinjau (bukan bukti perilaku Android):** `web_remote/` (simulator panel + bundel pratinjau desain), `.redesign-preview/` (prototipe arah desain, port 8091), `server.py` / `run_localhost.py` / `run_server.bat` (simulator lokal port 8080 memakai `web_remote`), `preview_masjidku.html`.

**Aturan:**

- `applicationId` `com.alijayanet.masjidkutv` **tidak boleh diubah** (pembaruan pengguna lama bergantung padanya). `namespace` kode adalah `com.example`; itu memang begitu, jangan "dirapikan".
- Kunci release asli (`masjidku-release-key.jks`) tidak ada di workspace. **Jangan membuat kunci pengganti.** `keystore.properties`, `local.properties`, `*.apk` di-gitignore. APK publik saat ini build debug.
- Versi di `app/build.gradle.kts` (`versionCode`/`versionName`, kini 8 / `1.0.7-beta.1`) dinaikkan hanya untuk rilis yang disetujui.
- Ubah skema Room = naikkan `version` di `MasjidDatabase.kt` dan sediakan migrasi; data pengurus di TV tidak boleh hilang.
- Inti tetap offline: jangan memindahkan data Room ke Supabase atau menambah layanan cloud (keputusan pemilik 2 Okt 2026, lihat `PRODUCT.md`).
- Menurut GitHub repo ini **publik** (per 27 Okt 2026), walau dokumen lama menyebutnya privat. Jangan menaruh kunci, data masjid, atau foto pribadi di sini.
- Branch kerja `codex/approved-service-brand-20261010` dengan perubahan ikon lokal belum di-commit.

**Uji:** `.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest`. Dokumen `ANALISIS_REDESIGN.md`, `PERBAIKAN_*.md`, `PLAYSTORE_*.md`, `RELEASE_NOTES.md` adalah riwayat/rencana; cocokkan dengan kode.
