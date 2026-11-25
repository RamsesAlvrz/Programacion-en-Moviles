
package com.alvarez.murocampus.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val ColoresMuroCampus = lightColorScheme(

    primary = AzulPrincipal,
    onPrimary = Blanco,

    primaryContainer = AzulClaro,
    onPrimaryContainer = AzulOscuro,

    secondary = AzulOscuro,
    onSecondary = Blanco,

    secondaryContainer = AzulClaro,
    onSecondaryContainer = AzulOscuro,

    tertiary = VerdeExito,
    onTertiary = Blanco,

    tertiaryContainer = FondoExito,
    onTertiaryContainer = VerdeExito,

    background = Fondo,
    onBackground = TextoPrincipal,

    surface = Blanco,
    onSurface = TextoPrincipal,

    surfaceVariant = SuperficieSuave,
    onSurfaceVariant = TextoSecundario,

    outline = TextoSecundario,
    outlineVariant = BordeSuave,

    error = RojoError,
    onError = Blanco,

    errorContainer = FondoError,
    onErrorContainer = RojoError
)

private val FormasMuroCampus = Shapes(

    extraSmall = RoundedCornerShape(6.dp),

    small = RoundedCornerShape(10.dp),

    medium = RoundedCornerShape(14.dp),

    large = RoundedCornerShape(20.dp),

    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun MuroCampusTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = ColoresMuroCampus,
        typography = Typography,
        shapes = FormasMuroCampus,
        content = content
    )
}
