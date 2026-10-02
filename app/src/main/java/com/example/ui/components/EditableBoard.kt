package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.util.ScreenCustomization
import com.example.viewmodel.MasjidTVUiState

private fun ScreenCustomization.Component.ink() = Color(android.graphics.Color.parseColor(color))
private fun ScreenCustomization.Component.surface() = Color(android.graphics.Color.parseColor(background)).copy(alpha = opacity)
private fun Modifier.panel(c: ScreenCustomization.Component) = clip(RoundedCornerShape(12.dp)).background(c.surface()).padding(12.dp)

/** Admin-configurable layout; clock and prayer data remain live and calculated offline. */
@Composable
fun EditableBoard(state: MasjidTVUiState, onOpenSettings: () -> Unit, onSelectSlide: (Int) -> Unit) {
    val config = state.config
    val parts = remember(config.screenCustomizationJson) { ScreenCustomization.ids.associateWith { ScreenCustomization.component(config.screenCustomizationJson, it) } }
    fun part(id: String) = parts.getValue(id)
    val schedule = state.prayerSchedule
    Column(Modifier.fillMaxSize().padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            val logo = part("logo")
            if (logo.visible) {
                val bitmap = remember(config.customLogoImagePath) { com.example.util.BitmapUtils.decodeSampledBitmapFromFile(config.customLogoImagePath, 256, 256) }
                Box(Modifier.size((64 * logo.size).dp).panel(logo), contentAlignment = Alignment.Center) {
                    if (bitmap != null) Image(bitmap, "Logo masjid", Modifier.fillMaxSize())
                    else Image(painterResource(R.drawable.ic_masjid_logo), "Logo masjid", Modifier.fillMaxSize())
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                val name = part("name")
                if (name.visible) Text(name.text.ifBlank { config.mosqueName }, Modifier.panel(name), color = name.ink(), fontSize = (25 * name.size).sp, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                val address = part("address")
                if (address.visible) Text(address.text.ifBlank { config.address }, Modifier.panel(address), color = address.ink(), fontSize = (13 * address.size).sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                val clock = part("clock")
                if (clock.visible) Text(state.currentTimeString, Modifier.panel(clock).clickable(onClick = onOpenSettings), color = clock.ink(), fontSize = (36 * clock.size).sp, fontWeight = FontWeight.Bold)
                val date = part("date")
                if (date.visible) Text(listOfNotNull(schedule?.dateStr, schedule?.hijriDateStr).joinToString(" · "), Modifier.panel(date), color = date.ink(), fontSize = (12 * date.size).sp, maxLines = 2)
            }
        }
        Row(Modifier.weight(1f).fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            val countdown = part("countdown")
            if (countdown.visible) Column(Modifier.weight(1f).fillMaxHeight().panel(countdown), verticalArrangement = Arrangement.Center) {
                Text(countdown.text.ifBlank { "Menuju salat ${state.nextPrayerInfo?.prayer?.displayName.orEmpty()}" }, color = countdown.ink(), fontSize = (22 * countdown.size).sp, maxLines = 3, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(14.dp))
                Text(state.nextPrayerInfo?.formattedCountdown ?: "--:--:--", color = countdown.ink(), fontSize = (48 * countdown.size).sp, fontWeight = FontWeight.Bold)
                Text("Adzan ${state.nextPrayerInfo?.timeStr ?: "--:--"}", color = countdown.ink(), fontSize = (18 * countdown.size).sp)
            }
            val information = part("information")
            if (information.visible) Box(Modifier.weight(1f).fillMaxHeight().panel(information)) {
                if (information.text.isNotBlank()) Text(information.text, Modifier.align(Alignment.Center), color = information.ink(), fontSize = (24 * information.size).sp, maxLines = 10, overflow = TextOverflow.Ellipsis)
                else CarouselContainer(slideIndex = state.currentSlideIndex, onSelectSlide = onSelectSlide,
                    nextPrayerInfo = state.nextPrayerInfo, config = config.copy(centerCardTransparency = information.opacity),
                    finances = state.finances, totalIncome = state.totalIncome, totalExpense = state.totalExpense,
                    balance = state.balance, fridaySchedule = state.fridaySchedule, activities = state.activities,
                    mediaSlides = state.activeMediaSlides, tarawihSchedule = state.activeTarawihSchedule, activeTarawihNight = state.activeTarawihNight)
            }
        }
        val times = part("schedule")
        if (times.visible) {
            val prayers = listOf(Triple("Imsak", schedule?.imsak, config.showImsak), Triple("Subuh", schedule?.subuh, config.showSubuh),
                Triple("Terbit", schedule?.terbit, config.showTerbit), Triple("Dhuha", schedule?.dhuha, config.showDhuha),
                Triple("Dzuhur", schedule?.dzuhur, config.showDzuhur), Triple("Ashar", schedule?.ashar, config.showAshar),
                Triple("Maghrib", schedule?.maghrib, config.showMaghrib), Triple("Isya", schedule?.isya, config.showIsya)).filter { it.third }
            Row(Modifier.fillMaxWidth().panel(times), horizontalArrangement = Arrangement.SpaceEvenly) {
                prayers.forEach { (label, time, _) -> Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(label, color = times.ink(), fontSize = (14 * times.size).sp)
                    Text(time ?: "--:--", color = times.ink(), fontSize = (26 * times.size).sp, fontWeight = FontWeight.Bold)
                } }
            }
        }
        val ticker = part("ticker")
        if (ticker.visible) Text(ticker.text.ifBlank { state.activeRunningTexts.joinToString("   •   ") { it.text } },
            Modifier.fillMaxWidth().panel(ticker), color = ticker.ink(), fontSize = (20 * ticker.size).sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}
