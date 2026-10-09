package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.alvarez.saludplus.ui.components.BotonPrimario
import com.alvarez.saludplus.ui.components.Encabezado
import com.alvarez.saludplus.ui.components.Tarjeta
import java.util.Locale

@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onVolver: () -> Unit,
    onConfirmada: (Int) -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val medico = Repositorio.obtenerMedico(medicoId)

    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    var error by rememberSaveable(medicoId, fecha, hora) {
        mutableStateOf("")
    }

    var enviada by rememberSaveable(medicoId, fecha, hora) {
        mutableStateOf(false)
    }

    val precioFormateado = String.format(
        Locale.US,
        "%.2f",
        medico?.precio ?: 0.0
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Encabezado(
            titulo = "Confirmar cita",
            onVolver = onVolver
        )

        Text(
            text = "Revisa los datos antes de reservar.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Tarjeta(
            titulo = "Paciente",
            detalle = if (usuario != null) {
                "${usuario.nombre} ${usuario.apellido}"
            } else {
                "No hay una sesión activa."
            }
        )

        Tarjeta(
            titulo = medico?.nombre ?: "Médico no encontrado",
            detalle = especialidad?.nombre.orEmpty()
        )

        Tarjeta(
            titulo = "Fecha y hora",
            detalle = "$fecha a las $hora"
        )

        Tarjeta(
            titulo = "Costo de consulta",
            detalle = "S/ $precioFormateado"
        )

        if (error.isNotBlank()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        BotonPrimario(
            texto = "Confirmar cita",
            onClick = {
                // Evitar procesar dos veces la misma confirmación.
                if (!enviada) {
                    enviada = true
                    error = ""

                    val cita = Repositorio.agendarCita(
                        medicoId = medicoId,
                        fecha = fecha,
                        hora = hora
                    )

                    if (cita != null) {
                        onConfirmada(cita.id)
                    } else {
                        enviada = false
                        error = "No se pudo reservar. Comprueba tu sesión o vuelve a elegir un horario disponible."
                    }
                }
            },
            habilitado = !enviada &&
                    usuario != null &&
                    medico != null &&
                    fecha.isNotBlank() &&
                    hora.isNotBlank()
        )
    }
}