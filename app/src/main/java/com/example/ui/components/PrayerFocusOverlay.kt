package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Static, opaque surface: no media, ticker or looping motion during worship. */
@Composable
fun PrayerFocusOverlay(khutbah: Boolean, blank: Boolean, showClock: Boolean, time: String) {
    Box(Modifier.fillMaxSize().background(Color(0xFF050D0B)).padding(40.dp)) {
        if (!blank) {
            Column(Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(if (khutbah) "Khutbah Jumat berlangsung" else "Salat berjamaah berlangsung",
                    color = Color(0xFFE9EEE9), fontSize = 32.sp, textAlign = TextAlign.Center)
                Spacer(Modifier.height(20.dp))
                Text(if (khutbah) "Mohon tenang dan dengarkan khutbah" else "Mohon senyapkan ponsel dan jaga ketenangan",
                    color = Color(0xFFAEBDB5), fontSize = 22.sp, textAlign = TextAlign.Center)
            }
        }
        if (showClock && !blank) Text(time, modifier = Modifier.align(Alignment.BottomEnd),
            color = Color(0xFFAEBDB5), fontSize = 20.sp)
    }
}
