# Distribusi MasjidKU TV

Credit by Muhammad Irsyad Sirojul Khoeir (irsyads.com)

## Tiga bagian produk

- **APK Android TV/STB:** aplikasi yang menjalankan layar dan menghitung jadwal secara offline.
- **Web admin lokal:** tertanam dalam APK; buka `http://IP-TV:8080` dari HP di jaringan yang sama, lalu masuk menggunakan PIN pengurus.
- **Web publik:** informasi produk, panduan pemasangan, kredit dan tautan APK di GitHub Releases. Tidak meminta PIN atau menyimpan data masjid.

Repo source `irsyadmsk/mosque` tetap privat. Repo `irsyadmsk/masjidku-downloads` menyimpan halaman web, panduan, dan rilis APK yang dapat diakses umum. GitHub Pages digunakan untuk web publik. Alamat publik: `https://mosque.irsyads.com/`. Custom domain GitHub Pages dan CNAME Cloudflare (`mosque` menuju `irsyadmsk.github.io`, DNS only, TTL Auto) dikonfigurasi pada 2 Oktober 2026.

## Status domain — 2 Oktober 2026

Domain sudah aktif dan diverifikasi pada 2 Oktober 2026. DNS CNAME terbaca; sertifikat GitHub Pages disetujui untuk `mosque.irsyads.com` (berlaku sampai 31 Desember 2026); Enforce HTTPS aktif. Halaman dan CSS merespons HTTP 200 melalui HTTPS dengan validasi sertifikat; HTTP dialihkan 301 ke HTTPS. Tampilan diperiksa di browser dan unduhan APK diverifikasi terhadap SHA-256 rilis. Nama aplikasi/repo tidak berubah dan APK tidak perlu dibangun ulang untuk perubahan domain ini.

## Rilis 1.0.7-beta.1

Rilis pengujian, bukan rilis stabil atau publikasi Play Store. Berisi perbaikan pemilihan wallpaper, lokasi/zona waktu, alur adzan/iqomah/salat/khutbah Jumat, editor komponen layar, dan pembaruan kredit.

APK publik sementara menggunakan build debug karena kunci penandatanganan release lama (`masjidku-release-key.jks`) tidak tersedia dalam workspace. Jangan membuat kunci pengganti untuk pembaruan pengguna lama. APK debug tidak dapat menimpa instalasi yang ditandatangani kunci berbeda; gunakan perangkat uji dan pertahankan instalasi serta data masjid lama.

Sebelum rilis stabil: pulihkan kunci release asli, build signed release, uji perangkat TV/STB nyata (D-pad, audio, boot/resume, migrasi database), dan selesaikan pemeriksaan keamanan media/admin. Pengujian lokal tidak setara dengan verifikasi perangkat nyata.
