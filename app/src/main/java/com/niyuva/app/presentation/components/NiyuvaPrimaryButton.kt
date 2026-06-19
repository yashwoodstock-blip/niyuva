package com.niyuva.app.presentation.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.NunitoFamily

@Composable
fun NiyuvaPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 32.dp),
    enabled: Boolean = true
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor = when {
        !enabled -> Color(0xFFC5A8B3)
        isPressed -> Color(0xFF7A4F5C)
        else -> DeepPlumRose
    }

    val contentColor = if (enabled) {
        Color.White
    } else {
        Color.White.copy(alpha = 0.5f) // #FFFFFF80
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Color(0xFFC5A8B3),
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        interactionSource = interactionSource,
        modifier = modifier
    ) {
        Text(
            text = text,
            fontFamily = NunitoFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}
