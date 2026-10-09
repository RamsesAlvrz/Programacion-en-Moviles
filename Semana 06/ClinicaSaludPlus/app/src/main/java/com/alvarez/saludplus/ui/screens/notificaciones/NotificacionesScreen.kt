package com.alvarez.saludplus.ui.screens.notificaciones

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.Encabezado
import com.alvarez.saludplus.ui.components.Tarjeta

@Composable
fun NotificacionesScreen(
    onVolver: () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val citas = usuario?.let {
        Repositorio.citasDelUsuario(it.id)
    }.orEmpty()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Encabezado(
                titulo = "Notificaciones",
                onVolver = onVolver
            )
        }

        item {
            Text(
                text = "Información de tus reservas actuales.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (usuario == null) {
            item {
                Tarjeta(
                    titulo = "Sesión no disponible",
                    detalle = "Inicia sesión para consultar tus reservas."
                )
            }
        } else if (citas.isEmpty()) {
            item {
                Tarjeta(
                    titulo = "No tienes notificaciones",
                    detalle = "Cuando reserves una cita, aparecerá aquí su información."
                )
            }
        } else {
            items(
                items = citas,
                key = { it.id }
            ) { cita ->
                val medico = Repositorio.obtenerMedico(cita.medicoId)
                val especialidad = medico?.let {
                    Repositorio.obtenerEspecialidad(it.especialidadId)
                }

                Tarjeta(
                    titulo = "Cita reservada #${cita.id}",
                    detalle = buildString {
                        append(medico?.nombre ?: "Médico no disponible")
                        append("\n")
                        append(especialidad?.nombre ?: "Especialidad no disponible")
                        append("\nFecha: ${cita.fecha}")
                        append("\nHora: ${cita.hora}")
                    }
                )
            }
        }
    }
}