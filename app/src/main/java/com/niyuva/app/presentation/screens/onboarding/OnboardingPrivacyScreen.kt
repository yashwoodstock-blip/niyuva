package com.niyuva.app.presentation.screens.onboarding

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.niyuva.app.R
import com.niyuva.app.presentation.components.NiyuvaCheckboxRow
import com.niyuva.app.presentation.components.NiyuvaPrimaryButton
import com.niyuva.app.presentation.components.NiyuvaTextLink
import com.niyuva.app.presentation.theme.*

@Composable
fun OnboardingPrivacyScreen(
    onBack: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var storeDataChecked by remember { mutableStateOf(false) }
    var privacyPolicyChecked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        // Top 50% - Illustration area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.50f)
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
            // TODO: Replace with Style A privacy illustration (shield + locks)
            Image(
                painter = painterResource(id = R.drawable.img_onboarding_privacy),
                contentDescription = "Privacy illustration",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Back button overlaid on top-left of the illustration box
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = PureWhite.copy(alpha = 0.6f)
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DeepWarmBrown
                    )
                }
            }
        }

        // Bottom 50% - Content & Input area
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.50f)
                .padding(top = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = "Tera data, sirf tera.",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,
                    color = DeepWarmBrown,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                // Subtitle
                Text(
                    text = "Jo bhi tum yahan share karogi, woh sirf is phone mein rehta hai. Koi server nahi. Koi company nahi.",
                    fontFamily = NunitoFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = DustyMauve,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Checkbox Row 1
                NiyuvaCheckboxRow(
                    checked = storeDataChecked,
                    onCheckedChange = { storeDataChecked = it }
                ) {
                    Text(
                        text = "Mujhe apni health data yahan store karni hai.",
                        fontFamily = NunitoFamily,
                        fontSize = 14.sp,
                        color = DeepWarmBrown
                    )
                }

                // Checkbox Row 2
                NiyuvaCheckboxRow(
                    checked = privacyPolicyChecked,
                    onCheckedChange = { privacyPolicyChecked = it }
                ) {
                    val annotatedString = buildAnnotatedString {
                        append("Maine ")
                        pushStringAnnotation(tag = "URL", annotation = "https://niyuva.app/privacy")
                        withStyle(style = SpanStyle(color = Terracotta, fontWeight = FontWeight.SemiBold)) {
                            append("Privacy Policy")
                        }
                        pop()
                        append(" padh li hai.")
                    }
                    ClickableText(
                        text = annotatedString,
                        style = TextStyle(
                            fontFamily = NunitoFamily,
                            fontSize = 14.sp,
                            color = DeepWarmBrown
                        ),
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(tag = "URL", start = offset, end = offset)
                                .firstOrNull()?.let { annotation ->
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                                    context.startActivity(intent)
                                }
                        }
                    )
                }

                // Accept All Link
                NiyuvaTextLink(
                    text = "Sab Accept karo",
                    onClick = {
                        storeDataChecked = true
                        privacyPolicyChecked = true
                    }
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                // Pagination Indicator (Second dot active)
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
                                    color = if (index == 1) DeepPlumRose else BlushMist,
                                    shape = CircleShape
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Next CTA Button
                NiyuvaPrimaryButton(
                    text = "Next",
                    onClick = onContinue,
                    enabled = storeDataChecked && privacyPolicyChecked
                )
            }
        }
    }
}
