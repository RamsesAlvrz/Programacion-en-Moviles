package com.alvarez.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    doctorId: Int,
    onBackClick: () -> Unit,
    onConfirmReservation: (String, String, String) -> Unit
) {
    val doctor = sampleDoctors.find { it.id == doctorId } ?: sampleDoctors[0]

    val dates = listOf("Lunes 28 Sept", "Martes 29 Sept", "Miércoles 30 Sept")
    val times = listOf("09:00 AM", "11:30 AM", "04:00 PM")

    var selectedDate by remember { mutableStateOf(dates[0]) }
    var selectedTime by remember { mutableStateOf(times[0]) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar Cita") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Seleccione Fecha para ${doctor.name}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                // Selección única de fecha (comportamiento de RadioButton con Chips)
                dates.forEach { date ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        RadioButton(
                            selected = selectedDate == date,
                            onClick = { selectedDate = date }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(date, modifier = Modifier.alignByBaseline())
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Seleccione Hora", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                // Selección única de hora
                times.forEach { time ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        RadioButton(
                            selected = selectedTime == time,
                            onClick = { selectedTime = time }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(time, modifier = Modifier.alignByBaseline())
                    }
                }
            }

            Button(
                onClick = { onConfirmReservation(doctor.name, selectedDate, selectedTime) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Cita")
            }
        }
    }
}
