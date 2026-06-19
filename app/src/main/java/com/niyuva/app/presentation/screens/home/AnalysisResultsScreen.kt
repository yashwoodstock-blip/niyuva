package com.niyuva.app.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.niyuva.app.presentation.components.NiyuvaShimmer
import com.niyuva.app.presentation.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisResultsScreen(
    navController: NavController,
    viewModel: AnalysisResultsViewModel = hiltViewModel()
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
    var showRefreshConfirm by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "AI Health Analysis",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = DeepWarmBrown
                    )
                }

                if (!state.isLoading) {
                    IconButton(
                        onClick = { showRefreshConfirm = true },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = "Refresh",
                            tint = DeepWarmBrown,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Error Banner
            if (state.error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DestructiveRose.copy(alpha = 0.1f)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DestructiveRose)
                ) {
                    Text(
                        text = state.error!!,
                        fontFamily = NunitoFamily,
                        fontSize = 14.sp,
                        color = DestructiveRose,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 24.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Progress Message if active
                if (state.isLoading && state.progressMessage.isNotBlank()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = WarmLinen)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Terracotta,
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = state.progressMessage,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = DeepWarmBrown
                                )
                            }
                        }
                    }
                }

                // Card 1 — Synthesized Hinglish takeaway (Chunk 3)
                item {
                    val content = state.synthesisSummary
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Aapka Takeaway 🧠",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DeepWarmBrown,
                            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                        )

                        if (content != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            brush = Brush.horizontalGradient(
                                                colors = listOf(
                                                    Color(0xFFFFF0F5),
                                                    Color(0xFFFDE6D2)
                                                )
                                            )
                                        )
                                        .border(1.dp, Color(0xFFEAD2C6), RoundedCornerShape(20.dp))
                                        .padding(20.dp)
                                ) {
                                    Text(
                                        text = content,
                                        fontFamily = NunitoFamily,
                                        fontSize = 15.sp,
                                        color = DeepWarmBrown,
                                        lineHeight = 22.sp
                                    )
                                }
                            }
                        } else if (state.isLoading && state.activeChunkIndex >= 1) {
                            NiyuvaShimmer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(140.dp),
                                cornerRadius = 20.dp
                            )
                        } else {
                            PlaceholderCard(text = "Overall takeaway reports aur correlation ke baad generate hogi...")
                        }
                    }
                }

                // Card 2 — Cycle pattern regularity (Chunk 1)
                item {
                    val content = state.cyclePatterns
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Cycle Patterns & Regularity 🔄",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DeepWarmBrown,
                            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                        )

                        if (content != null) {
                            val result = Chunk1Result.fromJson(content)
                            val pillBg = when (result?.regularityStatus?.lowercase()) {
                                "regular" -> Color(0xFFE2F3E4)
                                "irregular" -> Color(0xFFFCE8E6)
                                else -> WarmLinen
                            }
                            val pillText = when (result?.regularityStatus?.lowercase()) {
                                "regular" -> Color(0xFF2E7D32)
                                "irregular" -> Color(0xFFC62828)
                                else -> DeepWarmBrown
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhite),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Column(modifier = Modifier.padding(20.dp)) {
                                    if (result != null) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(pillBg)
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = result.regularityStatus,
                                                    fontFamily = NunitoFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp,
                                                    color = pillText
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))

                                        Text(
                                            text = result.analysisText,
                                            fontFamily = NunitoFamily,
                                            fontSize = 14.sp,
                                            color = DeepWarmBrown,
                                            lineHeight = 20.sp
                                        )

                                        val isLowConfidence = result.dataConfidence.lowercase() == "low" || state.cycleCount < 3
                                        if (isLowConfidence) {
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFE8965A).copy(alpha = 0.1f))
                                                    .border(1.dp, Color(0xFFE8965A).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                                    .padding(12.dp)
                                            ) {
                                                Text(
                                                    text = androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.low_data_analysis_warning),
                                                    fontFamily = NunitoFamily,
                                                    fontSize = 12.sp,
                                                    color = DeepWarmBrown,
                                                    lineHeight = 16.sp
                                                )
                                            }
                                        }
                                    } else {
                                        Text(
                                            text = content,
                                            fontFamily = NunitoFamily,
                                            fontSize = 14.sp,
                                            color = DeepWarmBrown,
                                            lineHeight = 20.sp
                                        )
                                        if (state.cycleCount < 3) {
                                            Spacer(modifier = Modifier.height(16.dp))
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(Color(0xFFE8965A).copy(alpha = 0.1f))
                                                    .border(1.dp, Color(0xFFE8965A).copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                                    .padding(12.dp)
                                            ) {
                                                Text(
                                                    text = androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.low_data_analysis_warning),
                                                    fontFamily = NunitoFamily,
                                                    fontSize = 12.sp,
                                                    color = DeepWarmBrown,
                                                    lineHeight = 16.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (state.isLoading && state.activeChunkIndex == 1) {
                            NiyuvaShimmer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                cornerRadius = 20.dp
                            )
                        } else {
                            PlaceholderCard(text = "Aapke cycle structure ko scan kiya ja raha hai...")
                        }
                    }
                }

                // Card 3 — Correlation patterns (Chunk 2)
                item {
                    val content = state.symptomsCorrelations
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Symptom Correlations ⚡",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = DeepWarmBrown,
                            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
                        )

                        if (content != null) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = PureWhite),
                                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                            ) {
                                Text(
                                    text = content,
                                    fontFamily = NunitoFamily,
                                    fontSize = 14.sp,
                                    color = DeepWarmBrown,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        } else if (state.isLoading && state.activeChunkIndex >= 1 && state.activeChunkIndex <= 2) {
                            NiyuvaShimmer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(130.dp),
                                cornerRadius = 20.dp
                            )
                        } else {
                            PlaceholderCard(text = "Symptom aur energy logs ke correlations shuru hone wale hain...")
                        }
                    }
                }
            }
        }

        // Confirmation dialog for fresh analysis
        if (showRefreshConfirm) {
            AlertDialog(
                onDismissRequest = { showRefreshConfirm = false },
                containerColor = WarmIvory,
                shape = RoundedCornerShape(24.dp),
                title = {
                    Text(
                        text = "Re-analyze karein? 🧠",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = DeepWarmBrown
                    )
                },
                text = {
                    Text(
                        text = "Kya aap apna AI health analysis refresh karna chahte hain? Naye logs ke correlations check karne me thoda samay lagega.",
                        fontFamily = NunitoFamily,
                        fontSize = 14.sp,
                        color = DustyMauve
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showRefreshConfirm = false
                            viewModel.runNewAnalysis()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepPlumRose)
                    ) {
                        Text("Re-analyze", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showRefreshConfirm = false }) {
                        Text("Cancel", color = DustyMauve)
                    }
                }
            )
        }
    }
}

@Composable
fun PlaceholderCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite.copy(alpha = 0.6f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, WarmLinen)
    ) {
        Text(
            text = text,
            fontFamily = NunitoFamily,
            fontSize = 13.sp,
            color = DustyMauve,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        )
    }
}

private data class Chunk1Result(
    val regularityStatus: String,
    val dataConfidence: String,
    val analysisText: String
) {
    companion object {
        fun fromJson(jsonStr: String): Chunk1Result? {
            return try {
                val obj = org.json.JSONObject(jsonStr)
                Chunk1Result(
                    regularityStatus = obj.optString("regularity_status", "Not enough data"),
                    dataConfidence = obj.optString("data_confidence", "low"),
                    analysisText = obj.optString("analysis_text", jsonStr)
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}
