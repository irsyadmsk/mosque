package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.*
import com.example.data.repository.MasjidRepository
import com.example.server.LocalHttpServer
import kotlinx.coroutines.*
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.net.HttpURLConnection
import java.net.ServerSocket
import java.net.URL
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class PrayerAdminApiTest {
    @Test fun realLocalServerEnforcesSetupAndPersistsWorshipSettings() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val db = Room.inMemoryDatabaseBuilder(context, MasjidDatabase::class.java).allowMainThreadQueries().build()
        db.mosqueConfigDao().insertOrUpdate(MosqueConfigEntity())
        val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
        val actions = LinkedBlockingQueue<String>()
        val port = ServerSocket(0).use { it.localPort }
        val server = LocalHttpServer(port, MasjidRepository(db, context), scope, context, { actions.add(it) })
        fun request(path: String, body: JSONObject? = null, token: String? = null): Pair<Int, JSONObject> {
            val conn = URL("http://127.0.0.1:$port$path").openConnection() as HttpURLConnection
            conn.connectTimeout = 3000; conn.readTimeout = 5000
            if (token != null) conn.setRequestProperty("Authorization", "Bearer $token")
            if (body != null) {
                conn.requestMethod = "POST"; conn.doOutput = true
                conn.setRequestProperty("Content-Type", "application/json")
                conn.outputStream.use { it.write(body.toString().toByteArray()) }
            }
            val status = conn.responseCode
            val stream = if (status >= 400) conn.errorStream else conn.inputStream
            val json = stream.bufferedReader().use { JSONObject(it.readText()) }
            conn.disconnect()
            return status to json
        }
        try {
            server.start()
            // Wait only for the listener to bind, not for an arbitrary fixed delay.
            withTimeout(5000) {
                while (true) {
                    try { java.net.Socket("127.0.0.1", port).use {}; break }
                    catch (_: java.net.ConnectException) { delay(10) }
                }
            }
            val login = request("/api/login", JSONObject().put("password", "123456")).second
            assertTrue(login.getBoolean("requiresPinChange"))
            val setupToken = login.getString("token")
            assertEquals(403, request("/api/trigger-khutbah", JSONObject(), setupToken).first)
            assertEquals("error", request("/api/change-password", JSONObject().put("oldPassword", "123456").put("newPassword", "123456").put("confirmPassword", "123456"), setupToken).second.getString("status"))
            assertEquals("ok", request("/api/change-password", JSONObject().put("oldPassword", "123456").put("newPassword", "masjid789").put("confirmPassword", "masjid789"), setupToken).second.getString("status"))
            assertEquals(401, request("/api/save-config", JSONObject(), setupToken).first)
            val token = request("/api/login", JSONObject().put("password", "masjid789")).second.getString("token")
            assertEquals(400, request("/api/save-config", JSONObject().put("latitude", 91), token).first)
            val settings = JSONObject().put("latitude", 0).put("longitude", 0).put("timezone", 0)
                .put("adhanDurationSeconds", 180).put("fridayKhutbahEnabled", true)
                .put("fridayKhutbahManual", false).put("fridayKhutbahMinutes", 25)
                .put("focusBlankScreen", true).put("focusShowClock", false).put("iqomahJumat", 0)
                .put("webAdminPassword", "bypass")
            assertEquals(200, request("/api/save-config", settings, token).first)
            val saved = db.mosqueConfigDao().getConfig()!!
            assertEquals(0.0, saved.latitude, 0.0); assertEquals(0.0, saved.timezone, 0.0)
            assertEquals(180, saved.adhanDurationSeconds); assertEquals(25, saved.fridayKhutbahMinutes)
            assertFalse(saved.fridayKhutbahManual); assertTrue(saved.focusBlankScreen)
            assertFalse(saved.focusShowClock); assertEquals(0, saved.iqomahJumat)
            assertEquals("masjid789", saved.webAdminPassword)
            val status = request("/api/status").second.getJSONObject("config")
            assertEquals(180, status.getInt("adhanDurationSeconds"))
            assertFalse(status.getBoolean("requiresPinChange"))
            assertEquals(200, request("/api/trigger-khutbah", JSONObject(), token).first)
            assertEquals("khutbah", actions.poll(3, TimeUnit.SECONDS))
            val components = JSONObject().put("clock", JSONObject().put("visible", false).put("color", "#FFFFFF").put("opacity", 0.2))
                .put("information", JSONObject().put("text", "Kajian Jumat").put("background", "#123456"))
            assertEquals(200, request("/api/save-config", JSONObject().put("screenCustomizationJson", components.toString()).put("displayLayoutModel", "CUSTOMIZABLE_BOARD"), token).first)
            val persisted = JSONObject(db.mosqueConfigDao().getConfig()!!.screenCustomizationJson)
            assertFalse(persisted.getJSONObject("clock").getBoolean("visible"))
            assertEquals("Kajian Jumat", persisted.getJSONObject("information").getString("text"))
            assertEquals("CUSTOMIZABLE_BOARD", db.mosqueConfigDao().getConfig()!!.displayLayoutModel)
            assertEquals(persisted.toString(), request("/api/status").second.getJSONObject("config").getString("screenCustomizationJson"))
            assertEquals(400, request("/api/save-config", JSONObject().put("screenCustomizationJson", "invalid"), token).first)
            assertEquals(persisted.toString(), db.mosqueConfigDao().getConfig()!!.screenCustomizationJson)
            // Render the actual embedded administrator UI for a separate browser contract check.
            val html = java.io.File("build/outputs/admin-review.html")
            html.parentFile.mkdirs()
            html.writeText(URL("http://127.0.0.1:$port/").readText())
            java.io.File("build/outputs/admin-review-status.json").writeText(request("/api/status").second.toString())
            request("/api/logout", JSONObject(), token)
            assertEquals(401, request("/api/trigger-reset", JSONObject(), token).first)
        } finally { server.stop(); scope.cancel(); db.close() }
    }
}
