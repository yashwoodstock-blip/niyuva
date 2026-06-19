package com.niyuva.app.presentation.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.domain.model.AnimationCard
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite

@Composable
fun AnimationCardView(
    animation: AnimationCard,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val minutes = animation.durationSeconds / 60
    val seconds = animation.durationSeconds % 60
    val durationText = String.format("%d:%02d", minutes, seconds)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DeepWarmBrown),
        modifier = modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(16f / 9f)
            .clickable {
                Toast.makeText(context, "Animation player coming soon 💛", Toast.LENGTH_SHORT).show()
            }
            .semantics { contentDescription = "Animation: ${animation.title}" }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // TODO: Replace dark background with actual video thumbnail image

            // Play button overlay: 48dp circle White copy(alpha = 0.9f)
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(PureWhite.copy(alpha = 0.9f))
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = "Play",
                    tint = DeepPlumRose,
                    modifier = Modifier.size(32.dp)
                )
            }

            // Bottom Text Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    ))
                    .padding(12.dp)
            ) {
                Text(
                    text = animation.title,
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = PureWhite
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = durationText,
                    fontFamily = NunitoFamily,
                    fontSize = 12.sp,
                    color = PureWhite.copy(alpha = 0.8f)
                )
            }
        }
    }
}
