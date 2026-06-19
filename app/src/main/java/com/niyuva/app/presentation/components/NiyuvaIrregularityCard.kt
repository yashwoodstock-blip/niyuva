package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.Terracotta
import com.niyuva.app.presentation.theme.WarmLinen

@Composable
fun NiyuvaIrregularityCard(
    userName: String,
    onLearnMore: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayName = if (userName.isNotBlank()) " $userName" else ""
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(WarmLinen)
            .drawBehind {
                drawRect(
                    color = Terracotta,
                    topLeft = Offset(0f, 0f),
                    size = Size(4.dp.toPx(), size.height)
                )
            }
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp) // Space for the close button
        ) {
            Text(
                text = "Teri cycle thodi irregular lag rahi hai,$displayName. Koi baat nahi — kabhi kabhi aisa hota hai stress ya khaane ki wajah se. Par ek baar iss baare mein padh lena acha hoga 💛",
                fontFamily = NunitoFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                color = DeepWarmBrown,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            NiyuvaTextLink(
                text = "Padh lo →",
                onClick = onLearnMore,
                modifier = Modifier.align(Alignment.End)
            )
        }

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Dismiss",
                tint = DeepWarmBrown
            )
        }
    }
}
