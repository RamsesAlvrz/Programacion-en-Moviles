package com.alvarez.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alvarez.centrotareas.data.local.EjecucionEntity
import com.alvarez.centrotareas.viewmodel.HistorialViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Azul = Color(0xFF176B9A)
private val Fondo = Color(0xFFF3F6FA)
private val Verde = Color(0xFF167448)
private val Rojo = Color(0xFFB42332)

@Composable
fun PantallaHistorial(
    viewModel: HistorialViewModel
) {
    val ejecuciones by viewModel.ejecuciones.collectAsState()

    var filtro by remember { mutableStateOf("Todas") }
    var mostrarConfirmacion by remember {
        mutableStateOf(false)
    }

    val exitosas = ejecuciones.count {
        it.estado == "Exitosa"
    }
    val fallidas = ejecuciones.count {
        it.estado == "Fallida"
    }

    val duracionesExitosas = ejecuciones
        .filter { it.estado == "Exitosa" }
        .mapNotNull { it.duracionMs }

    val promedio = if (duracionesExitosas.isNotEmpty()) {
        duracionesExitosas.average().toLong()
    } else {
        0L
    }

    val filtradas = when (filtro) {
        "Exitosas" -> ejecuciones.filter {
            it.estado == "Exitosa"
        }
        "Fallidas" -> ejecuciones.filter {
            it.estado == "Fallida"
        }
        else -> ejecuciones
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacion = false
            },
            title = {
                Text("Eliminar historial")
            },
            text = {
                Text(
                    "¿Deseas eliminar todos los registros? " +
                            "Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.limpiarHistorial()
                        mostrarConfirmacion = false
                    }
                ) {
                    Text("Eliminar", color = Rojo)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
            .padding(horizontal = 16.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            top = 16.dp,
            bottom = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Azul
                )
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "HISTORIAL DE ACTIVIDAD",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        "Tus ejecuciones",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Consulta tus operaciones y analiza su rendimiento.",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        item {
            Text(
                "Resumen general",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Azul
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TarjetaEstadistica(
                    "Total",
                    ejecuciones.size.toString(),
                    Modifier.weight(1f)
                )
                TarjetaEstadistica(
                    "Exitosas",
                    exitosas.toString(),
                    Modifier.weight(1f)
                )
                TarjetaEstadistica(
                    "Fallidas",
                    fallidas.toString(),
                    Modifier.weight(1f)
                )
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text(
                        "Tiempo promedio de ejecuciones exitosas",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    Text(
                        if (duracionesExitosas.isEmpty()) {
                            "Sin datos"
                        } else {
                            "$promedio ms"
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Azul
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Operaciones recientes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Azul
                )

                if (ejecuciones.isNotEmpty()) {
                    TextButton(
                        onClick = {
                            mostrarConfirmacion = true
                        }
                    ) {
                        Icon(
                            Icons.Filled.DeleteOutline,
                            contentDescription = null
                        )
                        Text("Limpiar")
                    }
                }
            }
        }

        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    "Todas",
                    "Exitosas",
                    "Fallidas"
                ).forEach { opcion ->
                    FilterChip(
                        selected = filtro == opcion,
                        onClick = { filtro = opcion },
                        label = { Text(opcion) }
                    )
                }
            }
        }

        if (filtradas.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Filled.History,
                            contentDescription = null,
                            tint = Azul
                        )

                        Text(
                            if (ejecuciones.isEmpty()) {
                                "Aún no hay registros"
                            } else {
                                "No hay resultados para este filtro"
                            },
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            if (ejecuciones.isEmpty()) {
                                "Ejecuta una carga desde Tareas " +
                                        "para comenzar a registrar tu actividad."
                            } else {
                                "Selecciona otro filtro para ver operaciones."
                            },
                            color = Color.Gray,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        } else {
            items(
                items = filtradas,
                key = { it.id }
            ) { ejecucion ->
                TarjetaEjecucion(ejecucion)
            }
        }
    }
}

@Composable
private fun TarjetaEstadistica(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                valor,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Azul
            )

            Text(
                titulo,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun TarjetaEjecucion(
    ejecucion: EjecucionEntity
) {
    val exitosa = ejecucion.estado == "Exitosa"

    val fecha = remember(ejecucion.fecha) {
        SimpleDateFormat(
            "dd/MM/yyyy · HH:mm",
            Locale.getDefault()
        ).format(Date(ejecucion.fecha))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Icon(
                    imageVector = if (exitosa) {
                        Icons.Filled.CheckCircle
                    } else {
                        Icons.Filled.Error
                    },
                    contentDescription = null,
                    tint = if (exitosa) Verde else Rojo
                )

                Text(
                    ejecucion.tipo,
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )

                Text(
                    if (exitosa) "Éxito" else "Fallida",
                    color = if (exitosa) Verde else Rojo,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Text(
                fecha,
                style = MaterialTheme.typography.labelMedium,
                color = Color.Gray
            )

            Text(
                ejecucion.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )

            if (ejecucion.duracionMs != null) {
                Text(
                    "Duración: ${ejecucion.duracionMs} ms",
                    color = Azul,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
