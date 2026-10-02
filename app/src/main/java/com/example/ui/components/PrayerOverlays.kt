package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.BackgroundPresetHelper
import com.example.data.model.MosqueConfig
import com.example.data.model.PrayerName
import com.example.ui.theme.*

// -------------------------------------------------------------------------
// RESPONSIVE PRAYER OFFICER BADGES (UI/UX Pro Max Elevated Glass Cards)
// -------------------------------------------------------------------------
private data class PrayerOfficerBadgeInfo(
    val icon: String,
    val roleTitle: String,
    val name: String,
    val borderColor: Color,
    val titleColor: Color
)

@Composable
fun PrayerOfficersBadges(
    imamName: String = "",
    muadzinName: String = "",
    khotibName: String = "",
    bilalName: String = "",
    isFridayDzuhur: Boolean = false,
    isHariRaya: Boolean = false,
    modifier: Modifier = Modifier
) {
    val badges = remember(imamName, muadzinName, khotibName, bilalName, isFridayDzuhur, isHariRaya) {
        val list = mutableListOf<PrayerOfficerBadgeInfo>()

        if ((isFridayDzuhur || isHariRaya) && khotibName.isNotBlank()) {
            list.add(
                PrayerOfficerBadgeInfo(
                    icon = "🎙️",
                    roleTitle = if (isHariRaya) "KHOTIB HARI RAYA" else "KHOTIB JUM'AT",
                    name = khotibName,
                    borderColor = AccentGoldVibrant,
                    titleColor = AccentGoldLight
                )
            )
        }

        if (imamName.isNotBlank()) {
            list.add(
                PrayerOfficerBadgeInfo(
                    icon = "👳",
                    roleTitle = if (isHariRaya) "IMAM SHOLAT IED" else "IMAM SHOLAT",
                    name = imamName,
                    borderColor = EmeraldAccentGlow,
                    titleColor = SleekEmerald200
                )
            )
        }

        if (muadzinName.isNotBlank()) {
            list.add(
                PrayerOfficerBadgeInfo(
                    icon = if (isHariRaya) "🌙" else "📢",
                    roleTitle = if (isHariRaya) "PEMANDU TAKBIR" else "MUADZIN",
                    name = muadzinName,
                    borderColor = AccentGoldPrimary,
                    titleColor = AccentGoldLight
                )
            )
        }

        if ((isFridayDzuhur || isHariRaya) && bilalName.isNotBlank()) {
            list.add(
                PrayerOfficerBadgeInfo(
                    icon = "📜",
                    roleTitle = if (isHariRaya) "BILAL / PROTOKOL" else "BILAL JUM'AT",
                    name = bilalName,
                    borderColor = SleekEmerald400,
                    titleColor = SleekEmerald200
                )
            )
        }

        list
    }

    if (badges.isEmpty()) return

    if (badges.size <= 2) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            badges.forEach { badge ->
                OfficerBadgeCardItem(
                    badge = badge,
                    modifier = Modifier.widthIn(min = 280.dp, max = 400.dp)
                )
            }
        }
    } else {
        // 2 Baris x 2 Kolom untuk responsive nama panjang di layar TV 1080p
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                badges.take(2).forEach { badge ->
                    OfficerBadgeCardItem(
                        badge = badge,
                        modifier = Modifier.widthIn(min = 290.dp, max = 420.dp)
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                badges.drop(2).forEach { badge ->
                    OfficerBadgeCardItem(
                        badge = badge,
                        modifier = Modifier.widthIn(min = 290.dp, max = 420.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun OfficerBadgeCardItem(
    badge: PrayerOfficerBadgeInfo,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        TvSurfaceElevated.copy(alpha = 0.90f),
                        TvSurfaceDeep.copy(alpha = 0.96f)
                    )
                )
            )
            .border(
                1.5.dp,
                Brush.horizontalGradient(
                    listOf(badge.borderColor.copy(alpha = 0.85f), badge.borderColor.copy(alpha = 0.35f))
                ),
                RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 18.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(badge.borderColor.copy(alpha = 0.16f))
                .border(1.2.dp, badge.borderColor.copy(alpha = 0.45f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(badge.icon, fontSize = 20.sp)
        }

        Column(
            modifier = Modifier.weight(1f, fill = false),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = badge.roleTitle,
                fontSize = 11.5.sp,
                color = badge.titleColor,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.8.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = badge.name,
                fontSize = if (badge.name.length > 22) 17.sp else 20.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimaryHigh,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// -------------------------------------------------------------------------
// 0. PRE-ADHAN COUNTDOWN OVERLAY (JELANG WAKTU SHOLAT — UI/UX PRO MAX)
// -------------------------------------------------------------------------
@Composable
fun PreAdhanCountdownOverlay(
    prayerName: PrayerName,
    remainingSeconds: Int,
    imamName: String = "",
    muadzinName: String = "",
    khotibName: String = "",
    bilalName: String = "",
    isFriday: Boolean = false,
    config: MosqueConfig? = null,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mins = remainingSeconds / 60
    val secs = remainingSeconds % 60
    val timeFormatted = if (mins > 0) {
        String.format(java.util.Locale.getDefault(), "%02d:%02d", mins, secs)
    } else {
        String.format(java.util.Locale.getDefault(), "%02d", secs)
    }

    val isUrgent = remainingSeconds in 1..10
    val isHariRaya = prayerName == PrayerName.IDUL_FITRI || prayerName == PrayerName.IDUL_ADHA
    val isFridayDzuhur = (isFriday && prayerName == PrayerName.DZUHUR) && !isHariRaya
    val prayerTitle = when {
        isHariRaya -> prayerName.displayName.uppercase()
        isFridayDzuhur -> "JUM'AT"
        else -> prayerName.displayName.uppercase()
    }

    val customCountdownBitmap = remember(config?.customCountdownBgPath) {
        val path = config?.customCountdownBgPath.orEmpty()
        com.example.util.BitmapUtils.decodeSampledBitmapFromFile(path, 1920, 1080)
    }

    val countdownResId = remember(config?.countdownBgPreset) {
        val preset = config?.countdownBgPreset ?: BackgroundPresetHelper.PRESET_COUNTDOWN_PLAQUE
        BackgroundPresetHelper.getDrawableResId(preset) ?: R.drawable.bg_preset_countdown_plaque
    }

    val infiniteTransition = rememberInfiniteTransition(label = "preadhan_pulse")
    val urgentAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(550, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "urgent_alpha"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // High quality Islamic Stage / Plaque Background
        if (customCountdownBitmap != null) {
            Image(
                bitmap = customCountdownBitmap,
                contentDescription = "Countdown Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = countdownResId),
                contentDescription = "Countdown Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Deep contrast scrim with sacred radial glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isUrgent) {
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xD97F1D1D).copy(alpha = urgentAlpha),
                                Color(0xCC09121E),
                                Color(0x99000000)
                            ),
                            radius = 1050f
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                TvSurfaceDeep.copy(alpha = 0.88f),
                                Color(0xCC000000),
                                Color(0x66000000)
                            ),
                            radius = 1050f
                        )
                    }
                )
        )

        // Close / Dismiss button (10-foot TV Remote / pointer click)
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .size(46.dp)
                .clip(CircleShape)
                .background(TvSurfaceElevated.copy(alpha = 0.75f))
                .border(1.dp, Color(0x33FFFFFF), CircleShape)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Tutup",
                tint = TextPrimaryHigh,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            // Header Pill Badge
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0x33D4AF37))
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(
                            listOf(AccentGoldLight, AccentGoldVibrant, AccentGoldPrimary)
                        ),
                        RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 26.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("✦", fontSize = 13.sp, color = AccentGoldVibrant)
                    Text(
                        text = when {
                            isHariRaya -> "GEMA TAKBIR & PERSIAPAN $prayerTitle"
                            isFridayDzuhur -> "HITUNG MUNDUR MENJELANG SHOLAT JUM'AT"
                            else -> "HITUNG MUNDUR MENJELANG WAKTU SHOLAT"
                        },
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentGoldLight,
                        letterSpacing = 2.sp
                    )
                    Text("✦", fontSize = 13.sp, color = AccentGoldVibrant)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isHariRaya) "PERSIAPAN $prayerTitle" else "PERSIAPAN SHOLAT $prayerTitle",
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimaryHigh,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Massive Digital Countdown Box (Gold Gradient Rim / Pulsing Crimson Alert)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color(0xF5000000))
                    .border(
                        3.5.dp,
                        if (isUrgent) {
                            Brush.sweepGradient(listOf(ExpenseRed, Color(0xFFF87171), ExpenseRed))
                        } else {
                            Brush.sweepGradient(
                                listOf(AccentGoldLight, AccentGoldVibrant, AccentGoldPrimary, AccentGoldLight)
                            )
                        },
                        RoundedCornerShape(26.dp)
                    )
                    .padding(horizontal = 60.dp, vertical = 10.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = timeFormatted,
                        fontSize = 138.sp,
                        lineHeight = 126.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isUrgent) ExpenseRed else AccentGoldVibrant,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = (-2).sp
                    )
                    if (mins == 0) {
                        Spacer(modifier = Modifier.width(14.dp))
                        Text(
                            text = "detik",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isUrgent) Color(0xFFFCA5A5) else SleekAmber300,
                            modifier = Modifier.padding(bottom = 22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Imam, Khotib, Muadzin, Bilal Badges
            PrayerOfficersBadges(
                imamName = imamName,
                muadzinName = muadzinName,
                khotibName = khotibName,
                bilalName = bilalName,
                isFridayDzuhur = isFridayDzuhur,
                isHariRaya = isHariRaya
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Guidance Chip
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(TvSurfaceGlass)
                    .border(1.2.dp, EmeraldAccentBorder, RoundedCornerShape(50.dp))
                    .padding(horizontal = 30.dp, vertical = 10.dp)
            ) {
                Text(
                    text = when {
                        isHariRaya -> "🌸 Disunnahkan mandi hari raya, berwudhu, memakai pakaian terbaik & memperbanyak takbir"
                        isFridayDzuhur -> "💧 Silakan mandi sunnah, berwudhu, memakai wewangian & mengisi shaf terdepan"
                        else -> "💧 Silakan mengambil air wudhu & bersiap mengisi shaf terdepan"
                    },
                    fontSize = 15.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldAccentGlow
                )
            }
        }
    }
}

