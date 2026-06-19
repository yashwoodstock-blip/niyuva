package com.niyuva.app.presentation.screens.home

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.niyuva.app.presentation.components.NiyuvaDotTimeline
import com.niyuva.app.presentation.components.NiyuvaBottomSheet
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.components.NiyuvaTextField
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite
import com.niyuva.app.presentation.theme.Terracotta
import com.niyuva.app.presentation.theme.WarmIvory
import com.niyuva.app.presentation.theme.WarmLinen
import com.niyuva.app.presentation.theme.DestructiveRose
import android.content.Intent
import android.widget.Toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CycleReportScreen(
    navController: NavController,
    viewModel: CycleReportViewModel = hiltViewModel()
) {
    val view = androidx.compose.ui.platform.LocalView.current
    if (!view.isInEditMode) {
        androidx.compose.runtime.SideEffect {
            val window = (view.context as? android.app.Activity)?.window
            if (window != null) {
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                val insetsController = androidx.core.view.WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = true // Dark icons
            }
        }
    }
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var patientName by remember(state.userName) { mutableStateOf(state.userName) }
    var selectedMonths by remember { mutableStateOf(3) }

    val formatterFull = remember { DateTimeFormatter.ofPattern("EEE, MMM d") }
    val formatterShort = remember { DateTimeFormatter.ofPattern("MMM d") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .statusBarsPadding()
    ) {
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                com.niyuva.app.presentation.components.NiyuvaShimmer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    cornerRadius = 20.dp
                )
                com.niyuva.app.presentation.components.NiyuvaShimmer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    cornerRadius = 20.dp
                )
                com.niyuva.app.presentation.components.NiyuvaShimmer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    cornerRadius = 20.dp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    bottom = androidx.compose.foundation.layout.WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 64.dp + 16.dp
                )
            ) {
                // Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = "Back",
                                tint = DeepWarmBrown,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Text(
                            text = "Cycle report",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = DeepWarmBrown
                        )

                        // Pill badge: Cycle [N]
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Terracotta)
                                .padding(vertical = 4.dp, horizontal = 10.dp)
                        ) {
                            Text(
                                text = "Cycle ${state.cycles.size}",
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                // Greeting row — compact, no mascot circle
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = if (state.userName.isNotBlank()) "Hi ${state.userName}," else "Hi,",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            color = DeepWarmBrown
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Apni cycle history aur averages yahan dekh lo 📊",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 13.sp,
                            color = DustyMauve,
                            lineHeight = 18.sp
                        )
                    }
                }

                // Empty state card check
                if (state.cycles.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Abhi tak koi cycle log nahi hua 💛 — Home pe ja aur apni period date add kar!",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 15.sp,
                                    color = DeepWarmBrown,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 22.sp
                                )
                            }
                        }
                    }
                } else {
                    val latestCycle = state.cycles.first()

                    // Card 1 — Last Period
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Teri last period shuru hui",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = DustyMauve
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = state.lastPeriodStart?.format(formatterFull) ?: "—",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 26.sp,
                                    color = Terracotta
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🕐", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Period ${latestCycle.periodLength ?: 5} din raha",
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = DeepWarmBrown
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "💧", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Flow ${state.lastPeriodFlow} tha",
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = DeepWarmBrown
                                    )
                                }
                            }
                        }
                    }

                    // Card 2 — Last Cycle Dates
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = "Tera last cycle",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 12.sp,
                                    color = DustyMauve
                                )
                                Spacer(modifier = Modifier.height(4.dp))

                                val rangeStartStr = latestCycle.startDate.format(formatterShort)
                                val rangeEndStr = latestCycle.endDate?.format(formatterShort)
                                    ?: latestCycle.startDate.plusDays((latestCycle.cycleLength ?: 28).toLong()).format(formatterShort)

                                Text(
                                    text = "$rangeStartStr – $rangeEndStr",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 24.sp,
                                    color = Terracotta
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "📅", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${latestCycle.cycleLength ?: 28} din ka cycle",
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = DeepWarmBrown
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🔄", fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(8.dp))

                                    val isRegularText = if (state.regularityStatus == RegularityStatus.REGULAR) "regular rhe" else "irregular rhe"
                                    val countText = state.cycles.size.coerceAtMost(3)
                                    Text(
                                        text = "Last $countText cycles $isRegularText",
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        color = DeepWarmBrown
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Dot Timeline
                                val rangeEndVal = latestCycle.endDate ?: latestCycle.startDate.plusDays((latestCycle.cycleLength ?: 28).toLong())
                                NiyuvaDotTimeline(
                                    cycleLength = latestCycle.cycleLength ?: 28,
                                    periodLength = latestCycle.periodLength ?: 5,
                                    startDate = latestCycle.startDate,
                                    endDate = rangeEndVal
                                )
                            }
                        }
                    }

                    // Card 3 — Ovulation
                    if (state.prediction?.ovulationDate != null) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhite),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Box(modifier = Modifier.padding(20.dp)) {
                                    Column(modifier = Modifier.padding(end = 40.dp)) {
                                        Text(
                                            text = "Ovulation likely thi",
                                            fontFamily = NunitoFamily,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp,
                                            color = DustyMauve
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = state.prediction!!.ovulationDate.format(formatterFull),
                                            fontFamily = NunitoFamily,
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 26.sp,
                                            color = Color(0xFFA8C5A0) // Sage Green
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Predicted Day of Cycle calculation
                                        val dayOfCycle = java.time.temporal.ChronoUnit.DAYS.between(latestCycle.startDate, state.prediction!!.ovulationDate) + 1
                                        Text(
                                            text = "Predicted · Day $dayOfCycle of cycle",
                                            fontFamily = NunitoFamily,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp,
                                            color = DustyMauve
                                        )
                                    }

                                    // Icon top-right
                                    Text(
                                        text = "🥚",
                                        fontSize = 32.sp,
                                        modifier = Modifier.align(Alignment.TopEnd)
                                    )
                                }
                            }
                        }
                    }

                    // Card 4 — Cycle Averages
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Average cycle",
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = DeepWarmBrown
                                    )
                                    val cycleAvgStr = state.averageCycleLength?.let { "$it din" } ?: "—"
                                    Text(
                                        text = cycleAvgStr,
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Terracotta
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Average period",
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = DeepWarmBrown
                                    )
                                    val periodAvgStr = state.averagePeriodLength?.let { "$it din" } ?: "—"
                                    Text(
                                        text = periodAvgStr,
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = Terracotta
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                // Regularity Badge
                                if (state.regularityStatus != RegularityStatus.UNKNOWN) {
                                    val isRegular = state.regularityStatus == RegularityStatus.REGULAR
                                    val badgeBg = if (isRegular) Color(0xFFD8EDD5) else Color(0xFFF5DDD5)
                                    val badgeText = if (isRegular) Color(0xFF4A8A42) else DestructiveRose
                                    val badgeLabel = if (isRegular) "Regular" else "Irregular"

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(badgeBg)
                                            .padding(vertical = 6.dp, horizontal = 12.dp)
                                    ) {
                                        Text(
                                            text = badgeLabel,
                                            fontFamily = NunitoFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            color = badgeText
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { showBottomSheet = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .height(56.dp),
                        border = BorderStroke(1.5.dp, DeepWarmBrown),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = DeepWarmBrown
                        )
                    ) {
                        Text(
                            text = "Doctor Report banayein 📄",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }

                // Bottom spacer
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }

        if (showBottomSheet) {
            NiyuvaBottomSheet(
                onDismissRequest = { showBottomSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 8.dp, bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Doctor Report Details 📄",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = DeepWarmBrown
                    )

                    Text(
                        text = "Apne doctor ke liye report custom karein. Report direct share ho sakegi.",
                        fontFamily = NunitoFamily,
                        fontSize = 14.sp,
                        color = DustyMauve
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Patient ka naam (Optional)",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = DeepWarmBrown
                    )

                    NiyuvaTextField(
                        value = patientName,
                        onValueChange = { patientName = it },
                        placeholder = "Name yahan likhein..."
                    )

                    Text(
                        text = "Report range choose karein",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = DeepWarmBrown
                    )

                    val monthOptions = listOf("3 Months", "6 Months", "12 Months")
                    val selectedIndex = when (selectedMonths) {
                        3 -> 0
                        6 -> 1
                        12 -> 2
                        else -> 0
                    }

                    com.niyuva.app.presentation.components.NiyuvaSegmentedControl(
                        options = monthOptions,
                        selectedIndex = selectedIndex,
                        onOptionSelected = { index ->
                            selectedMonths = when (index) {
                                0 -> 3
                                1 -> 6
                                2 -> 12
                                else -> 3
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NiyuvaPrimaryButton(
                        text = "PDF Banayein",
                        onClick = {
                            viewModel.generateDoctorReport(patientName, selectedMonths) { file ->
                                if (file != null) {
                                    val shareUri = viewModel.getShareUri(file)
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "application/pdf"
                                        putExtra(Intent.EXTRA_STREAM, shareUri)
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Doctor Report"))
                                } else {
                                    Toast.makeText(context, "PDF report generate karne me dikkat aayi", Toast.LENGTH_SHORT).show()
                                }
                                showBottomSheet = false
                            }
                        }
                    )
                }
            }
        }
    }
}
