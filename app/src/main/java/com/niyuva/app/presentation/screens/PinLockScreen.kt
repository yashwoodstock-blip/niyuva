package com.niyuva.app.presentation.screens

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.niyuva.app.presentation.components.NiyuvaGhostButton
import com.niyuva.app.presentation.components.NiyuvaNumericKeypad
import com.niyuva.app.presentation.components.NiyuvaPinEntry
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.components.NiyuvaTextLink
import com.niyuva.app.presentation.navigation.NavRoutes
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DestructiveRose
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.WarmIvory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PinLockScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PinLockViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val wrongAttempts by viewModel.wrongAttempts.collectAsStateWithLifecycle()

    var enteredPin by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }
    var isChecking by remember { mutableStateOf(false) }
    var subtitleText by remember { mutableStateOf("") }

    var showForgotPinSheet by remember { mutableStateOf(false) }
    var showLimitAlert by remember { mutableStateOf(false) }

    // Disable Back Navigation
    BackHandler(enabled = true) {
        // Do nothing - user cannot exit lock screen via back button
    }

    // Set subtitle text based on states
    LaunchedEffect(profile, isError) {
        if (!isError) {
            val name = profile?.name?.ifBlank { "User" } ?: "User"
            subtitleText = "Namaste, $name! 🌸"
        }
    }

    val handleDigitEntered: (Int) -> Unit = { digit ->
        if (enteredPin.length < 4 && !isError && !isChecking) {
            enteredPin += digit.toString()

            if (enteredPin.length == 4) {
                isChecking = true
                coroutineScope.launch {
                    delay(300) // Small delay for dot visual feedback
                    val verified = viewModel.verifyPin(enteredPin)
                    if (verified) {
                        isError = false
                        navController.navigate(NavRoutes.Main.route) {
                            popUpTo(NavRoutes.PinLock.route) { inclusive = true }
                        }
                    } else {
                        isError = true
                        subtitleText = "Galat PIN — phir try karo 💛"

                        if (wrongAttempts >= 5) {
                            showLimitAlert = true
                        }

                        delay(1500)
                        enteredPin = ""
                        isError = false
                    }
                    isChecking = false
                }
            }
        }
    }

    val handleBackspace: () -> Unit = {
        if (enteredPin.isNotEmpty() && !isError && !isChecking) {
            enteredPin = enteredPin.dropLast(1)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Wordmark
            Text(
                text = "NIYUVA",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = DeepPlumRose,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Apna PIN daalo 🔒",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 22.sp,
                color = DeepWarmBrown,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = subtitleText,
                fontFamily = NunitoFamily,
                fontSize = 14.sp,
                color = if (isError) DestructiveRose else DustyMauve,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.weight(0.3f))

            // Pin Dots
            NiyuvaPinEntry(
                enteredDigits = enteredPin.length,
                isError = isError,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.weight(0.3f))

            // Keypad
            NiyuvaNumericKeypad(
                onDigitEntered = handleDigitEntered,
                onBackspace = handleBackspace,
                modifier = Modifier.wrapContentWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Forgot PIN link
            NiyuvaTextLink(
                text = "PIN bhool gayi?",
                onClick = { showForgotPinSheet = true }
            )

            Spacer(modifier = Modifier.height(48.dp))
        }
    }

    // Forgot PIN Sheet
    if (showForgotPinSheet) {
        ForgotPinSheet(
            securityQuestion = profile?.securityQuestion,
            onVerifyAnswer = { viewModel.verifySecurityAnswer(it) },
            onSetPin = { viewModel.setPin(it) },
            onDeleteAllData = {
                viewModel.deleteAllData {
                    navController.navigate(NavRoutes.Onboarding.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            },
            onDismiss = { showForgotPinSheet = false },
            onPinResetSuccess = {
                Toast.makeText(context, "PIN successfully reset! 🌸", Toast.LENGTH_SHORT).show()
                navController.navigate(NavRoutes.Main.route) {
                    popUpTo(NavRoutes.PinLock.route) { inclusive = true }
                }
            }
        )
    }

    // Limit warning alert
    if (showLimitAlert) {
        AlertDialog(
            onDismissRequest = { showLimitAlert = false },
            title = {
                Text(
                    text = "Security Alert ⚠️",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = DeepWarmBrown
                )
            },
            text = {
                Text(
                    text = "5 baar galat PIN — kya teri security question yaad hai?",
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = DustyMauve
                )
            },
            confirmButton = {
                NiyuvaPrimaryButton(
                    text = "Security Question Try Karo",
                    onClick = {
                        showLimitAlert = false
                        showForgotPinSheet = true
                    }
                )
            },
            dismissButton = {
                NiyuvaGhostButton(
                    text = "Cancel",
                    onClick = { showLimitAlert = false }
                )
            },
            containerColor = WarmIvory,
            shape = RoundedCornerShape(20.dp)
        )
    }
}


