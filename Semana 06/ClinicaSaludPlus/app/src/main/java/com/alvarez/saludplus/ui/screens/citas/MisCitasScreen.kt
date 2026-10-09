package com.alvarez.saludplus.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.BotonPrimario
import com.alvarez.saludplus.ui.components.Encabezado
import com.alvarez.saludplus.ui.components.Tarjeta

@Composable
fun MisCitasScreen(
    onDetalle: (Int) -> Unit,
    onAgendar: () -> Unit
) {
    val usuario = Repositorio.usuarioActual

    val citas = usuario?.let {
        Repositorio.citasDelUsuario(it.id)
    }.orEmpty()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Encabezado(
                titulo = "Mis citas"
            )
        }

        if (usuario == null) {
            item {
                Text(
                    text = "No hay una sesión activa.",
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else if (citas.isEmpty()) {
            item {
                Tarjeta(
                    titulo = "Todavía no tienes citas",
                    detalle = "Reserva tu primera consulta médica."
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                BotonPrimario(
                    texto = "Agendar cita",
                    onClick = onAgendar
                )
            }
        } else {
            item {
                Text(
                    text = "${citas.size} cita(s) reservada(s)",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            items(
                items = citas,
                key = { cita ->
                    cita.id
                }
            ) { cita ->

                val medico = Repositorio.obtenerMedico(cita.medicoId)

                val especialidad = medico?.let {
                    Repositorio.obtenerEspecialidad(it.especialidadId)
                }

                Tarjeta(
                    titulo = medico?.nombre ?: "Médico no encontrado",
                    detalle = "${especialidad?.nombre.orEmpty()}\n${cita.fecha} a las ${cita.hora}\nReserva #${cita.id}",
                    onClick = {
                        onDetalle(cita.id)
                    }
                )
            }

            item {
                BotonPrimario(
                    texto = "Agendar otra cita",
                    onClick = onAgendar
                )
            }
        }
    }
}