package com.alvarez.saludplus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ColoresSaludPlus = lightColorScheme(
    primary = AzulClinica,
    onPrimary = Blanco,
    primaryContainer = CelesteClaro,
    onPrimaryContainer = TextoOscuro,
    background = FondoClaro,
    onBackground = TextoOscuro,
    surface = Blanco,
    onSurface = TextoOscuro,
    onSurfaceVariant = GrisTexto
)

@Composable
fun SaludPlusTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ColoresSaludPlus,
        typography = SaludPlusTypography,
        content = content
    )
}