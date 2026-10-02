# Pratinjau arah MasjidKU

Ini prototipe terpisah untuk memilih desain, bukan implementasi Android atau panel produksi. Nilai waktu, tanggal dan kas adalah contoh. Pengaturan tidak dikirim ke TV dan tidak disimpan.

Wallpaper diperbarui dengan 15 foto yang diberikan pengguna pada 2 Oktober 2026. Foto Roni Supriyanto menjadi latar awal. Pilihan foto, posisi vertikal, mode cover/contain dan peredupan tersedia. Original tidak disalin ke repository: `photos.py` mereferensikan 15 berkas tertentu di OneDrive. Server menggunakan Pillow yang sudah tersedia untuk membuat JPEG layar/thumbnail di memori dan cache terbatas. Tidak ada koreksi warna, retouch, atau modifikasi original. Untuk mesin lain, set environment `MASJIDKU_PHOTO_DIR` ke folder induk foto; server memerlukan `Pillow`. Foto belum dibundel ke APK atau dipasang di perangkat TV.

Pengguna telah memilih cara penilaian berupa pratinjau lokal. Pemilihan arah visual masih menunggu. Kontrak gerak yang ditampilkan: peralihan informasi 320 ms ease-out, feedback kontrol 160 ms, wallpaper/jam stabil, OS dan toggle reduced motion dihormati. Framer Motion dibundel lokal tanpa CDN.

Jalankan dari `web_remote`:

```powershell
npm run preview:build
npm run preview:serve
```

Buka http://127.0.0.1:8091/.redesign-preview/ . Server dibatasi ke file pratinjau dan aset wallpaper bawaan.

Dari root repository, uji prototipe:

```powershell
playwright-cli -s=masjidku open http://127.0.0.1:8091/.redesign-preview/
playwright-cli -s=masjidku run-code --filename=.redesign-preview/verify.cjs
```

Gunakan nilai path absolute pada Codex ketika membuka file. Screenshot adalah bukti prototype web, bukan pengujian TV.

## Pertimbangan arah

Sumber konteks: PRODUCT.md dan kode yang ada. Kandidat dari konteks pengurus/jamaah, diurutkan menurut kecocokan: Mihrab Tenang, papan maklumat, laporan kas, kalender kegiatan, Papan Jadwal, kisi arsitektur, album dokumentasi. Concept seed Impeccable memilih kandidat kelima (`fce5fbc1`). Kandidat itu disajikan sebagai Papan Jadwal; Mihrab Tenang menjadi alternatif yang paling familiar, dengan risiko ornamen wallpaper mengganggu teks; Grid Informasi menguji susunan samping yang lebih padat.

Challenger Labanotation tidak dipilih karena pembacaan waktu dari bawah ke atas mengubah kebiasaan jamaah. Challenger spesimen grid Crouwel tidak mengalahkan susunan jadwal yang familiar, tetapi memperjelas kebutuhan alignment dalam alternatif Grid Informasi. Tidak ada perubahan arah yang diklaim telah disetujui pengguna.

## Pratinjau ibadah

Bagian Alur ibadah mencoba lima mode: informasi, adzan, khutbah, iqomah, dan salat. Layar fokus menyediakan pesan statis/jam kecil opsional atau layar gelap. Tombol pratinjau tidak mengirim perintah ke TV. Implementasi dan pengaturan admin Android dijelaskan di `docs/MASJIDKU_IBADAH.md`.

Pengujian baru: `verify-worship.cjs` memeriksa mode fokus, pengaturan gelap/jam dan ukuran layar; `verify-admin-worship.cjs` memeriksa HTML admin embedded yang diekspor pengujian API Android, menggunakan respons fixture serta penangkapan payload browser. Pemeriksaan API Android memakai server nyata di Robolectric; uji browser fixture bukan uji perangkat TV.

## Tokens proposal

Admin: canvas #F1F5F3, surface #FFFFFF, ink #132D28, muted #52675F, line #D6E1DB, action #12634B. TV: wallpaper existing, panel #051F17 dengan alpha 0.74, schedule #031A12 alpha 0.80, accent #E6C37A, white text. Layout TV 16:9, jadwal tabular, konten tidak bergerak terus-menerus. Tidak ada WebGL/3D/partikel aktif; Three.js tersedia sebagai skill, bukan dependency yang dibebankan pada STB.
