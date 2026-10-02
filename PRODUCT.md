# MasjidKU TV

<!-- impeccable:product-schema 1 -->

## Platform

android

Android TV / STB menampilkan informasi untuk jamaah. Panel admin web dibuka dari HP atau laptop di jaringan lokal yang sama.

## Users

Pengurus masjid mengatur identitas, wallpaper, jadwal salat, media, audio, pengumuman, kas dan petugas. Jamaah membaca layar TV dari jarak jauh.

## Product Purpose

Layar informasi masjid yang mudah dibaca dan mudah disesuaikan pengurus. Jadwal dan fungsi inti tetap berjalan offline.

## Operating Context

Pengguna mengonfirmasi pada 2 Oktober 2026: pertahankan aplikasi Android TV offline dan admin lokal. Redesign mencakup layar jamaah serta panel admin. Tidak ada keputusan untuk memindahkan database Room ke Supabase atau menambahkan layanan cloud.

## Capabilities and Constraints

Kotlin / Jetpack Compose, Room, Media3, embedded HTTP server port 8080. Panel saat ini HTML/CSS/JavaScript. Pratinjau Python merupakan simulator, bukan bukti perilaku Android TV. Wallpaper JPG bawaan tersedia di app/src/main/res/drawable. Pertahankan seluruh fitur, data pengurus dan aset yang tersedia; perubahan belum di-commit adalah pekerjaan yang harus dijaga.

## Brand Commitments

Nama MasjidKU TV. Bahasa Indonesia. Identitas dan konten masjid dapat disesuaikan admin. Pengguna meminta wallpaper kembali terlihat, animasi relevan, antarmuka bersih dan mudah digunakan.

Identitas ekosistem: MasjidKU by Irsyads. Alamat publik yang dipilih untuk persiapan produk adalah `masjid.irsyads.com`; `mosque.irsyads.com` dapat menjadi alias. DNS dan situs publik belum diterbitkan. Situs informasi/demo/unduhan tidak memerlukan login; TV tetap offline dan pengaturan memakai PIN lokal pengurus.

Alur ibadah: informasi → adzan → iqomah → fokus salat. Pada Jumat, tahap fokus khutbah berada setelah adzan dan sebelum iqomah. Khutbah dapat berakhir melalui kontrol pengurus atau timer. Mode fokus memakai permukaan statis, menghentikan konten utama dan audio murottal, serta menyediakan pilihan pesan/jam kecil atau layar gelap.

## Evidence on Hand

README.md, source Android, panel embedded dalam LocalHttpServer.kt, web_remote/index.html dan preview_masjidku.html. Masjid Endan Andansih adalah identitas seed yang tersedia dalam source, bukan klaim akun produksi terverifikasi.

## Open Decisions

Arah visual menunggu penilaian pengguna melalui pratinjau lokal. Uji pada perangkat TV/STB nyata diperlukan sebelum menyatakan siap rilis.
