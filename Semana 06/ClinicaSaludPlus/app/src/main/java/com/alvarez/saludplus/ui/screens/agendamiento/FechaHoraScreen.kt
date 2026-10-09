// COMMIT 1 — Reemplaza todo el contenido de:
// app/src/main/java/com/alvarez/saludplus/ui/screens/agendamiento/FechaHoraScreen.kt

package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.Encabezado
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val AzulCalendario = Color(0xFF2378C9)
private val FondoCalendario = Color(0xFFF5FAFC)
private val CelesteCalendario = Color(0xFFE3F1F7)
private val BordeCalendario = Color(0xFFD4E1E8)

private fun siguientesDiasHabiles(
    desde: LocalDate,
    cantidad: Int = 5
): List<LocalDate> {
    val dias = mutableListOf<LocalDate>()
    var fecha = desde

    while (dias.size < cantidad) {
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

private fun tituloPeriodo(
    dias: List<LocalDate>,
    locale: Locale
): String {
    val formato = DateTimeFormatter.ofPattern("MMMM yyyy", locale)
    val primero = dias.first().format(formato)
        .replaceFirstChar { it.titlecase(locale) }
    val ultimo = dias.last().format(formato)
        .replaceFirstChar { it.titlecase(locale) }

    return if (primero == ultimo) primero else "$primero / $ultimo"
}

@Composable
fun FechaHoraScreen(
    medicoId: Int,
    onVolver: () -> Unit,
    onContinuar: (String, String) -> Unit
) {
    val locale = remember { Locale("es", "PE") }
    val hoy = LocalDate.now()

    var semanasAdelante by rememberSaveable(medicoId) {
        mutableStateOf(0)
    }

    var fechaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf("")
    }

    var horaSeleccionada by rememberSaveable(medicoId) {
        mutableStateOf("")
    }

    val dias = remember(hoy, semanasAdelante) {
        siguientesDiasHabiles(
            desde = hoy.plusWeeks(semanasAdelante.toLong())
        )
    }

    val fechasVisibles = dias.map { it.toString() }

    val medico = Repositorio.obtenerMedico(medicoId)
    val especialidad = medico?.let {
        Repositorio.obtenerEspecialidad(it.especialidadId)
    }

    // Se consulta directamente el repositorio para mantener
    // la reacción a las citas agregadas o canceladas.
    val horarios = if (
        medico != null &&
        fechaSeleccionada in fechasVisibles
    ) {
        Repositorio.horariosDisponibles(
            medicoId,
            fechaSeleccionada
        )
    } else {
        emptyList()
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

    val puedeContinuar =
        medico != null &&
                fechaSeleccionada in fechasVisibles &&
                horaSeleccionada in horarios

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoCalendario)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Encabezado(
            titulo = "Seleccionar fecha y hora",
            onVolver = onVolver
        )

        if (medico == null) {
            Text(
                text = "No se encontró el médico seleccionado.",
                color = MaterialTheme.colorScheme.error
            )
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CelesteCalendario
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = medico.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = especialidad?.nombre.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    enabled = semanasAdelante > 0,
                    onClick = {
                        if (semanasAdelante > 0) {
                            semanasAdelante--
                            fechaSeleccionada = ""
                            horaSeleccionada = ""
                        }
                    },
                    modifier = Modifier.width(48.dp)
                ) {
                    Text(
                        text = "‹",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }

                Text(
                    text = tituloPeriodo(dias, locale),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                TextButton(
                    onClick = {
                        semanasAdelante++
                        fechaSeleccionada = ""
                        horaSeleccionada = ""
                    },
                    modifier = Modifier.width(48.dp)
                ) {
                    Text(
                        text = "›",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                dias.forEach { dia ->
                    val seleccionada =
                        fechaSeleccionada == dia.toString()

                    val nombreDia = when (dia.dayOfWeek) {
                        DayOfWeek.MONDAY -> "LUN"
                        DayOfWeek.TUESDAY -> "MAR"
                        DayOfWeek.WEDNESDAY -> "MIÉ"
                        DayOfWeek.THURSDAY -> "JUE"
                        DayOfWeek.FRIDAY -> "VIE"
                        else -> ""
                    }

                    Card(
                        onClick = {
                            if (fechaSeleccionada != dia.toString()) {
                                fechaSeleccionada = dia.toString()
                                horaSeleccionada = ""
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (seleccionada) {
                                AzulCalendario
                            } else {
                                CelesteCalendario
                            },
                            contentColor = if (seleccionada) {
                                Color.White
                            } else {
                                Color(0xFF263746)
                            }
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = nombreDia,
                                style = MaterialTheme.typography.labelSmall
                            )

                            Text(
                                text = dia.dayOfMonth.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Text(
                text = "Horarios disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                when {
                    fechaSeleccionada !in fechasVisibles -> {
                        Text(
                            text = "Selecciona un día para consultar los horarios.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    horarios.isEmpty() -> {
                        Text(
                            text = "No hay horarios disponibles para este día.",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 8.dp)
                        ) {
                            items(
                                items = horarios,
                                key = { it }
                            ) { hora ->
                                val seleccionada =
                                    horaSeleccionada == hora

                                OutlinedButton(
                                    onClick = {
                                        horaSeleccionada = hora
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .heightIn(min = 48.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(4.dp),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = if (seleccionada) {
                                            AzulCalendario
                                        } else {
                                            BordeCalendario
                                        }
                                    ),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (seleccionada) {
                                            AzulCalendario
                                        } else {
                                            Color.White
                                        },
                                        contentColor = if (seleccionada) {
                                            Color.White
                                        } else {
                                            Color(0xFF263746)
                                        }
                                    )
                                ) {
                                    Text(
                                        text = hora,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (puedeContinuar) {
                Text(
                    text = "Seleccionado: $fechaSeleccionada · $horaSeleccionada",
                    style = MaterialTheme.typography.bodySmall,
                    color = AzulCalendario
                )
            }

            Button(
                onClick = {
                    // Revalidación para impedir continuar con
                    // un horario que ya no esté disponible.
                    val disponibles =
                        Repositorio.horariosDisponibles(
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
                    }
                },
                enabled = puedeContinuar,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulCalendario
                )
            ) {
                Text(text = "Continuar")
            }
        }
    }
}