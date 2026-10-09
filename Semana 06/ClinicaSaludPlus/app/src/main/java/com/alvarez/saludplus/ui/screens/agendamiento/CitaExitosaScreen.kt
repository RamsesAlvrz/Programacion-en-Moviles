package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onInicio: () -> Unit,
    onMisCitas: () -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Cita agendada · reserva $citaId",
        acciones = listOf(
            "Volver al inicio" to onInicio,
            "Ver mis citas" to onMisCitas
        )
    )
}