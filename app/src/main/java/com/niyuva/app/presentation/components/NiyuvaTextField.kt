package com.niyuva.app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.*

@Composable
fun NiyuvaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        capitalization = KeyboardCapitalization.Words,
        imeAction = ImeAction.Done
    ),
    keyboardActions: KeyboardActions? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    val focusManager = LocalFocusManager.current
    val actualKeyboardActions = keyboardActions ?: KeyboardActions(
        onDone = { focusManager.clearFocus() }
    )
    var isFocused by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterStart) {
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    singleLine = true,
                    textStyle = TextStyle(
                        fontFamily = NunitoFamily,
                        fontSize = 16.sp,
                        color = DeepWarmBrown
                    ),
                    cursorBrush = SolidColor(Terracotta),
                    keyboardOptions = keyboardOptions,
                    keyboardActions = actualKeyboardActions,
                    visualTransformation = visualTransformation,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isFocused = it.isFocused },
                    decorationBox = { innerTextField ->
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                fontFamily = NunitoFamily,
                                fontSize = 16.sp,
                                color = DustyMauve
                            )
                        }
                        innerTextField()
                    }
                )
            }
            if (trailingIcon != null) {
                Spacer(modifier = Modifier.width(8.dp))
                trailingIcon()
            }
        }
    }
}
