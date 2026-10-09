package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.BotonPrimario
import com.alvarez.saludplus.ui.components.Encabezado

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onVolver: () -> Unit,
    onContinuar: (String, String) -> Unit
) {
    // Fase 1: fechas fijas.
    val dias = listOf(
        "2026-10-12" to "Lun 12",
        "2026-10-13" to "Mar 13",
        "2026-10-14" to "Mié 14",
        "2026-10-15" to "Jue 15",
        "2026-10-16" to "Vie 16"
    )

    var fechaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf("")
    }

    var horaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf("")
    }

    val medico = Repositorio.obtenerMedico(medicoId)

    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    val horariosDisponibles = Repositorio.horariosDisponibles(
        medicoId = medicoId,
        fecha = fechaSeleccionada
    )

    // Limpiar la selección si esa hora deja de estar disponible.
    LaunchedEffect(fechaSeleccionada, horariosDisponibles) {
        if (
            horaSeleccionada.isNotBlank() &&
            horaSeleccionada !in horariosDisponibles
        ) {
            horaSeleccionada = ""
        }
    }

    val puedeContinuar =
        medico != null &&
                fechaSeleccionada.isNotBlank() &&
                horaSeleccionada.isNotBlank() &&
                horaSeleccionada in horariosDisponibles

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Encabezado(
            titulo = "Fecha y hora",
            onVolver = onVolver
        )

        Text(
            text = medico?.nombre ?: "Médico no encontrado",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = especialidad?.nombre.orEmpty(),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Octubre 2026",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Selecciona un día"
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = dias,
                key = { dia ->
                    dia.first
                }
            ) { dia ->

                val fecha = dia.first
                val etiqueta = dia.second

                FilterChip(
                    selected = fechaSeleccionada == fecha,
                    onClick = {
                        if (fechaSeleccionada != fecha) {
                            fechaSeleccionada = fecha
                            horaSeleccionada = ""
                        }
                    },
                    label = {
                        Text(etiqueta)
                    }
                )
            }
        }

        Text(
            text = "Horarios disponibles",
            style = MaterialTheme.typography.titleMedium
        )

        when {
            medico == null -> {
                Text(
                    text = "Vuelve y selecciona un médico válido.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            fechaSeleccionada.isBlank() -> {
                Text(
                    text = "Selecciona un día para ver sus horarios.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            horariosDisponibles.isEmpty() -> {
                Text(
                    text = "No hay horarios disponibles para este día.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = horariosDisponibles,
                key = { hora ->
                    hora
                }
            ) { hora ->

                FilterChip(
                    selected = horaSeleccionada == hora,
                    onClick = {
                        horaSeleccionada = hora
                    },
                    label = {
                        Text(hora)
                    }
                )
            }
        }

        if (horaSeleccionada.isNotBlank()) {
            Text(
                text = "Selección: $fechaSeleccionada a las $horaSeleccionada",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        BotonPrimario(
            texto = "Continuar",
            onClick = {
                onContinuar(
                    fechaSeleccionada,
                    horaSeleccionada
                )
            },
            habilitado = puedeContinuar
        )
    }
}