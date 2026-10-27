package com.alvarez.lavacampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// =====================================
// COLORES DEL TEMA CLARO
// =====================================

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0895B5),
    onPrimary = Color.White,

    secondary = Color(0xFF6D28D9),
    onSecondary = Color.White,

    background = Color(0xFFF7FAFC),
    onBackground = Color(0xFF202633),

    surface = Color.White,
    onSurface = Color(0xFF202633),

    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF64748B),

    outline = Color(0xFFE2E8F0),

    error = Color(0xFFDC2626)
)

// =====================================
// COLORES DEL TEMA OSCURO
// =====================================

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF22B8CF),
    onPrimary = Color(0xFF062C36),

    secondary = Color(0xFFA78BFA),
    onSecondary = Color(0xFF24104D),

    background = Color(0xFF111827),
    onBackground = Color(0xFFF1F5F9),

    surface = Color(0xFF1F2937),
    onSurface = Color(0xFFF1F5F9),

    surfaceVariant = Color(0xFF374151),
    onSurfaceVariant = Color(0xFFCBD5E1),

    outline = Color(0xFF4B5563),

    error = Color(0xFFF87171)
)

// =====================================
// TEMA PRINCIPAL DE LAVACAMPUS
// =====================================

@Composable
fun LavaCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}