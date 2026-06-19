package com.niyuva.app.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.niyuva.app.presentation.theme.BlushMist
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DestructiveRose

@Composable
fun NiyuvaPinEntry(
    enteredDigits: Int,
    isError: Boolean,
    modifier: Modifier = Modifier
) {
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(isError) {
        if (isError) {
            // Horizontal shake animation sequence (approx. 400ms total)
            for (i in 0..2) {
                shakeOffset.animateTo(20f, animationSpec = tween(50))
                shakeOffset.animateTo(-20f, animationSpec = tween(50))
            }
            shakeOffset.animateTo(0f, animationSpec = tween(50))
        }
    }

    val dotColor = if (isError) DestructiveRose else DeepPlumRose
    val borderColor = if (isError) DestructiveRose else BlushMist

    Row(
        modifier = modifier
            .offset(x = shakeOffset.value.dp)
            .semantics {
                this.contentDescription = "$enteredDigits PIN digits entered"
            },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(4) { index ->
            val isFilled = index < enteredDigits
            val fillScale by animateFloatAsState(
                targetValue = if (isFilled) 1.0f else 0.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMedium
                ),
                label = "PinDotScale"
            )

            Box(
                modifier = Modifier
                    .size(20.dp)
                    .border(2.dp, borderColor, CircleShape)
                    .background(Color.Transparent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(fillScale)
                        .background(dotColor, CircleShape)
                )
            }
        }
    }
}
