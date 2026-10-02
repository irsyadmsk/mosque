package com.example.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R

// High precision digital display fonts
val RajdhaniFont = FontFamily(
    Font(R.font.rajdhani_bold, FontWeight.Bold)
)

val OrbitronFont = FontFamily(
    Font(R.font.orbitron_bold, FontWeight.Bold)
)

val RadiolandFont = FontFamily(
    Font(R.font.radioland, FontWeight.Normal),
    Font(R.font.radioland, FontWeight.Bold)
)

/**
 * Resolver untuk preset font jam digital TV
 */
fun getClockFontFamily(fontKey: String?): FontFamily {
    return when (fontKey?.uppercase()?.trim()) {
        "RADIOLAND", "DIGITAL", "LED" -> RadiolandFont
        "ORBITRON", "FUTURISTIC", "TECH" -> OrbitronFont
        "RAJDHANI", "CONDENSED", "MODERN" -> RajdhaniFont
        "SANS_SERIF", "ROBOTO", "CLEAN", "SANS" -> FontFamily.SansSerif
        "SERIF", "CLASSIC", "ARABIC" -> FontFamily.Serif
        "MONOSPACE", "RETRO", "TERMINAL" -> FontFamily.Monospace
        else -> RadiolandFont
    }
}

// Set of Material typography styles to start with
val Typography =
  Typography(
    bodyLarge =
      TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
      )
  )

// =========================================================================
// UI/UX Pro Max — 10-Foot UI Standard Typography Scales (1080p Landscape)
// =========================================================================
val TvPrayerTimeStyle = TextStyle(
    fontFamily = RajdhaniFont,
    fontWeight = FontWeight.Bold,
    fontSize = 30.sp,
    letterSpacing = 0.5.sp
)

val TvPrayerTimeActiveStyle = TextStyle(
    fontFamily = RajdhaniFont,
    fontWeight = FontWeight.Bold,
    fontSize = 33.sp,
    letterSpacing = 0.5.sp
)

val TvPrayerNameStyle = TextStyle(
    fontWeight = FontWeight.Black,
    fontSize = 13.sp,
    letterSpacing = 1.sp
)

val TvPrayerArabicStyle = TextStyle(
    fontFamily = FontFamily.Serif,
    fontWeight = FontWeight.Bold,
    fontSize = 13.sp
)

val TvCountdownBadgeStyle = TextStyle(
    fontFamily = OrbitronFont,
    fontWeight = FontWeight.Bold,
    fontSize = 12.5.sp,
    letterSpacing = 0.5.sp
)
