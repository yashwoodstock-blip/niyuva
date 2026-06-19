package com.niyuva.app.presentation.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
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
import kotlinx.coroutines.launch

private enum class ForgotPinStep {
    VERIFY_QUESTION,
    ENTER_NEW_PIN,
    CONFIRM_NEW_PIN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPinSheet(
    securityQuestion: String?,
    onVerifyAnswer: suspend (String) -> Boolean,
    onSetPin: (String) -> Unit,
    onDeleteAllData: () -> Unit,
    onDismiss: () -> Unit,
    onPinResetSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    var currentStep by remember { mutableStateOf(ForgotPinStep.VERIFY_QUESTION) }
    var answerInput by remember { mutableStateOf("") }
    var enteredPin by remember { mutableStateOf("") }
    var newPinCandidate by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }
    var isChecking by remember { mutableStateOf(false) }
    var isVerifyingPin by remember { mutableStateOf(false) }
    var subtitleText by remember { mutableStateOf("") }
    var wrongAnswerAttempts by remember { mutableStateOf(0) }

    var showDeleteAllConfirmDialog by remember { mutableStateOf(false) }

    val shakeOffset = remember { Animatable(0f) }

    val triggerShake: (String) -> Unit = { msg ->
        subtitleText = msg
        isError = true
        coroutineScope.launch {
            for (i in 0..2) {
                shakeOffset.animateTo(15f, animationSpec = tween(50))
                shakeOffset.animateTo(-15f, animationSpec = tween(50))
            }
            shakeOffset.animateTo(0f, animationSpec = tween(50))
            delay(1500)
            isError = false
        }
    }

    LaunchedEffect(currentStep, isError) {
        if (!isError) {
            subtitleText = when (currentStep) {
                ForgotPinStep.VERIFY_QUESTION -> "Security sawaal ka jawab do"
                ForgotPinStep.ENTER_NEW_PIN -> "Naya PIN set karo 🔒"
                ForgotPinStep.CONFIRM_NEW_PIN -> "Naya PIN confirm karo ✨"
            }
        }
    }

    val handleDigitEntered: (Int) -> Unit = { digit ->
        if (enteredPin.length < 4 && !isError && !isVerifyingPin) {
            enteredPin += digit.toString()

            if (enteredPin.length == 4) {
                isVerifyingPin = true
                coroutineScope.launch {
                    delay(300)
                    if (currentStep == ForgotPinStep.ENTER_NEW_PIN) {
                        newPinCandidate = enteredPin
                        enteredPin = ""
                        currentStep = ForgotPinStep.CONFIRM_NEW_PIN
                    } else if (currentStep == ForgotPinStep.CONFIRM_NEW_PIN) {
                        if (enteredPin == newPinCandidate) {
                            onSetPin(enteredPin)
                            onPinResetSuccess()
                            onDismiss()
                        } else {
                            isError = true
                            triggerShake("PIN match nahi hua — phir try karo 💛")
                            delay(1500)
                            enteredPin = ""
                            currentStep = ForgotPinStep.ENTER_NEW_PIN
                            isError = false
                        }
                    }
                    isVerifyingPin = false
                }
            }
        }
    }

    val handleBackspace: () -> Unit = {
        if (enteredPin.isNotEmpty() && !isError && !isVerifyingPin) {
            enteredPin = enteredPin.dropLast(1)
        }
    }

    NiyuvaBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(bottom = 24.dp, top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Forgot PIN",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = DeepWarmBrown
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = subtitleText,
                fontFamily = NunitoFamily,
                fontSize = 14.sp,
                color = if (isError) DestructiveRose else DustyMauve,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedContent(
                targetState = currentStep,
                label = "ForgotPinStepTransition"
            ) { step ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    when (step) {
                        ForgotPinStep.VERIFY_QUESTION -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Tera security question hai:",
                                    fontFamily = NunitoFamily,
                                    fontSize = 13.sp,
                                    color = DustyMauve
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = securityQuestion ?: "Security question set nahi hai",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = DeepWarmBrown,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Box(modifier = Modifier.offset(x = shakeOffset.value.dp)) {
                                    NiyuvaTextField(
                                        value = answerInput,
                                        onValueChange = { answerInput = it },
                                        placeholder = "Jawab likho..."
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Case matter nahi karta 😊",
                                    fontFamily = NunitoFamily,
                                    fontSize = 11.sp,
                                    color = DustyMauve,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )

                                Spacer(modifier = Modifier.height(32.dp))

                                NiyuvaPrimaryButton(
                                    text = if (isChecking) "Verifying..." else "Verify Answer",
                                    onClick = {
                                        if (answerInput.trim().isNotEmpty() && !isChecking) {
                                            isChecking = true
                                            coroutineScope.launch {
                                                val success = onVerifyAnswer(answerInput)
                                                if (success) {
                                                    isError = false
                                                    currentStep = ForgotPinStep.ENTER_NEW_PIN
                                                } else {
                                                    wrongAnswerAttempts += 1
                                                    if (wrongAnswerAttempts >= 3) {
                                                        showDeleteAllConfirmDialog = true
                                                    } else {
                                                        triggerShake("Jawab match nahi hua — phir try karo 💛")
                                                    }
                                                }
                                                isChecking = false
                                            }
                                        }
                                    },
                                    enabled = answerInput.trim().isNotEmpty() && !isChecking,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                        ForgotPinStep.ENTER_NEW_PIN, ForgotPinStep.CONFIRM_NEW_PIN -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                NiyuvaPinEntry(
                                    enteredDigits = enteredPin.length,
                                    isError = isError,
                                    modifier = Modifier.padding(horizontal = 32.dp)
                                )

                                Spacer(modifier = Modifier.height(32.dp))

                                NiyuvaNumericKeypad(
                                    onDigitEntered = handleDigitEntered,
                                    onBackspace = handleBackspace,
                                    modifier = Modifier.wrapContentWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDeleteAllConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAllConfirmDialog = false },
            title = {
                Text(
                    text = "Restart Fresh? ⚠️",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DestructiveRose
                )
            },
            text = {
                Text(
                    text = "Agar jawab yaad nahi hai toh ek option hai: Data delete karke fresh start karo 💛",
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = DeepWarmBrown
                )
            },
            confirmButton = {
                NiyuvaPrimaryButton(
                    text = "Delete & Restart",
                    onClick = {
                        onDeleteAllData()
                        showDeleteAllConfirmDialog = false
                        onDismiss()
                    }
                )
            },
            dismissButton = {
                NiyuvaGhostButton(
                    text = "Cancel",
                    onClick = { showDeleteAllConfirmDialog = false }
                )
            },
            containerColor = WarmIvory,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
