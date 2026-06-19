package com.niyuva.app.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.niyuva.app.presentation.theme.WarmIvory
import com.niyuva.app.presentation.theme.WarmLinen

/**
 * A shimmering skeleton placeholder composable used during loading states.
 *
 * Displays a horizontally-sweeping gradient (WarmIvory → WarmLinen → WarmIvory) that animates
 * infinitely at 1200ms per cycle. Apply it to any sized Box to create a skeleton bone.
 *
 * @param modifier     Sizing + positioning constraints supplied by the caller.
 * @param cornerRadius Corner radius of the shimmer shape. Default 16.dp.
 */
@Composable
fun NiyuvaShimmer(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp
) {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")

    // Offset 0f→1f drives the gradient from fully left (off-screen) to fully right (off-screen)
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerOffset"
    )

    val shimmerBrush = Brush.linearGradient(
        colors = listOf(
            WarmIvory,
            WarmLinen,
            WarmIvory
        ),
        start = Offset(translateAnim - 300f, 0f),
        end   = Offset(translateAnim + 300f, 0f)
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius))
            .background(shimmerBrush)
    )
}
