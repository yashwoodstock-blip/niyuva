package com.niyuva.app.presentation.screens.body

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.niyuva.app.domain.model.Article
import com.niyuva.app.domain.model.ContentBlock
import com.niyuva.app.presentation.theme.BlushMist
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DestructiveRose
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PlayfairFamily
import com.niyuva.app.presentation.theme.PhaseColorOvulation
import com.niyuva.app.presentation.theme.PureWhite
import com.niyuva.app.presentation.theme.Terracotta
import com.niyuva.app.presentation.theme.TopicAnimations
import com.niyuva.app.presentation.theme.TopicDuringPeriods
import com.niyuva.app.presentation.theme.TopicHormones
import com.niyuva.app.presentation.theme.TopicHygiene
import com.niyuva.app.presentation.theme.TopicMyBody
import com.niyuva.app.presentation.theme.TopicMyCycle
import com.niyuva.app.presentation.theme.TopicPCOS
import com.niyuva.app.presentation.theme.TopicPeriodProducts
import com.niyuva.app.presentation.theme.WarmIvory
import com.niyuva.app.presentation.theme.WarmLinen

// ─────────────────────────────────────────────────────────────────────────────
// Article Dynamic Theming Constants
// ─────────────────────────────────────────────────────────────────────────────

private data class ArticleTheme(
    val mainColor: Color,
    val gradientColors: List<Color>,
    val accentColor: Color
)

private fun getArticleTheme(topicId: String): ArticleTheme = when (topicId) {
    "my_cycle" -> ArticleTheme(
        mainColor = TopicMyCycle,
        gradientColors = listOf(Color(0xFFE8F5E9), Color(0xFFD5E8D2)),
        accentColor = Color(0xFF4E7E4E)
    )
    "hormones" -> ArticleTheme(
        mainColor = TopicHormones,
        gradientColors = listOf(Color(0xFFF3E5F5), Color(0xFFE8E0F0)),
        accentColor = Color(0xFF7B1FA2)
    )
    "hygiene" -> ArticleTheme(
        mainColor = TopicHygiene,
        gradientColors = listOf(Color(0xFFE0F2F1), Color(0xFFD2EDE8)),
        accentColor = Color(0xFF00796B)
    )
    "period_products" -> ArticleTheme(
        mainColor = TopicPeriodProducts,
        gradientColors = listOf(Color(0xFFFBE9E7), Color(0xFFF5E0D5)),
        accentColor = Color(0xFFD84315)
    )
    "pcos", "pcos_health" -> ArticleTheme(
        mainColor = TopicPCOS,
        gradientColors = listOf(Color(0xFFFFFDE7), Color(0xFFF0E5C8)),
        accentColor = Color(0xFFF57F17)
    )
    "health_diet" -> ArticleTheme(
        mainColor = Color(0xFFE2F0D9),
        gradientColors = listOf(Color(0xFFF1F8E9), Color(0xFFDCEDC8)),
        accentColor = Color(0xFF33691E)
    )
    "during_periods" -> ArticleTheme(
        mainColor = TopicDuringPeriods,
        gradientColors = listOf(Color(0xFFFCE4EC), Color(0xFFF0D5DC)),
        accentColor = Color(0xFFC2185B)
    )
    "my_body" -> ArticleTheme(
        mainColor = TopicMyBody,
        gradientColors = listOf(Color(0xFFFFF8E1), Color(0xFFF5ECD5)),
        accentColor = Color(0xFFFF8F00)
    )
    "animations" -> ArticleTheme(
        mainColor = TopicAnimations,
        gradientColors = listOf(Color(0xFFFCE4EC), Color(0xFFE8D8E0)),
        accentColor = Color(0xFFAD1457)
    )
    else -> ArticleTheme(
        mainColor = WarmLinen,
        gradientColors = listOf(Color(0xFFFBF3EE), Color(0xFFF5E6DC)),
        accentColor = DeepPlumRose
    )
}

private data class ArticleTab(val title: String, val blocks: List<ContentBlock>)

