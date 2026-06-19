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
import com.niyuva.app.presentation.components.NiyuvaDrumPicker
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.theme.*

@Composable
fun OnboardingAgeScreen(
    onBack: () -> Unit,
    onAgeSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedYear by remember { mutableStateOf(2007) }
    val years = remember { (1998..2010).toList() }

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
                // Title / Greeting
                Text(
                    text = "Apni details batao 🌸",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                // Label above picker
                Text(
                    text = "Apna janam saal chunao",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Drum Year Picker
                NiyuvaDrumPicker(
                    values = years,
                    defaultValue = 2007,
                    onValueSelected = { year ->
                        if (year != null) {
                            selectedYear = year
                        }
                    },
                    isUnknown = false,
                    onScrollStarted = {},
                    modifier = Modifier.align(Alignment.CenterHorizontally)
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
                    onClick = { onAgeSelected(selectedYear) },
                    enabled = true
                )
            }
        }
    }
}
