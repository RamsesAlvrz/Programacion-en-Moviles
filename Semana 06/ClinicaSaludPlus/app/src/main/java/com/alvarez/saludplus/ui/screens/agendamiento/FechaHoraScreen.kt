package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onVolver: () -> Unit,
    onContinuar: (String, String) -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Fecha y hora · médico $medicoId",
        acciones = listOf(
            "Continuar con fecha y hora de prueba" to {
                onContinuar(
                    "2026-10-12",
                    "08:00"
                )
            },
            "Volver" to onVolver
        )
    )
}