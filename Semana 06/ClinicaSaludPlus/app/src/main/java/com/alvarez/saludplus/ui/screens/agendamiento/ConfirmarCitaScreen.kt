package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onVolver: () -> Unit,
    onConfirmada: (Int) -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Confirmar · médico $medicoId · $fecha · $hora",
        acciones = listOf(
            "Confirmar (prueba)" to {
                onConfirmada(1)
            },
            "Volver" to onVolver
        )
    )
}