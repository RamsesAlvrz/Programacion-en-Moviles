
package com.alvarez.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.campusgps.model.UbicacionUi
import com.alvarez.campusgps.util.formatearDistancia
import com.alvarez.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val estado by vm.estado.collectAsStateWithLifecycle()
    val lugares by vm.lugaresFiltrados.collectAsStateWithLifecycle()
    val categoria by vm.categoriaSeleccionada.collectAsStateWithLifecycle()
    val seleccionado by vm.seleccionado.collectAsStateWithLifecycle()
    val masCercano by vm.masCercanoVisible.collectAsStateWithLifecycle()

    val contexto = LocalContext.current
    val colores = MaterialTheme.colorScheme

    val permisos = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun solicitarUbicacion() {
        val tienePermiso = permisos.any { permiso ->
            ContextCompat.checkSelfPermission(
                contexto,
                permiso
            ) == PackageManager.PERMISSION_GRANTED
        }

        if (tienePermiso) {
            vm.buscar()
        } else {
            lanzador.launch(permisos)
        }
    }

    val posicion =
        (estado as? UbicacionUi.Lista)?.posicion

    val error =
        estado as? UbicacionUi.Error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {

        // Encabezado CampusGPS 2.0
        Surface(
            color = colores.primary,
            shape = RoundedCornerShape(
                bottomStart = 24.dp,
                bottomEnd = 24.dp
            ),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 18.dp
                    )
            ) {
                Text(
                    text = "CampusGPS",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.onPrimary
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = "Explora los lugares de tu campus",
                    fontSize = 13.sp,
                    color = colores.onPrimary.copy(alpha = 0.85f)
                )
            }
        }

        if (error != null) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "No pudimos mostrar tu ubicación",
                    style = MaterialTheme.typography.titleMedium,
                    color = colores.onBackground,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = { solicitarUbicacion() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Obtener mi ubicación")
                }

                MensajeError(
                    error = error,
                    onReintentar = { solicitarUbicacion() }
                )
            }

        } else {

            // Filtros por categorias - COMMIT 12
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Text(
                    text = "Explorar por categoría",
                    modifier = Modifier.padding(
                        horizontal = 18.dp,
                        vertical = 4.dp
                    ),
                    style = MaterialTheme.typography.labelLarge,
                    color = colores.onBackground,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(
                            horizontal = 14.dp,
                            vertical = 4.dp
                        ),
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {
                    MapaViewModel.CATEGORIAS.forEach { opcion ->
                        FilterChip(
                            selected = categoria == opcion,
                            onClick = {
                                vm.seleccionarCategoria(opcion)
                            },
                            label = {
                                Text(
                                    text = opcion,
                                    fontSize = 12.sp
                                )
                            },
                            shape = RoundedCornerShape(14.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = colores.primary,
                                selectedLabelColor = colores.onPrimary,
                                containerColor = colores.surface,
                                labelColor = colores.onSurface
                            )
                        )
                    }
                }
            }

            // Mapa principal
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 8.dp
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = colores.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    MapaUbicacion(
                        posicion = posicion,
                        lugares = lugares,
                        seleccionado = seleccionado,
                        masCercanoId = masCercano?.lugar?.id,
                        modifier = Modifier.fillMaxSize()
                    )

                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = colores.surface.copy(alpha = 0.95f),
                        shadowElevation = 2.dp
                    ) {
                        Text(
                            text = "MAPA DEL CAMPUS",
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 8.dp
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = colores.primary
                        )
                    }
                }
            }

            // Acciones principales
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 10.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { solicitarUbicacion() },
                        enabled = estado !is UbicacionUi.Buscando,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(
                            horizontal = 8.dp,
                            vertical = 12.dp
                        )
                    ) {
                        Text(
                            text = "Mi ubicación",
                            fontSize = 12.sp
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            masCercano?.let {
                                vm.seleccionar(it.lugar.id)
                            }
                        },
                        enabled = masCercano != null &&
                                estado is UbicacionUi.Lista,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(
                            horizontal = 8.dp,
                            vertical = 12.dp
                        )
                    ) {
                        Text(
                            text = "Más cercano",
                            fontSize = 12.sp
                        )
                    }
                }

                if (estado is UbicacionUi.Buscando) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Buscando tu ubicación...",
                        color = colores.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }

                masCercano?.let { item ->
                    Surface(
                        color = colores.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Más cercano: ${item.lugar.nombre} · " +
                                    (item.metros?.let {
                                        formatearDistancia(it)
                                    } ?: "Sin calcular"),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            color = colores.onPrimaryContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Panel inferior de lugares
            Surface(
                color = colores.surface,
                shape = RoundedCornerShape(
                    topStart = 22.dp,
                    topEnd = 22.dp
                ),
                shadowElevation = 5.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 18.dp,
                                end = 18.dp,
                                top = 14.dp,
                                bottom = 8.dp
                            ),
                        horizontalArrangement =
                            Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lugares del campus",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "${lugares.size} lugares",
                            color = colores.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }

                    if (lugares.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay lugares en esta categoría",
                                color = colores.onSurfaceVariant,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    } else {
                        ListaLugares(
                            lugares = lugares,
                            seleccionado = seleccionado,
                            masCercanoId = masCercano?.lugar?.id,
                            onClick = { id ->
                                vm.seleccionar(id)
                            },
                            modifier = Modifier.height(190.dp)
                        )
                    }

                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    }
}
