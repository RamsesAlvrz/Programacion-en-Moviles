package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun EspecialidadesScreen(
    onVolver: () -> Unit,
    onEspecialidad: (Int) -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Especialidades",
        acciones = listOf(
            "Seleccionar especialidad de prueba" to {
                onEspecialidad(1)
            },
            "Volver" to onVolver
        )
    )
}