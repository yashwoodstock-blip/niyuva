package com.niyuva.app.presentation.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.domain.model.VedicCard
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.WarmLinen

@Composable
fun VedicCardView(
    card: VedicCard,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val goldColor = Color(0xFFC9A85C)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = WarmLinen),
        modifier = modifier
            .size(width = 200.dp, height = 140.dp)
            .clickable {
                Toast.makeText(context, "Coming soon — owner is preparing Vedic content 💛", Toast.LENGTH_SHORT).show()
            }
            .semantics { contentDescription = "Vedic: ${card.chapterTitle}" }
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            // Gold left border: 4dp wide
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(goldColor)
            )

            // Left area: Lotus emoji and chapter number
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(64.dp)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "🪷", fontSize = 32.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ch ${card.chapterNumber}",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = DeepWarmBrown
                )
            }

            // Right area: Chapter details
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(vertical = 16.dp, horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = card.chapterTitle,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DeepWarmBrown,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = card.firstLine,
                    fontFamily = NunitoFamily,
                    fontSize = 12.sp,
                    color = DustyMauve,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }
        }
    }
}
