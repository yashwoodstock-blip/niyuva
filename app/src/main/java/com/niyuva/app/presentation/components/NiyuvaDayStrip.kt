package com.niyuva.app.presentation.components

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import com.niyuva.app.presentation.screens.home.DayStripItem
import com.niyuva.app.presentation.theme.NunitoFamily
import java.time.LocalDate

/**
 * Horizontal 7-day calendar strip for the Home screen.
 *
 * Layout: 3 days before today (indices 0–2), today (index 3), 3 days after (indices 4–6).
 * On first composition the list auto-scrolls to centre on today (index 3).
 *
 * @param days        Exactly 7 [DayStripItem]s produced by [BuildDayStripUseCase].
 * @param phaseColor  The active phase's primary colour, used for the today-circle and icon tints.
 * @param onDayTapped Callback fired with the tapped [LocalDate].
 * @param modifier    Outer modifier for sizing/positioning.
 */
@Composable
fun NiyuvaDayStrip(
    days: List<DayStripItem>,
    phaseColor: Color,
    onDayTapped: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Auto-scroll to centre today on first composition
    LaunchedEffect(Unit) {
        if (days.isNotEmpty()) {
            listState.scrollToItem(
                index = (days.indexOfFirst { it.isToday }).coerceAtLeast(0)
            )
        }
    }

    LazyRow(
        state          = listState,
        modifier       = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(days, key = { it.date.toString() }) { item ->
            DayCell(
                item       = item,
                phaseColor = phaseColor,
                onTapped   = { onDayTapped(item.date) }
            )
        }
    }
}

// ─────────────────────────────────────────────
// Individual day cell
// ─────────────────────────────────────────────

@Composable
private fun DayCell(
    item: DayStripItem,
    phaseColor: Color,
    onTapped: () -> Unit
) {
    val white = Color.White
    val white70 = white.copy(alpha = 0.70f)
    val white50 = white.copy(alpha = 0.50f)

    Box(
        modifier = Modifier
            .width(44.dp)
            .height(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                onClick = onTapped
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Day letter (M / T / W …)
            Text(
                text       = item.dayLetter,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize   = 11.sp,
                color      = if (item.isFutureDay) white50 else white70,
                textAlign  = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Day number — always a 32dp circle Box for consistent alignment
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(if (item.isToday) Color.White else Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text       = item.dayNumber.toString(),
                    fontFamily = NunitoFamily,
                    fontWeight = if (item.isToday) FontWeight.Bold else FontWeight.SemiBold,
                    fontSize   = 16.sp,
                    color      = if (item.isToday) phaseColor else (if (item.isFutureDay) white50 else white),
                    textAlign  = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Status row — fixed 12dp height for every cell so numbers stay aligned
            Box(
                modifier = Modifier.height(12.dp),
                contentAlignment = Alignment.Center
            ) {
                when {
                    item.isToday -> Text(
                        text          = "TODAY",
                        fontFamily    = NunitoFamily,
                        fontWeight    = FontWeight.Bold,
                        fontSize      = 8.sp,
                        color         = white,
                        textAlign     = TextAlign.Center,
                        letterSpacing = 0.5.sp
                    )
                    item.isPeriodDay && !item.isFutureDay -> Text(
                        text     = "\uD83E\uDE78",
                        fontSize = 10.sp
                    )
                    item.isLogged -> Text(
                        text       = "\u2713",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 10.sp,
                        color      = white.copy(alpha = 0.80f)
                    )
                    // else: empty Box keeps height consistent, no Spacer needed
                }
            }
        }
    }
}
