package com.alvarez.saludplus.ui.screens.auth

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onAcceso: () -> Unit,
    onRegistro: () -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Iniciar sesión",
        acciones = listOf(
            "Entrar (prueba)" to onAcceso,
            "Crear cuenta" to onRegistro,
            "Volver" to onVolver
        )
    )
}