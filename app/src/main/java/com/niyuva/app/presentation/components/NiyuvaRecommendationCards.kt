package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite
import com.niyuva.app.presentation.theme.WarmLinen

// ─────────────────────────────────────────────────────────────────────────────
// Phase-specific tip content (hardcoded — owner populates production copy later)
// ─────────────────────────────────────────────────────────────────────────────

private fun nutritionTip(phase: CyclePhase): String = when (phase) {
    CyclePhase.MENSTRUATION -> "Iron wali cheezein kha — spinach, dal, jaggery 🌿"
    CyclePhase.FOLLICULAR   -> "Fresh, light foods aur lean protein best hain ✨"
    CyclePhase.OVULATION    -> "Anti-inflammatory foods — leafy greens, zinc-rich cheezein 🥬"
    CyclePhase.LUTEAL       -> "Magnesium ke liye dark chocolate, nuts, seeds 🍫"
}

private fun activityTip(phase: CyclePhase): String = when (phase) {
    CyclePhase.MENSTRUATION -> "Light yoga ya walk — gentle movement cramps mein madad karti hai 🚶\u200D♀️"
    CyclePhase.FOLLICULAR   -> "Cardio aur strength training ka perfect time hai! 💪"
    CyclePhase.OVULATION    -> "High-intensity workouts — body peak performance pe hai 🔥"
    CyclePhase.LUTEAL       -> "Yoga, swimming, light walk — intense workout thoda avoid karo 🧘\u200D♀️"
}

// ─────────────────────────────────────────────────────────────────────────────
// Section header + two side-by-side cards
// ─────────────────────────────────────────────────────────────────────────────

/**
 * "Aaj ke liye" section: a header row followed by two equal-width recommendation cards
 * (Nutrition and Activity) driven by the current cycle [phase].
 *
 * @param phase          Active [CyclePhase] — selects the tip copy.
 * @param onSeeAllClicked Callback for the "See All" text link.
 * @param modifier        Outer modifier — caller supplies horizontal padding + top spacing.
 */
@Composable
fun NiyuvaRecommendationSection(
    phase: CyclePhase,
    onSeeAllClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {

        // ── Section header ────────────────────────────────────────────────────
        Row(
            modifier          = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text       = "Aaj ke liye",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 16.sp,
                color      = DeepWarmBrown,
                modifier   = Modifier.weight(1f)
            )
            NiyuvaTextLink(
                text    = "See All",
                onClick = onSeeAllClicked
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Activity card (full width)
        RecommendationCard(
            icon     = "🧘\u200D♀️",
            title    = "Aaj ki activity",
            tip      = activityTip(phase),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Individual recommendation card
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun RecommendationCard(
    icon: String,
    title: String,
    tip: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier  = modifier,
        shape     = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors    = CardDefaults.cardColors(containerColor = PureWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Icon in a WarmLinen square background
            Box(
                modifier         = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(WarmLinen),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text       = title,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize   = 14.sp,
                color      = DeepWarmBrown
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Phase-specific one-line tip
            Text(
                text       = tip,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize   = 12.sp,
                color      = DustyMauve,
                maxLines   = 3,
                overflow   = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}
