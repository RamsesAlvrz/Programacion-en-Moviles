package com.alvarez.clinicasalud.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alvarez.clinicasalud.model.Doctor

val sampleDoctors = listOf(
    Doctor(
        id = 1,
        name = "Dra. Ana Torres",
        specialty = "Cardióloga",
        rating = 4.9,
        experience = "12 años exp.",
        reviews = "128 reseñas",
        bio = "Especialista en arritmias e hipertensión, formación en la Clínica Mayo."
    ),
    Doctor(
        id = 2,
        name = "Dr. Luis Vega",
        specialty = "Pediatra",
        rating = 4.7,
        experience = "8 años exp.",
        reviews = "95 reseñas",
        bio = "Atención integral para niños y adolescentes con enfoque preventivo."
    ),
    Doctor(
        id = 3,
        name = "Dra. Rosa Díaz",
        specialty = "Dermatóloga",
        rating = 4.8,
        experience = "10 años exp.",
        reviews = "110 reseñas",
        bio = "Especialista en dermatología clínica y estética."
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenDrawer: () -> Unit,
    onDoctorClick: (Int) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Cardiología") }
    val categories = listOf("Cardiología", "Pediatría", "Dermatología")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Clínica Salud+",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Hola, Juan",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menú",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF512DA8)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Chips de especialidades
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { category ->
                    val isSelected = selectedCategory == category
                    Surface(
                        onClick = { selectedCategory = category },
                        shape = RoundedCornerShape(50),
                        color = if (isSelected) Color(0xFF512DA8) else Color(0xFFF3F4F6)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else Color(0xFF374151),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Médicos disponibles",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Lista de médicos
            val filteredDoctors = sampleDoctors.filter {
                (it.specialty.contains(selectedCategory, ignoreCase = true)) || selectedCategory == "Todos"
            }.ifEmpty { sampleDoctors }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(filteredDoctors) { doctor ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDoctorClick(doctor.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Caja con '+' morado
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(Color(0xFFF3E8FF), shape = RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = null,
                                    tint = Color(0xFF512DA8),
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = doctor.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = doctor.specialty,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(2.dp))
                                Text(
                                    text = doctor.rating.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}