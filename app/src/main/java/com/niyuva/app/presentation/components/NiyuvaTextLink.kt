package com.niyuva.app.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.Terracotta

@Composable
fun NiyuvaTextLink(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textColor: Color = Terracotta
) {
    Box(
        modifier = modifier
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(4.dp))
            .clickable(
                onClick = onClick
            )
            .padding(8.dp)
    ) {
        Text(
            text = text,
            fontFamily = NunitoFamily,
            fontSize = 14.sp,
            color = textColor
        )
    }
}
