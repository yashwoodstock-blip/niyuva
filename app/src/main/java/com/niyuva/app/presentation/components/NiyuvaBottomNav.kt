package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.navigation.NavRoutes
import com.niyuva.app.presentation.theme.BlushMist
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale

data class BottomNavTab(
    val route: String,
    val label: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)

@Composable
fun NiyuvaBottomNav(
    currentRoute: String?,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = remember {
        listOf(
            BottomNavTab(
                route = NavRoutes.Home.route,
                label = "Home",
                activeIcon = Icons.Filled.Home,
                inactiveIcon = Icons.Outlined.Home
            ),
            BottomNavTab(
                route = NavRoutes.Body.route,
                label = "Body",
                activeIcon = Icons.Filled.Spa,
                inactiveIcon = Icons.Outlined.Spa
            ),
            BottomNavTab(
                route = NavRoutes.Saarthi.route,
                label = "Saarthi",
                activeIcon = Icons.Filled.AutoAwesome,
                inactiveIcon = Icons.Outlined.AutoAwesome
            ),
            BottomNavTab(
                route = NavRoutes.Discover.route,
                label = "Discover",
                activeIcon = Icons.Filled.Explore,
                inactiveIcon = Icons.Outlined.Explore
            ),
            BottomNavTab(
                route = NavRoutes.Me.route,
                label = "Me",
                activeIcon = Icons.Filled.Person,
                inactiveIcon = Icons.Outlined.PersonOutline
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(PureWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = BlushMist
            )
            NavigationBar(
                containerColor = PureWhite,
                tonalElevation = 0.dp,
                windowInsets = WindowInsets(0, 0, 0, 0),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                tabs.forEach { tab ->
                    val selected = when (tab.route) {
                        NavRoutes.Home.route -> currentRoute == NavRoutes.Home.route || currentRoute == NavRoutes.Analytics.route || currentRoute == NavRoutes.CycleReport.route || currentRoute == NavRoutes.AnalysisResults.route || currentRoute?.startsWith("kya_khayen") == true
                        NavRoutes.Body.route -> currentRoute == NavRoutes.Body.route || currentRoute?.startsWith("body_article") == true
                        NavRoutes.Discover.route -> currentRoute == NavRoutes.Discover.route || currentRoute == NavRoutes.PehliBaarStories.route || currentRoute?.startsWith("theme_stories") == true || currentRoute?.startsWith("vedic_section") == true
                        else -> currentRoute == tab.route
                    }

                    val animatedBgColor by animateColorAsState(
                        targetValue = if (selected) Color(0xFF1E1418) else Color.Transparent,
                        animationSpec = tween(durationMillis = 200),
                        label = "tabBgColor"
                    )

                    val animatedScale by animateFloatAsState(
                        targetValue = if (selected) 1.05f else 1.0f,
                        animationSpec = tween(durationMillis = 200),
                        label = "tabScale"
                    )

                    NavigationBarItem(
                        selected = selected,
                        onClick = { onTabSelected(tab.route) },
                        icon = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .scale(animatedScale)
                                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(16.dp))
                                    .background(color = animatedBgColor)
                                    .padding(horizontal = 16.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = if (selected) tab.activeIcon else tab.inactiveIcon,
                                    contentDescription = tab.label,
                                    tint = if (selected) Color.White else Color(0xFFA07A85),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = tab.label,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 11.sp,
                                    color = if (selected) Color.White else Color(0xFFA07A85),
                                    fontFamily = NunitoFamily,
                                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                                )
                            }
                        },
                        label = null,
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor = Color.Transparent,
                            unselectedIconColor = Color.Transparent
                        )
                    )
                }
            }
        }
    }
}
