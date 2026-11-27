
package com.alvarez.avisoscampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = AzulPetroleo,
    onPrimary = Color.White,
    secondary = Turquesa,
    onSecondary = Color.White,
    primaryContainer = TurquesaClaro,
    onPrimaryContainer = AzulPetroleo,
    background = FondoClaro,
    onBackground = TextoPrincipal,
    surface = SuperficieClara,
    onSurface = TextoPrincipal,
    onSurfaceVariant = TextoSecundario,
    error = RojoDenegado
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF80CBC4),
    onPrimary = AzulPetroleo,
    secondary = Color(0xFF4DB6AC),
    background = FondoOscuro,
    surface = SuperficieOscura,
    onBackground = Color(0xFFE4EDF2),
    onSurface = Color(0xFFE4EDF2),
    onSurfaceVariant = Color(0xFFB0BEC5),
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
