package com.niyuva.app.presentation.navigation

sealed class NavRoutes(val route: String) {
    // Top-level
    object Splash : NavRoutes("splash")
    object PinLock : NavRoutes("pin_lock")
    object Onboarding : NavRoutes("onboarding")
    object Main : NavRoutes("main")
    
    // Main tabs
    object Home : NavRoutes("home")
    object Body : NavRoutes("body")
    object Saarthi : NavRoutes("saarthi")
    object Discover : NavRoutes("discover")
    object Me : NavRoutes("me")
    
    // Nested screens within tabs
    object CycleReport : NavRoutes("cycle_report")
    object Analytics : NavRoutes("analytics")
    object AnalysisResults : NavRoutes("analysis_results")
    object KyaKhayen : NavRoutes("kya_khayen?phase={phase}") {
        fun createRoute(phase: String) = "kya_khayen?phase=$phase"
    }
    object BodyArticle : NavRoutes("body_article/{topicId}") {
        fun createRoute(topicId: String) = "body_article/$topicId"
    }
    object SaarthiVoice : NavRoutes("saarthi_voice")
    object PehliBaarStories : NavRoutes("pehli_baar_stories")
    object ThemeStories : NavRoutes("theme_stories/{themeId}") {
        fun createRoute(themeId: String) = "theme_stories/$themeId"
    }
    object VedicSection : NavRoutes("vedic_section/{sectionId}") {
        fun createRoute(sectionId: String) = "vedic_section/$sectionId"
    }
    
    // Onboarding sub-screens
    object OnboardingWelcome : NavRoutes("onboarding_welcome")
    object OnboardingPrivacy : NavRoutes("onboarding_privacy")
    object OnboardingName : NavRoutes("onboarding_name")
    object OnboardingAge : NavRoutes("onboarding_age")
    object OnboardingLastPeriod : NavRoutes("onboarding_last_period")
    object OnboardingCycleBasics : NavRoutes("onboarding_cycle_basics")
    object OnboardingPin : NavRoutes("onboarding_pin")
    object OnboardingSecurityQuestion : NavRoutes("onboarding_security_question")
    object OnboardingNotifications : NavRoutes("onboarding_notifications")
    object OnboardingCelebration : NavRoutes("onboarding_celebration")
}
