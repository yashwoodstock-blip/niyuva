package com.niyuva.app.presentation.screens.saarthi

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.niyuva.app.presentation.navigation.NavRoutes
import com.niyuva.app.domain.model.ChatMessage
import com.niyuva.app.presentation.theme.BlushMist
import com.niyuva.app.presentation.theme.CyclePhase
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DisabledButton
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite
import com.niyuva.app.presentation.theme.Terracotta
import com.niyuva.app.presentation.theme.WarmIvory
import com.niyuva.app.presentation.theme.WarmLinen
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SaarthiScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    viewModel: SaarthiViewModel = hiltViewModel()
) {
    val view = androidx.compose.ui.platform.LocalView.current
    if (!view.isInEditMode) {
        androidx.compose.runtime.SideEffect {
            val window = (view.context as? android.app.Activity)?.window
            if (window != null) {
                window.statusBarColor = android.graphics.Color.TRANSPARENT
                val insetsController = androidx.core.view.WindowCompat.getInsetsController(window, view)
                insetsController.isAppearanceLightStatusBars = false // White icons
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val voiceResult by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("voice_result", null)
        ?.collectAsStateWithLifecycle() ?: remember { androidx.compose.runtime.mutableStateOf<String?>(null) }

    LaunchedEffect(voiceResult) {
        voiceResult?.let { text ->
            viewModel.onInputChanged(text)
            viewModel.onSendMessage()
            navController.currentBackStackEntry?.savedStateHandle?.remove<String>("voice_result")
        }
    }

    // Animated phase gradient transition (stays themed, does not fade to white)
    val animatedGradientStart by animateColorAsState(
        targetValue = uiState.phaseTheme.backgroundGradientStart,
        animationSpec = tween(durationMillis = 800, easing = androidx.compose.animation.core.LinearOutSlowInEasing),
        label = "gradientStart"
    )
    val animatedGradientEnd by animateColorAsState(
        targetValue = uiState.phaseTheme.backgroundGradientEnd,
        animationSpec = tween(durationMillis = 800, easing = androidx.compose.animation.core.LinearOutSlowInEasing),
        label = "gradientEnd"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(animatedGradientStart, animatedGradientEnd)
                )
            )
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            modifier = Modifier.fillMaxSize(),
            topBar = {
                SaarthiTopBar(
                    onClearChatClick = {
                        viewModel.clearConversation()
                        Toast.makeText(context, "Chat cleared successfully 🌸", Toast.LENGTH_SHORT).show()
                    }
                )
            },
            bottomBar = {
                val imeBottom = WindowInsets.ime.asPaddingValues().calculateBottomPadding()
                val navBarBottom = contentPadding.calculateBottomPadding()
                val bottomPadding = if (imeBottom > navBarBottom) imeBottom else navBarBottom
                
                SaarthiInputBox(
                    inputText = uiState.inputText,
                    showAiWarning = uiState.showAiWarning,
                    onInputChanged = { viewModel.onInputChanged(it) },
                    onSend = { viewModel.onSendMessage() },
                    onVoiceClick = {
                        navController.navigate(NavRoutes.SaarthiVoice.route)
                    },
                    modifier = Modifier.padding(bottom = bottomPadding)
                )
            }
        ) { innerPadding ->
            AnimatedContent(
                targetState = uiState.hasConversation,
                transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                },
                label = "saarthiStateTransition",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) { hasConversation ->
                if (uiState.isLoading && uiState.messages.isEmpty()) {
                    SaarthiShimmerLoading()
                } else if (hasConversation) {
                    SaarthiChatView(
                        messages = uiState.messages,
                        isLoading = uiState.isLoading
                    )
                } else {
                    SaarthiEmptyState(
                        userName = uiState.userName,
                        phase = uiState.phase,
                        onQuickQuestionClick = { viewModel.onQuickQuestionTapped(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SaarthiShimmerLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        com.niyuva.app.presentation.components.NiyuvaShimmer(
            modifier = Modifier
                .width(200.dp)
                .height(60.dp),
            cornerRadius = 16.dp
        )
        com.niyuva.app.presentation.components.NiyuvaShimmer(
            modifier = Modifier
                .width(250.dp)
                .height(80.dp)
                .align(Alignment.End),
            cornerRadius = 16.dp
        )
        com.niyuva.app.presentation.components.NiyuvaShimmer(
            modifier = Modifier
                .width(180.dp)
                .height(50.dp),
            cornerRadius = 16.dp
        )
    }
}

@Composable
private fun SaarthiTopBar(
    onClearChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(WarmLinen.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "🌸",
                fontSize = 20.sp
            )
        }

        Text(
            text = "Saarthi",
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = PureWhite
        )

        IconButton(
            onClick = onClearChatClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = androidx.compose.material.icons.Icons.Default.Delete,
                contentDescription = "Clear Chat",
                tint = PureWhite,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun SaarthiEmptyState(
    userName: String,
    phase: CyclePhase,
    onQuickQuestionClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName = if (userName.isNotBlank()) " $userName" else ""
    val greetingText = saarthiGreeting(phase)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Hi$displayName,",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                color = PureWhite.copy(alpha = 0.80f)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = greetingText,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 28.sp,
                color = PureWhite,
                lineHeight = 36.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            val questions = listOf(
                "Mera period late kyu ho raha hai? 🌸",
                "Periods mein itna dard normal hai kya?",
                "Ovulation kya hota hai? 🥚",
                "Menstrual cup safe hai?"
            )
            androidx.compose.foundation.lazy.LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(questions) { question ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(PureWhite.copy(alpha = 0.85f))
                            .clickable { onQuickQuestionClick(question) }
                            .semantics { contentDescription = question }
                            .padding(horizontal = 16.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = question,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = DeepPlumRose
                        )
                    }
                }
            }
        }
    }
}

data class DateHeader(val label: String)

fun groupMessagesByDate(messages: List<ChatMessage>): List<Any> {
    if (messages.isEmpty()) return emptyList()
    val result = mutableListOf<Any>()
    var lastDate: LocalDate? = null

    for (message in messages) {
        val messageDate = message.timestamp.toLocalDate()
        if (lastDate == null || lastDate != messageDate) {
            lastDate = messageDate
            result.add(DateHeader(getGroupDateHeader(messageDate)))
        }
        result.add(message)
    }
    return result
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SaarthiChatView(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val groupedItems = remember(messages) { groupMessagesByDate(messages) }
    // Track if first layout scroll has occurred — snap instantly on first load
    var initialScrollDone by remember { mutableStateOf(false) }

    LaunchedEffect(groupedItems.size) {
        if (groupedItems.isNotEmpty()) {
            if (!initialScrollDone) {
                // UI FIX: Delay scroll to bottom to ensure items are laid out/measured
                delay(100)
                listState.scrollToItem(groupedItems.size - 1)
                initialScrollDone = true
            } else {
                // Smooth scroll when a new message arrives
                listState.animateScrollToItem(groupedItems.size - 1)
            }
        }
    }

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
        modifier = modifier.fillMaxSize()
    ) {
        groupedItems.forEachIndexed { index, item ->
            when (item) {
                is DateHeader -> {
                    stickyHeader(key = "date_group_$index") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(WarmIvory)
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = item.label,
                                fontFamily = NunitoFamily,
                                fontSize = 11.sp,
                                color = DustyMauve
                            )
                        }
                    }
                }
                is ChatMessage -> {
                    val msgIndex = messages.indexOf(item)
                    val isFirst = msgIndex == 0 ||
                            messages[msgIndex].role != messages[msgIndex - 1].role ||
                            java.time.temporal.ChronoUnit.MINUTES.between(messages[msgIndex - 1].timestamp, item.timestamp) >= 5

                    val isLast = msgIndex == messages.size - 1 ||
                            messages[msgIndex].role != messages[msgIndex + 1].role ||
                            java.time.temporal.ChronoUnit.MINUTES.between(item.timestamp, messages[msgIndex + 1].timestamp) >= 5

                    item(key = "message_${item.id}_$index") {
                        ChatMessageItem(
                            message = item,
                            isFirst = isFirst,
                            isLast = isLast
                        )
                    }
                }
            }
        }

        if (isLoading) {
            item(key = "typing_indicator") {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(WarmLinen),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "S",
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = DeepPlumRose
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 18.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 18.dp
                        ),
                        colors = CardDefaults.cardColors(containerColor = PureWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        SaarthiTypingIndicator()
                    }
                }
            }
        }
    }
}

