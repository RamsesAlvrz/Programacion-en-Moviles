package com.alvarez.saludplus.ui.screens.auth

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onRegistrado: () -> Unit,
    onTerminos: () -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Registro de paciente",
        acciones = listOf(
            "Ir a login (prueba)" to onRegistrado,
            "Términos y condiciones" to onTerminos,
            "Volver" to onVolver
        )
    )
}