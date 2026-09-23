package com.alvarez.clinicasalud.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    doctorId: Int,
    onBackClick: () -> Unit,
    onScheduleClick: (Int) -> Unit
) {
    val doctor = sampleDoctors.find { it.id == doctorId } ?: sampleDoctors[0]

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del Médico") },
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
                Text(text = doctor.name, style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Especialidad: ${doctor.specialty}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Calificación: ⭐ ${doctor.rating}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Médico colegiado con amplia experiencia en el tratamiento de pacientes y atención personalizada.",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = { onScheduleClick(doctor.id) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agendar Cita")
            }
        }
    }
}