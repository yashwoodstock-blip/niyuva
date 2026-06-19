package com.niyuva.app.presentation.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.theme.*

@Composable
fun OnboardingSecurityQuestionScreen(
    onBack: () -> Unit,
    onContinue: (question: String, answer: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val questions = remember {
        listOf(
            "Teri best friend ka naam?",
            "School mein tera favourite subject?",
            "Ghar ke paas ki koi dukaan ka naam?",
            "Teri maa ka school ka naam?",
            "Tera pehla phone konsa tha?"
        )
    }

    var selectedQuestion by remember { mutableStateOf(questions[0]) }
    var answer by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isInputFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {
            // Top Row with Back Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = PureWhite.copy(alpha = 0.6f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepWarmBrown
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Agar PIN bhool jaaye toh? Ek sawaal set kar lo 😊",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Question Dropdown trigger card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WarmLinen, RoundedCornerShape(16.dp))
                        .clickable { expanded = true }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedQuestion,
                            fontFamily = NunitoFamily,
                            fontSize = 14.sp,
                            color = DeepWarmBrown,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Outlined.KeyboardArrowDown,
                            contentDescription = "Select Question",
                            tint = DustyMauve
                        )
                    }

                    // Dropdown menu
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .background(WarmLinen)
                    ) {
                        questions.forEach { question ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = question,
                                        fontFamily = NunitoFamily,
                                        fontSize = 14.sp,
                                        color = DeepWarmBrown
                                    )
                                },
                                onClick = {
                                    selectedQuestion = question
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Custom styled answer text input box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(WarmLinen, RoundedCornerShape(16.dp))
                        .then(
                            if (isInputFocused) {
                                Modifier.border(BorderStroke(1.5.dp, DeepPlumRose), RoundedCornerShape(16.dp))
                            } else {
                                Modifier
                            }
                        )
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    BasicTextField(
                        value = answer,
                        onValueChange = { answer = it },
                        singleLine = true,
                        textStyle = TextStyle(
                            fontFamily = NunitoFamily,
                            fontSize = 16.sp,
                            color = DeepWarmBrown
                        ),
                        cursorBrush = SolidColor(Terracotta),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = { focusManager.clearFocus() }
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { isInputFocused = it.isFocused },
                        decorationBox = { innerTextField ->
                            if (answer.isEmpty()) {
                                Text(
                                    text = "Jawab yahan likho...",
                                    fontFamily = NunitoFamily,
                                    fontSize = 16.sp,
                                    color = DustyMauve
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Small reminder note below input field
                Text(
                    text = "Yaad rakhna — case matter nahi karta 😊",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = DustyMauve,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
                )
            }

            // Bottom CTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                NiyuvaPrimaryButton(
                    text = "Continue",
                    onClick = { onContinue(selectedQuestion, answer.trim()) },
                    enabled = selectedQuestion.isNotEmpty() && answer.trim().isNotEmpty()
                )
            }
        }
    }
}
