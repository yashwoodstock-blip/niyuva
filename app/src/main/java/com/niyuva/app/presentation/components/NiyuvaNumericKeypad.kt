package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.*

@Composable
fun NiyuvaNumericKeypad(
    onDigitEntered: (Int) -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        val keys = listOf(
            listOf("1", "2", "3"),
            listOf("4", "5", "6"),
            listOf("7", "8", "9"),
            listOf("", "0", "backspace")
        )

        keys.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                row.forEach { key ->
                    if (key.isEmpty()) {
                        Spacer(modifier = Modifier.size(width = 72.dp, height = 56.dp))
                    } else {
                        val interactionSource = remember { MutableInteractionSource() }
                        val isPressed by interactionSource.collectIsPressedAsState()
                        val background = if (isPressed) BlushMist else WarmLinen

                        Box(
                            modifier = Modifier
                                .size(width = 72.dp, height = 56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(background)
                                .clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    if (key == "backspace") {
                                        onBackspace()
                                    } else {
                                        onDigitEntered(key.toInt())
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (key == "backspace") {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.Backspace,
                                    contentDescription = "Backspace",
                                    tint = DustyMauve,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = key,
                                    fontFamily = NunitoFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = DeepWarmBrown
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
