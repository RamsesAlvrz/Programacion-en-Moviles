package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.CampoTexto
import com.alvarez.saludplus.ui.components.Encabezado
import com.alvarez.saludplus.ui.components.Tarjeta
import java.util.Locale

@Composable
fun MedicosScreen(
    especialidadId: Int,
    onVolver: () -> Unit,
    onMedico: (Int) -> Unit
) {
    var busqueda by rememberSaveable(especialidadId) {
        mutableStateOf("")
    }

    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)

    val medicos = Repositorio.buscarMedicos(
        especialidadId = especialidadId,
        texto = busqueda
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Encabezado(
            titulo = "Médicos",
            onVolver = onVolver
        )

        Text(
            text = especialidad?.nombre ?: "Especialidad no encontrada",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Selecciona al médico para tu consulta.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CampoTexto(
            etiqueta = "Buscar médico",
            valor = busqueda,
            onCambio = {
                busqueda = it
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (medicos.isEmpty()) {
                item {
                    Text(
                        text = "No se encontraron médicos.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(
                items = medicos,
                key = { medico ->
                    medico.id
                }
            ) { medico ->

                val precioFormateado = String.format(
                    Locale.US,
                    "%.2f",
                    medico.precio
                )

                Tarjeta(
                    titulo = medico.nombre,
                    detalle = "${medico.experiencia} años de experiencia\nConsulta: S/ $precioFormateado",
                    onClick = {
                        onMedico(medico.id)
                    }
                )
            }
        }
    }
}