# mosque/app — APK MasjidKU TV

Modul Android tunggal. `applicationId` **`com.alijayanet.masjidkutv`** (jangan diubah), `namespace` kode `com.example`. minSdk 24, targetSdk 36. Kotlin + Jetpack Compose (Material 3), Room, Media3.

## Peta kode (`src/main/java/com/example/`)

| Berkas/folder | Isi |
|---|---|
| `MainActivity.kt` | Titik masuk; layar penuh untuk TV |
| `viewmodel/MasjidTVViewModel.kt` | Keadaan layar (jadwal, alur ibadah, media, pengaturan) |
| `ui/screens/HomeScreen.kt` | Layar utama jamaah |
| `ui/components/` | Komponen layar: `PrayerTimesBar`, `PrayerOverlays`, `PrayerFocusOverlay` (adzan/iqomah/salat/khutbah), `EditableBoard` (Papan Jadwal Editable, 9 komponen), `HeaderBar`, `AnalogClock`, `RunningTextTicker`, `CarouselCards`, `IslamicBackground`, `LocalVideoPlayerView`, `RtspCctvPlayerView`, `SpecialMosqueLayouts`, `SettingsDialog` |
| `ui/theme/` | Warna, tipografi, tema |
| `data/local/` | Room: `MasjidDatabase.kt` (**version 24**), `MasjidDao.kt`, `MasjidEntities.kt` |
| `data/prayer/` | `PrayerCalculator.kt` (hitung jadwal salat offline), `IndonesiaCityData.kt` (koordinat kota) |
| `data/repository/MasjidRepository.kt`, `data/model/` | Akses data dan model |
| `server/LocalHttpServer.kt` | Server HTTP port 8080 + **seluruh web admin** (HTML/CSS/JS tertanam, ±9.600 baris), API admin, PIN pengurus (awal `123456`, wajib diganti) |
| `server/NetworkUtils.kt`, `QrCodeUtils.kt` | Alamat IP dan QR "Sambung HP" |
| `util/` | Kebijakan murni yang diuji: `PrayerFlowPolicy`, `BackgroundSelectionPolicy`, `ScreenCustomization`, `LocalAdminSessions`, `BitmapUtils` |
| `audio/` | Buzzer dan murottal |
| `receiver/` | Mulai otomatis saat boot (`BootReceiver`, `BootLauncherService`) |
| `notification/WhatsAppGatewayManager.kt` | Gateway WhatsApp (butuh koneksi) |

Sumber daya: `src/main/res/drawable` (wallpaper JPG bawaan), `res/font` (Rajdhani, Radioland).

## Tes

`src/test/java/com/example/`: `PrayerFlowPolicyTest`, `BackgroundSelectionPolicyTest`, `ScreenCustomizationTest`, `PrayerAdminApiTest`, `PrayerConfigMigrationTest`, uji Robolectric dan screenshot (`src/test/screenshots/`).

```powershell
.\gradlew.bat :app:assembleDebug :app:testDebugUnitTest
```

## Aturan saat mengubah

- Logika baru sebaiknya di `util/` sebagai fungsi murni dan diberi tes, bukan di Composable atau string HTML.
- Perubahan entitas Room: naikkan `version` dan tambahkan migrasi; jangan `fallbackToDestructiveMigration` (data pengurus hilang).
- Admin web hanya untuk jaringan lokal dengan PIN. Jangan membuka akses dari internet atau menghapus pemeriksaan sesi (`LocalAdminSessions`).
- Navigasi D-pad harus tetap berfungsi untuk setiap kontrol baru di layar TV.
- Penandatanganan release memakai `keystore.properties` lokal yang tidak ada di repo. Jangan membuat keystore baru.
