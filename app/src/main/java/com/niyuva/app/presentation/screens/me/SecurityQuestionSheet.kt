package com.niyuva.app.presentation.screens.me

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.components.NiyuvaBottomSheet
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.components.NiyuvaTextField
import com.niyuva.app.presentation.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityQuestionSheet(
    existingQuestion: String?,
    onVerifyAnswer: suspend (String) -> Boolean,
    onSetQuestion: (String, String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val questions = remember {
        listOf(
            "Teri best friend ka naam?",
            "School mein tera favourite subject?",
            "Ghar ke paas ki koi dukaan ka naam?",
            "Teri maa ka school ka naam?",
            "Tera pehla phone konsa tha?"
        )
    }

    var isVerified by remember { mutableStateOf(existingQuestion == null) }
    var verifyAnswerInput by remember { mutableStateOf("") }
    var selectedQuestion by remember { mutableStateOf(questions[0]) }
    var newAnswerInput by remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }
    var isChecking by remember { mutableStateOf(false) }
    var expandedDropdown by remember { mutableStateOf(false) }
    var errorText by remember { mutableStateOf("") }

    val shakeOffset = remember { Animatable(0f) }

    val triggerShake: (String) -> Unit = { msg ->
        errorText = msg
        isError = true
        coroutineScope.launch {
            for (i in 0..2) {
                shakeOffset.animateTo(15f, animationSpec = tween(50))
                shakeOffset.animateTo(-15f, animationSpec = tween(50))
            }
            shakeOffset.animateTo(0f, animationSpec = tween(50))
            delay(1500)
            isError = false
        }
    }

    NiyuvaBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Security Question",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = DeepWarmBrown
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (!isVerified && existingQuestion != null) {
                // Step 1: Verification
                Text(
                    text = "Pehle apna purana jawab verify karo",
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = if (isError) DestructiveRose else DustyMauve,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Current question preview (truncated to 30 chars per requirements if needed, but here it's full for context)
                Text(
                    text = existingQuestion,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.offset(x = shakeOffset.value.dp)) {
                    NiyuvaTextField(
                        value = verifyAnswerInput,
                        onValueChange = { verifyAnswerInput = it },
                        placeholder = "Jawab yahan likho..."
                    )
                }

                if (isError) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorText,
                        fontFamily = NunitoFamily,
                        fontSize = 12.sp,
                        color = DestructiveRose,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                NiyuvaPrimaryButton(
                    text = if (isChecking) "Checking..." else "Verify Jawab",
                    onClick = {
                        if (verifyAnswerInput.trim().isNotEmpty() && !isChecking) {
                            isChecking = true
                            coroutineScope.launch {
                                val match = onVerifyAnswer(verifyAnswerInput)
                                if (match) {
                                    isVerified = true
                                    isError = false
                                } else {
                                    triggerShake("Jawab match nahi hua 💛")
                                }
                                isChecking = false
                            }
                        }
                    },
                    enabled = verifyAnswerInput.trim().isNotEmpty() && !isChecking
                )
            } else {
                // Step 2: Set new security question
                Text(
                    text = "Naya recovery question aur jawab set karo",
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = DustyMauve,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Question Dropdown
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WarmLinen, RoundedCornerShape(16.dp))
                        .clickable { expandedDropdown = true }
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Truncated question preview to 30 chars
                        val questionDisplay = if (selectedQuestion.length > 30) {
                            selectedQuestion.take(27) + "..."
                        } else {
                            selectedQuestion
                        }
                        Text(
                            text = questionDisplay,
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

                    DropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false },
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
                                    expandedDropdown = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                NiyuvaTextField(
                    value = newAnswerInput,
                    onValueChange = { newAnswerInput = it },
                    placeholder = "Apna naya jawab likho..."
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Yaad rakhna — case matter nahi karta 😊",
                    fontFamily = NunitoFamily,
                    fontSize = 12.sp,
                    color = DustyMauve,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(32.dp))

                NiyuvaPrimaryButton(
                    text = "Save Question",
                    onClick = {
                        if (newAnswerInput.trim().isNotEmpty()) {
                            onSetQuestion(selectedQuestion, newAnswerInput.trim())
                            onDismiss()
                        }
                    },
                    enabled = newAnswerInput.trim().isNotEmpty()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