// -------------------------------------------------------------------------
// 1. ADHAN FULLSCREEN OVERLAY (MAJESTIC SACRED MOMENT — UI/UX PRO MAX)
// -------------------------------------------------------------------------
@Composable
fun AdhanOverlay(
    prayerName: PrayerName,
    imamName: String = "",
    muadzinName: String = "",
    khotibName: String = "",
    bilalName: String = "",
    isFriday: Boolean = false,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "adhan_pulse")
    val glowScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_scale"
    )

    val isHariRaya = prayerName == PrayerName.IDUL_FITRI || prayerName == PrayerName.IDUL_ADHA
    val isFridayDzuhur = (isFriday && prayerName == PrayerName.DZUHUR) && !isHariRaya
    val prayerTitle = when {
        isHariRaya -> prayerName.displayName.uppercase()
        isFridayDzuhur -> "JUM'AT"
        else -> prayerName.displayName.uppercase()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    listOf(
                        Color(0xFF065F46), // Nabawi rich emerald
                        Color(0xFF03281E),
                        Color(0xFF01140E)  // Deep obsidian emerald
                    ),
                    radius = 1200f
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Top Right Close button for TV remote/click
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(46.dp)
                .clip(CircleShape)
                .background(TvSurfaceElevated.copy(alpha = 0.75f))
                .border(1.dp, Color(0x33FFFFFF), CircleShape)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Tutup",
                tint = TextPrimaryHigh,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Illuminated Mosque Logo Emblem with Pulsing Aura Ring
            Box(
                contentAlignment = Alignment.Center
            ) {
                // Outer subtle glowing aura ring
                Box(
                    modifier = Modifier
                        .size(112.dp * glowScale)
                        .clip(CircleShape)
                        .background(Color(0x1AFBBF24))
                        .border(1.5.dp, AccentGoldVibrant.copy(alpha = 0.45f), CircleShape)
                )

                // Inner Emblem
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(AccentGoldVibrant, Color(0xFFB45309))
                            )
                        )
                        .border(2.5.dp, AccentGoldLight, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_masjid_logo),
                        contentDescription = "Logo Adzan",
                        modifier = Modifier.size(56.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Header Status Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0x33D4AF37))
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(
                            listOf(AccentGoldLight, AccentGoldVibrant, AccentGoldPrimary)
                        ),
                        RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 7.dp)
            ) {
                Text(
                    text = when {
                        isHariRaya -> "✦ WAKTU PELAKSANAAN $prayerTitle TELAH TIBA ✦"
                        isFridayDzuhur -> "✦ WAKTU SHOLAT JUM'AT TELAH TIBA ✦"
                        else -> "✦ WAKTU SHOLAT TELAH TIBA ✦"
                    },
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentGoldLight,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isHariRaya) "GEMA TAKBIR $prayerTitle BERKUMANDANG" else "ADZAN $prayerTitle BERKUMANDANG",
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimaryHigh,
                textAlign = TextAlign.Center,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Majestic Arabic Calligraphy
            Text(
                text = if (isHariRaya) "اللهُ أَكْبَرُ اللهُ أَكْبَرُ • لَا إِلٰهَ إِلَّا اللهُ وَاللهُ أَكْبَرُ" else "حَيَّ عَلَى الصَّلَاةِ • حَيَّ عَلَى الْفَلَاحِ",
                fontSize = 42.sp,
                lineHeight = 52.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = AccentGoldVibrant,
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (isHariRaya) "“Allah Maha Besar, Allah Maha Besar • Tiada Tuhan selain Allah dan Allah Maha Besar”" else "“Marilah mendirikan sholat • Marilah meraih kemenangan”",
                fontSize = 16.5.sp,
                fontWeight = FontWeight.Medium,
                color = TextSecondaryMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Officers Badges (Imam, Muadzin, Khotib, Bilal)
            PrayerOfficersBadges(
                imamName = imamName,
                muadzinName = muadzinName,
                khotibName = khotibName,
                bilalName = bilalName,
                isFridayDzuhur = isFridayDzuhur,
                isHariRaya = isHariRaya
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Congregational Prompt
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(TvSurfaceGlass)
                    .border(1.2.dp, EmeraldAccentBorder, RoundedCornerShape(50.dp))
                    .padding(horizontal = 30.dp, vertical = 10.dp)
            ) {
                Text(
                    text = when {
                        isHariRaya -> "🌸 Mari bersama-sama mengumandangkan takbir, tahmid & tahlil dengan penuh khusyuk"
                        isFridayDzuhur -> "🕌 Mari mendengarkan adzan & khutbah Jum'at dengan khusyuk serta seksama"
                        else -> "🕌 Mari menjawab seruan adzan & bersiap mendirikan sholat berjamaah"
                    },
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldAccentGlow
                )
            }
        }
    }
}

