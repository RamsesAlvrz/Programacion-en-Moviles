package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.BotonPrimario
import com.alvarez.saludplus.ui.components.Tarjeta

@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onInicio: () -> Unit,
    onMisCitas: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)

    val medico = cita?.let {
        Repositorio.obtenerMedico(it.medicoId)
    }

    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = if (cita != null) "✓" else "?",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = if (cita != null) {
                "¡Cita agendada!"
            } else {
                "Cita no encontrada"
            },
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        if (cita != null) {
            Text(
                text = "Tu reserva se registró correctamente.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Tarjeta(
                titulo = "Reserva #${cita.id}",
                detalle = "${medico?.nombre.orEmpty()}\n${especialidad?.nombre.orEmpty()}\n${cita.fecha} a las ${cita.hora}"
            )
        } else {
            Text(
                text = "No se encontró esta reserva en tu sesión.",
                textAlign = TextAlign.Center
            )
        }

        BotonPrimario(
            texto = "Ver mis citas",
            onClick = onMisCitas
        )

        OutlinedButton(
            onClick = onInicio,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al inicio")
        }
    }
}