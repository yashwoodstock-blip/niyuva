package com.niyuva.app.presentation.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.domain.model.BloodColor
import com.niyuva.app.domain.model.ClotSize
import com.niyuva.app.domain.model.FlowLevel
import com.niyuva.app.domain.model.PainLevel
import com.niyuva.app.presentation.components.LogIconData
import com.niyuva.app.presentation.components.LogSection
import com.niyuva.app.presentation.components.LogSymptomIcon
import com.niyuva.app.presentation.components.NiyuvaBottomSheet
import com.niyuva.app.presentation.components.NiyuvaChip
import com.niyuva.app.presentation.theme.BlushMist
import com.niyuva.app.presentation.theme.CategoryFlow
import com.niyuva.app.presentation.theme.CategoryPain
import com.niyuva.app.presentation.theme.CategorySleep
import com.niyuva.app.presentation.theme.CategoryMood
import com.niyuva.app.presentation.theme.CategorySexual
import com.niyuva.app.presentation.theme.CategoryBirthControl
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.components.NiyuvaTextLink
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PhaseColorMenstruation
import com.niyuva.app.presentation.theme.Terracotta
import com.niyuva.app.presentation.theme.WarmIvory
import com.niyuva.app.presentation.theme.PureWhite
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

// ─────────────────────────────────────────────────────────────────────────────
// Section icon data constants
// ─────────────────────────────────────────────────────────────────────────────

private val flowItems = listOf(
    LogIconData("HEAVY",    "🩸🩸🩸", "Heavy"),
    LogIconData("MEDIUM",   "🩸🩸",   "Medium"),
    LogIconData("LIGHT",    "🩸",     "Light"),
    LogIconData("SPOTTING", "💧",     "Spotting")
)

private val bloodColorItems = listOf(
    LogIconData("PINK",       "🩷", "Pink"),
    LogIconData("BRIGHT_RED", "🔴", "Bright red"),
    LogIconData("DARK_RED",   "🟤", "Dark red"),
    LogIconData("ORANGE",     "🟠", "Orange"),
    LogIconData("BROWN",      "🪵", "Brown")
)

private val clotsItems = listOf(
    LogIconData("NONE",   "✅", "None"),
    LogIconData("SMALL",  "🔵", "Small"),
    LogIconData("MEDIUM", "⚫", "Medium"),
    LogIconData("LARGE",  "⬛", "Large")
)

private val painLevelItems = listOf(
    LogIconData("NONE",     "😊", "No pain"),
    LogIconData("MILD",     "😐", "Mild"),
    LogIconData("MODERATE", "😣", "Moderate"),
    LogIconData("SEVERE",   "😭", "Severe")
)

private val painTypeChips = listOf(
    "cramps"          to "Cramps 🫄",
    "back_ache"       to "Back ache 🔙",
    "headache"        to "Headache 🤯",
    "breast_pain"     to "Breast pain",
    "abdominal_pain"  to "Abdominal pain"
)

private val moodChips = listOf(
    "excited"   to "Excited 🎉",
    "calm"      to "Calm 😌",
    "happy"     to "Happy 😊",
    "stressed"  to "Stressed 😤",
    "irritated" to "Irritated 😠",
    "sad"       to "Sad 😢",
    "anxious"   to "Anxious 😰",
    "loving"    to "Loving 🥰",
    "moody"     to "Moody 🌀",
    "zen"       to "Zen 🧘‍♀️"
)

private val sleepItems = listOf(
    LogIconData("above_9hr", "😴", "Above 9 hrs"),
    LogIconData("6_9hr",     "🌙", "6–9 hrs"),
    LogIconData("3_6hr",     "🌛", "3–6 hrs"),
    LogIconData("0_3hr",     "😵", "0–3 hrs")
)

private val energyItems = listOf(
    LogIconData("low",    "🔋", "Low"),
    LogIconData("normal", "⚡", "Normal"),
    LogIconData("high",   "🚀", "High")
)

private val dischargeItems = listOf(
    LogIconData("dry",      "🏜️", "Dry"),
    LogIconData("cloudy",   "☁️", "Cloudy"),
    LogIconData("watery",   "💧", "Watery"),
    LogIconData("stretchy", "🥚", "Stretchy"),
    LogIconData("creamy",   "🤍", "Creamy")
)



