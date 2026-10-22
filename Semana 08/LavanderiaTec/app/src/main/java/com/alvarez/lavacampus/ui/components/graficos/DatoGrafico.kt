
package com.alvarez.lavacampus.ui.components.graficos

import androidx.compose.ui.graphics.Color

// Representa un elemento de los gráficos del panel.
data class DatoGrafico(
    val etiqueta: String,
    val valor: Double,
    val color: Color
)
