package com.alvarez.saludplus.ui.screens.citas

import androidx.compose.runtime.Composable
import com.alvarez.saludplus.ui.components.PantallaEnConstruccion

@Composable
fun MisCitasScreen(
    onDetalle: (Int) -> Unit,
    onAgendar: () -> Unit
) {

    PantallaEnConstruccion(
        titulo = "Mis citas",
        acciones = listOf(
            "Ver detalle de prueba" to {
                onDetalle(1)
            },
            "Agendar una cita" to onAgendar
        )
    )
}