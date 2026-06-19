package com.niyuva.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.domain.model.StoryCard
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite
import com.niyuva.app.presentation.theme.Terracotta
import com.niyuva.app.presentation.theme.WarmLinen

@Composable
fun StoryCardView(
    story: StoryCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = PureWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .size(width = 160.dp, height = 200.dp)
            .clickable(onClick = onClick)
            .semantics { contentDescription = "Story: ${story.title}" }
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top 120dp: WarmLinen placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(WarmLinen),
                    contentAlignment = Alignment.Center
                ) {
                    // TODO: Replace with actual story image
                    Text(text = "📖", fontSize = 36.sp)
                }

                // Bottom 80dp: Content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(8.dp)
                ) {
                    // Category pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Terracotta)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = story.category,
                            fontFamily = NunitoFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = PureWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = story.title,
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = DeepWarmBrown,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 16.sp
                    )
                }
            }

            // NEW badge top-right
            if (story.isNew) {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .height(20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Terracotta)
                        .padding(horizontal = 8.dp)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "NEW",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 9.sp,
                        color = PureWhite
                    )
                }
            }
        }
    }
}
