package com.niyuva.app.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.screens.body.BodyTopic
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite
 
@Composable
fun BodyTopicCard(
    topic: BodyTopic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1.0f,
        label = "scale"
    )

    // REFACTOR: Derive dynamic premium category color scheme matching the card topic type
    val darkAccentColor = remember(topic.id) {
        when (topic.id) {
            "my_cycle" -> Color(0xFF2D6A4F)
            "hormones" -> Color(0xFF5A189A)
            "hygiene" -> Color(0xFF0077B6)
            "period_products" -> Color(0xFFD00000)
            "pcos" -> Color(0xFFB5179E)
            "during_periods" -> Color(0xFF9E0059)
            "my_body", "health_diet" -> Color(0xFFE65F2B)
            "animations" -> Color(0xFF6F1D1B)
            else -> DeepPlumRose
        }
    }
 
    Box(
        modifier = modifier
            .heightIn(min = 145.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        topic.cardColor,
                        topic.cardColor.copy(alpha = 0.5f)
                    )
                )
            )
            .border(
                width = 1.2.dp,
                color = topic.cardColor.copy(alpha = 0.8f),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Premium Icon circular container
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(PureWhite.copy(alpha = 0.7f))
                    .border(0.5.dp, PureWhite, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = topic.emoji,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = topic.title,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                color = DeepWarmBrown,
                lineHeight = 22.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = topic.description,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = DeepWarmBrown.copy(alpha = 0.75f),
                lineHeight = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Category Badge - Top Right
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(darkAccentColor.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = topic.categoryLabel,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = darkAccentColor
            )
        }
    }
}

