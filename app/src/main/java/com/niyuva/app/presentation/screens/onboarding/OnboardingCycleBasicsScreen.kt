package com.niyuva.app.presentation.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.niyuva.app.presentation.components.NiyuvaTextLink
import com.niyuva.app.presentation.components.NiyuvaTooltipCard
import com.niyuva.app.presentation.theme.*

@Composable
fun OnboardingCycleBasicsScreen(
    onBack: () -> Unit,
    onContinue: (periodLength: Int?, cycleLength: Int?) -> Unit,
    modifier: Modifier = Modifier
) {
    var periodLength by remember { mutableStateOf<Int?>(5) }
    var cycleLength by remember { mutableStateOf<Int?>(28) }

    var isPeriodUnknown by remember { mutableStateOf(false) }
    var isCycleUnknown by remember { mutableStateOf(false) }

    var showTooltip by remember { mutableStateOf(false) }
    var tooltipText by remember { mutableStateOf("") }

    val periodValues = remember { (2..10).toList() }
    val cycleValues = remember { (20..45).toList() }

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
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Thoda aur bata — better predictions ke liye 💛",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Picker 1 - Period Length
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Period kitne din rehta hai?",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = DeepWarmBrown,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NiyuvaDrumPicker(
                        values = periodValues,
                        defaultValue = 5,
                        onValueSelected = {
                            periodLength = it
                            isPeriodUnknown = false
                        },
                        isUnknown = isPeriodUnknown,
                        onScrollStarted = {
                            isPeriodUnknown = false
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    NiyuvaTextLink(
                        text = "Pata nahi",
                        onClick = {
                            isPeriodUnknown = true
                            periodLength = null
                            tooltipText = "Period length matlab: period ke pehle din se aakhri din tak. Agar 3 January ko shuru hua aur 6 January ko khatam — toh 4 din ka period hai 🌸"
                            showTooltip = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Picker 2 - Cycle Length
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Cycle kitne din ka hota hai usually?",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = DeepWarmBrown,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 32.dp),
                        textAlign = TextAlign.Start
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    NiyuvaDrumPicker(
                        values = cycleValues,
                        defaultValue = 28,
                        onValueSelected = {
                            cycleLength = it
                            isCycleUnknown = false
                        },
                        isUnknown = isCycleUnknown,
                        onScrollStarted = {
                            isCycleUnknown = false
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    NiyuvaTextLink(
                        text = "Pata nahi",
                        onClick = {
                            isCycleUnknown = true
                            cycleLength = null
                            tooltipText = "Cycle length matlab period ke pehle din se agli period ke pehle din tak ke din. Jaise 1 January ko period aaya aur 29 January ko agli period — toh tera cycle 28 din ka hai. App dhheere dhheere khud seekh jaayegi 🌸"
                            showTooltip = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
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
                    onClick = { onContinue(periodLength, cycleLength) },
                    enabled = true
                )
            }
        }

        // Tooltip Overlay
        NiyuvaTooltipCard(
            visible = showTooltip,
            text = tooltipText,
            onDismiss = { showTooltip = false }
        )
    }
}
