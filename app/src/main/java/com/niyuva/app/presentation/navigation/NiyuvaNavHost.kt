package com.niyuva.app.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.niyuva.app.presentation.screens.MainScreen
import com.niyuva.app.presentation.screens.SplashScreen
import com.niyuva.app.presentation.screens.onboarding.OnboardingNavHost
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.WarmIvory

@Composable
fun NiyuvaNavHost(
    modifier: Modifier = Modifier,
    startDestination: String
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        // PIN Lock Screen
        composable(
            route = NavRoutes.PinLock.route,
            enterTransition = {
                androidx.compose.animation.fadeIn(animationSpec = tween(300))
            }
        ) {
            com.niyuva.app.presentation.screens.PinLockScreen(navController = navController)
        }

        // Onboarding Nested NavHost
        composable(
            route = NavRoutes.Onboarding.route,
            enterTransition = {
                androidx.compose.animation.fadeIn(animationSpec = tween(300))
            }
        ) {
            OnboardingNavHost(parentNavController = navController)
        }

        // Main Scaffold & Tabs Screen
        composable(
            route = NavRoutes.Main.route,
            enterTransition = {
                androidx.compose.animation.fadeIn(animationSpec = tween(300))
            }
        ) {
            MainScreen(parentNavController = navController)
        }

        // Didi Voice Input Screen
        composable(NavRoutes.DidiVoice.route) {
            com.niyuva.app.presentation.screens.didi.DidiVoiceScreen(navController = navController)
        }

        // Body Article Reader Screen
        composable(
            route = NavRoutes.BodyArticle.route,
            arguments = listOf(
                androidx.navigation.navArgument("topicId") {
                    type = androidx.navigation.NavType.StringType
                }
            )
        ) {
            com.niyuva.app.presentation.screens.body.ArticleScreen(navController = navController)
        }
    }
}

@Composable
fun PlaceholderOnboardingScreen(name: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Onboarding: $name",
            fontFamily = NunitoFamily,
            color = DeepWarmBrown
        )
    }
}
