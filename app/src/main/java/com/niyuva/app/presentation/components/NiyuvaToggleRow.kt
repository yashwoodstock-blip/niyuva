package com.niyuva.app.presentation.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.*

@Composable
fun NiyuvaToggleRow(
    icon: ImageVector,
    label: String,
    subLabel: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                ambientColor = DeepPlumRose.copy(alpha = 0.4f),
                spotColor = DeepPlumRose.copy(alpha = 0.4f)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(WarmLinen, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DeepPlumRose,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = label,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = DeepWarmBrown
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subLabel,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = DustyMauve
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            NiyuvaSwitch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
fun NiyuvaSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val trackWidth = 48.dp
    val trackHeight = 28.dp
    val thumbSize = 22.dp
    val padding = 3.dp

    val trackColor = if (checked) DeepPlumRose else BlushMist
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) trackWidth - thumbSize - padding else padding,
        animationSpec = tween(durationMillis = 200),
        label = "ThumbOffset"
    )

    Box(
        modifier = modifier
            .size(width = trackWidth, height = trackHeight)
            .background(trackColor, CircleShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onCheckedChange(!checked)
            }
            .padding(vertical = padding),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(thumbSize)
                .background(Color.White, CircleShape)
                .shadow(elevation = 1.dp, shape = CircleShape)
        )
    }
}
