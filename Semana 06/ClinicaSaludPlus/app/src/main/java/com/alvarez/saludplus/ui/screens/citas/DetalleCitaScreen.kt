package com.alvarez.saludplus.ui.screens.citas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.BotonPrimario
import com.alvarez.saludplus.ui.components.DatosConsulta
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
                text = "La cita no existe o no pertenece a tu sesión."
            )
        } else {
            Tarjeta(
                titulo = "Reserva #${cita.id}",
                detalle = "Cita médica reservada"
            )

            Tarjeta(
                titulo = medico?.nombre ?: "Médico no disponible",
                detalle = especialidad?.nombre.orEmpty()
            )

            Tarjeta(
                titulo = "Fecha y hora",
                detalle = "${DatosConsulta.fechaEnEspanol(cita.fecha)}\n${cita.hora}"
            )

            Tarjeta(
                titulo = DatosConsulta.TIPO_ATENCION,
                detalle = DatosConsulta.DIRECCION
            )

            Tarjeta(
                titulo = "Motivo de consulta",
                detalle = cita.motivoConsulta.ifBlank {
                    "No especificado"
                }
            )

            medico?.let {
                Tarjeta(
                    titulo = "Costo de consulta",
                    detalle = "S/ ${
                        String.format(Locale.US, "%.2f", it.precio)
                    }"
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
                Text("¿Cancelar cita?")
            },
            text = {
                Text(
                    "El horario volverá a quedar disponible para este médico y fecha."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false

                        if (Repositorio.cancelarCita(citaId)) {
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