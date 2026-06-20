package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.screens.home.DailyTip
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PhaseThemeData
import com.niyuva.app.presentation.theme.Terracotta

@Composable
fun NiyuvaTipCard(
    tip: DailyTip,
    dayInPhase: Int,
    onReadMoreClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (cardBgColor, leftBorderColor) = when (tip.phase) {
        CyclePhase.MENSTRUATION -> Color(0xFFF2D5DC) to Color(0xFFC17A8A)
        CyclePhase.FOLLICULAR   -> Color(0xFFE1F5F2) to Color(0xFF76C4B4)
        CyclePhase.OVULATION    -> Color(0xFFD8EDD5) to Color(0xFFA8C5A0)
        CyclePhase.LUTEAL       -> Color(0xFFF0DFC0) to Color(0xFFD4A574)
    }

    val emoji = when (tip.phase) {
        CyclePhase.MENSTRUATION -> "🌸"
        CyclePhase.FOLLICULAR   -> "✨"
        CyclePhase.OVULATION    -> "🔥"
        CyclePhase.LUTEAL       -> "💛"
    }

    val phaseLabel = when (tip.phase) {
        CyclePhase.MENSTRUATION -> "Menstruation"
        CyclePhase.FOLLICULAR   -> "Follicular"
        CyclePhase.OVULATION    -> "Ovulation"
        CyclePhase.LUTEAL       -> "Luteal"
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Left border accent: 4dp solid
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(leftBorderColor)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Header row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = emoji,
                            fontSize = 22.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Aaj ka tip",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = Color(0xFFA07A85)
                        )
                    }
                    Text(
                        text = "Day $dayInPhase of $phaseLabel",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 11.sp,
                        color = Color(0xFFA07A85)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tip Text
                Text(
                    text = tip.tipText,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 15.sp,
                    color = Color(0xFF1E1418),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Category pill chip
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.dp, leftBorderColor, RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.5f))
                            .padding(vertical = 4.dp, horizontal = 10.dp)
                    ) {
                        Text(
                            text = tip.category,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 11.sp,
                            color = Color(0xFF1E1418)
                        )
                    }

                    // Read more CTA
                    Box(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = onReadMoreClicked
                            )
                            .padding(vertical = 4.dp, horizontal = 8.dp)
                    ) {
                        Text(
                            text = "Aur padho →",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = Terracotta
                        )
                    }
                }
            }
        }
    }
}
