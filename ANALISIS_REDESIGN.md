# Audit MasjidKU TV — 2 Oktober 2026

Status: audit penyebab wallpaper, perbaikan pemilihan latar, instalasi alat, dan prototipe lokal selesai. Redesign keseluruhan aplikasi belum diterapkan; pilihan visual pengguna masih menunggu. Belum rilis.

## Perubahan yang sudah ada sebelum audit

Working tree awal berisi 28 file tracked berubah serta pratinjau, panel web, server Python dan folder skill yang belum di-commit. Git hanya memiliki dua commit: `84943e1` dan `7b26a5b`. Karena perubahan belum di-commit, riwayat tidak membuktikan siapa atau kapan perubahan tersebut dibuat.

| Bagian | File yang berubah | Dampak yang terlihat dari source |
| --- | --- | --- |
| Tampilan TV | HeaderBar.kt, PrayerTimesBar.kt, PrayerOverlays.kt, CarouselCards.kt, SpecialMosqueLayouts.kt, HomeScreen.kt | Warna, opasitas, ukuran, jarak dan susunan panel berubah. |
| Wallpaper | IslamicBackground.kt; BitmapUtils.kt baru | Decoder gambar unggahan diganti dengan decoder sampled untuk membatasi memori. Aset JPG preset tidak dihapus. |
| Tema dan font | Color.kt, Type.kt | Token permukaan baru serta tipografi berubah. |
| Admin dan jaringan | LocalHttpServer.kt, QrCodeUtils.kt | HTML/CSS dashboard dan QR koneksi berubah. |
| Data dan perilaku | MasjidModels.kt, MasjidEntities.kt, MasjidDatabase.kt, MasjidRepository.kt, MasjidTVViewModel.kt | Konfigurasi, skema database, murottal dan pemicu tampilan berubah. Database kini versi 22. |
| Runtime | Manifest, BootLauncherService.kt, BuzzerSoundPlayer.kt, LocalVideoPlayerView.kt | Izin/boot, bunyi penanda serta pemutaran media berubah. |
| Build dan dokumentasi | Gradle, README, listing, dua test | Konfigurasi build, dokumen dan test berubah. |

## Mengapa wallpaper tidak terlihat

1. **Pratinjau web tidak memuat wallpaper.** `preview_masjidku.html:111` semula memakai radial-gradient, tanpa URL gambar. Ini penyebab yang pasti untuk pratinjau tersebut. Sudah dipulihkan ke JPG mihrab bawaan dengan peredupan 35%.
2. **Android memakai panel lebih pekat.** `HeaderBar.kt:73-75` berubah ke alpha 0.92/0.96/0.92, dibanding 0.75/0.90/0.75 sebelumnya. Wallpaper tertutup di area panel; ini bukan penghapusan aset. Nilai baru belum dirombak karena arah visual masih menunggu.
3. **Gambar unggahan mengalahkan preset.** `IslamicBackground.kt` sebelumnya memuat custom image tanpa memeriksa mode yang dipilih, dan custom image berada sebelum preset dalam cabang render. Kini gambar hanya dipakai dalam mode IMAGE atau legacy PRESET + CUSTOM_UPLOAD. Unggahan yang gagal dibaca mendapat fallback mihrab.
4. **Pengiriman preset memaksa mode latar berubah.** `LocalHttpServer.kt:1530-1531` sebelumnya memaksa PRESET dan menghapus path video setiap kali parameter preset hadir. Kini mode eksplisit dihormati, pengiriman ulang preset yang sama mempertahankan mode aktif, dan path video tidak dihapus akibat penyimpanan formulir umum. Pilihan preset di admin mengirim mode secara eksplisit.

Poin 2–4 berasal dari audit statis dan uji kode. Belum ada bukti perangkat TV yang digunakan pengguna; tidak boleh disimpulkan bahwa seluruh gejala perangkat sudah teratasi.

## Hasil yang dibuat pada sesi ini

- `BackgroundSelectionPolicy.kt` dan empat test regresi untuk preservasi VIDEO/CCTV/IMAGE, pemilihan ulang preset, pergantian preset serta mode eksplisit.
- Perbaikan server dan renderer latar Android. Perubahan awal pengguna dipertahankan.
- `PRODUCT.md` mencatat keputusan pengguna: tetap Android TV offline dan admin di LAN.
- `.redesign-preview/`: prototipe admin dan TV dengan tiga susunan, tiga wallpaper lokal, peredupan, pengeditan identitas/maklumat, peralihan informasi, fullscreen dan reduced motion.
- `web_remote/redesign-preview.js`: Framer Motion dari `framer-motion/dom`, dibundel lokal 61.4 KiB. Tidak perlu React untuk prototipe DOM ini. Belum dimasukkan ke panel admin Android.
- Skrip server prototipe hanya melayani daftar file pratinjau dan wallpaper; bind 127.0.0.1. Tidak melayani source atau berkas rahasia repository.
- `.gitignore`: node_modules, data simulator, uploads, artefak browser, tool lokal dan cache Python.

