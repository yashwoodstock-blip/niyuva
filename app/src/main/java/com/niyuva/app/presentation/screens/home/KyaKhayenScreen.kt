package com.niyuva.app.presentation.screens.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.niyuva.app.data.local.content.FoodItem
import com.niyuva.app.data.local.content.KyaKhayenData
import com.niyuva.app.data.local.content.KyaKhayenPhaseData
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PlayfairFamily
import com.niyuva.app.presentation.theme.PureWhite

// ─────────────────────────────────────────────────────────────────────────────
// Phase tab metadata
// ─────────────────────────────────────────────────────────────────────────────

private data class PhaseTab(val phase: CyclePhase, val emoji: String, val shortLabel: String)

private val phaseTabs = listOf(
    PhaseTab(CyclePhase.MENSTRUATION, "\uD83C\uDF39", "Period"),
    PhaseTab(CyclePhase.FOLLICULAR,   "\uD83C\uDF38", "Follicular"),
    PhaseTab(CyclePhase.OVULATION,    "\u2728",       "Ovulation"),
    PhaseTab(CyclePhase.LUTEAL,       "\uD83E\uDD0D", "Luteal")
)

// ─────────────────────────────────────────────────────────────────────────────
// Screen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun KyaKhayenScreen(navController: NavController, modifier: Modifier = Modifier) {
    var selectedPhase by rememberSaveable { mutableStateOf(CyclePhase.MENSTRUATION) }
    val data = remember(selectedPhase) { KyaKhayenData.forPhase(selectedPhase) }
    val accentColor = remember(data.accentColorHex) {
        Color(android.graphics.Color.parseColor(data.accentColorHex))
    }
    val bgColor = remember(data.backgroundColorHex) {
        Color(android.graphics.Color.parseColor(data.backgroundColorHex))
    }
    val animatedBg by animateColorAsState(targetValue = bgColor, animationSpec = tween(500), label = "bgAnim")
    val animatedAccent by animateColorAsState(targetValue = accentColor, animationSpec = tween(500), label = "accentAnim")

    Box(modifier = modifier.fillMaxSize().background(animatedBg)) {
        // Watermark
        Box(modifier = Modifier.fillMaxSize().alpha(0.04f), contentAlignment = Alignment.BottomEnd) {
            Text(text = "\uD83C\uDF43", fontSize = 260.sp, modifier = Modifier.padding(bottom = 40.dp))
        }
        Column(modifier = Modifier.fillMaxSize().statusBarsPadding()) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth().height(56.dp).padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.size(40.dp)) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = animatedAccent, modifier = Modifier.size(24.dp))
                }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = "Kya Khayen? \uD83C\uDF43", fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = animatedAccent)
                }
                Spacer(modifier = Modifier.width(40.dp))
            }
            // Phase tab chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 12.dp)
            ) {
                items(phaseTabs) { tab ->
                    PhaseTabChip(tab = tab, isSelected = selectedPhase == tab.phase, accentColor = animatedAccent, onClick = { selectedPhase = tab.phase })
                }
            }
            // Animated phase content
            AnimatedContent(
                targetState = selectedPhase,
                transitionSpec = { (fadeIn(tween(300)) + slideInVertically { it / 10 }).togetherWith(fadeOut(tween(200))) },
                label = "phaseAnim",
                modifier = Modifier.fillMaxSize()
            ) { phase ->
                val phaseData = KyaKhayenData.forPhase(phase)
                val phaseAccent = remember(phaseData.accentColorHex) {
                    Color(android.graphics.Color.parseColor(phaseData.accentColorHex))
                }
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item(key = "hero") { HeroCard(data = phaseData, accentColor = phaseAccent) }
                    item(key = "food_label") {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.width(4.dp).height(20.dp).clip(RoundedCornerShape(2.dp)).background(phaseAccent))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "\u2728 Yeh kha \u2014 teri body ko chahiye", fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = phaseAccent, letterSpacing = 0.3.sp)
                        }
                    }
                    val pairs = phaseData.foods.chunked(2)
                    items(pairs, key = { "food_row_" + pairs.indexOf(it) }) { pair ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            pair.forEach { food -> FoodCard(food = food, accentColor = phaseAccent, modifier = Modifier.weight(1f)) }
                            if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    item(key = "avoid") { AvoidCard(text = phaseData.avoidNote) }
                    item(key = "tip") { DidiTipCard(text = phaseData.bonusTip, accentColor = phaseAccent) }
                    item(key = "combos") { NutrientCombosCard(phase = phase, accentColor = phaseAccent) }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Phase Tab Chip
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PhaseTabChip(tab: PhaseTab, isSelected: Boolean, accentColor: Color, onClick: () -> Unit) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.05f else 1.0f, animationSpec = spring(stiffness = Spring.StiffnessMediumLow), label = "scale")
    val bg by animateColorAsState(targetValue = if (isSelected) accentColor else accentColor.copy(alpha = 0.10f), animationSpec = tween(250), label = "chipBg")
    val tc by animateColorAsState(targetValue = if (isSelected) PureWhite else accentColor, animationSpec = tween(250), label = "chipText")
    Row(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(50.dp))
            .background(bg)
            .border(1.dp, if (isSelected) Color.Transparent else accentColor.copy(alpha = 0.3f), RoundedCornerShape(50.dp))
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(text = tab.emoji, fontSize = 14.sp)
        Text(text = tab.shortLabel, fontFamily = NunitoFamily, fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.SemiBold, fontSize = 13.sp, color = tc)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Hero Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HeroCard(data: KyaKhayenPhaseData, accentColor: Color) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
    ) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.verticalGradient(listOf(accentColor.copy(alpha = 0.18f), accentColor.copy(alpha = 0.06f))))) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(accentColor.copy(alpha = 0.15f)).padding(horizontal = 10.dp, vertical = 4.dp)) {
                        Text(text = data.phaseName, fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold, fontSize = 11.sp, color = accentColor, letterSpacing = 0.8.sp)
                    }
                    Text(text = "${data.foods.size} foods \uD83C\uDF7D\uFE0F", fontFamily = NunitoFamily, fontSize = 11.sp, color = accentColor.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(14.dp))
                Text(text = data.headline, fontFamily = PlayfairFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, color = DeepWarmBrown, lineHeight = 30.sp)
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = accentColor.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = data.subtext, fontFamily = NunitoFamily, fontSize = 14.sp, color = DeepWarmBrown.copy(alpha = 0.75f), lineHeight = 22.sp)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Food Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun FoodCard(food: FoodItem, accentColor: Color, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.border(1.dp, accentColor.copy(alpha = 0.12f), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.size(44.dp).clip(RoundedCornerShape(12.dp)).background(accentColor.copy(alpha = 0.10f)), contentAlignment = Alignment.Center) {
                Text(text = food.emoji, fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = food.name, fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = DeepWarmBrown, lineHeight = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = food.reason, fontFamily = NunitoFamily, fontSize = 11.sp, color = DustyMauve, lineHeight = 15.sp)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Avoid Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun AvoidCard(text: String) {
    Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFFFFF3F3)).border(1.dp, Color(0xFFE57373).copy(alpha = 0.35f), RoundedCornerShape(16.dp)).padding(16.dp)) {
        Column {
            Text(text = "\u26A0\uFE0F Kya Bachein?", fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = Color(0xFFC0445A), modifier = Modifier.padding(bottom = 6.dp))
            Text(text = text, fontFamily = NunitoFamily, fontSize = 13.sp, color = Color(0xFFC0445A).copy(alpha = 0.85f), lineHeight = 20.sp)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Didi Tip Card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DidiTipCard(text: String, accentColor: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.18f), accentColor.copy(alpha = 0.08f))))
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(accentColor.copy(alpha = 0.18f)), contentAlignment = Alignment.Center) {
                Text(text = "\uD83D\uDCA1", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = "Didi ki Salah", fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp, color = accentColor, letterSpacing = 0.5.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = text, fontFamily = NunitoFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = DeepWarmBrown, lineHeight = 20.sp)
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Power Nutrient Combos
// ─────────────────────────────────────────────────────────────────────────────

private data class NutrientCombo(val emoji: String, val title: String, val description: String)

private fun getCombosForPhase(phase: CyclePhase): List<NutrientCombo> = when (phase) {
    CyclePhase.MENSTRUATION -> listOf(
        NutrientCombo("\uD83C\uDF3F + \uD83C\uDF4B", "Iron + Vitamin C Combo", "Palak ke saath nimbu \u2014 Vitamin C iron absorption 3x badhata hai. Alag khaane se iron waste ho jaata hai!"),
        NutrientCombo("\uD83C\uDF6B + \uD83C\uDF30", "Magnesium Combo", "Dark chocolate aur kaju saath khao \u2014 magnesium cramps relax karta hai aur mood lift hota hai."),
        NutrientCombo("\uD83E\uDED9 + \u2615", "Desi Power Pair", "Gud ki chai \u2014 iron aur warmth ek saath. Teri dadi ka nuskha sahi tha!")
    )
    CyclePhase.FOLLICULAR -> listOf(
        NutrientCombo("\uD83E\uDD5A + \uD83E\uDD66", "Protein + Cruciferous", "Ande ke saath broccoli \u2014 protein cell repair karta hai, cruciferous estrogen metabolism support karta hai."),
        NutrientCombo("\uD83C\uDF31 + \uD83C\uDF4B", "Zinc + Vitamin C", "Sprouted moong ke saath nimbu \u2014 zinc egg quality ke liye, Vitamin C absorption boost karta hai."),
        NutrientCombo("\uD83C\uDF75 + \uD83E\uDED2", "Antioxidant Boost", "Green tea aur olive oil \u2014 powerful antioxidant combo for cell protection during this rebuilding phase.")
    )
    CyclePhase.OVULATION -> listOf(
        NutrientCombo("\uD83C\uDF83 + \uD83E\uDD6C", "Zinc + Folate Combo", "Kaddu ke beej aur saag \u2014 zinc ovulation support karta hai, folate uterine health ke liye best hai."),
        NutrientCombo("\uD83D\uDC20 + \uD83C\uDF47", "Omega-3 + Resveratrol", "Macchi aur angoor \u2014 inflammation control karta hai aur hormone balance support karta hai."),
        NutrientCombo("\uD83E\uDD5C + \uD83E\uDED9", "Gut + Brain Combo", "Akhrot aur dahi \u2014 probiotics gut health improve karta hai, omega-3 brain clarity ke liye.")
    )
    CyclePhase.LUTEAL -> listOf(
        NutrientCombo("\uD83C\uDF6B + \uD83C\uDF30", "Magnesium + B6 Combo", "Dark chocolate aur pista \u2014 magnesium bloating kum karta hai, B6 mood stabilize karta hai. PMS ka best combo!"),
        NutrientCombo("\uD83E\uDED1 + \uD83C\uDF3E", "Serotonin Builders", "Dal aur brown rice \u2014 tryptophan + complex carbs = natural serotonin boost. Mood swings se bachao!"),
        NutrientCombo("\uD83E\uDEAB + \uD83E\uDD51", "Calm + Progesterone", "Chamomile chai aur avocado \u2014 chai cortisol kum karta hai, avocado progesterone support karta hai.")
    )
}

@Composable
private fun NutrientCombosCard(phase: CyclePhase, accentColor: Color) {
    val combos = remember(phase) { getCombosForPhase(phase) }
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
            Box(modifier = Modifier.width(4.dp).height(20.dp).clip(RoundedCornerShape(2.dp)).background(accentColor))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "\u26A1 Power Food Combos", fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = accentColor, letterSpacing = 0.3.sp)
        }
        combos.forEachIndexed { idx, combo ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .then(if (idx < combos.size - 1) Modifier.padding(bottom = 10.dp) else Modifier)
                    .border(1.dp, accentColor.copy(alpha = 0.14f), RoundedCornerShape(16.dp))
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                    Box(modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(accentColor.copy(alpha = 0.12f)).padding(horizontal = 8.dp, vertical = 6.dp)) {
                        Text(text = combo.emoji, fontSize = 16.sp, textAlign = TextAlign.Center)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = combo.title, fontFamily = NunitoFamily, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp, color = accentColor)
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(text = combo.description, fontFamily = NunitoFamily, fontSize = 12.sp, color = DeepWarmBrown.copy(alpha = 0.8f), lineHeight = 18.sp)
                    }
                }
            }
        }
    }
}
