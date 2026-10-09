package com.alvarez.saludplus.ui.screens.home

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun HomeScreen(
    onEspecialidades: () -> Unit,
    onEspecialidad: (Int) -> Unit,
    onCitas: () -> Unit,
    onResultados: () -> Unit,
    onNotificaciones: () -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Inicio",
        acciones = listOf(
            "Agendar cita" to onEspecialidades,
            "Especialidad de prueba" to {
                onEspecialidad(1)
            },
            "Mis citas" to onCitas,
            "Resultados" to onResultados,
            "Notificaciones" to onNotificaciones
        )
    )
}