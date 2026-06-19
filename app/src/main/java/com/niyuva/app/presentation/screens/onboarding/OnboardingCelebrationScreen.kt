package com.niyuva.app.presentation.screens.onboarding

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.theme.*
import kotlinx.coroutines.launch
@Composable
fun OnboardingCelebrationScreen(
    userName: String,
    onNavigateToMain: () -> Unit,
    onCelebrationEntered: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Mark onboarding complete on entry
    LaunchedEffect(Unit) {
        onCelebrationEntered()
    }

    // Entrance Animation (scale from 0.95 -> 1.0 + alpha 0 -> 1, 400ms FastOutSlowIn)
    val scale = remember { Animatable(0.95f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            scale.animateTo(1f, animationSpec = tween(400, easing = FastOutSlowInEasing))
        }
        launch {
            alpha.animateTo(1f, animationSpec = tween(400, easing = FastOutSlowInEasing))
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Mascot illustration container (60% of screen height)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                contentAlignment = Alignment.Center
            ) {
                // Mascot card (scale & alpha animated)
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .graphicsLayer {
                            scaleX = scale.value
                            scaleY = scale.value
                            this.alpha = alpha.value
                        }
                        .background(WarmLinen, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // TODO: Replace with NIYUVA Mascot — standing pose, arms open, warm smile, botanicals blooming, sparkles and hearts floating
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "NIYUVA",
                            fontFamily = PlayfairFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = DeepPlumRose
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "🌿✨🌸💛",
                            fontSize = 28.sp
                        )
                    }
                }

                // Floating sparkle elements (staggered delay)
                FloatingElement(text = "✨", modifier = Modifier.align(Alignment.Center).offset(x = (-90).dp, y = (-100).dp), delayMillis = 0)
                FloatingElement(text = "✨", modifier = Modifier.align(Alignment.Center).offset(x = 100.dp, y = (-90).dp), delayMillis = 400)
                FloatingElement(text = "✨", modifier = Modifier.align(Alignment.Center).offset(x = (-110).dp, y = 50.dp), delayMillis = 800)
                FloatingElement(text = "✨", modifier = Modifier.align(Alignment.Center).offset(x = 110.dp, y = 60.dp), delayMillis = 1200)
                FloatingElement(text = "✨", modifier = Modifier.align(Alignment.Center).offset(x = (-60).dp, y = 110.dp), delayMillis = 1600)
                FloatingElement(text = "✨", modifier = Modifier.align(Alignment.Center).offset(x = 70.dp, y = 120.dp), delayMillis = 2000)

                // Floating heart elements (staggered delay)
                FloatingElement(text = "💛", modifier = Modifier.align(Alignment.Center).offset(x = (-70).dp, y = (-50).dp), delayMillis = 200)
                FloatingElement(text = "💛", modifier = Modifier.align(Alignment.Center).offset(x = 60.dp, y = (-60).dp), delayMillis = 600)
                FloatingElement(text = "💛", modifier = Modifier.align(Alignment.Center).offset(x = (-50).dp, y = 80.dp), delayMillis = 1000)
                FloatingElement(text = "💛", modifier = Modifier.align(Alignment.Center).offset(x = 80.dp, y = 90.dp), delayMillis = 1400)
            }

            // Bottom Section (40% of screen height)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$userName, Niyuva mein welcome hai! 💛",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Yeh app ab sirf tera hai.",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = DustyMauve,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                NiyuvaPrimaryButton(
                    text = "Apni Niyuva dekho 🌸",
                    onClick = onNavigateToMain
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun FloatingElement(
    text: String,
    modifier: Modifier = Modifier,
    delayMillis: Int = 0
) {
    val infiniteTransition = rememberInfiniteTransition(label = "floating")
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -24f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                delayMillis = delayMillis,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )

    Text(
        text = text,
        modifier = modifier.graphicsLayer {
            translationY = offsetY
        },
        fontSize = 22.sp
    )
}
