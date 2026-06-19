package com.niyuva.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.domain.model.CyclePrediction
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PhaseColorFollicular
import com.niyuva.app.presentation.theme.PhaseColorMenstruation
import com.niyuva.app.presentation.theme.PhaseColorOvulation
import com.niyuva.app.presentation.theme.PureWhite
import com.niyuva.app.presentation.theme.DeepPlumRose
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// ─────────────────────────────────────────────────────────────────────────────
// Date helpers
// ─────────────────────────────────────────────────────────────────────────────

private val shortDateFormatter = DateTimeFormatter.ofPattern("MMM d")

fun formatShortDate(date: LocalDate): String = date.format(shortDateFormatter)

/** Returns a human-readable "in N days" or "in 1 day" string, or "today" / "yesterday". */
private fun daysAwayLabel(date: LocalDate): String {
    val today = LocalDate.now()
    return when (val diff = ChronoUnit.DAYS.between(today, date)) {
        0L   -> "today"
        1L   -> "in 1 day"
        -1L  -> "yesterday"
        else -> if (diff > 0) "in $diff days" else "${-diff} days ago"
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// NiyuvaPredictionsRow
// ─────────────────────────────────────────────────────────────────────────────

/**
 * A horizontal row of 3 equal-weight prediction mini-cards:
 *  1. Next Period (🔴, [PhaseColorMenstruation])
 *  2. Ovulation (🥚, [PhaseColorOvulation])
 *  3. Fertile Window (🌿, [PhaseColorFollicular])
 *
 * When [prediction] is `null`, each card shows shimmer placeholders.
 */
@Composable
fun NiyuvaPredictionsRow(
    prediction: CyclePrediction?,
    confidenceLevel: com.niyuva.app.domain.model.ConfidenceLevel = com.niyuva.app.domain.model.ConfidenceLevel.ESTIMATED,
    irregularityFlag: Boolean = false,
    modifier: Modifier = Modifier
) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // ── Next Period ───────────────────────────────────────────────────────
        val nextPeriodValueText = if (prediction != null) {
            if (irregularityFlag) {
                val d1 = prediction.nextPeriodDate
                val d2 = d1.plusDays(2)
                val formatterEnd = DateTimeFormatter.ofPattern("MMM")
                if (d1.month == d2.month) {
                    "${d1.dayOfMonth}–${d2.dayOfMonth} ${d2.format(formatterEnd)}"
                } else {
                    "${d1.dayOfMonth} ${d1.format(formatterEnd)}–${d2.dayOfMonth} ${d2.format(formatterEnd)}"
                }
            } else {
                formatShortDate(prediction.nextPeriodDate)
            }
        } else "—"

        val nextPeriodSubLabel = if (prediction != null) {
            if (irregularityFlag) {
                "ke beech aa sakta hai"
            } else {
                daysAwayLabel(prediction.nextPeriodDate)
            }
        } else "—"

        PredictionMiniCard(
            icon            = "🔴",
            label           = "Next period",
            valueText       = nextPeriodValueText,
            valueColor      = PhaseColorMenstruation,
            subLabel        = nextPeriodSubLabel,
            isLoading       = prediction == null,
            confidenceLevel = confidenceLevel,
            showNote        = true,
            modifier        = Modifier.weight(1f)
        )

        // ── Ovulation ─────────────────────────────────────────────────────────
        val ovulationValueText = if (prediction != null) {
            if (irregularityFlag) {
                val d1 = prediction.ovulationDate.minusDays(1)
                val d2 = prediction.ovulationDate.plusDays(1)
                val formatterEnd = DateTimeFormatter.ofPattern("MMM")
                if (d1.month == d2.month) {
                    "${d1.dayOfMonth}–${d2.dayOfMonth} ${d2.format(formatterEnd)}"
                } else {
                    "${d1.dayOfMonth} ${d1.format(formatterEnd)}–${d2.dayOfMonth} ${d2.format(formatterEnd)}"
                }
            } else {
                formatShortDate(prediction.ovulationDate)
            }
        } else "—"

        val ovulationSubLabel = if (prediction != null) {
            if (irregularityFlag) {
                "±1 day range"
            } else {
                daysAwayLabel(prediction.ovulationDate)
            }
        } else "—"

        PredictionMiniCard(
            icon            = "🥚",
            label           = "Ovulation",
            valueText       = ovulationValueText,
            valueColor      = PhaseColorOvulation,
            subLabel        = ovulationSubLabel,
            isLoading       = prediction == null,
            confidenceLevel = confidenceLevel,
            showNote        = false,
            modifier        = Modifier.weight(1f)
        )

        // ── Fertile Window ────────────────────────────────────────────────────
        val fertileValueText = if (prediction != null) {
            val start = formatShortDate(prediction.fertileWindowStart)
            val endDay = prediction.fertileWindowEnd.dayOfMonth.toString()
            "$start–$endDay"
        } else "—"

        PredictionMiniCard(
            icon            = "🌿",
            label           = "Fertile window",
            valueText       = fertileValueText,
            valueColor      = PhaseColorFollicular,
            subLabel        = if (prediction != null) "5-day window" else "—",
            isLoading       = prediction == null,
            confidenceLevel = confidenceLevel,
            showNote        = false,
            modifier        = Modifier.weight(1f)
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Individual prediction card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun PredictionMiniCard(
    icon: String,
    label: String,
    valueText: String,
    valueColor: Color,
    subLabel: String,
    isLoading: Boolean,
    confidenceLevel: com.niyuva.app.domain.model.ConfidenceLevel,
    showNote: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors    = CardDefaults.cardColors(containerColor = PureWhite)
    ) {
        if (isLoading) {
            // Shimmer skeleton
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                NiyuvaShimmer(modifier = Modifier.fillMaxWidth().height(14.dp), cornerRadius = 6.dp)
                NiyuvaShimmer(modifier = Modifier.fillMaxWidth().height(18.dp), cornerRadius = 6.dp)
                NiyuvaShimmer(modifier = Modifier.fillMaxWidth(0.6f).height(10.dp), cornerRadius = 6.dp)
            }
        } else {
            val chipText = when (confidenceLevel) {
                com.niyuva.app.domain.model.ConfidenceLevel.ESTIMATED -> "Estimated"
                com.niyuva.app.domain.model.ConfidenceLevel.LIKELY -> "Likely"
                com.niyuva.app.domain.model.ConfidenceLevel.CONFIDENT -> "Confident ✦"
            }
            val chipBg = when (confidenceLevel) {
                com.niyuva.app.domain.model.ConfidenceLevel.ESTIMATED -> DustyMauve.copy(alpha = 0.09f)
                com.niyuva.app.domain.model.ConfidenceLevel.LIKELY -> DustyMauve.copy(alpha = 0.15f)
                com.niyuva.app.domain.model.ConfidenceLevel.CONFIDENT -> PhaseColorMenstruation.copy(alpha = 0.15f)
            }
            val chipTextColor = when (confidenceLevel) {
                com.niyuva.app.domain.model.ConfidenceLevel.ESTIMATED -> DustyMauve.copy(alpha = 0.6f)
                com.niyuva.app.domain.model.ConfidenceLevel.LIKELY -> DustyMauve
                com.niyuva.app.domain.model.ConfidenceLevel.CONFIDENT -> DeepPlumRose
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                // Top-right confidence badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 6.dp, end = 6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(chipBg)
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = chipText,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = chipTextColor,
                        fontFamily = NunitoFamily
                    )
                }

                Column(
                    modifier            = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                        .padding(top = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon
                    Text(text = icon, fontSize = 20.sp)

                    Spacer(modifier = Modifier.height(4.dp))

                    // Label
                    Text(
                        text       = label,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize   = 11.sp,
                        color      = DustyMauve
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Date value
                    Text(
                        text       = valueText,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize   = 12.sp,
                        color      = valueColor,
                        textAlign  = TextAlign.Center,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    // Days-away sub-label
                    Text(
                        text       = subLabel,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize   = 10.sp,
                        color      = DustyMauve,
                        textAlign  = TextAlign.Center,
                        lineHeight = 13.sp
                    )

                    // Estimated sub-note below predicted date / subLabel
                    if (showNote && confidenceLevel == com.niyuva.app.domain.model.ConfidenceLevel.ESTIMATED) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text       = androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.estimated_note),
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize   = 9.sp,
                            color      = DustyMauve.copy(alpha = 0.8f),
                            fontStyle  = androidx.compose.ui.text.font.FontStyle.Italic,
                            textAlign  = TextAlign.Center,
                            lineHeight = 12.sp,
                            modifier   = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
