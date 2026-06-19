package com.niyuva.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.niyuva.app.domain.repository.UserProfileRepository
import com.niyuva.app.presentation.navigation.NavRoutes
import com.niyuva.app.presentation.navigation.NiyuvaNavHost
import com.niyuva.app.presentation.theme.NiyuvaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userProfileRepository: UserProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        var startRoute by mutableStateOf<String?>(null)
        var isReady by mutableStateOf(false)
        splashScreen.setKeepOnScreenCondition { !isReady }

        super.onCreate(savedInstanceState)

        // Edge-to-edge: let the app draw behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        lifecycleScope.launch {
            val profile = userProfileRepository.getProfile()
            startRoute = when {
                profile == null || !profile.onboardingComplete -> NavRoutes.Onboarding.route
                profile.pinHash != null && profile.pinHash.isNotEmpty() -> NavRoutes.PinLock.route
                else -> NavRoutes.Main.route
            }
        }

        setContent {
            NiyuvaTheme {
                val route = startRoute
                if (route != null) {
                    NiyuvaNavHost(startDestination = route)
                    LaunchedEffect(route) {
                        isReady = true
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF8B5E6D))
                    )
                }
            }
        }
    }
}

