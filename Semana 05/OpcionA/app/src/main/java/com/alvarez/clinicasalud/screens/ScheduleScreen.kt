package com.alvarez.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class DateItem(val dayName: String, val dayNum: String, val fullString: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    doctorId: Int,
    onBackClick: () -> Unit,
    onConfirmReservation: (String, String, String) -> Unit
) {
    val doctor = sampleDoctors.find { it.id == doctorId } ?: sampleDoctors[0]

    val dateList = listOf(
        DateItem("Jue", "26", "Jueves 26"),
        DateItem("Vie", "27", "Viernes 27"),
        DateItem("Sáb", "28", "Sábado 28")
    )
    val timeList = listOf("9:00", "10:30", "3:00")

    var selectedDateItem by remember { mutableStateOf(dateList[1]) } // Default "Vie 27"
    var selectedTime by remember { mutableStateOf("10:30") } // Default "10:30"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita", fontWeight = FontWeight.Bold) },
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
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Selecciona fecha",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Selector de Fecha Horizontal Parejo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    dateList.forEach { dateItem ->
                        val isSelected = selectedDateItem == dateItem
                        Surface(
                            onClick = { selectedDateItem = dateItem },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isSelected) Color(0xFF512DA8) else Color(0xFFF3F4F6),
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Text(
                                    text = dateItem.dayName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isSelected) Color.White.copy(alpha = 0.9f) else Color(0xFF6B7280)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = dateItem.dayNum,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color(0xFF111827)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Selecciona hora",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Selector de Hora Horizontal Parejo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    timeList.forEach { time ->
                        val isSelected = selectedTime == time
                        Surface(
                            onClick = { selectedTime = time },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) Color(0xFF512DA8) else Color(0xFFF3F4F6),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = time,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else Color(0xFF374151)
                                )
                            }
                        }
                    }
                }
            }

            // Botón inferior Confirmar cita
            Button(
                onClick = {
                    val formattedDate = selectedDateItem.fullString
                    val isAmOrPm = selectedTime.contains("am") || selectedTime.contains("pm")
                    val formattedTime = if (isAmOrPm) selectedTime else if (selectedTime == "9:00" || selectedTime == "10:30") "$selectedTime am" else "$selectedTime pm"
                    onConfirmReservation(doctor.name, formattedDate, formattedTime)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF512DA8))
            ) {
                Text("Confirmar cita", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}