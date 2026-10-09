package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.R
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.DatosConsulta
import com.alvarez.saludplus.ui.components.Encabezado
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.Locale

private val AzulConfirmacion = Color(0xFF2378C9)
private val CelesteConfirmacion = Color(0xFFE3F1F7)
private val FondoConfirmacion = Color(0xFFF5FAFC)

private fun esFechaReservable(fecha: LocalDate?): Boolean {
    return fecha != null &&
            !fecha.isBefore(LocalDate.now()) &&
            fecha.dayOfWeek != DayOfWeek.SATURDAY &&
            fecha.dayOfWeek != DayOfWeek.SUNDAY
}

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

    val fechaLocal = remember(fecha) {
        runCatching { LocalDate.parse(fecha) }.getOrNull()
    }

    var motivo by rememberSaveable(medicoId, fecha, hora) {
        mutableStateOf("")
    }

    var enviando by rememberSaveable(medicoId, fecha, hora) {
        mutableStateOf(false)
    }

    var error by rememberSaveable(medicoId, fecha, hora) {
        mutableStateOf("")
    }

    val fechaValida = esFechaReservable(fechaLocal)

    val disponibles = if (medico != null && fechaValida) {
        Repositorio.horariosDisponibles(medicoId, fecha)
    } else {
        emptyList()
    }

    val puedeConfirmar =
        usuario != null &&
                medico != null &&
                fechaValida &&
                hora in disponibles &&
                !enviando

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoConfirmacion)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Encabezado(
            titulo = "Confirmar cita",
            onVolver = onVolver
        )

        if (medico != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CelesteConfirmacion
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (medico.id == 1) {
                        Image(
                            painter = painterResource(R.drawable.medico_ana),
                            contentDescription = "Fotografía de ${medico.nombre}",
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(AzulConfirmacion, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = medico.nombre
                                    .split(" ")
                                    .filter {
                                        it.isNotBlank() &&
                                                it != "Dr." &&
                                                it != "Dra."
                                    }
                                    .take(2)
                                    .joinToString("") {
                                        it.first().uppercaseChar().toString()
                                    },
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = medico.nombre,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = especialidad?.nombre.orEmpty(),
                            style = MaterialTheme.typography.bodySmall
                        )

                        Text(
                            text = "${medico.experiencia} años de experiencia",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            DatoConfirmacion(
                tipo = "fecha",
                titulo = "Fecha",
                contenido = if (fechaLocal != null) {
                    DatosConsulta.fechaEnEspanol(fecha)
                } else {
                    "Fecha no válida"
                }
            )

            HorizontalDivider(color = Color(0xFFDDE8EE))

            DatoConfirmacion(
                tipo = "hora",
                titulo = "Hora",
                contenido = hora
            )

            HorizontalDivider(color = Color(0xFFDDE8EE))

            DatoConfirmacion(
                tipo = "consulta",
                titulo = "Tipo de atención",
                contenido = DatosConsulta.TIPO_ATENCION
            )

            HorizontalDivider(color = Color(0xFFDDE8EE))

            DatoConfirmacion(
                tipo = "direccion",
                titulo = "Dirección",
                contenido = DatosConsulta.DIRECCION
            )
        }

        Text(
            text = "Motivo de consulta (opcional)",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = motivo,
            onValueChange = {
                motivo = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Motivo de consulta...")
            },
            enabled = !enviando,
            minLines = 2,
            maxLines = 4,
            shape = RoundedCornerShape(12.dp)
        )

        Text(
            text = medico?.let {
                "Costo de consulta: S/ ${
                    String.format(Locale.US, "%.2f", it.precio)
                }"
            } ?: "Costo no disponible",
            style = MaterialTheme.typography.bodySmall
        )

        val mensaje = when {
            error.isNotBlank() -> error
            usuario == null -> "Inicia sesión para reservar."
            medico == null -> "No se encontró el médico."
            !fechaValida -> "Selecciona una fecha hábil desde hoy."
            hora !in disponibles && !enviando ->
                "El horario ya no está disponible. Selecciona otro."
            else -> ""
        }

        if (mensaje.isNotBlank()) {
            Text(
                text = mensaje,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                if (!enviando) {
                    error = ""

                    val sigueDisponible =
                        esFechaReservable(fechaLocal) &&
                                hora in Repositorio.horariosDisponibles(
                            medicoId,
                            fecha
                        )

                    if (!sigueDisponible) {
                        error = "El horario ya no está disponible."
                    } else {
                        enviando = true

                        val cita = Repositorio.agendarCita(
                            medicoId = medicoId,
                            fecha = fecha,
                            hora = hora,
                            motivoConsulta = motivo
                        )

                        if (cita != null) {
                            onConfirmada(cita.id)
                        } else {
                            enviando = false
                            error = "No se pudo reservar. Revisa tu sesión y el horario."
                        }
                    }
                }
            },
            enabled = puedeConfirmar,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulConfirmacion
            )
        ) {
            Text(
                text = if (enviando) {
                    "Confirmando..."
                } else {
                    "Confirmar cita"
                }
            )
        }
    }
}

