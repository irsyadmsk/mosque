# mosque/web_remote — simulator dan bundel pratinjau

Alat pengembangan lokal, **bukan** web admin yang ada di APK. Web admin sebenarnya tertanam di `../app/src/main/java/com/example/server/LocalHttpServer.kt`.

| Berkas | Fungsi |
|---|---|
| `index.html`, `screen-editor.js` | Tiruan panel admin dan editor komponen layar untuk dicoba di peramban |
| `server.py`, `db.json` | Server simulasi dengan data contoh (dipakai `../server.py` / `../run_localhost.py`, port 8080) |
| `redesign-preview.js` | Sumber bundel prototipe desain di `../.redesign-preview/` |

```bash
npm ci
npm run preview:build     # esbuild → ../.redesign-preview/preview.js
npm run preview:serve     # python ../.redesign-preview/server.py → http://127.0.0.1:8091/.redesign-preview/
```

Hasil di simulator tidak membuktikan perilaku Android TV (D-pad, audio, boot, Room). Perubahan yang dimaksudkan untuk pengguna harus dibuat di `LocalHttpServer.kt` / kode Compose, lalu diuji di perangkat. `db.json` di-gitignore (data lokal); berkas lain dilacak git.