// ─────────────────────────────────────────────────────────────────────────────
// Bottom sheet root
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Log Symptoms bottom sheet — Phase 17 (sections 1–4: Flow, Blood Color, Clots, Pain).
 *
 * Uses [NiyuvaBottomSheet] (ModalBottomSheet) with `skipPartiallyExpanded = true`.
 * Inner content scrolls via [LazyColumn]; the sheet itself is not scrollable.
 * [imePadding] handles keyboard push-up.
 *
 * @param state          Current [LogSheetState].
 * @param onDismiss      Fired when the sheet is dismissed (drag / back press).
 * @param onFlowSelected     Single-select callback for flow level.
 * @param onBloodColorSelected Single-select callback for blood colour.
 * @param onClotSelected     Single-select callback for clot size.
 * @param onPainLevelSelected  Single-select callback for pain intensity.
 * @param onPainTypeToggled  Multi-select toggle for pain type chips.
 * @param onSave         Fired when the save button is tapped (Phase 18 wires this fully).
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun LogSymptomsSheet(
    state: LogSheetState,
    onDismiss: () -> Unit,
    onFlowSelected: (String?) -> Unit,
    onBloodColorSelected: (String?) -> Unit,
    onClotSelected: (String?) -> Unit,
    onPainLevelSelected: (String?) -> Unit,
    onPainTypeToggled: (String) -> Unit,
    onMoodToggled: (String) -> Unit,
    onSleepQualitySelected: (String?) -> Unit,
    onEnergyLevelSelected: (String?) -> Unit,
    onDischargeTypeSelected: (String?) -> Unit,
    onSave: () -> Unit,
    onOpenSaarthi: () -> Unit
) {
    NiyuvaBottomSheet(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
        ) {
            // ── Sheet header ──────────────────────────────────────────────────
            LogSheetHeader(
                selectedDate = state.selectedDate,
                modifier     = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            HorizontalDivider(
                color    = BlushMist,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // ── Mini date strip ───────────────────────────────────────────────
            MiniDateStrip(
                selectedDate = state.selectedDate,
                modifier     = Modifier.fillMaxWidth()
            )

            HorizontalDivider(
                color    = BlushMist,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // ── Scrollable sections ───────────────────────────────────────────
            LazyColumn(
                modifier            = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                contentPadding      = androidx.compose.foundation.layout.PaddingValues(
                    start = 20.dp, end = 20.dp, bottom = 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Section 1 — Flow
                stickyHeader(key = "header_flow") {
                    SectionStickyHeader(title = "🩸  Period Flow")
                }
                item(key = "section_flow") {
                    LogSection(
                        title         = "",
                        items         = flowItems,
                        selectedKey   = state.flowLevel?.name,
                        categoryColor = CategoryFlow,
                        onItemSelected = { key ->
                            onFlowSelected(if (state.flowLevel?.name == key) null else key)
                        }
                    )
                }

                // Section 2 — Blood Color
                stickyHeader(key = "header_color") {
                    SectionStickyHeader(title = "🎨  Blood Color")
                }
                item(key = "section_color") {
                    LogSection(
                        title         = "",
                        items         = bloodColorItems,
                        selectedKey   = state.bloodColor?.name,
                        categoryColor = CategoryFlow,
                        onItemSelected = { key ->
                            onBloodColorSelected(if (state.bloodColor?.name == key) null else key)
                        }
                    )
                }

                // Section 3 — Clots
                stickyHeader(key = "header_clots") {
                    SectionStickyHeader(title = "⚫  Clots")
                }
                item(key = "section_clots") {
                    LogSection(
                        title         = "",
                        items         = clotsItems,
                        selectedKey   = state.clotSize?.name,
                        categoryColor = CategoryFlow,
                        onItemSelected = { key ->
                            onClotSelected(if (state.clotSize?.name == key) null else key)
                        }
                    )
                }

                // Section 4 — Pain
                stickyHeader(key = "header_pain") {
                    SectionStickyHeader(title = "💜  Pain")
                }
                item(key = "section_pain") {
                    PainSection(
                        state              = state,
                        onIntensitySelected = onPainLevelSelected,
                        onTypeToggled      = onPainTypeToggled
                    )
                }

                if (state.flowLevel != null) {
                    // Period Day: Show all optional fields individually
                    stickyHeader(key = "header_mood") {
                        SectionStickyHeader(title = "🎭  Mood")
                    }
                    item(key = "section_mood") {
                        MoodSectionWidget(state, onMoodToggled)
                    }

                    stickyHeader(key = "header_sleep") {
                        SectionStickyHeader(title = "😴  Sleep")
                    }
                    item(key = "section_sleep") {
                        SleepSectionWidget(state, onSleepQualitySelected)
                    }

                    stickyHeader(key = "header_energy") {
                        SectionStickyHeader(title = "⚡  Energy")
                    }
                    item(key = "section_energy") {
                        EnergySectionWidget(state, onEnergyLevelSelected)
                    }

                    stickyHeader(key = "header_discharge") {
                        SectionStickyHeader(title = "💧  Discharge")
                    }
                    item(key = "section_discharge") {
                        DischargeSectionWidget(state, onDischargeTypeSelected)
                    }
                } else {
                    // Non-Period Day: Show rotated fields under a single "Aaj kaisa chal raha hai?" card
                    val todaysPair = getTodaysOptionalCategories(state.selectedDate)
                    val visibleCats = filterAlreadyLoggedViaConversation(todaysPair, state.saarthiLoggedToday)

                    if (visibleCats.isNotEmpty()) {
                        stickyHeader(key = "header_optional_rotated") {
                            SectionStickyHeader(title = androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.aaj_kaisa_title))
                        }

                        item(key = "section_optional_rotated") {
                            androidx.compose.material3.Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = PureWhite),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BlushMist)
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(20.dp)
                                ) {
                                    visibleCats.forEach { cat ->
                                        when (cat) {
                                            OptionalLogCategory.MOOD -> {
                                                Column {
                                                    Text("🎭  Mood", fontFamily = NunitoFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepWarmBrown)
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    MoodSectionWidget(state, onMoodToggled)
                                                }
                                            }
                                            OptionalLogCategory.SLEEP -> {
                                                Column {
                                                    Text("😴  Sleep", fontFamily = NunitoFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepWarmBrown)
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    SleepSectionWidget(state, onSleepQualitySelected)
                                                }
                                            }
                                            OptionalLogCategory.ENERGY -> {
                                                Column {
                                                    Text("⚡  Energy", fontFamily = NunitoFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepWarmBrown)
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    EnergySectionWidget(state, onEnergyLevelSelected)
                                                }
                                            }
                                            OptionalLogCategory.DISCHARGE -> {
                                                Column {
                                                    Text("💧  Discharge", fontFamily = NunitoFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = DeepWarmBrown)
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                    DischargeSectionWidget(state, onDischargeTypeSelected)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

            // ── Error Card ───────────────────────────────────────────────────
            if (state.hasError) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE8965A).copy(alpha = 0.15f))
                        .border(1.dp, Color(0xFFE8965A).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text       = "Kuch hua — phir try karo 💛",
                            fontFamily = NunitoFamily,
                            fontSize   = 13.sp,
                            color      = DeepWarmBrown,
                            modifier   = Modifier.weight(1f)
                        )
                        NiyuvaTextLink(
                            text    = "Retry",
                            onClick = onSave
                        )
                    }
                }
            }

            // ── Bottom: Save button ──────────────────────────────────────────
            val saveText = if (state.isSaving) "Saving..." else "Save"
            NiyuvaPrimaryButton(
                text    = saveText,
                onClick = onSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            )

            // ── Bottom: Saarthi link ──────────────────────────────────────────
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                NiyuvaTextLink(
                    text    = "+ Saarthi se batao",
                    onClick = onOpenSaarthi
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sheet header
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun LogSheetHeader(
    selectedDate: LocalDate,
    modifier: Modifier = Modifier
) {
    Row(
        modifier          = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(48.dp)) // balance the right link

        Text(
            text       = "Log Symptoms",
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize   = 18.sp,
            color      = DeepWarmBrown,
            textAlign  = TextAlign.Center,
            modifier   = Modifier.weight(1f)
        )

        NiyuvaChip(
            text       = "Today",
            isSelected = selectedDate == LocalDate.now(),
            onToggle   = { /* Date picker — Phase 18 */ }
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Mini date strip (5 days centred on today)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun MiniDateStrip(
    selectedDate: LocalDate,
    modifier: Modifier = Modifier
) {
    val today = LocalDate.now()
    val dates = (-2..2).map { today.plusDays(it.toLong()) }

    LazyRow(
        modifier       = modifier,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dates, key = { it.toString() }) { date ->
            val isSelected = date == selectedDate
            val isPeriodDay = false // placeholder — Phase 18 resolves from cycle data

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(44.dp)
            ) {
                Text(
                    text       = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).first().toString(),
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize   = 10.sp,
                    color      = DustyMauve
                )
                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) Terracotta else Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text       = date.dayOfMonth.toString(),
                        fontFamily = NunitoFamily,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize   = 14.sp,
                        color      = if (isSelected) Color.White else DeepWarmBrown
                    )
                }

                // Period day dot
                if (isPeriodDay) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(PhaseColorMenstruation)
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sticky section header
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SectionStickyHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(WarmIvory)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text       = title,
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp,
            color      = DeepWarmBrown
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Pain section (intensity + conditional multi-select chips)
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PainSection(
    state: LogSheetState,
    onIntensitySelected: (String?) -> Unit,
    onTypeToggled: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIntensityKey = state.painLevel?.name

    Column(modifier = modifier) {
        // Pain intensity — single-select icons
        LogSection(
            title         = "",
            items         = painLevelItems,
            selectedKey   = selectedIntensityKey,
            categoryColor = CategoryPain,
            onItemSelected = { key ->
                onIntensitySelected(if (selectedIntensityKey == key) null else key)
            }
        )

        // Pain type chips — animated in/out based on selection (not NONE)
        val showChips = state.painLevel != null && state.painLevel != PainLevel.NONE
        AnimatedVisibility(
            visible = showChips,
            enter   = expandVertically(),
            exit    = shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text       = "Pain types",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp,
                    color      = DustyMauve
                )
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement   = Arrangement.spacedBy(8.dp)
                ) {
                    painTypeChips.forEach { (key, label) ->
                        NiyuvaChip(
                            text       = label,
                            isSelected = key in state.painTypes,
                            onToggle   = { onTypeToggled(key) }
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Mood section widget
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MoodSectionWidget(
    state: LogSheetState,
    onMoodToggled: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement   = Arrangement.spacedBy(8.dp)
    ) {
        moodChips.forEach { (key, label) ->
            NiyuvaChip(
                text       = label,
                isSelected = key in state.moods,
                onToggle   = { onMoodToggled(key) }
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Sleep section widget
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun SleepSectionWidget(
    state: LogSheetState,
    onSleepQualitySelected: (String?) -> Unit
) {
    val selectedKey = state.sleepQuality?.toDbString()
    LogSection(
        title = "",
        items = sleepItems,
        selectedKey = selectedKey,
        categoryColor = CategorySleep,
        onItemSelected = { key ->
            onSleepQualitySelected(if (selectedKey == key) null else key)
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Energy section widget
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun EnergySectionWidget(
    state: LogSheetState,
    onEnergyLevelSelected: (String?) -> Unit
) {
    val selectedKey = state.energyLevel?.name?.lowercase()
    LogSection(
        title = "",
        items = energyItems,
        selectedKey = selectedKey,
        categoryColor = CategoryMood,
        onItemSelected = { key ->
            onEnergyLevelSelected(if (selectedKey == key) null else key)
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Discharge section widget
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun DischargeSectionWidget(
    state: LogSheetState,
    onDischargeTypeSelected: (String?) -> Unit
) {
    val selectedKey = state.dischargeType?.name?.lowercase()
    LogSection(
        title = "",
        items = dischargeItems,
        selectedKey = selectedKey,
        categoryColor = CategorySexual,
        onItemSelected = { key ->
            onDischargeTypeSelected(if (selectedKey == key) null else key)
        }
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Optional categories rotation schedule & helpers
// ─────────────────────────────────────────────────────────────────────────────

private val ROTATION_SCHEDULE = listOf(
    listOf(OptionalLogCategory.MOOD, OptionalLogCategory.SLEEP),
    listOf(OptionalLogCategory.SLEEP, OptionalLogCategory.ENERGY),
    listOf(OptionalLogCategory.ENERGY, OptionalLogCategory.DISCHARGE),
    listOf(OptionalLogCategory.DISCHARGE, OptionalLogCategory.MOOD),
    listOf(OptionalLogCategory.MOOD, OptionalLogCategory.ENERGY),
    listOf(OptionalLogCategory.SLEEP, OptionalLogCategory.DISCHARGE),
    listOf(OptionalLogCategory.MOOD, OptionalLogCategory.DISCHARGE)
)

private fun getTodaysOptionalCategories(date: LocalDate): List<OptionalLogCategory> {
    val index = Math.abs(date.dayOfYear) % ROTATION_SCHEDULE.size
    return ROTATION_SCHEDULE[index]
}

private fun filterAlreadyLoggedViaConversation(
    categories: List<OptionalLogCategory>,
    saarthiLoggedToday: Set<OptionalLogCategory>
): List<OptionalLogCategory> {
    return categories.filter { it !in saarthiLoggedToday }
}