@Composable
private fun DatoConfirmacion(
    tipo: String,
    titulo: String,
    contenido: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    CelesteConfirmacion,
                    RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            IconoConfirmacion(tipo)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = contenido,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun IconoConfirmacion(tipo: String) {
    Canvas(modifier = Modifier.size(24.dp)) {
        val grosor = 2.dp.toPx()
        val trazo = Stroke(grosor)
        val ancho = size.width
        val alto = size.height

        when (tipo) {
            "fecha" -> {
                drawRoundRect(
                    color = AzulConfirmacion,
                    topLeft = Offset(ancho * 0.15f, alto * 0.22f),
                    size = Size(ancho * 0.70f, alto * 0.63f),
                    style = trazo
                )

                drawLine(
                    color = AzulConfirmacion,
                    start = Offset(ancho * 0.15f, alto * 0.42f),
                    end = Offset(ancho * 0.85f, alto * 0.42f),
                    strokeWidth = grosor
                )

                listOf(0.32f, 0.68f).forEach { posicion ->
                    drawLine(
                        color = AzulConfirmacion,
                        start = Offset(ancho * posicion, alto * 0.12f),
                        end = Offset(ancho * posicion, alto * 0.30f),
                        strokeWidth = grosor
                    )
                }
            }

            "hora" -> {
                drawCircle(
                    color = AzulConfirmacion,
                    radius = ancho * 0.36f,
                    style = trazo
                )

                drawLine(
                    color = AzulConfirmacion,
                    start = center,
                    end = Offset(ancho * 0.50f, alto * 0.28f),
                    strokeWidth = grosor
                )

                drawLine(
                    color = AzulConfirmacion,
                    start = center,
                    end = Offset(ancho * 0.68f, alto * 0.58f),
                    strokeWidth = grosor
                )
            }

            "consulta" -> {
                drawRoundRect(
                    color = AzulConfirmacion,
                    topLeft = Offset(ancho * 0.18f, alto * 0.18f),
                    size = Size(ancho * 0.64f, alto * 0.64f),
                    style = trazo
                )

                drawLine(
                    color = AzulConfirmacion,
                    start = Offset(ancho * 0.50f, alto * 0.32f),
                    end = Offset(ancho * 0.50f, alto * 0.68f),
                    strokeWidth = grosor
                )

                drawLine(
                    color = AzulConfirmacion,
                    start = Offset(ancho * 0.32f, alto * 0.50f),
                    end = Offset(ancho * 0.68f, alto * 0.50f),
                    strokeWidth = grosor
                )
            }

            "direccion" -> {
                val pin = Path().apply {
                    moveTo(ancho * 0.50f, alto * 0.90f)

                    cubicTo(
                        ancho * 0.12f, alto * 0.52f,
                        ancho * 0.10f, alto * 0.12f,
                        ancho * 0.50f, alto * 0.12f
                    )

                    cubicTo(
                        ancho * 0.90f, alto * 0.12f,
                        ancho * 0.88f, alto * 0.52f,
                        ancho * 0.50f, alto * 0.90f
                    )

                    close()
                }

                drawPath(
                    path = pin,
                    color = AzulConfirmacion,
                    style = trazo
                )

                drawCircle(
                    color = AzulConfirmacion,
                    radius = ancho * 0.10f,
                    center = Offset(ancho * 0.50f, alto * 0.36f),
                    style = trazo
                )
            }
        }
    }
}