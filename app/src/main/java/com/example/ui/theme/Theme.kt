package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TokyoGhoulDarkColorScheme = darkColorScheme(
    primary = KaguneCrimson,
    onPrimary = Color.White,
    primaryContainer = KaguneDarkRed,
    onPrimaryContainer = Color.White,
    secondary = QuinqueSilver,
    onSecondary = GhoulDarkBg,
    secondaryContainer = GhoulSurfaceElevated,
    onSecondaryContainer = QuinqueSilver,
    tertiary = QuinqueAccent,
    onTertiary = GhoulDarkBg,
    background = GhoulDarkBg,
    onBackground = GhoulTextPrimary,
    surface = GhoulSurface,
    onSurface = GhoulTextPrimary,
    surfaceVariant = GhoulSurfaceVariant ?: GhoulSurfaceElevated,
    onSurfaceVariant = GhoulTextSecondary,
    outline = GhoulBorder,
    error = KaguneAwakeningGlow,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = TokyoGhoulDarkColorScheme,
        typography = Typography,
        content = content
    )
}