## Instalasi alat

| Permintaan | Status |
| --- | --- |
| Athevon/genjutsu | Cast, Paint dan subskill utama terpasang global; modul shared `_jutsu` disertakan. |
| zanwei/design-dna | Skill lengkap terpasang global. |
| greensock/gsap-skills | Delapan skill GSAP terpasang global. |
| CloudAI-X/threejs-skills | Sepuluh skill Three.js terpasang global. |
| LottieFiles/motion-design-skill | Skill motion-design terpasang global. |
| Framer Motion | Instalasi npm berhasil; benar-benar dipakai pada prototipe. Tidak dapat dipakai langsung oleh Kotlin Compose. |
| Playwright CLI | Terpasang; browser dan pengujian prototipe berhasil. |
| Supabase Plugin | Plugin terpasang via `npx plugins add ... --target codex --yes`. MCP Supabase dikonfigurasi read_only. Autentikasi akun belum selesai. Tidak ada proyek/database Supabase yang dibuat atau diubah. |
| Context7 | MCP stdio terdaftar dengan `npx -y @upstash/context7-mcp`. Tool belum dimuat dalam sesi ini; belum diklaim berhasil menjalankan lookup. |
| Strix | `strix-agent` terpasang dalam `C:/Users/T6N0CV02S35823C/.codex/tools/strix-venv`. CLI --help dan pip check berhasil. Docker engine belum berjalan; belum ada pentest Strix. Instalasi user sementara dipindahkan ke venv untuk menghindari konflik MCP; pip check Python utama kembali bersih. |
| Skill UI | Terpasang, CLI berjalan dan ekstraksi statis prototipe berhasil ke `.redesign-preview/extraction/masjidku-design/DESIGN.md`. |
| UI UX Pro Max | Search design-system dijalankan; hasil aksesibilitas dipakai sebagai panduan, bukan klaim lulus WCAG. |
| Impeccable, baseline-ui, web-design-guidelines | Dipakai untuk konteks produk, penilaian arah, pengurangan dekorasi, fokus dan audit prototipe. Detector Impeccable menghasilkan `[]`. Panduan web terbaru dibaca dari sumber resmi. |

Skill dan MCP baru memerlukan giliran/sesi baru atau restart agen agar tercantum pada inventaris tool. Konfigurasi bukan bukti koneksi akun terautentikasi.

## Bukti validasi

- `:app:testDebugUnitTest --tests com.example.BackgroundSelectionPolicyTest :app:assembleDebug`: BUILD SUCCESSFUL setelah perbaikan renderer terakhir. Empat test, nol failure/error. APK debug tersedia di `app/build/outputs/apk/debug/app-debug.apk`.
- Playwright: 16 pemeriksaan lulus; tidak ada overflow halaman di 390/768/1440 px; tiga layout, wallpaper, peredupan, identitas, maklumat, visibilitas dan reduced motion berfungsi; input identitas dirender sebagai teks, bukan HTML; nol JavaScript exception pada run tersebut.
- Screenshot desktop dan mobile ditinjau. Prototipe menggunakan data waktu contoh yang ditandai jelas.
- Npm audit setelah instalasi Framer Motion/esbuild: nol kerentanan yang dilaporkan untuk dependency web_remote saat pemeriksaan.
- Detector Impeccable tidak menemukan pelanggaran mekanis pada prototipe. Ini tidak membuktikan kontras setiap piksel wallpaper, aksesibilitas penuh, atau keamanan aplikasi Android.

## Temuan yang harus diselesaikan sebelum rilis

### Pembaruan wallpaper pengguna

Kelima belas foto yang diberikan pengguna tersedia pada pemilih wallpaper pratinjau lokal. Foto Roni Supriyanto menjadi pilihan awal. Pengaturan mencakup peredupan, posisi vertikal, memenuhi layar TV atau menampilkan foto utuh. Foto asli tetap berada di OneDrive; server hanya mengirim JPEG yang diperkecil di memori tanpa mengubah atau menyalin berkas asli ke repository.

Playwright memverifikasi 15 pilihan, 30 respons gambar/thumbnail, perpindahan foto, posisi, kedua mode ukuran, dan halaman pada 390/768/1440 px tanpa overflow maupun JavaScript exception. Ini merupakan bukti pratinjau lokal; koleksi foto belum dibundel ke APK atau dipasang pada TV. Penerapan keseluruhan redesign tetap menunggu pilihan arah visual.

