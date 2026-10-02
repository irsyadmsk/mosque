package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.BitmapFactory
import java.io.File
import com.example.R
import com.example.data.model.MosqueConfig
import com.example.ui.theme.*

@Composable
fun HeaderBar(
    config: MosqueConfig,
    gregorianDate: String,
    hijriDate: String,
    localIp: String = "",
    modifier: Modifier = Modifier
) {
    val (headerBg, headerBorder) = when (config.activeTheme) {
        "NABAWI_EMERALD" -> Pair(
            Brush.horizontalGradient(listOf(NabawiEmeraldPrimary.copy(alpha = 0.85f), NabawiEmeraldDark.copy(alpha = 0.95f), NabawiEmeraldPrimary.copy(alpha = 0.85f))),
            NabawiGoldPrimary.copy(alpha = 0.45f)
        )
        "KISWAH_GOLD" -> Pair(
            Brush.horizontalGradient(listOf(KiswahObsidianPrimary.copy(alpha = 0.9f), KiswahObsidianDark.copy(alpha = 0.98f), KiswahObsidianPrimary.copy(alpha = 0.9f))),
            KiswahGoldPrimary.copy(alpha = 0.55f)
        )
        "OTTOMAN_BLUE" -> Pair(
            Brush.horizontalGradient(listOf(OttomanSapphirePrimary.copy(alpha = 0.85f), OttomanSapphireDark.copy(alpha = 0.95f), OttomanSapphirePrimary.copy(alpha = 0.85f))),
            OttomanTurquoise.copy(alpha = 0.45f)
        )
        "ROYAL_NAVY" -> Pair(
            Brush.horizontalGradient(listOf(RoyalNavyPrimary.copy(alpha = 0.85f), RoyalNavyDark.copy(alpha = 0.95f), RoyalNavyPrimary.copy(alpha = 0.85f))),
            RoyalCyanAccent.copy(alpha = 0.35f)
        )
        "SUNSET_AMBER" -> Pair(
            Brush.horizontalGradient(listOf(SunsetAmberPrimary.copy(alpha = 0.85f), SunsetAmberDark.copy(alpha = 0.95f), SunsetAmberPrimary.copy(alpha = 0.85f))),
            SunsetOrangeAccent.copy(alpha = 0.35f)
        )
        "MIDNIGHT_CHARCOAL" -> Pair(
            Brush.horizontalGradient(listOf(MidnightCharcoalPrimary.copy(alpha = 0.85f), MidnightCharcoalDark.copy(alpha = 0.95f), MidnightCharcoalPrimary.copy(alpha = 0.85f))),
            IslamicGoldPrimary.copy(alpha = 0.35f)
        )
        else -> Pair(
            Brush.horizontalGradient(
                listOf(
                    TvSurfaceElevated.copy(alpha = 0.92f),
                    TvSurfaceDeep.copy(alpha = 0.96f),
                    TvSurfaceElevated.copy(alpha = 0.92f)
                )
            ),
            AccentGoldPrimary.copy(alpha = 0.35f)
        )
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(headerBg)
            .border(1.2.dp, headerBorder, RoundedCornerShape(16.dp))
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: Mosque Identity & Logo
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            // Load custom logo from disk if available
            val customLogoBitmap = remember(config.customLogoImagePath) {
                com.example.util.BitmapUtils.decodeSampledBitmapFromFile(config.customLogoImagePath, 256, 256)
            }

            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(AccentGoldVibrant, AccentGoldPrimary)
                        )
                    )
                    .border(2.dp, AccentGoldLight.copy(alpha = 0.85f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (customLogoBitmap != null) {
                    Image(
                        bitmap = customLogoBitmap,
                        contentDescription = "Logo Masjid",
                        modifier = Modifier.size(42.dp).clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.ic_masjid_logo),
                        contentDescription = "Logo Masjid",
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = config.mosqueName.uppercase(),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextPrimaryHigh,
                    letterSpacing = 0.2.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = if (config.tagline.isNotBlank()) config.tagline.uppercase() else "SMART MOSQUE SYSTEM",
                        fontSize = 11.5.sp,
                        color = AccentGoldVibrant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    val addrText = listOf(config.address, config.city).filter { it.isNotBlank() }.joinToString(" ")
                    if (addrText.isNotBlank()) {
                        Text(
                            text = "• $addrText",
                            fontSize = 11.5.sp,
                            color = TextSecondaryMedium,
                            fontWeight = FontWeight.Normal,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (localIp.isNotBlank() && localIp != "127.0.0.1") {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0x2E10B981))
                                .border(0.8.dp, Color(0x6610B981), RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Wifi,
                                    contentDescription = "Remote IP",
                                    tint = EmeraldAccentGlow,
                                    modifier = Modifier.size(11.dp)
                                )
                                Text(
                                    text = "REMOTE: http://$localIp:8080",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldAccentGlow,
                                    fontFamily = OrbitronFont
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Center-Right: Date & Hijri
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_islamic_star),
                    contentDescription = "Islamic Star",
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = hijriDate,
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentGoldVibrant
                )
            }
            Text(
                text = gregorianDate.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextSecondaryMedium,
                letterSpacing = 0.5.sp
            )
        }
    }
}

