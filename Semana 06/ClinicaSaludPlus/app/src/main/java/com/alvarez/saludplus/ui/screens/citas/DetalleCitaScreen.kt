package com.alvarez.saludplus.ui.screens.citas

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onVolver: () -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Detalle de cita · reserva $citaId",
        acciones = listOf(
            "Volver" to onVolver
        )
    )
}