package com.niyuva.app.presentation.screens

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.niyuva.app.presentation.components.NiyuvaBottomNav
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.navigation.NavRoutes
import com.niyuva.app.presentation.screens.body.BodyScreen
import com.niyuva.app.presentation.screens.discover.DiscoverScreen
import com.niyuva.app.presentation.screens.home.CycleReportScreen
import com.niyuva.app.presentation.screens.home.HomeScreen
import com.niyuva.app.presentation.screens.me.MeScreen
import com.niyuva.app.presentation.screens.didi.DidiScreen

@Composable
fun MainScreen(
    parentNavController: NavController,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            bottomBar = {
                NiyuvaBottomNav(
                    currentRoute = currentRoute,
                    onTabSelected = { route ->
                        // Determine if we are already inside this tab (root or sub-screen)
                        val isInsideTargetTab = when (route) {
                            NavRoutes.Home.route ->
                                currentRoute == NavRoutes.Home.route ||
                                currentRoute == NavRoutes.Analytics.route ||
                                currentRoute == NavRoutes.CycleReport.route ||
                                currentRoute == NavRoutes.AnalysisResults.route ||
                                currentRoute?.startsWith("kya_khayen") == true
                            NavRoutes.Body.route ->
                                currentRoute == NavRoutes.Body.route ||
                                currentRoute?.startsWith("body_article") == true
                            NavRoutes.Discover.route ->
                                currentRoute == NavRoutes.Discover.route ||
                                currentRoute == NavRoutes.PehliBaarStories.route ||
                                currentRoute?.startsWith("theme_stories") == true ||
                                currentRoute?.startsWith("vedic_section") == true
                            else -> currentRoute == route
                        }

                        if (isInsideTargetTab) {
                            // Pop sub-screens back to tab root; ignore if already on root
                            if (currentRoute != route) {
                                navController.popBackStack(route, inclusive = false)
                            }
                        } else {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = NavRoutes.Home.route,
                modifier = Modifier.fillMaxSize(),
                enterTransition = {
                    fadeIn(tween(220)) + slideInHorizontally(
                        initialOffsetX = { it / 10 },
                        animationSpec = tween(220)
                    )
                },
                exitTransition = {
                    fadeOut(tween(180))
                },
                popEnterTransition = {
                    fadeIn(tween(220)) + slideInHorizontally(
                        initialOffsetX = { -it / 10 },
                        animationSpec = tween(220)
                    )
                },
                popExitTransition = {
                    fadeOut(tween(180)) + slideOutHorizontally(
                        targetOffsetX = { it / 10 },
                        animationSpec = tween(180)
                    )
                }
            ) {
                composable(NavRoutes.Home.route) {
                    HomeScreen(navController = navController, contentPadding = innerPadding)
                }
                composable(NavRoutes.CycleReport.route) {
                    CycleReportScreen(navController = navController)
                }
                composable(NavRoutes.Analytics.route) {
                    com.niyuva.app.presentation.screens.home.AnalyticsScreen(navController = navController)
                }
                composable(NavRoutes.AnalysisResults.route) {
                    com.niyuva.app.presentation.screens.home.AnalysisResultsScreen(navController = navController)
                }
                composable(
                    route = NavRoutes.KyaKhayen.route,
                    arguments = listOf(navArgument("phase") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = "menstruation"
                    })
                ) { backStackEntry ->
                    val phaseStr = backStackEntry.arguments?.getString("phase") ?: "menstruation"
                    val phase = CyclePhase.entries.find { it.name.lowercase() == phaseStr.lowercase() } ?: CyclePhase.MENSTRUATION
                    com.niyuva.app.presentation.screens.home.KyaKhayenScreen(
                        navController = navController,
                        initialPhase = phase,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }
                composable(NavRoutes.Body.route) {
                    BodyScreen(navController = navController, contentPadding = innerPadding)
                }
                composable(
                    route = NavRoutes.BodyArticle.route,
                    arguments = listOf(
                        androidx.navigation.navArgument("topicId") {
                            type = androidx.navigation.NavType.StringType
                        }
                    )
                ) {
                    com.niyuva.app.presentation.screens.body.ArticleScreen(
                        navController = navController,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }
                composable(NavRoutes.Didi.route) {
                    DidiScreen(
                        navController = parentNavController,
                        contentPadding = innerPadding
                    )
                }
                composable(NavRoutes.Discover.route) {
                    DiscoverScreen(navController = navController, contentPadding = innerPadding)
                }
                composable(NavRoutes.PehliBaarStories.route) {
                    com.niyuva.app.presentation.screens.discover.PehliBaarStoriesScreen(
                        navController = navController,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }
                composable(
                    route = NavRoutes.ThemeStories.route,
                    arguments = listOf(
                        androidx.navigation.navArgument("themeId") {
                            type = androidx.navigation.NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    val themeId = backStackEntry.arguments?.getString("themeId") ?: ""
                    com.niyuva.app.presentation.screens.discover.ThemeStoriesScreen(
                        themeId = themeId,
                        navController = navController,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }
                composable(
                    route = NavRoutes.VedicSection.route,
                    arguments = listOf(
                        androidx.navigation.navArgument("sectionId") {
                            type = androidx.navigation.NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    val sectionId = backStackEntry.arguments?.getString("sectionId") ?: ""
                    com.niyuva.app.presentation.screens.discover.VedicWisdomSectionScreen(
                        sectionId = sectionId,
                        navController = navController,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    )
                }
                composable(NavRoutes.Me.route) {
                    MeScreen(parentNavController = parentNavController, contentPadding = innerPadding)
                }
            }
        }
    }
}
