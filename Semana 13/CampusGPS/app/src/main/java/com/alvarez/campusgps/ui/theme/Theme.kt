package com.alvarez.campusgps.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Emerald,
    onPrimary = Color.White,
    primaryContainer = SelectedBackground,
    onPrimaryContainer = EmeraldDark,
    secondary = EmeraldLight,
    onSecondary = EmeraldDark,
    background = AppBackground,
    onBackground = AppText,
    surface = AppSurface,
    onSurface = AppText,
    surfaceVariant = SelectedBackground,
    onSurfaceVariant = AppTextSecondary,
    outline = CardBorder,
    error = ErrorText,
    errorContainer = ErrorBackground
)

private val DarkColors = darkColorScheme(
    primary = EmeraldLight,
    onPrimary = EmeraldDark,
    primaryContainer = Emerald,
    onPrimaryContainer = Color.White,
    secondary = EmeraldLight,
    background = Color(0xFF101C1B),
    onBackground = Color(0xFFEAF3F0),
    surface = Color(0xFF1B2B29),
    onSurface = Color(0xFFEAF3F0),
    surfaceVariant = Color(0xFF29413D),
    onSurfaceVariant = Color(0xFFB4CAC5),
    outline = Color(0xFF48645E)
)

@Composable
fun CampusGPSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Se mantiene una identidad de marca consistente.
    // Los colores dinamicos no reemplazan la paleta.
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
