package com.example

import com.example.util.ScreenCustomization
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class ScreenCustomizationTest {
    @Test fun invalidStoredDataFallsBackWithoutCrashingDisplay() {
        assertEquals(ScreenCustomization.Component(), ScreenCustomization.component("broken", "clock"))
    }
    @Test fun untrustedStyleValuesAreBoundedAndUnknownComponentsRemoved() {
        val json = ScreenCustomization.normalize("""{"clock":{"visible":false,"opacity":9,"size":0,"background":"url(evil)","color":"#123456"},"unknown":{"visible":true}}""")
        val c = ScreenCustomization.component(json, "clock")
        assertFalse(c.visible); assertEquals(1f, c.opacity); assertEquals(0.7f, c.size)
        assertEquals("#102B24", c.background); assertEquals("#123456", c.color)
        assertFalse(json.contains("unknown"))
    }
    @Test(expected = IllegalArgumentException::class) fun malformedAdminJsonIsRejected() {
        ScreenCustomization.normalize("broken")
    }
}
