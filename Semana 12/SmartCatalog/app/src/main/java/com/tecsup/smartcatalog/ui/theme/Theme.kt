
package com.tecsup.smartcatalog.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = AzulPrincipal,
    onPrimary = Blanco,

    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuro,

    secondary = AzulOscuro,
    onSecondary = Blanco,

    tertiary = AmarilloFavorito,
    onTertiary = TextoPrincipal,

    background = FondoClaro,
    onBackground = TextoPrincipal,

    surface = Blanco,
    onSurface = TextoPrincipal,

    surfaceVariant = AzulClaro,
    onSurfaceVariant = TextoSecundario,

    error = RojoError,
    onError = Blanco
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF93C5FD),
    onPrimary = AzulOscuro,

    primaryContainer = AzulOscuro,
    onPrimaryContainer = Blanco,

    secondary = Color(0xFFBFDBFE),
    onSecondary = AzulOscuro,

    tertiary = AmarilloFavorito,

    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),

    surface = SuperficieOscura,
    onSurface = Color(0xFFF8FAFC),

    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),

    error = Color(0xFFFCA5A5)
)

@Composable
fun SmartCatalogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
