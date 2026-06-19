package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.WarmLinen

@Composable
fun NiyuvaTooltipCard(
    visible: Boolean,
    text: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (visible) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onDismiss
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WarmLinen),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {} // Consume click inside the card
                    )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = text,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = DeepWarmBrown,
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
