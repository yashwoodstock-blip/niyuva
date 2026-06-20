package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.screens.home.HomeUiState
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PhaseColorFollicular
import com.niyuva.app.presentation.theme.PhaseColorLuteal
import com.niyuva.app.presentation.theme.PhaseColorMenstruation
import com.niyuva.app.presentation.theme.PhaseColorOvulation
import com.niyuva.app.presentation.theme.PureWhite

// ─────────────────────────────────────────────────────────────────────────────
// Cycle Ring Card
// ─────────────────────────────────────────────────────────────────────────────

/**
 * The centrepiece card of the Home tab.
 *
 * Layout (top → bottom, inside a white card):
 *  1. [NiyuvaCycleRingArc] — animated semicircle + day/phase/emoji overlay
 *  2. 16dp spacer
 *  3. [PhaseLegendRow] — 4 phase dots with labels
 *  4. 16dp spacer
 *  5. "Log today's symptoms" full-width pill button
 *
 * @param uiState              Current [HomeUiState] — provides phase, day, theme data.
 * @param onLogSymptomsClicked Callback fired when the log button is tapped (Phase 17 wires the sheet).
 * @param modifier             Outer modifier — caller supplies width + positioning.
 */
@Composable
fun NiyuvaCycleRingCard(
    uiState: HomeUiState,
    onLogSymptomsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors    = CardDefaults.cardColors(containerColor = PureWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
                // 1. Semicircular arc
                NiyuvaCycleRingArc(
                    currentDayInCycle = uiState.currentDayInCycle,
                    totalCycleDays    = uiState.totalCycleDays,
                    currentPhase      = uiState.currentPhase,
                    phaseTheme        = uiState.phaseTheme,
                    confidenceLevel   = uiState.confidenceLevel,
                    irregularityFlag  = uiState.irregularityFlag,
                    modifier          = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 2. Phase legend
                PhaseLegendRow(activePhase = uiState.currentPhase)

                Spacer(modifier = Modifier.height(16.dp))

                // 3. Log symptoms button
                val isToday = uiState.selectedDate == java.time.LocalDate.now()
                val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM d")
                val buttonLabel = if (isToday) "Log today's symptoms" else "Log symptoms for ${uiState.selectedDate.format(formatter)}"
                LogSymptomsButton(label = buttonLabel, onClick = onLogSymptomsClicked)
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Phase Legend Row
// ─────────────────────────────────────────────────────────────────────────────

private data class PhaseLegendItem(
    val phase: CyclePhase,
    val label: String,
    val color: Color
)

private val phaseLegendItems = listOf(
    PhaseLegendItem(CyclePhase.MENSTRUATION, "Menstrual",  PhaseColorMenstruation),
    PhaseLegendItem(CyclePhase.FOLLICULAR,   "Follicular", PhaseColorFollicular),
    PhaseLegendItem(CyclePhase.OVULATION,    "Ovulation",  PhaseColorOvulation),
    PhaseLegendItem(CyclePhase.LUTEAL,       "Luteal",     PhaseColorLuteal)
)

@Composable
private fun PhaseLegendRow(
    activePhase: CyclePhase,
    modifier: Modifier = Modifier
) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        phaseLegendItems.forEach { item ->
            PhaseLegendCell(item = item, isActive = item.phase == activePhase)
        }
    }
}

@Composable
private fun PhaseLegendCell(
    item: PhaseLegendItem,
    isActive: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val dotSize = if (isActive) 10.dp else 8.dp

        Box(
            modifier = Modifier
                .size(dotSize)
                .clip(CircleShape)
                .background(item.color)
                .then(
                    if (isActive) Modifier.border(1.5.dp, DeepPlumRose, CircleShape)
                    else Modifier
                )
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text       = item.label,
            fontFamily = NunitoFamily,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            fontSize   = 10.sp,
            color      = DustyMauve
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Log Symptoms Button
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun LogSymptomsButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(50.dp))
            .background(DeepPlumRose)
            .clickable(
                onClick           = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text       = label,
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp,
            color      = Color.White
        )
    }
}
