package com.alvarez.saludplus.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.BotonPrimario
import com.alvarez.saludplus.ui.components.Encabezado
import com.alvarez.saludplus.ui.components.Tarjeta
import java.util.Locale

@Composable
fun DetalleCitaScreen(
    citaId: Int,
    onVolver: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)

    val medico = cita?.let {
        Repositorio.obtenerMedico(it.medicoId)
    }

    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    var mostrarConfirmacion by rememberSaveable(citaId) {
        mutableStateOf(false)
    }

    var error by rememberSaveable(citaId) {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Encabezado(
            titulo = "Detalle de cita",
            onVolver = onVolver
        )

        if (cita == null) {
            Text(
                text = "La cita no existe o no pertenece a tu sesión.",
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Tarjeta(
                titulo = "Reserva #${cita.id}",
                detalle = "Cita registrada"
            )

            Tarjeta(
                titulo = medico?.nombre ?: "Médico no encontrado",
                detalle = especialidad?.nombre.orEmpty()
            )

            Tarjeta(
                titulo = "Fecha y hora",
                detalle = "${cita.fecha} a las ${cita.hora}"
            )

            if (medico != null) {
                val precio = String.format(
                    Locale.US,
                    "%.2f",
                    medico.precio
                )

                Tarjeta(
                    titulo = "Costo de consulta",
                    detalle = "S/ $precio"
                )
            }

            if (error.isNotBlank()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            BotonPrimario(
                texto = "Cancelar cita",
                onClick = {
                    mostrarConfirmacion = true
                }
            )
        }
    }

    if (mostrarConfirmacion && cita != null) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacion = false
            },
            title = {
                Text("¿Cancelar esta cita?")
            },
            text = {
                Text("La reserva se eliminará y el horario volverá a estar disponible.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false

                        val cancelada = Repositorio.cancelarCita(citaId)

                        if (cancelada) {
                            onVolver()
                        } else {
                            error = "No se pudo cancelar la cita."
                        }
                    }
                ) {
                    Text("Sí, cancelar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                    }
                ) {
                    Text("Conservar cita")
                }
            }
        )
    }
}