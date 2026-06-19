package com.niyuva.app.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = DeepPlumRose,
    secondary = Terracotta,
    background = WarmIvory,
    surface = WarmLinen,
    onPrimary = PureWhite,
    onBackground = DeepWarmBrown,
    onSurface = DeepWarmBrown,
    outline = BlushMist
)

@Composable
fun NiyuvaTheme(
    content: @Composable () -> Unit
) {
    androidx.compose.runtime.CompositionLocalProvider(
        androidx.compose.ui.platform.LocalDensity provides androidx.compose.ui.unit.Density(
            density = androidx.compose.ui.platform.LocalDensity.current.density,
            fontScale = 1f // Lock font scale to 1 — ignore user system setting
        )
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = NiyuvaTypography,
            content = content
        )
    }
}
