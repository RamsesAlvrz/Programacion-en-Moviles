package com.alvarez.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.campusgps.model.UbicacionUi
import com.alvarez.campusgps.util.formatearDistancia
import com.alvarez.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val estado by vm.estado.collectAsStateWithLifecycle()
    val lugares by vm.lugares.collectAsStateWithLifecycle()
    val seleccionado by vm.seleccionado.collectAsStateWithLifecycle()
    val masCercano by vm.masCercano.collectAsStateWithLifecycle()

    val contexto = LocalContext.current

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

    fun alPulsar() {
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

    val esError = estado is UbicacionUi.Error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (esError) Color(0xFFFCFAFC)
                else Color.White
            )
    ) {

        // Encabezado azul
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f),
            color = Color(0xFF176B9A)
        ) {
            Text(
                text = "CampusGPS",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }

        if (esError) {

            // Pantalla de error como en la guía
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 18.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Button(
                    onClick = { alPulsar() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF176B9A)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 4.dp
                    ),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text(
                        text = "Obtener mi ubicación",
                        fontSize = 12.sp
                    )
                }

                val error = estado as UbicacionUi.Error

                MensajeError(
                    error = error,
                    onReintentar = { alPulsar() }
                )
            }

        } else {

            // Mapa original del COMMIT 8
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .clipToBounds()
            ) {
                MapaUbicacion(
                    posicion = posicion,
                    lugares = lugares,
                    seleccionado = seleccionado,
                    masCercanoId = masCercano?.lugar?.id,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Botones debajo del mapa
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 5.dp,
                        bottom = 4.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {

                Button(
                    onClick = { alPulsar() },
                    enabled = estado !is UbicacionUi.Buscando,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF176B9A)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 4.dp
                    ),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text(
                        text = "Obtener mi ubicación",
                        fontSize = 12.sp
                    )
                }

                // Botón para ir al lugar más cercano
                OutlinedButton(
                    onClick = {
                        masCercano?.let { item ->
                            vm.seleccionar(item.lugar.id)
                        }
                    },
                    enabled = masCercano != null &&
                            estado is UbicacionUi.Lista,
                    border = BorderStroke(
                        1.dp,
                        Color(0xFF176B9A)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF176B9A)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 18.dp,
                        vertical = 3.dp
                    ),
                    modifier = Modifier.height(34.dp)
                ) {
                    Text(
                        text = "Ir al más cercano",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Nombre y distancia del más cercano
                masCercano?.let { item ->
                    Text(
                        text = "Más cercano: ${item.lugar.nombre} " +
                                "(${item.metros?.let {
                                    formatearDistancia(it)
                                } ?: "Sin calcular"})",
                        fontSize = 11.sp,
                        color = Color(0xFF263746),
                        fontWeight = FontWeight.Medium
                    )
                }

                // Estado de búsqueda
                if (estado is UbicacionUi.Buscando) {
                    Text(
                        text = "Buscando ubicación...",
                        fontSize = 12.sp
                    )
                }
            }

            // Lista original de lugares
            ListaLugares(
                lugares = lugares,
                seleccionado = seleccionado,
                masCercanoId = masCercano?.lugar?.id,
                onClick = { id ->
                    vm.seleccionar(id)
                },
                modifier = Modifier.height(220.dp)
            )
        }
    }
}
