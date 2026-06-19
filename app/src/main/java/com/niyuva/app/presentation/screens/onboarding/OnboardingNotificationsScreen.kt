package com.niyuva.app.presentation.screens.onboarding

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.components.NiyuvaTextLink
import com.niyuva.app.presentation.components.NiyuvaToggleRow
import com.niyuva.app.presentation.theme.*
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OnboardingNotificationsScreen(
    onComplete: (period: Boolean, tip: Boolean, ovulation: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var periodReminder by remember { mutableStateOf(true) }
    var dailyTip by remember { mutableStateOf(true) }
    var ovulationReminder by remember { mutableStateOf(false) }

    val isAndroid13Plus = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    val permissionState = if (isAndroid13Plus) {
        rememberPermissionState(permission = android.Manifest.permission.POST_NOTIFICATIONS)
    } else {
        null
    }

    var hasClickedContinue by remember { mutableStateOf(false) }
    var useAllToggles by remember { mutableStateOf(false) }

    val performSaveAndNavigate = {
        val finalPeriod = if (useAllToggles) true else periodReminder
        val finalTip = if (useAllToggles) true else dailyTip
        val finalOvulation = if (useAllToggles) true else ovulationReminder

        onComplete(finalPeriod, finalTip, finalOvulation)
    }

    // Handle permission check results when the permission request completes
    if (isAndroid13Plus && permissionState != null) {
        LaunchedEffect(permissionState.status) {
            if (hasClickedContinue) {
                // The permission prompt completed (either granted or denied)
                performSaveAndNavigate()
                hasClickedContinue = false
            }
        }
    }

    val onContinueClick = { all: Boolean ->
        useAllToggles = all
        if (all) {
            periodReminder = true
            dailyTip = true
            ovulationReminder = true
        }

        if (isAndroid13Plus && permissionState != null && !permissionState.status.isGranted) {
            hasClickedContinue = true
            permissionState.launchPermissionRequest()
        } else {
            // Android 12 or below, or permission already granted
            performSaveAndNavigate()
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
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Kya main tujhe remind kar sakti hoon? 🔔",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Toggle Row 1 — Period Reminder
                NiyuvaToggleRow(
                    icon = Icons.Outlined.CalendarMonth,
                    label = "Period aa raha hai — 3 din pehle bataaun?",
                    subLabel = "3 days before",
                    checked = periodReminder,
                    onCheckedChange = { periodReminder = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Toggle Row 2 — Daily Morning Tip
                NiyuvaToggleRow(
                    icon = Icons.Outlined.WbSunny,
                    label = "Aaj ka tip — har subah 🌸",
                    subLabel = "Every morning at 8:00 AM",
                    checked = dailyTip,
                    onCheckedChange = { dailyTip = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Toggle Row 3 — Ovulation Reminder
                NiyuvaToggleRow(
                    icon = Icons.Outlined.Opacity,
                    label = "Ovulation day ke baare mein bhi bataaun? 🥚",
                    subLabel = "1 day before predicted ovulation",
                    checked = ovulationReminder,
                    onCheckedChange = { ovulationReminder = it }
                )
            }

            // Bottom CTA Buttons
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                NiyuvaPrimaryButton(
                    text = "Haan, sab remind karo",
                    onClick = { onContinueClick(true) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                NiyuvaTextLink(
                    text = "Main customize karoongi",
                    onClick = { onContinueClick(false) }
                )
            }
        }
    }
}
