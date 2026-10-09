package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.*
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun generarDiasHabiles(desde: LocalDate): List<LocalDate> {
    val dias = mutableListOf<LocalDate>()
    var fecha = desde

    while (dias.size < 5) {
        if (
            fecha.dayOfWeek != DayOfWeek.SATURDAY &&
            fecha.dayOfWeek != DayOfWeek.SUNDAY
        ) {
            dias.add(fecha)
        }

        fecha = fecha.plusDays(1)
    }

    return dias
}

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onVolver: () -> Unit,
    onContinuar: (String, String) -> Unit
) {
    val medico = Repositorio.obtenerMedico(medicoId)

    if (medico == null) {
        Column(
            Modifier
                .fillMaxSize()
                .background(FondoAgendamiento)
                .padding(20.dp)
        ) {
            EncabezadoAgendamiento("Fecha y hora", onVolver)
            Text("Médico no encontrado.")
        }
        return
    }

    var semanas by rememberSaveable(medicoId) {
        mutableStateOf(0)
    }

    var fechaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf("")
    }

    var horaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf("")
    }

    var error by rememberSaveable(medicoId) {
        mutableStateOf("")
    }

    val hoy = LocalDate.now()
    val dias = generarDiasHabiles(hoy.plusWeeks(semanas.toLong()))
    val fechasVisibles = dias.map { it.toString() }

    val horarios = if (fechaSeleccionada in fechasVisibles) {
        Repositorio.horariosDisponibles(
            medicoId,
            fechaSeleccionada
        )
    } else {
        emptyList()
    }

    val formatoMes = DateTimeFormatter.ofPattern(
        "MMMM yyyy",
        Locale("es", "PE")
    )

    val primerMes = dias.first().format(formatoMes)
    val ultimoMes = dias.last().format(formatoMes)

    val periodo = if (primerMes == ultimoMes) {
        primerMes.replaceFirstChar { it.titlecase() }
    } else {
        primerMes.replaceFirstChar { it.titlecase() } +
                " / " +
                ultimoMes.replaceFirstChar { it.titlecase() }
    }

    LaunchedEffect(fechasVisibles) {
        if (fechaSeleccionada !in fechasVisibles) {
            fechaSeleccionada = ""
            horaSeleccionada = ""
        }
    }

    LaunchedEffect(fechaSeleccionada, horarios) {
        if (horaSeleccionada !in horarios) {
            horaSeleccionada = ""
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoAgendamiento)
    ) {
        val filasHorarios = ((horarios.size + 2) / 3)
            .coerceAtLeast(1)

        val altoHorario = ((maxHeight - 430.dp) / filasHorarios.toFloat())
            .coerceIn(48.dp, 78.dp)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 20.dp,
                    vertical = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EncabezadoAgendamiento(
                titulo = "Seleccionar fecha y hora",
                onVolver = onVolver
            )

            ResumenMedico(medico)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    enabled = semanas > 0,
                    onClick = {
                        if (semanas > 0) {
                            semanas--
                            fechaSeleccionada = ""
                            horaSeleccionada = ""
                            error = ""
                        }
                    }
                ) {
                    Text("‹", fontSize = 30.sp)
                }

                Text(
                    text = periodo,
                    modifier = Modifier.weight(1f),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoAgendamiento,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                TextButton(
                    onClick = {
                        semanas++
                        fechaSeleccionada = ""
                        horaSeleccionada = ""
                        error = ""
                    }
                ) {
                    Text("›", fontSize = 30.sp)
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                dias.forEach { dia ->
                    val seleccionada = fechaSeleccionada == dia.toString()

                    val nombreDia = when (dia.dayOfWeek) {
                        DayOfWeek.MONDAY -> "LUN"
                        DayOfWeek.TUESDAY -> "MAR"
                        DayOfWeek.WEDNESDAY -> "MIÉ"
                        DayOfWeek.THURSDAY -> "JUE"
                        else -> "VIE"
                    }

                    Card(
                        onClick = {
                            if (fechaSeleccionada != dia.toString()) {
                                fechaSeleccionada = dia.toString()
                                horaSeleccionada = ""
                            }
                            error = ""
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(82.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (seleccionada) {
                                AzulAgendamiento
                            } else {
                                CelesteAgendamiento
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            val color = if (seleccionada) {
                                Color.White
                            } else {
                                TextoAgendamiento
                            }

                            Text(
                                text = nombreDia,
                                fontSize = 12.sp,
                                color = color
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = dia.dayOfMonth.toString(),
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                color = color
                            )
                        }
                    }
                }
            }

            Text(
                text = "Horarios disponibles",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextoAgendamiento
            )

            when {
                fechaSeleccionada.isBlank() -> {
                    Text(
                        text = "Selecciona un día para consultar sus horarios.",
                        color = Color(0xFF536672),
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                horarios.isEmpty() -> {
                    Text(
                        text = "No quedan horarios disponibles para este día.",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }

                else -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        horarios.chunked(3).forEach { fila ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                fila.forEach { hora ->
                                    val seleccionada = horaSeleccionada == hora

                                    OutlinedButton(
                                        onClick = {
                                            horaSeleccionada = hora
                                            error = ""
                                        },
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(altoHorario),
                                        shape = RoundedCornerShape(11.dp),
                                        contentPadding = PaddingValues(4.dp),
                                        border = BorderStroke(
                                            1.dp,
                                            if (seleccionada) {
                                                AzulAgendamiento
                                            } else {
                                                Color(0xFFCDDDE6)
                                            }
                                        ),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = if (seleccionada) {
                                                AzulAgendamiento
                                            } else {
                                                CelesteAgendamiento
                                            },
                                            contentColor = if (seleccionada) {
                                                Color.White
                                            } else {
                                                TextoAgendamiento
                                            }
                                        )
                                    ) {
                                        Text(
                                            text = hora,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }
                                }

                                repeat(3 - fila.size) {
                                    Spacer(Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            if (error.isNotBlank()) {
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Button(
                onClick = {
                    val disponibles = Repositorio.horariosDisponibles(
                        medicoId,
                        fechaSeleccionada
                    )

                    if (
                        fechaSeleccionada in fechasVisibles &&
                        horaSeleccionada in disponibles
                    ) {
                        onContinuar(
                            fechaSeleccionada,
                            horaSeleccionada
                        )
                    } else {
                        horaSeleccionada = ""
                        error = "El horario ya no está disponible. Elige otro."
                    }
                },
                enabled = fechaSeleccionada in fechasVisibles &&
                        horaSeleccionada in horarios,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulAgendamiento
                )
            ) {
                Text(
                    text = "Continuar",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}