package com.niyuva.app.presentation.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.niyuva.app.presentation.navigation.NavRoutes
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.PlayfairFamily
import com.niyuva.app.presentation.theme.PureWhite
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                // Set status bar and navigation bar transparent, edge-to-edge
                window.statusBarColor = Color.Transparent.toArgb()
                window.navigationBarColor = Color.Transparent.toArgb()
                val insetsController = WindowCompat.getInsetsController(window, view)
                // Set system bar icons to white (for Plum background)
                insetsController.isAppearanceLightStatusBars = false
                insetsController.isAppearanceLightNavigationBars = false
            }
        }
    }

    val profileState by viewModel.profileState.collectAsStateWithLifecycle()

    LaunchedEffect(profileState) {
        val state = profileState
        if (state is UserProfileState.Success) {
            delay(100L)
            val profile = state.profile
            if (profile == null || !profile.onboardingComplete) {
                navController.navigate(NavRoutes.Onboarding.route) {
                    popUpTo(NavRoutes.Splash.route) { inclusive = true }
                }
            } else if (profile.pinHash != null) {
                navController.navigate(NavRoutes.PinLock.route) {
                    popUpTo(NavRoutes.Splash.route) { inclusive = true }
                }
            } else {
                navController.navigate(NavRoutes.Main.route) {
                    popUpTo(NavRoutes.Splash.route) { inclusive = true }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepPlumRose),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = com.niyuva.app.R.drawable.ic_splash_logo),
                contentDescription = "NIYUVA Splash Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "NIYUVA",
                fontFamily = PlayfairFamily,
                fontSize = 40.sp,
                color = PureWhite
            )
        }
    }
}
