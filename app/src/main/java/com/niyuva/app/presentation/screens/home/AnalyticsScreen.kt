package com.niyuva.app.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.niyuva.app.presentation.components.EmptyStateView
import com.niyuva.app.presentation.components.SymptomTrendChart
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite
import com.niyuva.app.presentation.theme.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    navController: NavController,
    viewModel: AnalyticsViewModel = hiltViewModel()
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

    val state by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tera Health Trends",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = DeepWarmBrown
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepWarmBrown
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmIvory)
            )
        },
        containerColor = WarmIvory
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Chart Type Selector (Energy / Mood / Pain / Flow)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0xFFF0E5E8)) // Very soft plum background
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnalyticsChartType.entries.forEach { type ->
                    val selected = state.selectedChartType == type
                    val bg = if (selected) Color(0xFF8B5E6D) else Color.Transparent
                    val tc = if (selected) PureWhite else Color(0xFFA07A85)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(100.dp))
                            .background(bg)
                            .clickable { viewModel.selectChartType(type) }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                            fontFamily = NunitoFamily,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = tc,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Time Period Selector (3 months / 6 months / 12 months)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .background(Color(0xFFF0E5E8))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AnalyticsTimePeriod.entries.forEach { period ->
                    val selected = state.selectedTimePeriod == period
                    val bg = if (selected) Color(0xFF8B5E6D) else Color.Transparent
                    val tc = if (selected) PureWhite else Color(0xFFA07A85)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(100.dp))
                            .background(bg)
                            .clickable { viewModel.selectTimePeriod(period) }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${period.months} months",
                            fontFamily = NunitoFamily,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp,
                            color = tc,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Chart Rendering
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(PureWhite),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF8B5E6D))
                }
            } else if (state.isEmpty) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = PureWhite),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val name = state.selectedChartType.name.lowercase()
                        EmptyStateView(
                            emoji = "📊",
                            text = "Abhi tak $name log nahi kiya — jab karegi, yahan trend dikhega 🌸"
                        )
                    }
                }
            } else {
                SymptomTrendChart(
                    yAxisLabels = state.yAxisLabels,
                    xAxisLabels = state.xAxisLabels,
                    chartLines = state.chartLines,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Encouragement Text Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5E6DC)), // Warm Linen
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Didi phase insight ✨",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E1418)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    val description = when (state.selectedChartType) {
                        AnalyticsChartType.ENERGY -> "Tera energy levels tere cycle phases ke sath move karta hai. Menstruation ke baad energy slowly badhti hai."
                        AnalyticsChartType.MOOD -> "Mood shifts normal hain! Chart dekh kar tu samajh sakti hai ki kab tu thoda heavy feel karti hai aur kab super active."
                        AnalyticsChartType.PAIN -> "Pain patterns ko follow karke tu doctor ko accurate data de sakti hai. Period cramps ya backache ko yahan dekh lo."
                        AnalyticsChartType.FLOW -> "Har cycle ka flow verify karo taaki cycle regularity aur heaviness ko better monitor kiya ja sake."
                    }
                    Text(
                        text = description,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 13.sp,
                        color = Color(0xFF1E1418),
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}
