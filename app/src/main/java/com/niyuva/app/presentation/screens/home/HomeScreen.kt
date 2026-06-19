package com.niyuva.app.presentation.screens.home

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.compose.ui.text.style.TextAlign
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.components.NiyuvaCycleRingCard
import com.niyuva.app.presentation.components.NiyuvaTextLink
import com.niyuva.app.presentation.components.NiyuvaDayStrip
import com.niyuva.app.presentation.components.NiyuvaPredictionsRow
import com.niyuva.app.presentation.components.NiyuvaRecommendationSection
import com.niyuva.app.presentation.components.NiyuvaShimmer
import com.niyuva.app.presentation.components.NiyuvaTipCard
import com.niyuva.app.presentation.components.NiyuvaIrregularityCard
import com.niyuva.app.presentation.navigation.NavRoutes
import com.niyuva.app.presentation.theme.BlushMist
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite
import com.niyuva.app.presentation.theme.WarmIvory
import com.niyuva.app.presentation.theme.WarmLinen
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.draw.alpha
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.components.formatShortDate
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.material3.HorizontalDivider
import com.niyuva.app.data.local.content.KyaKhayenData
import com.niyuva.app.presentation.theme.PlayfairFamily


// ─────────────────────────────────────────────────────────────────────────────
// Home Screen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun HomeScreen(
    navController: NavController,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: HomeViewModel = hiltViewModel()
) {
    val view = androidx.compose.ui.platform.LocalView.current
    if (!view.isInEditMode) {
        androidx.compose.runtime.SideEffect {
            val window = (view.context as? android.app.Activity)?.window
            if (window != null) {
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                val insetsController = androidx.core.view.WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false // White icons
            }
        }
    }

    val uiState        by viewModel.uiState.collectAsStateWithLifecycle()
    val showLogSheet   by viewModel.showLogSheet.collectAsStateWithLifecycle()
    val logSheetState  by viewModel.logSheetState.collectAsStateWithLifecycle()
    val context         = LocalContext.current

    // ── Animated phase gradient colours ──────────────────────────────────────
    val animatedGradientStart by animateColorAsState(
        targetValue    = uiState.phaseTheme.backgroundGradientStart,
        animationSpec  = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label          = "phaseGradientStart"
    )
    val animatedGradientEnd by animateColorAsState(
        targetValue    = uiState.phaseTheme.backgroundGradientEnd,
        animationSpec  = tween(durationMillis = 800, easing = LinearOutSlowInEasing),
        label          = "phaseGradientEnd"
    )

    // ── Full-bleed background ─────────────────────────────────────────────────
    Box(modifier = Modifier.fillMaxSize()) {

        // Phase gradient — fills entire screen, goes under status bar & behind nav bar
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(animatedGradientStart, animatedGradientEnd)
                    )
                )
        )

        // Scrollable content
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = 0.dp,
                bottom = contentPadding.calculateBottomPadding() + 16.dp
            )
        ) {

            // ── Top App Bar ───────────────────────────────────────────────────
            item(key = "topbar") {
                HomeTopBar(
                    userName    = uiState.userName,
                    onAvatarClick = {
                        navController.navigate(NavRoutes.Me.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onBellClick   = {
                        Toast.makeText(context, "Notifications coming soon", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(top = 16.dp, start = 16.dp, end = 8.dp)
                )
            }

            // ── Greeting Section ──────────────────────────────────────────────
            item(key = "greeting") {
                HomeGreeting(
                    greeting  = uiState.greeting,
                    modifier  = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                )
            }

            // ── Streak Badge (Phase 5.1) ──────────────────────────────────────
            if (uiState.currentStreak > 0) {
                item(key = "streak_badge") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        StreakBadge(currentStreak = uiState.currentStreak)
                    }
                }
            }

            // ── Day Strip ─────────────────────────────────────────────────────
            item(key = "daystrip") {
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.isLoading) {
                    HomeDayStripShimmer()
                } else {
                    NiyuvaDayStrip(
                        days       = uiState.dayStrip,
                        phaseColor = uiState.phaseTheme.ringColor,
                        onDayTapped = { /* Phase 15 will handle day tap navigation */ },
                        modifier   = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // ── Error Banner ──────────────────────────────────────────────────
            if (uiState.error != null) {
                item(key = "error") {
                    HomeErrorBanner(
                        message  = uiState.error!!,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // ── Loading Skeleton for cards ────────────────────────────────────
            if (uiState.isLoading) {
                item(key = "loading_skeleton") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        NiyuvaShimmer(
                            modifier     = Modifier.fillMaxWidth().height(72.dp),
                            cornerRadius = 20.dp
                        )
                        NiyuvaShimmer(
                            modifier     = Modifier.fillMaxWidth().height(120.dp),
                            cornerRadius = 20.dp
                        )
                        NiyuvaShimmer(
                            modifier     = Modifier.fillMaxWidth().height(56.dp),
                            cornerRadius = 20.dp
                        )
                    }
                }
            }

            if (uiState.showRecoveryPromptCard) {
                item(key = "streak_recovery_prompt") {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 12.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = WarmLinen),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.streak_recovery_title),
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = DeepWarmBrown,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.streak_recovery_body),
                                fontFamily = NunitoFamily,
                                fontSize = 12.sp,
                                color = DustyMauve,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.showLogSheetFromRecovery() },
                                    colors = ButtonDefaults.buttonColors(containerColor = com.niyuva.app.presentation.theme.DeepPlumRose),
                                    shape = RoundedCornerShape(50.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.streak_recovery_btn_yes),
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                OutlinedButton(
                                    onClick = { viewModel.dismissRecoveryPrompt(applyReset = true) },
                                    border = androidx.compose.foundation.BorderStroke(1.dp, DustyMauve),
                                    shape = RoundedCornerShape(50.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = androidx.compose.ui.res.stringResource(com.niyuva.app.R.string.streak_recovery_btn_no),
                                        color = DustyMauve,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── Cycle Ring Card / Empty State (Phase 15 / 35) ──────────────────
            if (!uiState.isLoading) {
                item(key = "ringcard_or_empty") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    ) {
                        if (uiState.showEmptyState) {
                            EmptyStateCard(
                                onAddDateClick = { viewModel.showLogSheet() },
                                modifier = Modifier.fillMaxWidth()
                            )
                        } else {
                            NiyuvaCycleRingCard(
                                uiState              = uiState,
                                onLogSymptomsClicked = { viewModel.showLogSheet() },
                                modifier             = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                NiyuvaTextLink(
                                    text = "See trends 📈",
                                    textColor = DeepPlumRose,
                                    onClick = {
                                        navController.navigate(NavRoutes.Analytics.route)
                                    }
                                )
                                NiyuvaTextLink(
                                    text = "See cycle report →",
                                    textColor = DeepPlumRose,
                                    onClick = {
                                        navController.navigate(NavRoutes.CycleReport.route)
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // ── Daily Tip Card (Phase 16) ────────────────────────────────
            if (!uiState.isLoading) {
                item(key = "tipcard") {
                    NiyuvaTipCard(
                        tip              = uiState.todayTip,
                        dayInPhase       = uiState.dayInPhase,
                        onReadMoreClicked = {
                            navController.navigate(NavRoutes.Body.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    )
                }

                if (uiState.showIrregularityCard) {
                    item(key = "irregularity") {
                        NiyuvaIrregularityCard(
                            userName = uiState.userName,
                            onLearnMore = {
                                navController.navigate(NavRoutes.BodyArticle.createRoute("pcos"))
                            },
                            onDismiss = { viewModel.dismissIrregularityCard() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp)
                        )
                    }
                }

                // ── Predictions Row ───────────────────────────────────────
                item(key = "predictions") {
                    val isMenstrual = uiState.currentPhase == CyclePhase.MENSTRUATION
                    val isExpanded = uiState.isSnapshotRowExpanded

                    if (isMenstrual && !isExpanded) {
                        val nextPeriodStr = uiState.prediction?.nextPeriodDate?.let { formatShortDate(it) } ?: "—"
                        val ovulationStr = uiState.prediction?.ovulationDate?.let { formatShortDate(it) } ?: "—"
                        val stripText = "Agla period: $nextPeriodStr  ·  Ovulation: $ovulationStr  ·  Aur details dekho ›"

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(horizontal = 16.dp)
                                .padding(top = 8.dp)
                                .clickable {
                                    viewModel.toggleSnapshotRowExpanded()
                                },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = PureWhite),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stripText,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = DustyMauve,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            if (isMenstrual && isExpanded) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 4.dp)
                                        .clickable { viewModel.toggleSnapshotRowExpanded() },
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Collapse details ‹",
                                        fontFamily = NunitoFamily,
                                        fontSize = 11.sp,
                                        color = DustyMauve,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            NiyuvaPredictionsRow(
                                prediction       = uiState.prediction,
                                confidenceLevel  = uiState.confidenceLevel,
                                irregularityFlag = uiState.irregularityFlag,
                                modifier         = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp)
                                    .padding(top = if (isMenstrual) 4.dp else 16.dp)
                            )
                        }
                    }
                }

                // ── Recommendations Section ───────────────────────────────
                item(key = "recommendations") {
                    NiyuvaRecommendationSection(
                        phase           = uiState.currentPhase,
                        onSeeAllClicked = {
                            Toast.makeText(context, "Coming soon 💛", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                    )
                }

                // ── Kya Khayen Section ───────────────────────────────────
                item(key = "kya_khayen") {
                    KyaKhayenSection(
                        phase = uiState.currentPhase,
                        navController = navController,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 20.dp)
                    )
                }

                val showAiCard = uiState.aiEnabled && uiState.currentPhase != CyclePhase.MENSTRUATION
                if (showAiCard) {
                    val isLuteal = uiState.currentPhase == CyclePhase.LUTEAL
                    item(key = "ai_analysis_entry") {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(top = if (isLuteal) 10.dp else 16.dp)
                                .clickable {
                                    navController.navigate(NavRoutes.AnalysisResults.route)
                                }
                                .then(if (isLuteal) Modifier.alpha(0.8f) else Modifier),
                            shape = RoundedCornerShape(if (isLuteal) 14.dp else 20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isLuteal) 1.dp else 2.dp)
                        ) {
                            Row(
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
                                    .border(1.dp, Color(0xFFEAD2C6), RoundedCornerShape(if (isLuteal) 14.dp else 20.dp))
                                    .padding(if (isLuteal) 12.dp else 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Apni Health Patterns Samjho ✨",
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = if (isLuteal) 14.sp else 16.sp,
                                        color = DeepWarmBrown
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Cycles aur symptoms ke correlations ka AI analysis dekh lo 🧠",
                                        fontFamily = NunitoFamily,
                                        fontSize = if (isLuteal) 11.sp else 13.sp,
                                        color = DustyMauve,
                                        lineHeight = if (isLuteal) 15.sp else 18.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "⚡",
                                    fontSize = if (isLuteal) 22.sp else 28.sp
                                )
                            }
                        }
                    }
                }

                // ── Bottom spacer (above nav bar) ────────────────────────────
                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // ── Log Symptoms Sheet (Phase 17) ───────────────────────────────────
        if (showLogSheet) {
            LogSymptomsSheet(
                state                   = logSheetState,
                onDismiss               = { viewModel.hideLogSheet() },
                onFlowSelected          = { viewModel.onFlowSelected(it) },
                onBloodColorSelected    = { viewModel.onBloodColorSelected(it) },
                onClotSelected          = { viewModel.onClotSelected(it) },
                onPainLevelSelected     = { viewModel.onPainLevelSelected(it) },
                onPainTypeToggled       = { viewModel.onPainTypeToggled(it) },
                onMoodToggled           = { viewModel.onMoodToggled(it) },
                onSleepQualitySelected  = { viewModel.onSleepQualitySelected(it) },
                onEnergyLevelSelected   = { viewModel.onEnergyLevelSelected(it) },
                onDischargeTypeSelected = { viewModel.onDischargeTypeSelected(it) },
                onSave                  = { viewModel.saveLog() },
                onOpenSaarthi           = {
                    viewModel.hideLogSheet()
                    navController.navigate(NavRoutes.Saarthi.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        // ── Save Success Snackbar Overlay ────────────────────────────────────
        if (logSheetState.savedSuccess) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxWidth()
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .background(WarmLinen)
                    .border(1.dp, BlushMist, androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text       = "✅ Aaj ka log save ho gaya 🌸",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 14.sp,
                    color      = DeepWarmBrown
                )
            }
        }

        // ── Missed Day Snackbar Overlay (Phase 5.1) ──────────────────────────
        if (uiState.showMissedDaySnackbar) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .fillMaxWidth()
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .background(Color(0xFFF5E6DC)) // Warm Linen
                    .border(1.dp, BlushMist, androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
                    .clickable { viewModel.dismissMissedDaySnackbar() }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text       = "Koi baat nahi, life mein break aate hain — chal phir se shuru karte hain 💛",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize   = 13.sp,
                    color      = Color(0xFF1E1418)
                )
            }
        }

        // ── Milestone Celebration Overlay (Phase 5.1) ────────────────────────
        AnimatedVisibility(
            visible = uiState.streakMilestoneCelebration != null,
            enter = fadeIn(animationSpec = tween(400, easing = FastOutSlowInEasing)) + scaleIn(initialScale = 0.95f, animationSpec = tween(400, easing = FastOutSlowInEasing)),
            exit = fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.95f, animationSpec = tween(300))
        ) {
            uiState.streakMilestoneCelebration?.let { milestone ->
                MilestoneCelebrationOverlay(
                    milestone = milestone,
                    onDismiss = { viewModel.dismissMilestoneCelebration() },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Kya Khayen — Phase-aware nutrition section
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun KyaKhayenSection(
    phase: CyclePhase,
    navController: NavController? = null,
    modifier: Modifier = Modifier
) {
    val data = remember(phase) { KyaKhayenData.forPhase(phase) }
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    val accentColor = remember(data.accentColorHex) {
        Color(android.graphics.Color.parseColor(data.accentColorHex))
    }
    val bgColor = remember(data.backgroundColorHex) {
        Color(android.graphics.Color.parseColor(data.backgroundColorHex))
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = spring(stiffness = 300f))
                .padding(20.dp)
        ) {
            // ── Header row ─────────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🍃", fontSize = 26.sp)
                }
                Spacer(modifier = Modifier.width(14.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Kya Khayen? 🌿",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = accentColor
                    )
                    Text(
                        text = data.phaseName,
                        fontFamily = NunitoFamily,
                        fontSize = 12.sp,
                        color = DeepWarmBrown.copy(alpha = 0.6f),
                        letterSpacing = 0.5.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(accentColor.copy(alpha = 0.12f))
                        .clickable { isExpanded = !isExpanded }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = if (isExpanded) "Band karo" else "Dekho",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = accentColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Headline ───────────────────────────────────────────────────
            Text(
                text = data.headline,
                fontFamily = PlayfairFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = DeepWarmBrown,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = data.subtext,
                fontFamily = NunitoFamily,
                fontSize = 13.sp,
                color = DeepWarmBrown.copy(alpha = 0.75f),
                lineHeight = 19.sp
            )

            // ── Expanded content ──────────────────────────────────────────
            if (isExpanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = accentColor.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "✨ Yeh kha — teri body ko chahiye",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 13.sp,
                    color = accentColor,
                    letterSpacing = 0.3.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Food items grid (2 columns)
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    data.foods.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowItems.forEach { food ->
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = PureWhite.copy(alpha = 0.80f)
                                    ),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, accentColor.copy(alpha = 0.15f), RoundedCornerShape(14.dp))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(10.dp)
                                    ) {
                                        Text(text = food.emoji, fontSize = 22.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = food.name,
                                            fontFamily = NunitoFamily,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            color = DeepWarmBrown
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = food.reason,
                                            fontFamily = NunitoFamily,
                                            fontSize = 11.sp,
                                            color = DustyMauve,
                                            lineHeight = 15.sp
                                        )
                                    }
                                }
                            }
                            // Fill last row if odd number of items
                            if (rowItems.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Avoid note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFF3F3))
                        .border(1.dp, Color(0xFFE57373).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = data.avoidNote,
                        fontFamily = NunitoFamily,
                        fontSize = 12.sp,
                        color = Color(0xFFC0445A),
                        lineHeight = 18.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Didi bonus tip
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(accentColor.copy(alpha = 0.10f))
                        .padding(14.dp)
                ) {
                    Text(
                        text = data.bonusTip,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = accentColor,
                        lineHeight = 20.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // "Pura Guide" CTA button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(accentColor)
                        .clickable { navController?.navigate(com.niyuva.app.presentation.navigation.NavRoutes.KyaKhayen.route) }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📖 Pura Nutrition Guide Padhein ➡",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = PureWhite,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Top App Bar
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HomeTopBar(
    userName: String,
    onAvatarClick: () -> Unit,
    onBellClick:   () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier            = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment   = Alignment.CenterVertically
    ) {
        // ── Avatar ───────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(WarmLinen)
                .border(1.5.dp, PureWhite.copy(alpha = 0.5f), CircleShape)
                .clickable(
                    onClick = onAvatarClick
                ),
            contentAlignment = Alignment.Center
        ) {
            val initial = userName.firstOrNull()?.uppercase() ?: "N"
            Text(
                text       = initial,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Bold,
                fontSize   = 14.sp,
                color      = DeepWarmBrown
            )
        }

        // ── Wordmark ─────────────────────────────────────────────────────────
        Text(
            text       = "NIYUVA",
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp,
            color      = PureWhite,
            letterSpacing = 2.sp
        )

        // ── Bell icon ─────────────────────────────────────────────────────────
        IconButton(
            onClick  = onBellClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector         = Icons.Outlined.Notifications,
                contentDescription  = "Notifications",
                tint                = PureWhite,
                modifier            = Modifier.size(24.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Greeting Section
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HomeGreeting(
    greeting: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Animated greeting — fade-through on phase change
        AnimatedContent(
            targetState    = greeting,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(150))
            },
            label = "greetingFade"
        ) { targetGreeting ->
            Text(
                text       = targetGreeting.ifBlank { "Swagat hai! 💛" },
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize   = 26.sp,
                color      = PureWhite,
                lineHeight  = 32.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Day-strip loading skeleton
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HomeDayStripShimmer() {
    Row(
        modifier              = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(7) {
            NiyuvaShimmer(
                modifier     = Modifier.width(44.dp).height(72.dp),
                cornerRadius = 12.dp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Error Banner
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HomeErrorBanner(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp))
            .background(Color(0xFFE8965A).copy(alpha = 0.20f))
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text       = "⚠ $message",
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.Normal,
            fontSize   = 13.sp,
            color      = PureWhite
        )
    }
}

@Composable
private fun EmptyStateCard(
    onAddDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = WarmLinen),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌸",
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = androidx.compose.ui.res.stringResource(id = com.niyuva.app.R.string.empty_state_title),
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DeepWarmBrown,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = androidx.compose.ui.res.stringResource(id = com.niyuva.app.R.string.empty_state_body),
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = DustyMauve,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(20.dp))
            NiyuvaPrimaryButton(
                text = androidx.compose.ui.res.stringResource(id = com.niyuva.app.R.string.btn_add_period_date),
                onClick = onAddDateClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// ── Streak Badge (Phase 5.1) ──────────────────────────────────────────────────
@Composable
fun StreakBadge(
    currentStreak: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.90f))
            .padding(vertical = 8.dp, horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "🌸 $currentStreak din ka streak",
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            color = Color(0xFF1E1418)
        )
    }
}

// ── Milestone Celebration Overlay (Phase 5.1) ─────────────────────────────────
@Composable
fun MilestoneCelebrationOverlay(
    milestone: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f))
            .clickable(
                onClick = onDismiss,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = WarmIvory),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .padding(16.dp)
                .clickable(
                    onClick = {},
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Stylized Mascot portrait
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(CircleShape)
                        .background(WarmLinen)
                        .border(4.dp, Color(0xFF8B5E6D), CircleShape), // Plum border
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "🙋🏽‍♀️", fontSize = 72.sp) // South Asian girl
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(width = 40.dp, height = 24.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF8B5E6D))) // Plum kurta
                            Box(modifier = Modifier.size(width = 24.dp, height = 24.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFFD4956A))) // Terracotta dupatta
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "$milestone din! Tu apne aap ko itna acha samajh rahi hai 🌸",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1E1418),
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Continue button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(100.dp))
                        .background(Color(0xFF8B5E6D)) // Plum
                        .clickable(onClick = onDismiss)
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continue",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
