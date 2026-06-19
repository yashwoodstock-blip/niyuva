package com.niyuva.app.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.screens.body.BodyTopic
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
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
 
    Box(
        modifier = modifier
            .heightIn(min = 140.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(RoundedCornerShape(20.dp))
            .background(topic.cardColor)
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
            Text(
                text = topic.emoji,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = topic.title,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = DeepWarmBrown,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = topic.description,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = DustyMauve,
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
                .background(DeepPlumRose.copy(alpha = 0.12f))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = topic.categoryLabel,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = DeepPlumRose
            )
        }
    }
}

