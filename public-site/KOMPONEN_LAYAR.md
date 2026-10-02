# Editor komponen layar MasjidKU

Admin dapat menyembunyikan atau menampilkan logo, nama, alamat, jam, tanggal, hitung mundur, informasi/media, jadwal salat, dan maklumat. Setiap komponen memiliki warna latar, warna teks, kepekatan latar (0–100%), dan ukuran (70–140%). Teks pengganti tersedia untuk nama, alamat, judul hitung mundur, informasi, dan maklumat. Teks kosong memakai data asli masjid.

## Pratinjau lokal

Buka `http://127.0.0.1:8091/.redesign-preview/`, pilih **Tampilan layar → Komponen layar**. Tombol **Simpan di browser ini** menyimpan pengaturan lokal, tidak mengirimnya ke TV. Logo diunggah melalui **Identitas masjid** (PNG/JPEG/WebP, maksimal 1 MB). **Unduh pengaturan** membuat JSON yang dapat diimpor ke admin LAN; logo tidak termasuk JSON dan perlu diunggah terpisah. **Kembalikan contoh** menghapus pengaturan komponen dan logo pratinjau yang tersimpan.

## APK / admin LAN

Pada APK terbaru, masuk ke admin LAN, buka **Kontrol & Tampilan → Komponen layar**. **Simpan & terapkan di TV** menyimpan ke Room dan mengaktifkan model **Papan Jadwal Editable**. Model lama tetap dapat dipilih dari daftar tata letak. Logo menggunakan unggahan **Media → Logo Masjid** yang sudah tersedia.

Jam, tanggal, waktu adzan, dan hitung mundur tetap berasal dari waktu/perhitungan aplikasi. Untuk mengoreksi jadwal, gunakan lokasi, zona waktu, metode, dan offset di pengaturan waktu. Menyembunyikan komponen tidak menghapus data atau mematikan alur adzan/iqomah/salat.

Jika teks **Informasi & media** kosong, carousel lama tetap digunakan; warna, isi, dan ukuran elemen dalam kartu kas/petugas/hadits/media diatur melalui pengaturan masing-masing konten. Kepekatan panel mengikuti editor. Jika diisi, teks pengganti menampilkan kartu informasi sederhana dengan warna dan ukuran dari editor. Maklumat pada model Editable berupa teks tetap maksimal dua baris; model lama tetap memiliki running ticker. Editor ini mengatur komponen model Editable, bukan seluruh template lama dan bukan layar fokus ibadah.

Mode khutbah/salat tetap memakai layar statis gelap dan kontrol fokus khusus, sehingga latar transparan tidak menampilkan wallpaper atau media saat ibadah.

## Penyimpanan dan validasi

Room 23 → 24 menambahkan `screenCustomizationJson` tanpa menghapus konfigurasi sebelumnya. API membatasi ukuran JSON, menerima warna heksadesimal enam digit, membatasi ukuran/kepekatan/teks, dan mengabaikan komponen tak dikenal. Pengaturan rusak yang sudah tersimpan menggunakan nilai aman agar layar tidak crash.

Pengujian browser memeriksa sembilan toggle, warna/kepekatan, teks, simpan/muat ulang, logo, dan lebar 390/768/1440. Pengujian backend memeriksa API dan migrasi Room. Build APK debug dan pengujian lokal tidak membuktikan tampilan pada TV/STB nyata; pemeriksaan D-pad, keterbacaan dari jarak jamaah, dan perangkat nyata masih diperlukan sebelum rilis.
