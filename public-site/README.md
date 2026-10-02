# MasjidKU TV — by Irsyads

Layar informasi masjid untuk Android TV/STB, dengan jadwal salat offline dan pengaturan melalui HP pada jaringan lokal.

**Credit by Muhammad Irsyad Sirojul Khoeir (irsyads.com)** · [irsyads.com](https://irsyads.com)

## Unduh aplikasi

[Buka GitHub Releases](https://github.com/irsyadmsk/masjidku-downloads/releases) · [Web MasjidKU](https://irsyadmsk.github.io/masjidku-downloads/)

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

Domain yang direncanakan adalah `mosque.irsyads.com`; alamat sementara menggunakan GitHub Pages. Source pengembangan disimpan pada repo privat terpisah.

## Fitur beta

- Lokasi dan zona waktu untuk perhitungan jadwal salat.
- Adzan, iqomah, salat, serta mode fokus khutbah Jumat.
- Sembilan komponen layar yang bisa disembunyikan atau diubah warna, kepekatan, ukuran dan teksnya pada model Papan Jadwal Editable.
- Logo/wallpaper unggahan, kas, petugas, agenda, media dan audio.

[Panduan komponen layar](KOMPONEN_LAYAR.md) · [Panduan alur ibadah](MASJIDKU_IBADAH.md) · [Laporkan masalah](https://github.com/irsyadmsk/masjidku-downloads/issues)

Kredit proyek ini tidak menggantikan lisensi library pihak ketiga atau hak foto milik fotografer.
