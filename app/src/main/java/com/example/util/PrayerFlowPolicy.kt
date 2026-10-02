package com.example.util

import com.example.data.model.MosqueConfig
import com.example.data.model.PrayerName
import com.example.data.model.TVDisplayMode
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.delay

/** One policy for automatic and administrator-controlled prayer sessions. */
object PrayerFlowPolicy {
    fun manualPrayer(active: PrayerName, friday: Boolean): PrayerName =
        if (friday) PrayerName.DZUHUR else active
    suspend fun waitForKhutbahEnd(config: MosqueConfig) {
        if (config.fridayKhutbahManual) awaitCancellation()
        else delay(config.fridayKhutbahMinutes.coerceIn(1, 120) * 60_000L)
    }
    fun afterAdhan(prayer: PrayerName, friday: Boolean, config: MosqueConfig): TVDisplayMode =
        if (friday && prayer == PrayerName.DZUHUR && config.fridayKhutbahEnabled)
            TVDisplayMode.KHUTBAH_FOCUS else TVDisplayMode.IQOMAH_COUNTDOWN

    fun iqomahMinutes(prayer: PrayerName, friday: Boolean, config: MosqueConfig): Int =
        when (prayer) {
            PrayerName.SUBUH -> config.iqomahSubuh
            PrayerName.DZUHUR -> if (friday) config.iqomahJumat else config.iqomahDzuhur
            PrayerName.ASHAR -> config.iqomahAshar
            PrayerName.MAGHRIB -> config.iqomahMaghrib
            PrayerName.ISYA -> config.iqomahIsya
            PrayerName.IDUL_FITRI -> config.idulFitriIqomahMinutes
            PrayerName.IDUL_ADHA -> config.idulAdhaIqomahMinutes
            else -> 10
        }.coerceIn(0, 120)

    fun hidesInformation(mode: TVDisplayMode): Boolean = mode in setOf(
        TVDisplayMode.ADHAN, TVDisplayMode.IQOMAH_COUNTDOWN,
        TVDisplayMode.KHUTBAH_FOCUS, TVDisplayMode.SHOLAT_SILENT
    )

    fun validLocation(lat: Double, lng: Double, timezone: Double): Boolean =
        lat.isFinite() && lat in -90.0..90.0 &&
            lng.isFinite() && lng in -180.0..180.0 &&
            timezone.isFinite() && timezone in -12.0..14.0
}