// -------------------------------------------------------------------------
// 2. IQOMAH COUNTDOWN OVERLAY (PRE-PRAYER URGENCY — UI/UX PRO MAX)
// -------------------------------------------------------------------------
@Composable
fun IqomahOverlay(
    prayerName: PrayerName,
    remainingSeconds: Int,
    imamName: String = "",
    khotibName: String = "",
    bilalName: String = "",
    isFriday: Boolean = false,
    config: MosqueConfig? = null,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mins = remainingSeconds / 60
    val secs = remainingSeconds % 60
    val timeFormatted = String.format(java.util.Locale.getDefault(), "%02d:%02d", mins, secs)

    val isLastSeconds = remainingSeconds in 1..10
    val isHariRaya = prayerName == PrayerName.IDUL_FITRI || prayerName == PrayerName.IDUL_ADHA
    val isFridayDzuhur = (isFriday && prayerName == PrayerName.DZUHUR) && !isHariRaya
    val prayerTitle = when {
        isHariRaya -> prayerName.displayName.uppercase()
        isFridayDzuhur -> "JUM'AT"
        else -> prayerName.displayName.uppercase()
    }

    val customCountdownBitmap = remember(config?.customCountdownBgPath) {
        val path = config?.customCountdownBgPath.orEmpty()
        com.example.util.BitmapUtils.decodeSampledBitmapFromFile(path, 1920, 1080)
    }

    val countdownResId = remember(config?.countdownBgPreset) {
        val preset = config?.countdownBgPreset ?: BackgroundPresetHelper.PRESET_COUNTDOWN_PLAQUE
        BackgroundPresetHelper.getDrawableResId(preset) ?: R.drawable.bg_preset_countdown_plaque
    }

    val infiniteTransition = rememberInfiniteTransition(label = "iqomah_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "iqomah_pulse_alpha"
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // High quality Islamic Stage / Plaque Background
        if (customCountdownBitmap != null) {
            Image(
                bitmap = customCountdownBitmap,
                contentDescription = "Iqomah Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = countdownResId),
                contentDescription = "Iqomah Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Center-translucent contrast scrim
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (isLastSeconds) {
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xD9881337).copy(alpha = pulseAlpha),
                                Color(0xCC09121E),
                                Color(0x99000000)
                            ),
                            radius = 1050f
                        )
                    } else {
                        Brush.radialGradient(
                            colors = listOf(
                                TvSurfaceDeep.copy(alpha = 0.90f),
                                Color(0xCC000000),
                                Color(0x66000000)
                            ),
                            radius = 1050f
                        )
                    }
                )
        )

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .size(46.dp)
                .clip(CircleShape)
                .background(TvSurfaceElevated.copy(alpha = 0.75f))
                .border(1.dp, Color(0x33FFFFFF), CircleShape)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Tutup",
                tint = TextPrimaryHigh,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            // Header Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0x33D4AF37))
                    .border(
                        1.5.dp,
                        Brush.horizontalGradient(
                            listOf(AccentGoldLight, AccentGoldVibrant, AccentGoldPrimary)
                        ),
                        RoundedCornerShape(50.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 7.dp)
            ) {
                Text(
                    text = if (isHariRaya) "✦ HITUNG MUNDUR MENJELANG SHOLAT ✦" else "✦ HITUNG MUNDUR MENUJU IQOMAH ✦",
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentGoldLight,
                    letterSpacing = 2.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isHariRaya) "$prayerTitle BERJAMAAH" else "SHOLAT $prayerTitle BERJAMAAH",
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                color = TextPrimaryHigh,
                letterSpacing = (-0.5).sp
            )

            // Imam, Khotib, Bilal Badges
            if (imamName.isNotBlank() || khotibName.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                PrayerOfficersBadges(
                    imamName = imamName,
                    muadzinName = "",
                    khotibName = khotibName,
                    bilalName = bilalName,
                    isFridayDzuhur = isFridayDzuhur,
                    isHariRaya = isHariRaya
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Massive Digital Countdown Box
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color(0xF5000000))
                    .border(
                        3.5.dp,
                        if (isLastSeconds) {
                            Brush.sweepGradient(listOf(ExpenseRed, Color(0xFFF87171), ExpenseRed))
                        } else {
                            Brush.sweepGradient(
                                listOf(AccentGoldLight, AccentGoldVibrant, AccentGoldPrimary, AccentGoldLight)
                            )
                        },
                        RoundedCornerShape(26.dp)
                    )
                    .padding(horizontal = 64.dp, vertical = 10.dp)
            ) {
                Text(
                    text = timeFormatted,
                    fontSize = 138.sp,
                    lineHeight = 126.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isLastSeconds) ExpenseRed else AccentGoldVibrant,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = (-2).sp
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Dual Guidance & Reminder Badges (Side-by-Side elevated glass cards)
            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    TvSurfaceElevated.copy(alpha = 0.90f),
                                    TvSurfaceDeep.copy(alpha = 0.95f)
                                )
                            )
                        )
                        .border(1.2.dp, EmeraldAccentBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 22.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.VolumeOff,
                        contentDescription = "Silent HP",
                        tint = AccentGoldVibrant,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Matikan / Senyapkan Nada Dering HP",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryHigh
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    TvSurfaceElevated.copy(alpha = 0.90f),
                                    TvSurfaceDeep.copy(alpha = 0.95f)
                                )
                            )
                        )
                        .border(1.2.dp, EmeraldAccentBorder, RoundedCornerShape(16.dp))
                        .padding(horizontal = 22.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        Icons.Default.NotificationsActive,
                        contentDescription = "Shaf",
                        tint = EmeraldAccentGlow,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = if (isHariRaya) "Luruskan dan Rapatkan Shaf Sholat Ied" else "Luruskan dan Rapatkan Shaf Sholat",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryHigh
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. SHOLAT SILENT (DIMMED / HENING) OVERLAY (SOLEMN OLED BLACK — UI/UX PRO MAX)
// -------------------------------------------------------------------------
@Composable
fun SholatSilentOverlay(
    prayerName: PrayerName = PrayerName.DZUHUR,
    remainingSeconds: Int,
    imamName: String = "",
    khotibName: String = "",
    bilalName: String = "",
    isFriday: Boolean = false,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mins = remainingSeconds / 60
    val secs = remainingSeconds % 60
    val timeFormatted = String.format(java.util.Locale.getDefault(), "%02d:%02d", mins, secs)

    val isHariRaya = prayerName == PrayerName.IDUL_FITRI || prayerName == PrayerName.IDUL_ADHA
    val isFridayDzuhur = (isFriday && prayerName == PrayerName.DZUHUR) && !isHariRaya
    val prayerTitle = when {
        isHariRaya -> prayerName.displayName.uppercase()
        isFridayDzuhur -> "JUM'AT"
        else -> prayerName.displayName.uppercase()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF000000)) // True OLED Black to protect prayer reverence
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0x1AFFFFFF))
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Tutup",
                tint = Color(0x66FFFFFF),
                modifier = Modifier.size(24.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Soft glowing pulse status
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(EmeraldAccentGlow)
                )
                Text(
                    text = if (isHariRaya) "$prayerTitle & KHUTBAH SEDANG BERLANGSUNG" else "SHOLAT $prayerTitle BERJAMAAH SEDANG BERLANGSUNG",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = EmeraldAccentGlow,
                    letterSpacing = 1.5.sp,
                    textAlign = TextAlign.Center
                )
            }

            // Tampilkan semua petugas (Khotib, Imam, Bilal) untuk Jumat & Hari Raya
            if (imamName.isNotBlank() || khotibName.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                PrayerOfficersBadges(
                    imamName = imamName,
                    muadzinName = "",
                    khotibName = khotibName,
                    bilalName = bilalName,
                    isFridayDzuhur = isFridayDzuhur,
                    isHariRaya = isHariRaya
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Majestic Arabic Calligraphy (Reverent Gold)
            Text(
                text = if (isHariRaya || isFridayDzuhur) "سَوُّوا صُفُوفَكُمْ وَاسْتَمِعُوا لِلْخُطْبَةِ رَحِمَكُمُ اللهُ" else "سَوُّوا صُفُوفَكُمْ فَإِنَّ تَسْوِيَةَ الصُّفُوفِ مِنْ إِقَامَةِ الصَّلَاةِ",
                fontSize = 38.sp,
                lineHeight = 50.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = AccentGoldPrimary.copy(alpha = 0.95f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = when {
                    isHariRaya -> "“Luruskan shaf, dirikan sholat dan dengarkan khutbah dengan khusyuk serta seksama.”"
                    isFridayDzuhur -> "“Luruskan shaf, dengarkan khutbah Jum'at dan dirikan sholat dengan khusyuk serta seksama.”"
                    else -> "“Luruskan shaf-shaf kalian, karena meluruskan shaf adalah bagian dari kesempurnaan sholat.” (HR. Bukhari & Muslim)"
                },
                fontSize = 17.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondaryMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Subtle Dimmed Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0x14FFFFFF))
                    .border(0.5.dp, Color(0x1AFFFFFF), RoundedCornerShape(50.dp))
                    .padding(horizontal = 24.dp, vertical = 7.dp)
            ) {
                Text(
                    text = "Mode Layar Hening ($timeFormatted) • Layar akan kembali aktif seusai sholat",
                    fontSize = 13.5.sp,
                    color = TextMutedLow,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}

// -------------------------------------------------------------------------
// 5. FULLSCREEN VIDEO OVERLAY (PENAYANGAN VIDEO LOKAL — UI/UX PRO MAX)
// -------------------------------------------------------------------------
@Composable
fun FullscreenVideoOverlay(
    videoPath: String,
    title: String,
    isMuted: Boolean = false,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        LocalVideoPlayerView(
            videoPath = videoPath,
            modifier = Modifier.fillMaxSize(),
            isMuted = isMuted,
            isLooping = false,
            volume = 1.0f,
            onCompletion = onDismiss,
            onError = onDismiss
        )

        // Subtle Top Bar Info (Glass Gradient)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent)
                    )
                )
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.horizontalGradient(listOf(Color(0xFFEF4444), Color(0xFFDC2626)))
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "▶ TAYANGAN VIDEO MASJID",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = title.ifBlank { "Dokumentasi & Kajian Masjid" },
                    color = TextPrimaryHigh,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup Video",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. FULLSCREEN CCTV / IP CAMERA LIVE OVERLAY (UI/UX PRO MAX)
// -------------------------------------------------------------------------
@Composable
fun FullscreenCctvOverlay(
    cctvUrl: String,
    title: String = "Live Kamera Masjid",
    isMuted: Boolean = true,
    cameras: List<com.example.data.model.CctvCameraItem> = emptyList(),
    activeCameraId: String = "",
    displayLayout: String = "SINGLE",
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (cameras.isNotEmpty()) {
            MultiCctvPlayerGrid(
                cameras = cameras,
                activeCameraId = activeCameraId,
                displayLayout = displayLayout,
                modifier = Modifier.fillMaxSize(),
                isMuted = isMuted
            )
        } else {
            RtspCctvPlayerView(
                rtspUrl = cctvUrl,
                modifier = Modifier.fillMaxSize(),
                isMuted = isMuted
            )
        }

        // Top sleek title bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.88f), Color.Transparent)
                    )
                )
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFDC2626))
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Text(
                            text = "LIVE CCTV MASJID",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                val currentCamName = cameras.firstOrNull { it.id == activeCameraId }?.name ?: title
                Text(
                    text = if (displayLayout != "SINGLE" && cameras.size > 1) "Multi-Kamera CCTV ($displayLayout)" else currentCamName.ifBlank { "Siaran Langsung CCTV Masjid" },
                    color = TextPrimaryHigh,
                    fontSize = 16.5.sp,
                    fontWeight = FontWeight.Bold
                )

                if (isMuted) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color.Black.copy(alpha = 0.5f))
                            .border(0.5.dp, Color(0x33FFFFFF), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "🔇 Audio Senyap",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .border(1.dp, Color(0x33FFFFFF), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tutup Siaran CCTV",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