private fun cleanTabTitle(title: String): String {
    var result = title.trim()
    if (result.contains("—")) {
        val split = result.split("—")
        val last = split.last().trim()
        if (last.length in 3..25) result = last
    } else if (result.contains("–")) {
        val split = result.split("–")
        val last = split.last().trim()
        if (last.length in 3..25) result = last
    }
    if (result.contains("(")) {
        result = result.substringBefore("(").trim()
    }
    
    val words = result.split(" ")
    val firstWord = words.firstOrNull() ?: ""
    val hasEmoji = firstWord.any { it.code > 127 || Character.isSurrogate(it) }
    
    val cleanWords = result.replace("[#\\*\\?\\!]".toRegex(), "").trim().split(" ")
    val wordsToTake = if (hasEmoji) 3 else 2
    val joined = cleanWords.take(wordsToTake).joinToString(" ")
    
    return joined.ifEmpty { "Intro" }
}

// ─────────────────────────────────────────────────────────────────────────────
// Shimmer Loader for Premium Feel
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun shimmerBrush(targetValue: Float = 1000f): Brush {
    val shimmerColors = listOf(
        WarmLinen,
        PureWhite,
        WarmLinen
    )

    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnimation = transition.animateFloat(
        initialValue = 0f,
        targetValue = targetValue,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    return Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnimation.value, y = translateAnimation.value)
    )
}

