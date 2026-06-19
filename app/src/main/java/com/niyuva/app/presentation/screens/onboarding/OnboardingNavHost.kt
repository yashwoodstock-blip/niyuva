package com.niyuva.app.presentation.screens.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.niyuva.app.presentation.navigation.NavRoutes
import com.niyuva.app.presentation.screens.onboarding.*

@Composable
fun OnboardingNavHost(
    parentNavController: NavController,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel()
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
    val navController = rememberNavController()
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Sync ViewModel step with navigation
    LaunchedEffect(state.currentStep) {
        val targetRoute = when (state.currentStep) {
            OnboardingStep.WELCOME -> NavRoutes.OnboardingWelcome.route
            OnboardingStep.PRIVACY -> NavRoutes.OnboardingPrivacy.route
            OnboardingStep.NAME -> NavRoutes.OnboardingName.route
            OnboardingStep.AGE_SELECT -> NavRoutes.OnboardingAge.route
            OnboardingStep.LAST_PERIOD -> NavRoutes.OnboardingLastPeriod.route
            OnboardingStep.CYCLE_BASICS -> NavRoutes.OnboardingCycleBasics.route
            OnboardingStep.PIN_SETUP -> NavRoutes.OnboardingPin.route
            OnboardingStep.SECURITY_QUESTION -> NavRoutes.OnboardingSecurityQuestion.route
            OnboardingStep.NOTIFICATIONS -> NavRoutes.OnboardingNotifications.route
            OnboardingStep.CELEBRATION -> NavRoutes.OnboardingCelebration.route
        }

        val currentRoute = navController.currentBackStackEntry?.destination?.route
        if (currentRoute != targetRoute) {
            navController.navigate(targetRoute) {
                launchSingleTop = true
            }
        }
    }

    // Observe onboarding completion to navigate to Main Screen
    LaunchedEffect(state.isOnboardingComplete) {
        if (state.isOnboardingComplete) {
            parentNavController.navigate(NavRoutes.Main.route) {
                popUpTo(NavRoutes.Onboarding.route) {
                    inclusive = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavRoutes.OnboardingWelcome.route,
        modifier = modifier
    ) {
        composable(NavRoutes.OnboardingWelcome.route) {
            OnboardingWelcomeScreen(
                onContinue = { viewModel.onWelcomeComplete() }
            )
        }

        composable(NavRoutes.OnboardingPrivacy.route) {
            BackHandler { viewModel.onBackClicked() }
            OnboardingPrivacyScreen(
                onBack = { viewModel.onBackClicked() },
                onContinue = { viewModel.onPrivacyComplete() }
            )
        }

        composable(NavRoutes.OnboardingName.route) {
            BackHandler { viewModel.onBackClicked() }
            OnboardingNameScreen(
                onBack = { viewModel.onBackClicked() },
                onNameEntered = { name -> viewModel.onNameEntered(name) }
            )
        }

        composable(NavRoutes.OnboardingAge.route) {
            BackHandler { viewModel.onBackClicked() }
            OnboardingAgeScreen(
                onBack = { viewModel.onBackClicked() },
                onAgeSelected = { year -> viewModel.onAgeSelected(year) }
            )
        }

        composable(NavRoutes.OnboardingLastPeriod.route) {
            BackHandler { viewModel.onBackClicked() }
            OnboardingLastPeriodScreen(
                userName = state.userName,
                onBack = { viewModel.onBackClicked() },
                onContinue = { date -> viewModel.onLastPeriodDateSelected(date) }
            )
        }

        composable(NavRoutes.OnboardingCycleBasics.route) {
            BackHandler { viewModel.onBackClicked() }
            OnboardingCycleBasicsScreen(
                onBack = { viewModel.onBackClicked() },
                onContinue = { cycleLen, periodLen ->
                    viewModel.onCycleBasicsEntered(cycleLen, periodLen)
                }
            )
        }

        composable(NavRoutes.OnboardingPin.route) {
            BackHandler { viewModel.onBackClicked() }
            OnboardingPinScreen(
                userName = state.userName,
                onPinSet = { pin -> viewModel.onPinSet(pin) },
                onSkipPin = { viewModel.onPinSkipped() }
            )
        }

        composable(NavRoutes.OnboardingSecurityQuestion.route) {
            BackHandler { viewModel.onBackClicked() }
            OnboardingSecurityQuestionScreen(
                onBack = { viewModel.onBackClicked() },
                onContinue = { question, answer ->
                    viewModel.onSecurityQuestionSet(question, answer)
                }
            )
        }

        composable(NavRoutes.OnboardingNotifications.route) {
            BackHandler { viewModel.onBackClicked() }
            OnboardingNotificationsScreen(
                onComplete = { period, tip, ovulation ->
                    viewModel.onNotificationsConfigured(period, tip, ovulation)
                }
            )
        }

        composable(NavRoutes.OnboardingCelebration.route) {
            BackHandler { viewModel.onBackClicked() }
            OnboardingCelebrationScreen(
                userName = state.userName,
                onCelebrationEntered = { viewModel.onOnboardingComplete() },
                onNavigateToMain = {
                    parentNavController.navigate(NavRoutes.Main.route) {
                        popUpTo(NavRoutes.Onboarding.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
