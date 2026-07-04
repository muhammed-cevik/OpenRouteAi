package com.tdev.openrai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val ColorScheme = darkColorScheme(
    primary          = Accent,
    onPrimary        = Bg,
    background       = Bg,
    onBackground     = TextPrimary,
    surface          = Surface,
    onSurface        = TextPrimary,
    surfaceVariant   = CardBg,
    onSurfaceVariant = TextSecondary,
    outline          = Divider,
)

@Composable
fun OpenRouteAiTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ColorScheme, content = content)
}
