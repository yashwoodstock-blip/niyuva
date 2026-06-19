package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.Terracotta
import com.niyuva.app.presentation.theme.WarmLinen

@Composable
fun NiyuvaErrorCard(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(WarmLinen)
            .drawBehind {
                val strokeWidth = 4.dp.toPx()
                drawLine(
                    color = Terracotta,
                    start = Offset(strokeWidth / 2, 0f),
                    end = Offset(strokeWidth / 2, size.height),
                    strokeWidth = strokeWidth
                )
            }
            .padding(start = 20.dp, end = 16.dp, top = 16.dp, bottom = 16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = message,
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = DeepWarmBrown
            )
            if (onRetry != null) {
                Spacer(modifier = Modifier.height(10.dp))
                NiyuvaTextLink(
                    text = "Phir try karo 🔄",
                    onClick = onRetry
                )
            }
        }
    }
}