/**
 * Standalone Prominent Digital Clock Card (Pojok Kanan Atas TV)
 * Dipisah dari HeaderBar agar angka Radioland tampil utuh, besar, dan tidak terpotong.
 */
@Composable
fun DigitalClockCard(
    currentTime: String,
    currentSeconds: String,
    isBlinkColon: Boolean,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier,
    config: MosqueConfig? = null
) {
    val clockFont = getClockFontFamily(config?.clockFontFamily)
    val hourMinColor = parseHexColor(config?.clockColor, TextWhite)
    val colonColor = parseHexColor(config?.clockColonColor, SleekAmber400)
    val secColor = parseHexColor(config?.clockSecondsColor, SleekAmber400)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            TvSurfaceElevated.copy(alpha = 0.95f),
                            TvSurfaceDeep.copy(alpha = 0.98f)
                        )
                    )
                )
                .border(
                    1.6.dp,
                    Brush.linearGradient(
                        listOf(AccentGoldPrimary, AccentGoldVibrant, EmeraldAccentGlow.copy(alpha = 0.4f))
                    ),
                    RoundedCornerShape(16.dp)
                )
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                val timeParts = currentTime.split(":")
                val hours = timeParts.getOrElse(0) { "12" }
                val mins = timeParts.getOrElse(1) { "00" }

                // Hours
                Text(
                    text = hours,
                    fontSize = 58.sp,
                    fontWeight = FontWeight.Bold,
                    color = hourMinColor,
                    fontFamily = clockFont,
                    letterSpacing = 1.sp
                )

                // Pulsing Colon
                Text(
                    text = ":",
                    fontSize = 54.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isBlinkColon) colonColor else Color.Transparent,
                    fontFamily = clockFont,
                    modifier = Modifier.padding(horizontal = 2.dp)
                )

                // Minutes
                Text(
                    text = mins,
                    fontSize = 58.sp,
                    fontWeight = FontWeight.Bold,
                    color = hourMinColor,
                    fontFamily = clockFont,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Seconds & WIB Indicator Column
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = ":$currentSeconds",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = secColor,
                        fontFamily = clockFont,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "WIB",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldAccentGlow,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Settings Action Button for Remote/D-Pad
        IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(Color(0x26000000))
                .border(1.dp, AccentGoldMuted, CircleShape)
                .testTag("btn_settings")
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Pengaturan",
                tint = AccentGoldVibrant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

/**
 * Backward-compatible HeaderBar overload
 */
@Composable
fun HeaderBar(
    config: MosqueConfig,
    currentTime: String,
    currentSeconds: String,
    isBlinkColon: Boolean,
    gregorianDate: String,
    hijriDate: String,
    localIp: String,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        HeaderBar(
            config = config,
            gregorianDate = gregorianDate,
            hijriDate = hijriDate,
            localIp = localIp,
            modifier = Modifier.weight(1f)
        )
        DigitalClockCard(
            currentTime = currentTime,
            currentSeconds = currentSeconds,
            isBlinkColon = isBlinkColon,
            onOpenSettings = onOpenSettings
        )
    }
}
