package com.alvarez.saludplus.ui.screens.perfil

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun PerfilScreen(
    onCerrarSesion: () -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Mi perfil",
        acciones = listOf(
            "Cerrar sesión (prueba)" to onCerrarSesion
        )
    )
}