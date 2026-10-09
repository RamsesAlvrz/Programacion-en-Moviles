package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

@Composable
fun ConfirmarCitaScreen(
    medicoId: Int,
    fecha: String,
    hora: String,
    onVolver: () -> Unit,
    onConfirmada: (Int) -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)

    var motivo by rememberSaveable(medicoId, fecha, hora) {
        mutableStateOf("")
    }

    var enviando by rememberSaveable(medicoId, fecha, hora) {
        mutableStateOf(false)
    }

    var error by rememberSaveable(medicoId, fecha, hora) {
        mutableStateOf("")
    }

    val fechaLocal = runCatching {
        LocalDate.parse(fecha)
    }.getOrNull()

    val fechaValida = fechaLocal != null &&
            !fechaLocal.isBefore(LocalDate.now()) &&
            fechaLocal.dayOfWeek != DayOfWeek.SATURDAY &&
            fechaLocal.dayOfWeek != DayOfWeek.SUNDAY

    val horarioDisponible = hora in Repositorio.horariosDisponibles(
        medicoId,
        fecha
    )

    val puedeConfirmar = medico != null &&
            Repositorio.usuarioActual != null &&
            fechaValida &&
            horarioDisponible &&
            !enviando

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoAgendamiento)
            .imePadding()
    ) {
        val altoDato = ((maxHeight - 390.dp) / 4f)
            .coerceIn(76.dp, 115.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            EncabezadoAgendamiento(
                titulo = "Confirmar cita",
                onVolver = onVolver
            )

            if (medico != null) {
                ResumenMedico(medico)
            } else {
                Text(
                    text = "Médico no encontrado.",
                    color = MaterialTheme.colorScheme.error
                )
            }

            Column {
                DatoConfirmacion(
                    titulo = "Fecha",
                    valor = DatosConsulta.fechaEnEspanol(fecha),
                    simbolo = "▦",
                    alto = altoDato
                )

                DatoConfirmacion(
                    titulo = "Hora",
                    valor = hora,
                    simbolo = "◷",
                    alto = altoDato
                )

                DatoConfirmacion(
                    titulo = "Tipo de atención",
                    valor = DatosConsulta.TIPO_ATENCION,
                    simbolo = "✚",
                    alto = altoDato
                )

                DatoConfirmacion(
                    titulo = "Dirección",
                    valor = DatosConsulta.DIRECCION,
                    simbolo = "⌖",
                    alto = altoDato
                )
            }

            Text(
                text = "Motivo de consulta (opcional)",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextoAgendamiento
            )

            OutlinedTextField(
                value = motivo,
                onValueChange = {
                    motivo = it
                    error = ""
                },
                enabled = !enviando,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text("Motivo de consulta...")
                },
                minLines = 2,
                maxLines = 4,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulAgendamiento,
                    unfocusedBorderColor = Color(0xFFBCCFD9),
                    cursorColor = AzulAgendamiento
                )
            )

            if (medico != null) {
                Text(
                    text = String.format(
                        Locale.US,
                        "Costo de consulta: S/ %.2f",
                        medico.precio
                    ),
                    fontSize = 14.sp,
                    color = TextoAgendamiento
                )
            }

            val mensaje = when {
                error.isNotBlank() -> error
                Repositorio.usuarioActual == null ->
                    "Inicia sesión para confirmar la cita."
                !fechaValida ->
                    "Selecciona una fecha hábil que no haya pasado."
                !horarioDisponible ->
                    "Este horario ya no está disponible. Vuelve y elige otro."
                else -> ""
            }

            if (mensaje.isNotBlank()) {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                enabled = puedeConfirmar,
                onClick = {
                    val fechaSigueValida = fechaLocal != null &&
                            !fechaLocal.isBefore(LocalDate.now()) &&
                            fechaLocal.dayOfWeek != DayOfWeek.SATURDAY &&
                            fechaLocal.dayOfWeek != DayOfWeek.SUNDAY

                    val horaSigueDisponible =
                        hora in Repositorio.horariosDisponibles(
                            medicoId,
                            fecha
                        )

                    if (!fechaSigueValida || !horaSigueDisponible) {
                        error = "La fecha o el horario ya no están disponibles."
                    } else if (Repositorio.usuarioActual == null) {
                        error = "Inicia sesión para confirmar la cita."
                    } else if (!enviando) {
                        enviando = true

                        val cita = Repositorio.agendarCita(
                            medicoId = medicoId,
                            fecha = fecha,
                            hora = hora,
                            motivoConsulta = motivo.trim()
                        )

                        if (cita != null) {
                            onConfirmada(cita.id)
                        } else {
                            enviando = false
                            error = "No se pudo agendar la cita. Revisa el horario."
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulAgendamiento
                )
            ) {
                Text(
                    text = if (enviando) {
                        "Confirmando..."
                    } else {
                        "Confirmar cita"
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun DatoConfirmacion(
    titulo: String,
    valor: String,
    simbolo: String,
    alto: Dp
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = alto)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                color = CelesteAgendamiento,
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(
                    modifier = Modifier.size(46.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = simbolo,
                        fontSize = 29.sp,
                        color = AzulAgendamiento
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = titulo,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoAgendamiento
                )

                Text(
                    text = valor,
                    fontSize = 15.sp,
                    lineHeight = 20.sp,
                    color = TextoAgendamiento
                )
            }
        }

        HorizontalDivider(
            color = Color(0xFFDCE7EC)
        )
    }
}