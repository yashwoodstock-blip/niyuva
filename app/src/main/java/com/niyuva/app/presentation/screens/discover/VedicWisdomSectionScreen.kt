package com.niyuva.app.presentation.screens.discover

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.niyuva.app.data.local.content.VedicSectionContent
import com.niyuva.app.data.local.content.VedicWisdomData
import com.niyuva.app.presentation.theme.*

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun VedicWisdomSectionScreen(
    sectionId: String,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val section = remember(sectionId) {
        VedicWisdomData.list.find { it.id == sectionId }
    }

    if (section == null) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
        return
    }

    val bg = remember(section.backgroundColorHex) {
        Color(android.graphics.Color.parseColor(section.backgroundColorHex))
    }
    val tc = remember(section.textColorHex) {
        Color(android.graphics.Color.parseColor(section.textColorHex))
    }

    // Define slides:
    // Slide 0: Title, Subtitle, Description, and First Paragraph
    // Slide 1 to M: Subsequent paragraphs one by one
    // Slide M+1 (Last Slide): Quote, Takeaway, and completion
    val totalSlides = 1 + section.paragraphs.size + 1
    val currentSectionIndex = remember(sectionId) {
        VedicWisdomData.list.indexOfFirst { it.id == sectionId }
    }
    var currentSlideIndex by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        // Subtle background watermark of the section's icon
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.06f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = section.icon,
                fontSize = 280.sp
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Top Bar
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
                        text = "Vedic Wisdom 🪷",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = DeepWarmBrown
                    )
                }

                Spacer(modifier = Modifier.width(40.dp)) // Symmetry spacer
            }

            // Slide content transition
            AnimatedContent(
                targetState = currentSlideIndex,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { width -> width } + fadeIn() with
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() with
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) { slideIndex ->
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = bg),
                    modifier = Modifier
                        .fillMaxSize()
                        .border(1.dp, tc.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp)
                    ) {
                        // Header Metadata of the slide
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val sectionNumStr = section.id.replace("sec_", "")
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(tc.copy(alpha = 0.12f))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "Section $sectionNumStr",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = tc
                                )
                            }

                            Text(
                                text = "Paath ${slideIndex + 1} of $totalSlides 📖",
                                fontFamily = NunitoFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = tc.copy(alpha = 0.8f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(scrollState)
                        ) {
                            if (slideIndex == 0) {
                                // Slide 0: Introduction
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(tc.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = section.icon,
                                        fontSize = 36.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = section.title,
                                    fontFamily = PlayfairFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = tc,
                                    lineHeight = 32.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = section.subtitle,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = tc.copy(alpha = 0.85f),
                                    lineHeight = 22.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    color = tc.copy(alpha = 0.2f)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = section.description,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = DeepWarmBrown,
                                    lineHeight = 24.sp
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                // Drop-cap styling for the first paragraph on Slide 0
                                if (section.paragraphs.isNotEmpty()) {
                                    EditorialParagraph(
                                        text = section.paragraphs[0],
                                        textColor = tc
                                    )
                                }

                            } else if (slideIndex <= section.paragraphs.size) {
                                // Slides 1 to M: Individual paragraphs
                                val paragraphIndex = slideIndex - 1
                                val paragraphText = section.paragraphs[paragraphIndex]

                                EditorialParagraph(
                                    text = paragraphText,
                                    textColor = tc,
                                    useDropCap = true
                                )

                            } else {
                                // Slide M+1: Quote & Key Takeaway
                                if (!section.quote.isNullOrBlank()) {
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
                                                text = section.quote,
                                                fontFamily = PlayfairFamily,
                                                fontWeight = FontWeight.Bold,
                                                fontStyle = FontStyle.Italic,
                                                fontSize = 16.sp,
                                                lineHeight = 24.sp,
                                                color = tc
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(24.dp))
                                }

                                Text(
                                    text = "Main Key Takeaway ✨",
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 16.sp,
                                    color = tc.copy(alpha = 0.8f),
                                    letterSpacing = 1.sp,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(tc)
                                        .padding(20.dp)
                                ) {
                                    Text(
                                        text = section.keyTakeaway,
                                        fontFamily = NunitoFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        lineHeight = 24.sp,
                                        color = PureWhite,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Spacer(modifier = Modifier.height(32.dp))

                                Text(
                                    text = "Humare poorvaj jaante the ki periods gande nahi hain. Yeh life ki foundation hain, dharti ke seasons ki tarah. Uss gyaan ko wapas laayein aur garv karein. 🪷",
                                    fontFamily = NunitoFamily,
                                    fontSize = 14.sp,
                                    lineHeight = 22.sp,
                                    color = DeepWarmBrown,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp)
                                )
                            }
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
                    progress = (currentSlideIndex + 1).toFloat() / totalSlides.toFloat(),
                    color = tc,
                    trackColor = tc.copy(alpha = 0.15f),
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
                    if (currentSlideIndex > 0) {
                        Button(
                            onClick = { currentSlideIndex-- },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = tc.copy(alpha = 0.12f),
                                contentColor = tc
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "⬅ Pichli Baat",
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
                            if (currentSlideIndex < totalSlides - 1) {
                                currentSlideIndex++
                            } else {
                                // Last slide: navigate to next section or pop back
                                if (currentSectionIndex >= 0 && currentSectionIndex < VedicWisdomData.list.size - 1) {
                                    val nextSection = VedicWisdomData.list[currentSectionIndex + 1]
                                    navController.navigate(
                                        com.niyuva.app.presentation.navigation.NavRoutes.VedicSection.createRoute(nextSection.id)
                                    ) {
                                        popUpTo(com.niyuva.app.presentation.navigation.NavRoutes.Discover.route)
                                    }
                                } else {
                                    navController.popBackStack()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = tc,
                            contentColor = PureWhite
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = when {
                                currentSlideIndex < totalSlides - 1 -> "Agli Baat ➡"
                                currentSectionIndex >= 0 && currentSectionIndex < VedicWisdomData.list.size - 1 -> "Agli Vidya ➡"
                                else -> "Samajh Gayi! 🌸"
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

/**
 * Editorial style text renderer that supports drop cap (large decorative first letter).
 */
@Composable
fun EditorialParagraph(
    text: String,
    textColor: Color,
    modifier: Modifier = Modifier,
    useDropCap: Boolean = false
) {
    if (text.isBlank()) return

    val cleanText = text.trim()
    val annotatedString = remember(cleanText, textColor, useDropCap) {
        buildAnnotatedString {
            if (useDropCap && cleanText.length > 1) {
                // Drop cap formatting
                val firstChar = cleanText.substring(0, 1)
                val restOfText = cleanText.substring(1)

                withStyle(
                    style = SpanStyle(
                        fontFamily = PlayfairFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 42.sp,
                        color = textColor
                    )
                ) {
                    append(firstChar)
                }

                withStyle(
                    style = SpanStyle(
                        fontFamily = NunitoFamily,
                        fontSize = 15.sp,
                        color = DeepWarmBrown
                    )
                ) {
                    append(restOfText)
                }
            } else {
                withStyle(
                    style = SpanStyle(
                        fontFamily = NunitoFamily,
                        fontSize = 15.sp,
                        color = DeepWarmBrown
                    )
                ) {
                    append(cleanText)
                }
            }
        }
    }

    Text(
        text = annotatedString,
        lineHeight = 24.sp,
        modifier = modifier.padding(bottom = 12.dp)
    )
}
