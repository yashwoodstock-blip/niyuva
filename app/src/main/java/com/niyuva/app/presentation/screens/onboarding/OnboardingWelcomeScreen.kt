package com.niyuva.app.presentation.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.R
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.theme.*

@Composable
fun OnboardingWelcomeScreen(
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        // Top 55% - Illustration area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.55f)
                .clip(
                    RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 24.dp,
                        bottomEnd = 24.dp
                    )
                )
                .background(WarmLinen),
            contentAlignment = Alignment.Center
        ) {
            // TODO: Replace this with final Style A editorial illustration from owner
            Image(
                painter = painterResource(id = R.drawable.img_onboarding_welcome),
                contentDescription = "Welcome illustration",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Bottom 45% - Content area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.45f)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Meet Niyuva",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 28.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Subtitle
                Text(
                    text = "Teri body ki puri samajh — bilkul free, bilkul private.",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = DustyMauve,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                // Pagination Indicator
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(8.dp)
                                .background(
                                    color = if (index == 0) DeepPlumRose else BlushMist,
                                    shape = CircleShape
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CTA Button
                NiyuvaPrimaryButton(
                    text = "Chalo shuru karte hain 🌸",
                    onClick = onContinue
                )
            }
        }
    }
}
