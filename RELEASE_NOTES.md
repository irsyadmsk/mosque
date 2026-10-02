# MasjidKU TV 1.0.7-beta.1 — 2 Oktober 2026

Credit by Muhammad Irsyad Sirojul Khoeir (irsyads.com)

- Perbaikan pemilihan wallpaper dan pemulihan fallback gambar.
- Lokasi/zona waktu, durasi adzan, iqomah Jumat, serta fokus khutbah/salat.
- Papan Jadwal Editable dengan sembilan komponen, warna/kepekatan/ukuran/teks, logo dan ekspor/impor.
- PIN awal wajib diganti; sesi admin lokal acak dan pencabutan sesi setelah perubahan PIN.
- Migrasi Room 22 → 23 → 24 tanpa reset data otomatis.
- Repository unduhan publik dan halaman web informasi.

APK beta adalah build debug untuk perangkat uji, belum diuji pada TV/STB nyata. Kunci release asli diperlukan sebelum rilis stabil. Tidak dapat menimpa APK dengan kunci signing berbeda.

---

## Arsip catatan rilis lama

# 📦 Catatan Rilis (Release Notes) - MasjidKU TV v1.0.1

Dokumen ini berisi teks catatan rilis resmi yang siap disalin langsung ke kolom **Catatan Rilis (*Release Notes*)** di Google Play Console saat meluncurkan versi baru.

---

## 🇮🇩 Bahasa Indonesia (id-ID) - Siap Salin ke Play Console
> **Batas Google Play Console: Maksimal 500 karakter per bahasa**

```text
Pembaruan MasjidKU TV v1.0.1:
• Keamanan DKM: Layar Login & Proteksi PIN Web Remote (ip:8080) agar pengaturan aman.
• Fitur Ubah PIN langsung dari browser HP & tampilan PIN di menu TV.
• 8 Desain Eksklusif: Grand Royal Mihrab, Cordoba Alhambra Moorish, Nabawi, dll.
• Laporan Kas Keuangan & Petugas Sholat Jum'at transparan.
• Infaq Digital QRIS: Unggah gambar barcode QRIS langsung dari HP.
• Jadwal Sholat Akurat Kemenag RI, alarm adzan, iqomah & hening sholat.
```
*(Karakter: 494 / 500 karakter)*

---

## 🌐 English (en-US) - Ready for Play Console

```text
MasjidKU TV v1.0.1 Update:
• Security: Web Remote Login & PIN protection (ip:8080) to secure mosque settings.
• Change PIN feature via mobile web & PIN display on TV settings.
• 8 Royal & Elegant Themes: Grand Royal Mihrab, Cordoba Moorish, Nabawi, and more.
• Transparent Mosque Financial Reports & Friday Officer Schedule.
• Digital Donation: Support uploading mosque QRIS barcode directly from phone.
• Accurate prayer times with standard calculation methods & adzan alarm.
```
*(Characters: 492 / 500 characters)*

---

## 🏷️ Detail Teknis Rilis

| Parameter | Nilai |
| :--- | :--- |
| **Versi Aplikasi (*versionName*)** | `1.0.1` |
| **Nomor Kode Versi (*versionCode*)** | `2` |
| **Target SDK** | `36` (Android 15 / 16 compliant) |
| **Minimum SDK** | `24` (Android 7.0 Nougat+) |
| **Format Berkas Rilis** | Android App Bundle (`.aab`) |
| **Status Tanda Tangan (*Signing*)** | Ditandatangani dengan kunci rilis resmi `masjidku-release-key.jks` |
| **Kompilasi** | ProGuard / R8 dioptimalkan untuk Android TV |
