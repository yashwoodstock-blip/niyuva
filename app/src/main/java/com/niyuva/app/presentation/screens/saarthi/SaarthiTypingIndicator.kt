package com.niyuva.app.presentation.screens.saarthi

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.niyuva.app.presentation.theme.DeepPlumRose

@Composable
fun SaarthiTypingIndicator(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(vertical = 4.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "typingIndicator")

        val dot1OffsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 600
                    0f at 0 with LinearEasing
                    -6f.dp.value at 150 with FastOutSlowInEasing
                    0f at 300 with FastOutSlowInEasing
                    0f at 600 with LinearEasing
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "dot1Offset"
        )

        val dot2OffsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 600
                    0f at 0 with LinearEasing
                    0f at 100 with LinearEasing
                    -6f.dp.value at 250 with FastOutSlowInEasing
                    0f at 400 with FastOutSlowInEasing
                    0f at 600 with LinearEasing
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "dot2Offset"
        )

        val dot3OffsetY by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = keyframes {
                    durationMillis = 600
                    0f at 0 with LinearEasing
                    0f at 200 with LinearEasing
                    -6f.dp.value at 350 with FastOutSlowInEasing
                    0f at 500 with FastOutSlowInEasing
                    0f at 600 with LinearEasing
                },
                repeatMode = RepeatMode.Restart
            ),
            label = "dot3Offset"
        )

        Box(
            modifier = Modifier
                .size(8.dp)
                .graphicsLayer(translationY = dot1OffsetY)
                .clip(CircleShape)
                .background(DeepPlumRose)
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .graphicsLayer(translationY = dot2OffsetY)
                .clip(CircleShape)
                .background(DeepPlumRose)
        )
        Box(
            modifier = Modifier
                .size(8.dp)
                .graphicsLayer(translationY = dot3OffsetY)
                .clip(CircleShape)
                .background(DeepPlumRose)
        )
    }
}
