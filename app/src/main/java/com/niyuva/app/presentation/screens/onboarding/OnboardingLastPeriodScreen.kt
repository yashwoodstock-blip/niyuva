package com.niyuva.app.presentation.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.components.NiyuvaCalendarPicker
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.components.NiyuvaTextLink
import com.niyuva.app.presentation.components.NiyuvaTooltipCard
import com.niyuva.app.presentation.theme.*
import java.time.LocalDate

@Composable
fun OnboardingLastPeriodScreen(
    userName: String,
    onBack: () -> Unit,
    onContinue: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var showTooltip by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // Top row with Back Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = PureWhite.copy(alpha = 0.6f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepWarmBrown
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "$userName, teri last period kab shuru hui thi?",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Calendar Picker
                NiyuvaCalendarPicker(
                    onDateSelected = { date ->
                        selectedDate = date
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Link to show tooltip
                NiyuvaTextLink(
                    text = "Exactly yaad nahi — approximate bata deti hoon",
                    onClick = { showTooltip = true }
                )
            }

            // Bottom Continue CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                NiyuvaPrimaryButton(
                    text = "Continue",
                    onClick = { selectedDate?.let { onContinue(it) } },
                    enabled = selectedDate != null
                )
            }
        }

        // Tooltip Overlay
        NiyuvaTooltipCard(
            visible = showTooltip,
            text = "Koi baat nahi! Approximate date select kar lo — app dhheere dhheere khud seekh jaayegi 🌸",
            onDismiss = { showTooltip = false }
        )
    }
}
