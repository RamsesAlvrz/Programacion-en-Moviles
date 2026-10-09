package com.alvarez.saludplus.ui.screens.auth

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun SplashScreen(
    onRegistro: () -> Unit,
    onLogin: () -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Bienvenida",
        acciones = listOf(
            "Registrarme" to onRegistro,
            "Iniciar sesión" to onLogin
        )
    )
}