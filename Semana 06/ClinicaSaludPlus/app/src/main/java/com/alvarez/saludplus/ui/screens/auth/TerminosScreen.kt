package com.alvarez.saludplus.ui.screens.auth

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun TerminosScreen(
    onVolver: () -> Unit
) {
    PantallaEnConstruccion(
        titulo = "Términos y condiciones",
        acciones = listOf(
            "Volver" to onVolver
        )
    )
}