@Composable
private fun SaarthiInputBox(
    inputText: String,
    showAiWarning: Boolean,
    onInputChanged: (String) -> Unit,
    onSend: () -> Unit,
    onVoiceClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp, start = 16.dp, end = 16.dp)
        ) {
            if (showAiWarning) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFFF3CD))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = androidx.compose.ui.res.stringResource(id = com.niyuva.app.R.string.warning_ai_connection_fail),
                        fontFamily = NunitoFamily,
                        fontSize = 12.sp,
                        color = Color(0xFF856404),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Top Row: Text field + [✨ Send]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = inputText,
                    onValueChange = onInputChanged,
                    textStyle = TextStyle(
                        fontFamily = NunitoFamily,
                        fontSize = 15.sp,
                        color = DeepWarmBrown
                    ),
                    cursorBrush = SolidColor(Terracotta),
                    modifier = Modifier
                        .weight(1f)
                        .defaultMinSize(minHeight = 40.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(WarmLinen)
                        .border(1.dp, BlushMist, RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (inputText.isEmpty()) {
                                Text(
                                    text = "Apna sawaal poochho...",
                                    fontFamily = NunitoFamily,
                                    fontSize = 15.sp,
                                    color = DustyMauve
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.width(12.dp))

                val isEnabled = inputText.isNotBlank()
                Text(
                    text = "✨ Send",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = if (isEnabled) Terracotta else DisabledButton,
                    modifier = Modifier
                        .clickable(enabled = isEnabled, onClick = onSend)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Row: [🎤] only — upload/attach removed (nothing can be uploaded)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Mic,
                    contentDescription = "Voice input",
                    tint = DustyMauve,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onVoiceClick)
                )
            }
        }
    }
}

private fun saarthiGreeting(phase: CyclePhase): String = when (phase) {
    CyclePhase.MENSTRUATION -> "Kuch poochna hai? Main hoon yahan 🌸"
    CyclePhase.FOLLICULAR   -> "Kya chal raha hai aaj? ✨"
    CyclePhase.OVULATION    -> "Kuch bhi poochh — aaj ka energy level hi poochh! 🔥"
    CyclePhase.LUTEAL       -> "Baat karte hain — kya feel ho raha hai? 💛"
}

private fun getGroupDateHeader(date: LocalDate): String {
    val today = LocalDate.now()
    return when (date) {
        today -> "Today"
        today.minusDays(1) -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("MMM dd"))
    }
}
