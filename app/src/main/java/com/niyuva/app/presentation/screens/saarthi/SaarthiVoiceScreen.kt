package com.niyuva.app.presentation.screens.saarthi

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite

enum class SpeechState {
    LISTENING, PROCESSING, ERROR
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SaarthiVoiceScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SaarthiViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val phaseTheme = uiState.phaseTheme

    var speechState by remember { mutableStateOf(SpeechState.LISTENING) }
    var recognizedText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val recognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }

    val intent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
    }

    val permissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    fun startListening() {
        if (permissionState.status.isGranted) {
            speechState = SpeechState.LISTENING
            recognizedText = ""
            try {
                recognizer.startListening(intent)
            } catch (e: Exception) {
                speechState = SpeechState.ERROR
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            recognizer.destroy()
        }
    }

    val listener = remember {
        object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                speechState = SpeechState.LISTENING
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                speechState = SpeechState.PROCESSING
            }

            override fun onError(error: Int) {
                speechState = SpeechState.ERROR
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val bestResult = matches?.firstOrNull()
                if (bestResult != null) {
                    navController.previousBackStackEntry?.savedStateHandle?.set("voice_result", bestResult)
                    navController.popBackStack()
                } else {
                    speechState = SpeechState.ERROR
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val bestResult = matches?.firstOrNull()
                if (bestResult != null) {
                    recognizedText = bestResult
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }

    LaunchedEffect(recognizer) {
        recognizer.setRecognitionListener(listener)
    }

    LaunchedEffect(permissionState.status.isGranted) {
        if (permissionState.status.isGranted) {
            startListening()
        }
    }

    BackHandler {
        recognizer.stopListening()
        navController.popBackStack()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(phaseTheme.backgroundGradientStart, phaseTheme.backgroundGradientEnd)
                )
            )
    ) {
        // Back arrow Top-left
        IconButton(
            onClick = {
                recognizer.stopListening()
                navController.popBackStack()
            },
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(PureWhite.copy(alpha = 0.3f))
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = "Go back",
                tint = PureWhite,
                modifier = Modifier.size(24.dp)
            )
        }

        if (!permissionState.status.isGranted) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "Microphone permission chahiye — Settings mein allow karo 💛",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = PureWhite,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                IconButton(
                    onClick = { permissionState.launchPermissionRequest() },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PureWhite)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = "Request Permission",
                        tint = DeepPlumRose
                    )
                }
            }
        } else {
            // Pulse & Breath Animations
            val infiniteTransition = rememberInfiniteTransition(label = "pulsingOrb")
            val pulse by infiniteTransition.animateFloat(
                initialValue = 1.0f,
                targetValue = 1.08f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "pulseScale"
            )

            val breathScale by if (speechState == SpeechState.LISTENING) {
                infiniteTransition.animateFloat(
                    initialValue = 1.0f,
                    targetValue = 1.05f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1400, easing = FastOutSlowInEasing),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "breathScale"
                )
            } else {
                remember { mutableStateOf(1.0f) }
            }

            // Center Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val center = this.center
                        val baseRadius = size.minDimension / 2f
                        val currentScale = pulse * breathScale

                        // Outermost glow: 160dp
                        drawCircle(
                            color = PureWhite.copy(alpha = 0.15f),
                            radius = baseRadius * currentScale,
                            center = center
                        )

                        // Middle glow: 120dp
                        drawCircle(
                            color = PureWhite.copy(alpha = 0.30f),
                            radius = (baseRadius * 120f / 160f) * currentScale,
                            center = center
                        )

                        // Core orb: 80dp
                        drawCircle(
                            color = PureWhite.copy(alpha = 0.85f),
                            radius = (baseRadius * 80f / 160f),
                            center = center
                        )
                    }

                    Text(
                        text = "🎤",
                        fontSize = 32.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                val statusText = when (speechState) {
                    SpeechState.LISTENING -> if (recognizedText.isNotBlank()) recognizedText else "Bol, main sun rahi hoon..."
                    SpeechState.PROCESSING -> "Samajh rahi hoon..."
                    SpeechState.ERROR -> "Kuch suna nahi — phir try karo 🎤"
                }

                Text(
                    text = statusText,
                    fontFamily = NunitoFamily,
                    fontSize = 16.sp,
                    color = PureWhite.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            // Bottom Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 32.dp, vertical = 48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { startListening() },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PureWhite)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Mic,
                        contentDescription = "Restart listening",
                        tint = DeepPlumRose,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(
                    onClick = {
                        recognizer.stopListening()
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(PureWhite)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Cancel",
                        tint = DustyMauve,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}
