package com.niyuva.app.presentation.screens.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.niyuva.app.data.local.content.ThemeStoriesData
import com.niyuva.app.presentation.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ThemeStoriesScreen(
    themeId: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val theme = remember(themeId) {
        ThemeStoriesData.list.firstOrNull { it.id == themeId }
    }

    if (theme == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Theme nahi mila 😢",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DeepWarmBrown
            )
        }
        return
    }

    val stories = theme.stories
    val currentThemeIndex = remember(themeId) {
        ThemeStoriesData.list.indexOfFirst { it.id == themeId }
    }
    var currentIndex by remember { mutableStateOf(0) }

    if (stories.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .statusBarsPadding(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Is theme mein koi kahani nahi hai 📝",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DeepWarmBrown
            )
        }
        return
    }

    val currentStory = stories[currentIndex]
    val parsedTextColor = remember(currentStory.textColorHex) {
        Color(android.graphics.Color.parseColor(currentStory.textColorHex))
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepWarmBrown,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${theme.icon} ${theme.title}",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        color = DeepWarmBrown,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.width(40.dp)) // symmetry spacer
            }

            // Slide content transition
            AnimatedContent(
                targetState = currentIndex,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width } + fadeOut()
                        )
                    } else {
                        (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> width } + fadeOut()
                        )
                    }
                },
                label = "StorySlideAnim",
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) { targetIndex ->
                val story = stories[targetIndex]
                val bg = Color(android.graphics.Color.parseColor(story.backgroundColorHex))
                val tc = Color(android.graphics.Color.parseColor(story.textColorHex))

                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = bg),
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, BlushMist, RoundedCornerShape(24.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        // Card Header Metadata
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(tc.copy(alpha = 0.12f))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = story.subtitle.split("|").first().trim(),
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = tc,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Text(
                                text = "Story ${targetIndex + 1} of ${stories.size} 📝",
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = tc.copy(alpha = 0.8f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Scrollable Story Content
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(scrollState)
                                .padding(end = 4.dp)
                        ) {
                            // Large Title
                            Text(
                                text = story.title,
                                fontFamily = PlayfairFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = tc,
                                lineHeight = 28.sp,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            // Paragraphs
                            story.paragraphs.forEach { p ->
                                Text(
                                    text = p,
                                    fontFamily = NunitoFamily,
                                    fontSize = 15.sp,
                                    lineHeight = 24.sp,
                                    color = DeepWarmBrown,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                            }

                            // Quote Callout block
                            if (!story.quote.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(PureWhite.copy(alpha = 0.5f))
                                        .padding(16.dp)
                                ) {
                                    Row {
                                        Icon(
                                            imageVector = Icons.Default.FormatQuote,
                                            contentDescription = "Quote",
                                            tint = tc,
                                            modifier = Modifier.size(32.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = story.quote,
                                            fontFamily = NunitoFamily,
                                            fontWeight = FontWeight.Normal,
                                            fontStyle = FontStyle.Italic,
                                            fontSize = 14.sp,
                                            lineHeight = 22.sp,
                                            color = tc
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Key message highlight card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(tc)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = story.keyMessage,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    color = PureWhite,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Source attribution
                            Text(
                                text = story.source,
                                fontFamily = NunitoFamily,
                                fontSize = 11.sp,
                                color = tc.copy(alpha = 0.7f),
                                lineHeight = 16.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }
                }
            }

            // Bottom Navigation and Progress Indicator
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Progress Bar
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / stories.size.toFloat() },
                    color = parsedTextColor,
                    trackColor = parsedTextColor.copy(alpha = 0.15f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Navigation Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Previous Button
                    if (currentIndex > 0) {
                        Button(
                            onClick = { currentIndex-- },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = parsedTextColor.copy(alpha = 0.12f),
                                contentColor = parsedTextColor
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "⬅ Pichli Kahani",
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    // Next / Complete Button
                    Button(
                        onClick = {
                            if (currentIndex < stories.size - 1) {
                                currentIndex++
                            } else {
                                if (currentThemeIndex >= 0 && currentThemeIndex < ThemeStoriesData.list.size - 1) {
                                    val nextTheme = ThemeStoriesData.list[currentThemeIndex + 1]
                                    navController.navigate(com.niyuva.app.presentation.navigation.NavRoutes.ThemeStories.createRoute(nextTheme.id)) {
                                        popUpTo(com.niyuva.app.presentation.navigation.NavRoutes.Discover.route)
                                    }
                                } else {
                                    navController.popBackStack()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = parsedTextColor,
                            contentColor = PureWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = if (currentIndex < stories.size - 1) {
                                "Agli Kahani ➡"
                            } else if (currentThemeIndex >= 0 && currentThemeIndex < ThemeStoriesData.list.size - 1) {
                                "Agli Theme ➡"
                            } else {
                                "Main Sikh Gayi! 🌸"
                            },
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
