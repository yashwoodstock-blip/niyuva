package com.niyuva.app.presentation.screens.discover

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.niyuva.app.presentation.navigation.NavRoutes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.niyuva.app.presentation.theme.*
import com.niyuva.app.presentation.components.*
@Composable
fun DiscoverScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: DiscoverViewModel = hiltViewModel()
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
    val context = LocalContext.current
    val stories by viewModel.stories.collectAsStateWithLifecycle()
    val vedicCards by viewModel.vedicCards.collectAsStateWithLifecycle()
    val animations by viewModel.animations.collectAsStateWithLifecycle()
 
    val onSeeAllClick = {
        Toast.makeText(context, "Aur content aa raha hai jald! 💛", Toast.LENGTH_SHORT).show()
    }
 
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                bottom = androidx.compose.foundation.layout.WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 64.dp + 16.dp
            )
        ) {
            // Status bar spacer
            item(key = "status_bar_spacer") {
                Spacer(modifier = Modifier.statusBarsPadding())
            }
 
            // Header - "Discover"
            item(key = "header") {
                Text(
                    text = "Discover",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = DeepWarmBrown,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                )
            }
 
            // Mascot placeholder Box
            item(key = "mascot") {
                // TODO: Replace with Discover Mascot — sitting with open book, lush botanical surround
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(WarmLinen),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "📖",
                            fontSize = 64.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "READ & LEARN",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp,
                            color = DeepWarmBrown,
                            letterSpacing = 2.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
 
            if (stories.isEmpty()) {
                // Section 1 — Stories (loading shimmer)
                item(key = "stories_section_loading") {
                    DiscoverSection(
                        title = "Stories",
                        seeAllLabel = "",
                        items = listOf("shimmer1", "shimmer2"),
                        itemContent = {
                            com.niyuva.app.presentation.components.NiyuvaShimmer(
                                modifier = Modifier.size(width = 160.dp, height = 200.dp),
                                cornerRadius = 16.dp
                            )
                        }
                    )
                }
 
 
                // Section 3 — Animations (loading shimmer)
                item(key = "animations_section_loading") {
                    DiscoverSection(
                        title = "Animations",
                        seeAllLabel = "",
                        items = listOf("shimmer1", "shimmer2"),
                        itemContent = {
                            com.niyuva.app.presentation.components.NiyuvaShimmer(
                                modifier = Modifier.fillMaxWidth(0.85f).aspectRatio(16f / 9f),
                                cornerRadius = 16.dp
                            )
                        }
                    )
                }
            } else {
                // Section 1 — Stories
                item(key = "stories_section") {
                    DiscoverSection(
                        title = "Stories",
                        items = stories,
                        key = { it.id },
                        onSeeAll = onSeeAllClick,
                        itemContent = { story ->
                            StoryCardView(
                                story = story,
                                onClick = {
                                    if (story.id == "story_1") {
                                        navController.navigate(NavRoutes.PehliBaarStories.route)
                                    } else {
                                        Toast.makeText(context, "Honest stories read coming soon 🌸", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    )
                }

                // Section: Explore Story Themes
                item(key = "themes_section_header") {
                    Text(
                        text = "Explore Story Themes 📖",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = DeepWarmBrown,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
                    )
                }

                items(
                    items = com.niyuva.app.data.local.content.ThemeStoriesData.list,
                    key = { it.id }
                ) { theme ->
                    val themeBgColor = remember(theme.backgroundColorHex) {
                        Color(android.graphics.Color.parseColor(theme.backgroundColorHex))
                    }
                    val themeTextColor = remember(theme.textColorHex) {
                        Color(android.graphics.Color.parseColor(theme.textColorHex))
                    }
                    
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = themeBgColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .border(1.dp, BlushMist.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clickable {
                                navController.navigate(NavRoutes.ThemeStories.createRoute(theme.id))
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(themeTextColor.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = theme.icon,
                                    fontSize = 24.sp
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = theme.title,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = themeTextColor
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = theme.description,
                                    fontFamily = NunitoFamily,
                                    fontSize = 12.sp,
                                    color = DeepWarmBrown.copy(alpha = 0.8f),
                                    lineHeight = 16.sp
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = "➡",
                                fontSize = 18.sp,
                                color = themeTextColor
                            )
                        }
                    }
                }

                // Section: Vedic Wisdom & Period
                item(key = "vedic_wisdom_header") {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp)
                    ) {
                        Text(
                            text = "Vedic Wisdom & Period 🌸",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = DeepWarmBrown
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "“Sadiyon Pehle Bhi Teri Period Sacred Thi — Sirf Tu Bhool Gayi Thi”",
                            fontFamily = PlayfairFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DeepWarmBrown.copy(alpha = 0.8f),
                            lineHeight = 20.sp
                        )
                    }
                }

                items(
                    items = com.niyuva.app.data.local.content.VedicWisdomData.list,
                    key = { it.id }
                ) { section ->
                    val bg = remember(section.backgroundColorHex) {
                        Color(android.graphics.Color.parseColor(section.backgroundColorHex))
                    }
                    val tc = remember(section.textColorHex) {
                        Color(android.graphics.Color.parseColor(section.textColorHex))
                    }
                    val sectionNumber = section.id.replace("sec_", "")
                    val formattedSection = if (sectionNumber.toIntOrNull() != null) {
                        "Section %02d".format(sectionNumber.toInt())
                    } else {
                        "Section $sectionNumber"
                    }

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = bg),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .border(1.dp, BlushMist.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                            .clickable {
                                navController.navigate(NavRoutes.VedicSection.createRoute(section.id))
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(tc.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = section.icon,
                                    fontSize = 24.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = formattedSection,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = tc.copy(alpha = 0.7f),
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = section.title,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = tc,
                                    lineHeight = 22.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = section.description,
                                    fontFamily = NunitoFamily,
                                    fontSize = 12.sp,
                                    color = DeepWarmBrown.copy(alpha = 0.85f),
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "➡",
                                fontSize = 18.sp,
                                color = tc
                            )
                        }
                    }
                }

                // Section 3 — Animations
                item(key = "animations_section") {
                    DiscoverSection(
                        title = "Animations",
                        items = animations,
                        key = { it.id },
                        onSeeAll = onSeeAllClick,
                        itemContent = { anim ->
                            AnimationCardView(animation = anim)
                        }
                    )
                }
            }
        }
    }
}
