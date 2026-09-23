package com.alvarez.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alvarez.clinicasalud.model.Appointment

val sampleAppointments = listOf(
    Appointment(1, "Dr. Carlos Pérez", "Cardiología", "Lunes 28 Sept", "09:00 AM", "Confirmada"),
    Appointment(2, "Dra. Ana Gómez", "Pediatría", "Viernes 15 Ago", "11:30 AM", "Completada")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    doctorName: String,
    date: String,
    time: String,
    onBackHome: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Resumen de Cita") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("¡Cita Agendada con Éxito!", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Médico: $doctorName")
                        Text("Fecha: $date")
                        Text("Hora: $time")
                        Text("Estado: Confirmada", color = Color.Green)
                    }
                }
            }
            Button(onClick = onBackHome, modifier = Modifier.fillMaxWidth()) {
                Text("Volver al Inicio")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppointmentsScreen(onOpenDrawer: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Citas") },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(sampleAppointments) { app ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(app.doctorName, style = MaterialTheme.typography.titleMedium)
                        Text("${app.specialty} - ${app.date} a las ${app.time}")
                        Text("Estado: ${app.status}", color = if(app.status == "Confirmada") Color.Blue else Color.Gray)
                    }
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
                title = { Text("Historial Médico") },
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