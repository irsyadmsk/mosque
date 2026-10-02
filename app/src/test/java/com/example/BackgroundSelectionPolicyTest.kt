package com.example

import com.example.util.BackgroundSelectionPolicy
import org.junit.Assert.assertEquals
import org.junit.Test

class BackgroundSelectionPolicyTest {
    private val preset = "PRESET_EMERALD_MIHRAB"

    @Test fun savingOtherSettingsKeepsVideoAndCctv() {
        for (type in listOf("VIDEO", "CCTV", "IMAGE")) {
            assertEquals(type, BackgroundSelectionPolicy.resolveType(type, preset,
                mapOf("mosqueName" to "Masjid", "mainScreenBgPreset" to preset)))
        }
    }

    @Test fun explicitSelectionCanRestoreTheSamePreset() {
        assertEquals("PRESET", BackgroundSelectionPolicy.resolveType("VIDEO", preset,
            mapOf("mainScreenBgPreset" to preset, "backgroundType" to "PRESET")))
    }

    @Test fun aDifferentPresetIsAnIntentionalSwitch() {
        assertEquals("PRESET", BackgroundSelectionPolicy.resolveType("CCTV", preset,
            mapOf("mainScreenBgPreset" to "PRESET_MIDNIGHT_KISWAH")))
        assertEquals("IMAGE", BackgroundSelectionPolicy.resolveType("PRESET", preset,
            mapOf("mainScreenBgPreset" to "CUSTOM_UPLOAD")))
    }

    @Test fun explicitModeWinsAndUnknownModesDoNotOverwrite() {
        assertEquals("VIDEO", BackgroundSelectionPolicy.resolveType("PRESET", preset,
            mapOf("mainScreenBgPreset" to "PRESET_MIDNIGHT_KISWAH", "backgroundType" to "VIDEO")))
        assertEquals("CCTV", BackgroundSelectionPolicy.resolveType("CCTV", preset,
            mapOf("backgroundType" to "DEFAULT")))
    }
}
