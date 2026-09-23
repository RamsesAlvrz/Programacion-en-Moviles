package com.alvarez.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.clinicasalud.model.Appointment

val sampleAppointments = listOf(
    Appointment(1, "Dra. Ana Torres", "Cardióloga", "Viernes 27", "10:30 am", "Confirmada"),
    Appointment(2, "Dr. Luis Vega", "Pediatra", "Miércoles 15", "3:00 pm", "Completada")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    doctorName: String,
    date: String,
    time: String,
    onBackHome: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Círculo verde con checkmark
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .background(Color(0xFFDCFCE7), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Éxito",
                        tint = Color(0xFF16A34A),
                        modifier = Modifier.size(48.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "¡Cita agendada!",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (doctorName.isNotEmpty()) doctorName else "Dra. Ana Torres",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = if (date.isNotEmpty() && time.isNotEmpty()) "$date, $time" else "Viernes 27, 10:30 am",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Botón "Ver mis citas"
            Button(
                onClick = onBackHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3F4F6),
                    contentColor = Color(0xFF374151)
                )
            ) {
                Text(
                    text = "Ver mis citas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(onOpenDrawer: () -> Unit) {
    val appointments = remember { sampleAppointments.toMutableStateList() }
    var appointmentToCancel by remember { mutableStateOf<Appointment?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis citas", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(appointments, key = { it.id }) { app ->
                    val (statusBg, statusFg) = when (app.status) {
                        "Confirmada" -> Color(0xFFDCFCE7) to Color(0xFF16A34A)
                        "Completada" -> Color(0xFFE5E7EB) to Color(0xFF6B7280)
                        "Cancelada" -> Color(0xFFFCE8E8) to MaterialTheme.colorScheme.error
                        else -> Color(0xFFE5E7EB) to Color(0xFF6B7280)
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F4F6))
                    ) {
                        IntrinsicRow(
                            app = app,
                            statusBg = statusBg,
                            statusFg = statusFg,
                            onCancelClick = { appointmentToCancel = app }
                        )
                    }
                }
            }

            if (appointmentToCancel != null) {
                val cita = appointmentToCancel!!
                AlertDialog(
                    onDismissRequest = { appointmentToCancel = null },
                    title = { Text("Cancelar cita") },
                    text = { Text("¿Deseas cancelar tu cita con ${cita.doctorName} el ${cita.date} a las ${cita.time}?") },
                    confirmButton = {
                        TextButton(onClick = {
                            val index = appointments.indexOfFirst { it.id == cita.id }
                            if (index != -1) {
                                appointments[index] = appointments[index].copy(status = "Cancelada")
                            }
                            appointmentToCancel = null
                        }) {
                            Text("Sí, cancelar", color = MaterialTheme.colorScheme.error)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { appointmentToCancel = null }) {
                            Text("No")
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun IntrinsicRow(
    app: Appointment,
    statusBg: Color,
    statusFg: Color,
    onCancelClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Barra lateral morada
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(Color(0xFF512DA8))
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = app.doctorName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${app.date}, ${app.time}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(50),
                    color = statusBg
                ) {
                    Text(
                        text = app.status,
                        color = statusFg,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            if (app.status == "Confirmada") {
                IconButton(onClick = onCancelClick) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Cancelar cita",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicalHistoryScreen(onOpenDrawer: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial Médico", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text("Aquí se mostrará el historial de recetas y diagnósticos pasados.", style = MaterialTheme.typography.bodyLarge)
        }
    }
}