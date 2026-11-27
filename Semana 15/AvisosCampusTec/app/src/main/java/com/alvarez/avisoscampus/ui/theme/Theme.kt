
package com.alvarez.avisoscampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = AzulPrincipal,
    onPrimary = Color.White,
    secondary = AzulSecundario,
    onSecondary = Color.White,
    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulPrincipal,
    background = FondoClaro,
    onBackground = TextoPrincipal,
    surface = SuperficieClara,
    onSurface = TextoPrincipal,
    onSurfaceVariant = TextoSecundario,
    outline = BordeClaro,
    outlineVariant = BordeClaro,
    error = RojoDenegado
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF90CAF9),
    onPrimary = Color(0xFF123B4A),
    secondary = Color(0xFF64B5F6),
    background = FondoOscuro,
    surface = SuperficieOscura,
    onBackground = Color(0xFFE4EDF2),
    onSurface = Color(0xFFE4EDF2),
    onSurfaceVariant = Color(0xFFB0BEC5),
    outline = Color(0xFF455A64),
    error = Color(0xFFFF8A80)
)

@Composable
fun AvisosCampusTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
