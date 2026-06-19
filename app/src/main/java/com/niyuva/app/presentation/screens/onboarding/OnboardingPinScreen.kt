package com.niyuva.app.presentation.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.components.*
import com.niyuva.app.presentation.theme.*
import kotlinx.coroutines.delay

enum class PinState {
    ENTERING, CONFIRMING
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingPinScreen(
    userName: String,
    onPinSet: (String) -> Unit,
    onSkipPin: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pinState by remember { mutableStateOf(PinState.ENTERING) }
    var enteredPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    var subtitleText by remember { mutableStateOf("Yeh sirf tere phone pe rehta hai — kuch bhi kahin nahi jaata 💛") }
    var showBottomSheet by remember { mutableStateOf(false) }

    val activePin = if (pinState == PinState.ENTERING) enteredPin else confirmPin

    // Trigger mismatch logic
    LaunchedEffect(isError) {
        if (isError) {
            delay(1000L)
            enteredPin = ""
            confirmPin = ""
            isError = false
            pinState = PinState.ENTERING
            subtitleText = "Hmm, match nahi hua — phir try karo 💛"
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                val titleText = if (pinState == PinState.ENTERING) {
                    "$userName, yeh app sirf tera hai. PIN set karogi? 🔒"
                } else {
                    "Ek baar aur confirm karo 🔒"
                }
                Text(
                    text = titleText,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle
                Text(
                    text = subtitleText,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = DustyMauve,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Pin Indicator Dots
                NiyuvaPinEntry(
                    enteredDigits = activePin.length,
                    isError = isError
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                // Keypad
                NiyuvaNumericKeypad(
                    onDigitEntered = { digit ->
                        if (isError) return@NiyuvaNumericKeypad

                        if (pinState == PinState.ENTERING) {
                            if (enteredPin.length < 4) {
                                enteredPin += digit
                                if (enteredPin.length == 4) {
                                    pinState = PinState.CONFIRMING
                                }
                            }
                        } else {
                            if (confirmPin.length < 4) {
                                confirmPin += digit
                                if (confirmPin.length == 4) {
                                    if (enteredPin == confirmPin) {
                                        onPinSet(enteredPin)
                                    } else {
                                        isError = true
                                    }
                                }
                            }
                        }
                    },
                    onBackspace = {
                        if (isError) return@NiyuvaNumericKeypad

                        if (pinState == PinState.ENTERING) {
                            if (enteredPin.isNotEmpty()) {
                                enteredPin = enteredPin.dropLast(1)
                            }
                        } else {
                            if (confirmPin.isNotEmpty()) {
                                confirmPin = confirmPin.dropLast(1)
                            } else {
                                pinState = PinState.ENTERING
                                enteredPin = enteredPin.dropLast(1)
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Small "Abhi nahi" skip button
                NiyuvaGhostButton(
                    text = "Abhi nahi",
                    onClick = { showBottomSheet = true },
                    modifier = Modifier.size(width = 140.dp, height = 44.dp)
                )
            }
        }

        // Bottom Sheet Skip Warning
        if (showBottomSheet) {
            NiyuvaBottomSheet(
                onDismissRequest = { showBottomSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Bina PIN ke koi bhi yeh app khol sakta hai 🔒 — sure hai?",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = DeepWarmBrown,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    NiyuvaPrimaryButton(
                        text = "Set PIN now",
                        onClick = { showBottomSheet = false }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NiyuvaGhostButton(
                        text = "Haan, skip for now",
                        onClick = {
                            showBottomSheet = false
                            onSkipPin()
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
