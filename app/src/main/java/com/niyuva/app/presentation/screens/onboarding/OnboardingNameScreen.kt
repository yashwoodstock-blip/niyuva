package com.niyuva.app.presentation.screens.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
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
fun OnboardingNameScreen(
    onBack: () -> Unit,
    onNameEntered: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
    ) {
        // Top row with Back Button
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
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Label
            Text(
                text = "Chalo pehchaan karte hain! 😊",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = DustyMauve,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Title
            Text(
                text = "Main tujhe kya bulaaun?",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                color = DeepWarmBrown,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Custom styled text input box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 16.dp)
                    .background(WarmLinen, RoundedCornerShape(16.dp))
                    .then(
                        if (isFocused) {
                            Modifier.border(BorderStroke(1.5.dp, DeepPlumRose), RoundedCornerShape(16.dp))
                        } else {
                            Modifier
                        }
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = NunitoFamily,
                        fontSize = 16.sp,
                        color = DeepWarmBrown
                    ),
                    cursorBrush = SolidColor(Terracotta),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { isFocused = it.isFocused },
                    decorationBox = { innerTextField ->
                        if (name.isEmpty()) {
                            Text(
                                text = "Apna naam likho...",
                                fontFamily = NunitoFamily,
                                fontSize = 16.sp,
                                color = DustyMauve
                            )
                        }
                        innerTextField()
                    }
                )
            }
        }

        // Bottom CTA Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            val trimmedName = name.trim()
            val isValid = trimmedName.isNotBlank() && trimmedName.length >= 2

            NiyuvaPrimaryButton(
                text = "Continue",
                onClick = { onNameEntered(trimmedName) },
                enabled = isValid
            )
        }
    }
}
