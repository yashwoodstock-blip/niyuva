package com.niyuva.app.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.NunitoFamily

@Composable
fun NiyuvaGhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 32.dp),
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(50.dp),
        border = BorderStroke(1.5.dp, DeepPlumRose),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.Transparent,
            contentColor = DeepPlumRose,
            disabledContentColor = DeepPlumRose.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp
        ),
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
