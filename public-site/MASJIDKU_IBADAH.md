# Lokasi, admin lokal, dan mode ibadah

Implementasi Android dan admin lokal, 2 Oktober 2026. Aplikasi TV tidak membutuhkan akun internet. Halaman informasi dan unduhan publik menggunakan `https://mosque.irsyads.com/` dengan identitas MasjidKU by Irsyads. Hosting GitHub Pages dan CNAME Cloudflare sudah dikonfigurasi.

## Pengaturan awal

1. Buka alamat admin TV dari HP/laptop di jaringan masjid yang sama.
2. Pada pemasangan dengan PIN awal `123456`, masuk lalu tetapkan PIN pribadi minimal enam karakter melalui Profil. PIN awal hanya dapat dipakai untuk penyiapan PIN; perubahan konfigurasi dan kontrol ibadah dibatasi server sampai PIN diganti. Masuk kembali dengan PIN baru.
3. Pada Profil, pilih kota dari daftar bawaan atau isi koordinat dan zona waktu manual. Peta/GPS merupakan bantuan opsional; daftar kota dan input manual tetap tersedia tanpa peta internet. Memuat peta tidak boleh menimpa koordinat nol atau zona waktu tersimpan. Perubahan koordinat manual mempertahankan zona waktu yang dipilih; pemilihan kota memakai zona waktu dari katalog kota.
4. Pada Sholat, sesuaikan koreksi menit dengan jadwal yang dipakai masjid. Jam TV, tanggal sesi, dan perhitungan jadwal mengikuti zona waktu masjid; jam sistem perangkat tetap harus benar.

Sesi admin memakai token acak, berlaku delapan jam, dan dicabut saat logout atau perubahan PIN. PIN pribadi tidak ditampilkan pada panel koneksi TV. Ini bukan bukti audit keamanan menyeluruh atau pengamanan jaringan publik.

## Alur harian

Informasi → adzan → hitung mundur iqomah → fokus salat → informasi.

- Durasi tampilan adzan: 10–900 detik; default 30 detik menjaga pengaturan lama. Sesuaikan dengan durasi adzan masjid.
- Hitung mundur iqomah: 0–120 menit per waktu salat. Hitungan dimulai setelah tahap adzan; nol berarti langsung beralih ke salat.
- Fokus salat: durasi sesuai pengaturan, minimal tiga menit. Seluruh carousel, ticker, video/CCTV latar, dan audio murottal berhenti. Pengurus dapat memilih pesan tenang dengan jam kecil opsional atau layar gelap sepenuhnya.

Tampilan adzan adalah tahap layar, bukan klaim bahwa aplikasi mendeteksi suara adzan atau menyediakan rekaman adzan baru.

## Jumat

Informasi → adzan → fokus khutbah → iqomah → fokus salat → informasi.

- Aktifkan tahap khutbah Jumat pada tab Sholat. Tahap ini terpisah dari visibilitas kartu petugas Jumat.
- Pilihan manual (default): layar khutbah tetap fokus sampai pengurus menekan **Mulai iqomah Jumat**. Tidak ada batas timer tersembunyi.
- Pilihan otomatis: selesai setelah durasi khutbah 1–120 menit (default 20), kemudian mengikuti iqomah Jumat yang tersimpan.
- Kontrol langsung: **Mulai khutbah**, **Mulai iqomah Jumat**, **Mulai salat Jumat**, dan **Selesai salat / kembali ke informasi**. Kontrol manual menggantikan timer tahap sebelumnya.
- Tombol Kembali pada remote TV dapat mengakhiri mode adzan, iqomah, khutbah, atau salat dan kembali ke informasi.

## Penyimpanan dan batas validasi

Room naik dari versi 22 ke 23 melalui migrasi yang menambah enam pengaturan. Fallback penghapusan database dihapus; versi tanpa jalur migrasi gagal dibuka daripada menghapus data masjid.

Pengujian mencakup aturan alur/timer, validasi koordinat, sesi admin, roundtrip pengaturan dan kontrol melalui server HTTP Android, preservasi data serta validasi skema lengkap Room 22→23. Browser memeriksa form embedded Android dan pratinjau mode fokus. Ini belum menggantikan uji Android TV/STB nyata, upgrade database produksi, remote D-pad, keluaran audio, boot/resume, dan operasi Jumat di masjid.

Koleksi 15 foto pengguna masih tersedia pada pratinjau lokal; belum dibundel ke APK. Redesign keseluruhan tampilan tetap menunggu pemilihan arah visual.
