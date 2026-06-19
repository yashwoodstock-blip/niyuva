package com.niyuva.app.presentation.screens.me

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.components.NiyuvaBottomSheet
import com.niyuva.app.presentation.components.NiyuvaGhostButton
import com.niyuva.app.presentation.components.NiyuvaNumericKeypad
import com.niyuva.app.presentation.components.NiyuvaPinEntry
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.components.NiyuvaTextLink
import com.niyuva.app.presentation.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class PinSheetStep {
    VERIFY_CURRENT,
    ENTER_NEW,
    CONFIRM_NEW
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinManagementSheet(
    hasExistingPin: Boolean,
    onVerifyPin: suspend (String) -> Boolean,
    onSetPin: (String) -> Unit,
    onRemovePin: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    var currentStep by remember {
        mutableStateOf(
            if (hasExistingPin) PinSheetStep.VERIFY_CURRENT else PinSheetStep.ENTER_NEW
        )
    }

    var enteredPin by remember { mutableStateOf("") }
    var newPinCandidate by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isVerifying by remember { mutableStateOf(false) }
    var subtitleText by remember { mutableStateOf("") }
    var showRemoveConfirmDialog by remember { mutableStateOf(false) }

    // Update subtitle text based on current step
    LaunchedEffect(currentStep, isError) {
        if (!isError) {
            subtitleText = when (currentStep) {
                PinSheetStep.VERIFY_CURRENT -> "Pehle apna current PIN verify karo"
                PinSheetStep.ENTER_NEW -> if (hasExistingPin) "Naya PIN daalo 🔒" else "Apna naya PIN set karo 🔒"
                PinSheetStep.CONFIRM_NEW -> "Naya PIN confirm karo ✨"
            }
        }
    }

    val handleDigitEntered: (Int) -> Unit = { digit ->
        if (enteredPin.length < 4 && !isError && !isVerifying) {
            enteredPin += digit.toString()

            if (enteredPin.length == 4) {
                isVerifying = true
                coroutineScope.launch {
                    delay(300) // Small delay for visual feedback of final dot
                    when (currentStep) {
                        PinSheetStep.VERIFY_CURRENT -> {
                            val verified = onVerifyPin(enteredPin)
                            if (verified) {
                                currentStep = PinSheetStep.ENTER_NEW
                                enteredPin = ""
                            } else {
                                isError = true
                                subtitleText = "Galat PIN — phir try karo 💛"
                                delay(1500)
                                enteredPin = ""
                                isError = false
                            }
                        }
                        PinSheetStep.ENTER_NEW -> {
                            newPinCandidate = enteredPin
                            enteredPin = ""
                            currentStep = PinSheetStep.CONFIRM_NEW
                        }
                        PinSheetStep.CONFIRM_NEW -> {
                            if (enteredPin == newPinCandidate) {
                                onSetPin(enteredPin)
                                onDismiss()
                            } else {
                                isError = true
                                subtitleText = "PIN match nahi hua — phir try karo 💛"
                                delay(1500)
                                enteredPin = ""
                                currentStep = PinSheetStep.ENTER_NEW
                                isError = false
                            }
                        }
                    }
                    isVerifying = false
                }
            }
        }
    }

    val handleBackspace: () -> Unit = {
        if (enteredPin.isNotEmpty() && !isError && !isVerifying) {
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
                .padding(bottom = 24.dp, top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Drag handle placeholder area
            Spacer(modifier = Modifier.height(16.dp))

            AnimatedContent(
                targetState = currentStep,
                label = "PinSheetHeaderTransition",
                modifier = Modifier.fillMaxWidth()
            ) { step ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = when (step) {
                            PinSheetStep.VERIFY_CURRENT -> "PIN Verify Karo"
                            else -> "PIN Management"
                        },
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = DeepWarmBrown,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = subtitleText,
                        fontFamily = NunitoFamily,
                        fontSize = 14.sp,
                        color = if (isError) DestructiveRose else DustyMauve,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            NiyuvaPinEntry(
                enteredDigits = enteredPin.length,
                isError = isError,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            NiyuvaNumericKeypad(
                onDigitEntered = handleDigitEntered,
                onBackspace = handleBackspace,
                modifier = Modifier.wrapContentWidth()
            )

            // Remove PIN option - only show in change PIN mode, not set new PIN mode
            if (hasExistingPin && currentStep == PinSheetStep.VERIFY_CURRENT) {
                Spacer(modifier = Modifier.height(24.dp))
                NiyuvaTextLink(
                    text = "PIN remove karo",
                    onClick = { showRemoveConfirmDialog = true }
                )
            }
        }
    }

    if (showRemoveConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveConfirmDialog = false },
            title = {
                Text(
                    text = "PIN hatana chahte ho?",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DeepWarmBrown
                )
            },
            text = {
                Text(
                    text = "PIN hatane se koi bhi app khol sakta hai — sure hai?",
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = DustyMauve
                )
            },
            confirmButton = {
                NiyuvaPrimaryButton(
                    text = "Haan, hata do",
                    onClick = {
                        onRemovePin()
                        showRemoveConfirmDialog = false
                        onDismiss()
                    }
                )
            },
            dismissButton = {
                NiyuvaGhostButton(
                    text = "Cancel",
                    onClick = { showRemoveConfirmDialog = false }
                )
            },
            containerColor = WarmIvory,
            shape = RoundedCornerShape(20.dp)
        )
    }
}
