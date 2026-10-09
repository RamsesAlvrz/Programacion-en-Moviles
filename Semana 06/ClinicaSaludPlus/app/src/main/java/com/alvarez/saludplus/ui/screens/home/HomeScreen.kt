package com.alvarez.saludplus.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.Tarjeta

@Composable
fun HomeScreen(
    onEspecialidades: () -> Unit,
    onEspecialidad: (Int) -> Unit,
    onCitas: () -> Unit,
    onResultados: () -> Unit,
    onNotificaciones: () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val destacadas = Repositorio.especialidadesDestacadas()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SaludPlus",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = onNotificaciones
                ) {
                    Text("Notificaciones")
                }
            }
        }

        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "¡Hola, ${usuario?.nombre ?: "paciente"}!",
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = "¿Cómo podemos cuidar de ti hoy?",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item {
            Tarjeta(
                titulo = "Agenda tu cita",
                detalle = "Elige una especialidad, un médico y el horario que prefieras.",
                onClick = onEspecialidades
            )
        }

        item {
            Text(
                text = "Especialidades destacadas",
                style = MaterialTheme.typography.titleMedium
            )
        }

        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = destacadas,
                    key = { especialidad ->
                        especialidad.id
                    }
                ) { especialidad ->

                    Card(
                        onClick = {
                            onEspecialidad(especialidad.id)
                        },
                        modifier = Modifier.width(180.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "+",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = especialidad.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            Text(
                                text = especialidad.descripcion,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }
        }

        item {
            TextButton(
                onClick = onEspecialidades
            ) {
                Text("Ver todas las especialidades ›")
            }
        }

        item {
            Tarjeta(
                titulo = "Mis citas",
                detalle = "Consulta las citas médicas que hayas reservado.",
                onClick = onCitas
            )
        }

        item {
            Tarjeta(
                titulo = "Resultados médicos",
                detalle = "Accede a la sección de resultados.",
                onClick = onResultados
            )
        }

        item {
            Text(
                text = "Tu salud, nuestra prioridad.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}