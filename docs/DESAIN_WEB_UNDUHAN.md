# Desain web unduhan MasjidKU

Status: publikasi disetujui pengguna pada 2 Oktober 2026; deploy dan verifikasi produksi selesai.

Pengguna mengonfirmasi mengikuti halaman aplikasi Irsyads dan tombol Ke irsyads.com di navigasi atas. Referensi diperiksa langsung: apps/site/src/pages/apps.astro, PageIntro.astro, EtalaseAplikasi.tsx, packages/design-tokens/src/tokens.css, Logo.astro, sublogo/masjidku_light.svg, serta halaman aktif https://irsyads.com/apps pada 2 Oktober 2026. Repo irsyads hanya dibaca; tidak diubah.

Palet dan font mengikuti sumber Irsyads: mist #f7f8fa, ink #192a40, mint #66d6bf, aksen teks #126a5a; Manrope dan Bricolage Grotesque. Logo MasjidKU dan logomark Irsyads disalin dari aset first-party pada repo irsyads. Font mengikuti sumber Google Fonts yang juga digunakan situs utama.

## Direction contract

THESIS: Halaman unduhan adalah bagian dari katalog aplikasi Irsyads, mempertahankan kebenaran rilis beta dan alur pemasangan lokal.
OWN-WORLD: Warna, pasangan font, logo, jejak halaman dan tombol memakai bahasa visual Irsyads yang sudah ada.
STORY: Pengunjung memahami fungsi MasjidKU, mengunduh beta dan mengikuti pemasangan; kembali ke situs utama melalui tombol atas.
FIRST VIEWPORT: Identitas MasjidKU di kiri atas, navigasi dan tautan utama di kanan, breadcrumb ke Aplikasi, teks produk di kiri dan ilustrasi papan di kanan.
FORM: Mewarisi arah visual yang dipilih pengguna; tidak memilih dunia baru. Kartu fitur mengikuti etalase aplikasi.
FINISH: unreviewed and undocumented is unfinished; this build ends with the finish review, the verdict, DESIGN.md, and every shipping raster carrying its provenance

## Batas

Tidak mengubah APK, admin lokal, domain, versi, URL berkas APK, PIN, atau repo utama. Foto wallpaper pengguna tidak dipublikasikan. Kartu MasjidKU pada /apps masih Info TV; tautan balik di situs utama perlu konfirmasi terpisah sebelum diubah. Publikasi halaman unduhan disetujui pengguna.

## Finish review

Verdict: pratinjau mengikuti desain halaman aplikasi Irsyads sesuai persetujuan arah. Desktop dan ponsel 390px diperiksa dalam satu pass; logo/font tampil, tombol ke situs utama berada di navigasi, tautan APK tetap sama, tidak ada overflow. Kontras pasangan teks yang digunakan lulus 4.5:1. Dokumentasi desain: docs/design/public-site/DESIGN.md. Screenshot JPEG adalah bukti render lokal di folder visualizations Codex, bukan aset yang dikirim ke situs. Tidak ada raster baru yang dipublikasikan. Desain halaman unduhan dipublikasikan setelah persetujuan pengguna. Perubahan tautan pada repo irsyads belum dilakukan.

## Bukti produksi

GitHub Pages workflow 36987463632 berhasil pada commit publik ae22051. Halaman, CSS dan kedua SVG merespons HTTPS 200 serta isinya cocok dengan source yang disetujui setelah normalisasi newline. Browser produksi menampilkan tombol Ke irsyads.com dengan target https://irsyads.com, logo, breadcrumb Aplikasi, dan URL APK beta yang sama. Repo irsyads tidak diubah.
