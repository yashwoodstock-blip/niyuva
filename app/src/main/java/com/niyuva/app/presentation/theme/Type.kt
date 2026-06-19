package com.niyuva.app.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.niyuva.app.R

// Google Font Provider
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

// Nunito Font Family
val NunitoFont = GoogleFont("Nunito")

val NunitoFamily = FontFamily(
    Font(googleFont = NunitoFont, fontProvider = provider, weight = FontWeight.Normal),
    Font(googleFont = NunitoFont, fontProvider = provider, weight = FontWeight.SemiBold),
    Font(googleFont = NunitoFont, fontProvider = provider, weight = FontWeight.Bold),
    Font(googleFont = NunitoFont, fontProvider = provider, weight = FontWeight.ExtraBold)
)

// Playfair Display Font Family
val PlayfairFont = GoogleFont("Playfair Display")

val PlayfairFamily = FontFamily(
    Font(
        googleFont = PlayfairFont,
        fontProvider = provider,
        weight = FontWeight.Bold,
        style = FontStyle.Italic
    )
)

val NiyuvaTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineLarge = TextStyle(       // H1 — screen titles
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = (-0.3).sp
    ),
    headlineMedium = TextStyle(      // H2 — card titles
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 30.sp,
        letterSpacing = (-0.2).sp
    ),
    headlineSmall = TextStyle(       // H3 — section headers
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 26.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(         // Caption
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp,
        letterSpacing = 0.2.sp
    ),
    labelSmall = TextStyle(          // Micro
        fontFamily = NunitoFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.3.sp
    )
)
