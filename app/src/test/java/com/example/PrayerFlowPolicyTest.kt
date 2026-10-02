package com.example

import com.example.data.model.*
import com.example.util.PrayerFlowPolicy
import com.example.util.LocalAdminSessions
import org.junit.Assert.*
import org.junit.Test
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent

class PrayerFlowPolicyTest {
    private val config = MosqueConfig()
    @Test fun fridayControlSelectsFridayPrayerEvenAfterAnotherPrayerSession() {
        assertEquals(PrayerName.DZUHUR, PrayerFlowPolicy.manualPrayer(PrayerName.SUBUH, true))
        assertEquals(PrayerName.SUBUH, PrayerFlowPolicy.manualPrayer(PrayerName.SUBUH, false))
    }
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test fun manualKhutbahNeverAdvancesUntilAdministratorInterrupts() = runTest {
        var advanced = false
        val khutbah = launch { PrayerFlowPolicy.waitForKhutbahEnd(config); advanced = true }
        advanceTimeBy(3 * 60 * 60 * 1000L)
        assertFalse(advanced)
        assertTrue(khutbah.isActive)
        khutbah.cancel(); runCurrent()
        assertFalse(advanced)
    }
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    @Test fun automaticKhutbahRespectsDurationAndCanBeInterrupted() = runTest {
        var advanced = false
        val cfg = config.copy(fridayKhutbahManual = false, fridayKhutbahMinutes = 2)
        launch { PrayerFlowPolicy.waitForKhutbahEnd(cfg); advanced = true }
        advanceTimeBy(119_999); assertFalse(advanced)
        advanceTimeBy(1); runCurrent(); assertTrue(advanced)
        advanced = false
        val cancelled = launch { PrayerFlowPolicy.waitForKhutbahEnd(cfg); advanced = true }
        advanceTimeBy(1000); cancelled.cancel(); advanceTimeBy(120_000); runCurrent()
        assertFalse(advanced)
    }
    @Test fun fridayKhutbahDoesNotDependOnOfficerCardVisibility() {
        assertEquals(TVDisplayMode.KHUTBAH_FOCUS,
            PrayerFlowPolicy.afterAdhan(PrayerName.DZUHUR, true, config.copy(showFridayOfficers = false)))
        assertEquals(config.iqomahJumat,
            PrayerFlowPolicy.iqomahMinutes(PrayerName.DZUHUR, true, config.copy(showFridayOfficers = false)))
    }
    @Test fun ordinaryPrayersAndDisabledFridayModeSkipKhutbah() {
        assertEquals(TVDisplayMode.IQOMAH_COUNTDOWN, PrayerFlowPolicy.afterAdhan(PrayerName.DZUHUR, false, config))
        assertEquals(TVDisplayMode.IQOMAH_COUNTDOWN, PrayerFlowPolicy.afterAdhan(PrayerName.SUBUH, true, config))
        assertEquals(TVDisplayMode.IQOMAH_COUNTDOWN, PrayerFlowPolicy.afterAdhan(PrayerName.DZUHUR, true, config.copy(fridayKhutbahEnabled = false)))
    }
    @Test fun zeroIqomahAndExtremeValuesHaveSafeMeaning() {
        assertEquals(0, PrayerFlowPolicy.iqomahMinutes(PrayerName.MAGHRIB, false, config.copy(iqomahMaghrib = 0)))
        assertEquals(0, PrayerFlowPolicy.iqomahMinutes(PrayerName.SUBUH, false, config.copy(iqomahSubuh = -1)))
        assertEquals(120, PrayerFlowPolicy.iqomahMinutes(PrayerName.ISYA, false, config.copy(iqomahIsya = 999)))
    }
    @Test fun worshipModesUnmountMediaButNormalModeRetainsWallpaper() {
        for (mode in listOf(TVDisplayMode.ADHAN, TVDisplayMode.KHUTBAH_FOCUS, TVDisplayMode.IQOMAH_COUNTDOWN, TVDisplayMode.SHOLAT_SILENT))
            assertTrue(PrayerFlowPolicy.hidesInformation(mode))
        assertFalse(PrayerFlowPolicy.hidesInformation(TVDisplayMode.NORMAL))
    }
    @Test fun locationRejectsOutOfRangeAndNonFiniteValuesWithoutRejectingEquator() {
        assertTrue(PrayerFlowPolicy.validLocation(0.0, 0.0, 0.0))
        assertTrue(PrayerFlowPolicy.validLocation(-6.6961, 107.4239, 7.0))
        assertFalse(PrayerFlowPolicy.validLocation(91.0, 107.0, 7.0))
        assertFalse(PrayerFlowPolicy.validLocation(-6.0, 181.0, 7.0))
        assertFalse(PrayerFlowPolicy.validLocation(-6.0, 107.0, Double.NaN))
        assertFalse(PrayerFlowPolicy.validLocation(Double.POSITIVE_INFINITY, 107.0, 7.0))
    }
    @Test fun localSessionsAreUniqueExpireAndCanBeRevoked() {
        var time = 0L
        val sessions = LocalAdminSessions { time }
        val first = sessions.issue(); val second = sessions.issue()
        assertNotEquals(first, second)
        assertTrue(sessions.contains(first))
        sessions.remove(first); assertFalse(sessions.contains(first))
        time = 8 * 60 * 60 * 1000L
        assertFalse(sessions.contains(second))
        val third = sessions.issue(); sessions.clear(); assertFalse(sessions.contains(third))
    }
}