@Composable
fun ArticleSkeletonLoader(modifier: Modifier = Modifier) {
    val brush = shimmerBrush()
    Column(modifier = modifier.fillMaxSize().background(WarmIvory)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.width(24.dp))
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(120.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(brush)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth(0.5f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(brush)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Premium Interactive Article Screen
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun ArticleScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ArticleViewModel = hiltViewModel()
) {
    val article by viewModel.article.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        if (isLoading) {
            ArticleSkeletonLoader()
        } else if (article == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Yeh topic abhi aa raha hai — jald! 💛",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepPlumRose)
                ) {
                    Text(text = "Wapas Chalein", color = PureWhite, fontFamily = NunitoFamily)
                }
            }
        } else {
            val art = article!!
            val theme = remember(art.topicId) { getArticleTheme(art.topicId) }

            // Group blocks into tabs by SectionHeader dynamically
            val tabs = remember(art.blocks) {
                val list = mutableListOf<ArticleTab>()
                var currentTitle = "Pehle Baat ✨"
                val currentBlocks = mutableListOf<ContentBlock>()

                for (block in art.blocks) {
                    if (block is ContentBlock.SectionHeader) {
                        if (currentBlocks.isNotEmpty()) {
                            list.add(ArticleTab(currentTitle, currentBlocks.toList()))
                            currentBlocks.clear()
                        }
                        currentTitle = block.text
                    } else {
                        currentBlocks.add(block)
                    }
                }
                if (currentBlocks.isNotEmpty()) {
                    list.add(ArticleTab(currentTitle, currentBlocks.toList()))
                }
                list
            }

            var selectedTabIdx by remember { mutableStateOf(0) }
            val selectedTab = tabs.getOrNull(selectedTabIdx) ?: tabs.firstOrNull()

            Column(modifier = Modifier.fillMaxSize()) {
                // Top Custom Header Actions
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
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
                            text = art.title,
                            fontFamily = PlayfairFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = DeepWarmBrown,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(40.dp))
                }

                // Magazine Header Banner Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Brush.verticalGradient(theme.gradientColors))
                        .padding(20.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(PureWhite.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = getTopicEmoji(art.topicId),
                                fontSize = 32.sp
                            )
                        }
                        Column {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(theme.accentColor.copy(alpha = 0.15f))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = art.topicId.replace("_", " ").uppercase(),
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = theme.accentColor,
                                    letterSpacing = 1.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = art.title,
                                fontFamily = PlayfairFamily,
                                fontWeight = FontWeight.Black,
                                fontSize = 22.sp,
                                color = DeepWarmBrown
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Horizontal Pill Tabs Row
                if (tabs.size > 1) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(tabs) { idx, tab ->
                            val isSelected = idx == selectedTabIdx
                            val bgAnimatedColor by animateColorAsState(
                                targetValue = if (isSelected) theme.accentColor else PureWhite,
                                label = "tabBg"
                            )
                            val textAnimatedColor by animateColorAsState(
                                targetValue = if (isSelected) PureWhite else DeepWarmBrown,
                                label = "tabText"
                            )
                            val scale by animateFloatAsState(
                                targetValue = if (isSelected) 1.05f else 1.0f,
                                label = "tabScale"
                            )

                            Box(
                                modifier = Modifier
                                    .scale(scale)
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(bgAnimatedColor)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) Color.Transparent else BlushMist,
                                        shape = RoundedCornerShape(50.dp)
                                    )
                                    .clickable { selectedTabIdx = idx }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cleanTabTitle(tab.title),
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = textAnimatedColor
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Tab Content Render Area
                AnimatedContent(
                    targetState = selectedTab,
                    transitionSpec = {
                        (slideInVertically { it / 2 } + fadeIn()) togetherWith fadeOut()
                    },
                    label = "tabContent",
                    modifier = Modifier.weight(1f)
                ) { targetTab ->
                    if (targetTab != null) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            contentPadding = PaddingValues(bottom = 32.dp, top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Text(
                                    text = targetTab.title.replace("#", "").trim(),
                                    fontFamily = PlayfairFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = theme.accentColor,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            items(targetTab.blocks) { block ->
                                ContentBlockRenderer(block, theme)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getTopicEmoji(topicId: String): String = when (topicId) {
    "my_cycle" -> "🌙"
    "hormones" -> "⚗️"
    "hygiene" -> "🚿"
    "period_products" -> "📦"
    "pcos_health", "pcos" -> "💛"
    "during_periods" -> "🌸"
    "my_body" -> "🌺"
    "animations" -> "🎬"
    else -> "📚"
}

// ─────────────────────────────────────────────────────────────────────────────
// Custom Content Block Renderers (Premium Styling)
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ContentBlockRenderer(block: ContentBlock, theme: ArticleTheme) = when (block) {
    is ContentBlock.SectionHeader -> {} // Omitted, title displayed by Tab header
    is ContentBlock.BodyText -> BodyTextBlock(block.text)
    is ContentBlock.BulletList -> BulletListBlock(block.items, theme)
    is ContentBlock.MythFact -> MythFactBlock(block.myth, block.fact)
    is ContentBlock.QuoteBlock -> QuoteBlockView(block.quote, block.attribution, theme)
    is ContentBlock.InfoCallout -> InfoCalloutBlock(block.label, block.body, block.accentColor, theme)
    is ContentBlock.DividerBlock -> DividerBlockView(block.label)
    is ContentBlock.ComparisonTable -> ComparisonTableBlock(block.headers, block.rows, theme)
}

@Composable
private fun BodyTextBlock(text: String) {
    Text(
        text = parseMarkdownToAnnotatedString(text),
        fontFamily = NunitoFamily,
        fontSize = 15.sp,
        color = DeepWarmBrown,
        lineHeight = 24.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    )
}

@Composable
private fun BulletListBlock(items: List<String>, theme: ArticleTheme) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items.forEach { item ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = PureWhite.copy(alpha = 0.6f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, BlushMist.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp, end = 10.dp)
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(theme.accentColor)
                    )
                    Text(
                        text = parseMarkdownToAnnotatedString(item),
                        fontFamily = NunitoFamily,
                        fontSize = 14.sp,
                        color = DeepWarmBrown,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun MythFactBlock(myth: String, fact: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF5F5)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD6D6)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(text = "❌", fontSize = 20.sp, modifier = Modifier.padding(end = 12.dp))
                Column {
                    Text(
                        text = "Myth (Galat Fahami)",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = DestructiveRose,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = myth,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = DeepWarmBrown,
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF2F9F1)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD3ECD0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(text = "✅", fontSize = 20.sp, modifier = Modifier.padding(end = 12.dp))
                Column {
                    Text(
                        text = "Sach (Fact)",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = PhaseColorOvulation,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = fact,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DeepWarmBrown,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun QuoteBlockView(quote: String, attribution: String?, theme: ArticleTheme) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = theme.mainColor.copy(alpha = 0.15f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, theme.accentColor.copy(alpha = 0.25f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(text = "👩‍🦰", fontSize = 24.sp, modifier = Modifier.padding(end = 12.dp))
            Column {
                Text(
                    text = "Didi Tip ✨",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = theme.accentColor,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = quote,
                    fontFamily = NunitoFamily,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = DeepWarmBrown,
                    lineHeight = 21.sp
                )
                if (attribution != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "— $attribution",
                        fontFamily = NunitoFamily,
                        fontSize = 11.sp,
                        color = DustyMauve,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoCalloutBlock(label: String, body: String, accentColorHex: String?, theme: ArticleTheme) {
    val customColor = remember(accentColorHex) {
        if (accentColorHex != null) {
            runCatching { Color(android.graphics.Color.parseColor(accentColorHex)) }.getOrDefault(theme.accentColor)
        } else {
            theme.accentColor
        }
    }
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmLinen.copy(alpha = 0.35f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, BlushMist.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(customColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = customColor
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = body,
                fontFamily = NunitoFamily,
                fontSize = 13.sp,
                color = DeepWarmBrown,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
private fun DividerBlockView(label: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            color = BlushMist,
            thickness = 1.dp,
            modifier = Modifier.weight(1f)
        )
        if (label != null) {
            Text(
                text = label,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = DustyMauve,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            HorizontalDivider(
                color = BlushMist,
                thickness = 1.dp,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ComparisonTableBlock(headers: List<String>, rows: List<List<String>>, theme: ArticleTheme) {
    val scrollState = rememberScrollState()
    val isScrollable = headers.size > 3

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, BlushMist.copy(alpha = 0.7f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        val tableContent = @Composable {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(theme.mainColor.copy(alpha = 0.25f))
                        .padding(vertical = 12.dp)
                ) {
                    headers.forEach { header ->
                        TableCell(
                            text = header,
                            isHeader = true,
                            modifier = if (isScrollable) Modifier.width(130.dp) else Modifier.weight(1f)
                        )
                    }
                }
                rows.forEachIndexed { rowIndex, row ->
                    HorizontalDivider(color = BlushMist.copy(alpha = 0.4f), thickness = 1.dp)
                    val bgColor = if (rowIndex % 2 == 0) PureWhite else WarmIvory.copy(alpha = 0.5f)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(bgColor)
                            .padding(vertical = 12.dp)
                    ) {
                        row.forEach { cell ->
                            TableCell(
                                text = cell,
                                isHeader = false,
                                modifier = if (isScrollable) Modifier.width(130.dp) else Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        if (isScrollable) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            ) {
                tableContent()
            }
        } else {
            tableContent()
        }
    }
}

@Composable
private fun TableCell(text: String, isHeader: Boolean, modifier: Modifier = Modifier) {
    Text(
        text = parseMarkdownToAnnotatedString(text),
        fontFamily = NunitoFamily,
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal,
        fontSize = 12.sp,
        color = if (isHeader) DustyMauve else DeepWarmBrown,
        textAlign = TextAlign.Start,
        lineHeight = 16.sp,
        modifier = modifier
            .padding(horizontal = 8.dp)
    )
}

@Composable
fun parseMarkdownToAnnotatedString(text: String): AnnotatedString {
    return buildAnnotatedString {
        val parts = text.split("(?=\\*\\*)|(?<=\\*\\*)|(?=\\*)|(?<=\\*)".toRegex())
        var isBold = false
        var isItalic = false

        for (part in parts) {
            if (part == "**") {
                isBold = !isBold
                continue
            }
            if (part == "*") {
                isItalic = !isItalic
                continue
            }
            val style = SpanStyle(
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal
            )
            pushStyle(style)
            append(part)
            pop()
        }
    }
}