- **P1, pembacaan file media:** handler `/media/...` melayani sebelum gate autentikasi, lalu `File.resolve` memakai path dari request tanpa pemeriksaan canonical containment (`LocalHttpServer.kt:261-272, 822-830`; handler audio/video memiliki pola sejenis). Potensi traversal perlu diperbaiki dan diuji di server nyata; belum menjalankan exploit.
- **P1, keamanan sesi (diperbarui 2 Oktober):** token statis diganti token acak lokal dengan kedaluwarsa delapan jam dan pencabutan saat perubahan PIN/logout. PIN awal wajib diganti sebelum kontrol/konfigurasi. Audit brute force, CSRF dan jalur token query/body masih diperlukan sebelum klaim keamanan menyeluruh.
- **P1, preservasi data:** `fallbackToDestructiveMigration()` masih ada di `MasjidDatabase.kt:508`. Jalur migrasi 21→22 ada, tetapi upgrade data nyata belum diuji. Rilis tidak boleh hanya mengandalkan build.
- **P2, sumber admin terduplikasi:** HTML embedded Android dan `web_remote/index.html` berbeda. Server Python mensimulasikan beberapa operasi; jangan menyatakan hasil localhost sama dengan TV. Redesign harus menyiapkan satu sumber UI dan kontrak API yang tervalidasi.
- **P2, offline pratinjau lama:** `preview_masjidku.html` masih memakai CDN Tailwind/Google Fonts. Prototipe baru memuat aset lokal; admin produksi perlu audit seluruh dependensi offline.
- **P2, jangkauan perangkat:** uji remote D-pad/fokus, jarak baca, 720p/1080p, TV/STB rendah memori, image/video/CCTV fallback, audio, adzan/iqomah, layar salat dan resume/boot masih belum dilakukan.

## Penerapan berikutnya setelah pilihan visual

### Implementasi alur ibadah, 2 Oktober 2026

Permintaan lokasi, adzan, iqomah, fokus salat dan khutbah Jumat telah diterapkan pada source Android serta kedua antarmuka admin. Jam dan tanggal sesi mengikuti zona waktu masjid. Form dan peta mempertahankan koordinat nol serta zona waktu tersimpan; pilihan kota memakai zona waktu katalog. Durasi adzan dapat diatur, iqomah Jumat tidak lagi bergantung pada visibilitas kartu petugas, dan kontrol adzan manual menggunakan alur yang sama dengan pemicu otomatis.

Tahap `KHUTBAH_FOCUS` menyediakan akhir manual atau otomatis. Khutbah/salat memakai layar statis berpesan atau gelap, jam kecil opsional, dan melepas konten utama dari komposisi. Kontrol pengurus tersedia untuk khutbah, iqomah Jumat, salat Jumat dan kembali ke informasi; tombol Kembali pada remote juga dapat mengakhiri mode ibadah. Pengaturan baru tersimpan dengan migrasi Room 22→23. PIN awal dibatasi untuk penyiapan PIN pribadi; sesi memakai token acak, kedaluwarsa dan pencabutan.

Validasi akhir: build APK debug berhasil; 16 test lulus tanpa failure/error. Aturan alur dan timer memakai waktu virtual, API HTTP Android diuji dengan Room/Robolectric, dan migrasi diuji terhadap skema lengkap serta konfigurasi lama. Pemeriksaan Playwright form embedded memakai HTML hasil server Android dan fixture status: pemuatan konfigurasi, koordinat nol, zona waktu tersimpan, payload simpan dan tombol khutbah lulus. Pratinjau lima mode, pilihan gelap/jam serta ukuran 390/768/1440 px juga lulus tanpa JavaScript exception. Bukti lokal ini belum merupakan pengujian pada TV/STB nyata.

Panduan pengurus dan batas rilis: `docs/MASJIDKU_IBADAH.md`. Web unduhan diterbitkan melalui GitHub Pages dengan custom domain `mosque.irsyads.com` dan CNAME Cloudflare menuju `irsyadmsk.github.io`. Redesign keseluruhan berikut tetap menunggu pilihan visual.

1. Tetapkan token bersama TV/admin dan kontrak gerak; jaga wallpaper serta kontras teks pada tiap preset.
2. Terapkan keseluruhan layar Compose: header, jam, carousel, kas/petugas/kajian, jadwal, ticker, pengaturan dan semua overlay salat. Animasi native Compose mengikuti skala animasi sistem.
3. Rombak admin dengan navigasi per tugas, ringkasan layar aktif, preview konfigurasi, feedback simpan/error, form terlabel dan fokus keyboard. Pertahankan seluruh endpoint/fungsi yang tersedia.
4. Satukan sumber web, bundel semua aset offline, dan hilangkan beda simulasi dengan aplikasi nyata melalui kontrak pengujian.
5. Perbaiki temuan rilis, uji migrasi, jalankan pemeriksaan keamanan serta uji perangkat nyata. Tunjukkan diff dan bukti sebelum publikasi.

Referensi setup yang diperiksa: [Playwright CLI](https://github.com/microsoft/playwright-cli), [Supabase Plugin](https://supabase.com/docs/guides/ai-tools/plugins), [Strix](https://github.com/usestrix/strix), [Skill UI](https://github.com/amaancoderx/npxskillui), [Context7](https://github.com/upstash/context7), [Web Interface Guidelines](https://raw.githubusercontent.com/vercel-labs/web-interface-guidelines/main/command.md).
