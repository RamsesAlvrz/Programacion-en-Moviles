package com.alvarez.murocampus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColoresMuroCampus = lightColorScheme(
    primary = AzulPrincipal,
    onPrimary = Blanco,
    secondary = AzulOscuro,
    secondaryContainer = AzulClaro,
    background = Fondo,
    onBackground = TextoPrincipal,
    surface = Blanco,
    onSurface = TextoPrincipal,
    onSurfaceVariant = TextoSecundario,
    error = RojoError,
    errorContainer = FondoError
)

@Composable
fun MuroCampusTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColoresMuroCampus,
        typography = Typography,
        content = content
    )
}