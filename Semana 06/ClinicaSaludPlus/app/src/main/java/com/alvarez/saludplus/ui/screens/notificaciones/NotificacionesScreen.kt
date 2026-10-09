package com.alvarez.saludplus.ui.screens.notificaciones

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun NotificacionesScreen(
    onVolver: () -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Notificaciones",
        acciones = listOf(
            "Volver" to onVolver
        )
    )
}