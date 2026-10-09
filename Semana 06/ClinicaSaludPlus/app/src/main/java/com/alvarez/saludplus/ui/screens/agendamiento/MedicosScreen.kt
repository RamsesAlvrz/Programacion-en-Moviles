package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun MedicosScreen(
    especialidadId: Int,
    onVolver: () -> Unit,
    onMedico: (Int) -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Médicos · especialidad $especialidadId",
        acciones = listOf(
            "Seleccionar médico de prueba" to {
                onMedico(1)
            },
            "Volver" to onVolver
        )
    )
}