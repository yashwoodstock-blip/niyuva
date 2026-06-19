package com.niyuva.app.presentation.screens.saarthi

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.domain.model.ChatMessage
import com.niyuva.app.domain.model.ChatRole
import com.niyuva.app.presentation.theme.DeepPlumRose
import com.niyuva.app.presentation.theme.DeepWarmBrown
import com.niyuva.app.presentation.theme.DustyMauve
import com.niyuva.app.presentation.theme.NunitoFamily
import com.niyuva.app.presentation.theme.PureWhite
import com.niyuva.app.presentation.theme.WarmLinen
import java.time.format.DateTimeFormatter

private val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

@Composable
fun ChatMessageItem(
    message: ChatMessage,
    isFirst: Boolean,
    isLast: Boolean,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        modifier = modifier
    ) {
        if (message.role == ChatRole.USER) {
            UserMessageBubble(message = message, isLast = isLast)
        } else {
            SaarthiMessageBubble(message = message, isFirst = isFirst, isLast = isLast)
        }
    }
}

@Composable
private fun UserMessageBubble(message: ChatMessage, isLast: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.75f),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 18.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 4.dp
                        )
                    )
                    .background(DeepPlumRose)
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.message,
                    fontFamily = NunitoFamily,
                    fontSize = 14.sp,
                    color = PureWhite
                )
            }
        }
        if (isLast) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = message.timestamp.format(timeFormatter),
                fontFamily = NunitoFamily,
                fontSize = 10.sp,
                color = DustyMauve
            )
        }
    }
}

@Composable
private fun SaarthiMessageBubble(message: ChatMessage, isFirst: Boolean, isLast: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(0.75f),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            if (isFirst) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(WarmLinen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "S",
                        fontFamily = NunitoFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = DeepPlumRose
                    )
                }
            } else {
                Spacer(modifier = Modifier.width(28.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Card(
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 18.dp,
                    bottomStart = 18.dp,
                    bottomEnd = 18.dp
                ),
                colors = CardDefaults.cardColors(containerColor = PureWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = message.message,
                        fontFamily = NunitoFamily,
                        fontSize = 14.sp,
                        color = DeepWarmBrown
                    )
                }
            }
        }
        if (isLast) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = message.timestamp.format(timeFormatter),
                fontFamily = NunitoFamily,
                fontSize = 10.sp,
                color = DustyMauve,
                modifier = Modifier.padding(start = 36.dp)
            )
        }
    }
}
