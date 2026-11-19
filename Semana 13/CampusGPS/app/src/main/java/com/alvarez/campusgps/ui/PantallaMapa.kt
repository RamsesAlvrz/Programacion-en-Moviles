
package com.alvarez.campusgps.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.campusgps.model.UbicacionUi
import com.alvarez.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val estado by vm.estado.collectAsStateWithLifecycle()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        // Encabezado azul original
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
                modifier = Modifier.padding(20.dp)
            )
        }

        val posicion =
            (estado as? UbicacionUi.Lista)?.posicion

        // Mapa en el espacio disponible
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clipToBounds()
        ) {
            MapaUbicacion(
                posicion = posicion,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Botón y coordenadas debajo del mapa
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(Color.White)
                .padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = { alPulsar() },
                enabled = estado !is UbicacionUi.Buscando,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF176B9A)
                )
            ) {
                Text("Obtener mi ubicación")
            }

            Spacer(Modifier.height(8.dp))

            when (val actual = estado) {

                is UbicacionUi.Lista -> {
                    Text(
                        "Latitud: ${actual.posicion.latitud}",
                        fontSize = 13.sp
                    )
                    Text(
                        "Longitud: ${actual.posicion.longitud}",
                        fontSize = 13.sp
                    )
                }

                UbicacionUi.Buscando -> {
                    CircularProgressIndicator()
                    Text("Buscando ubicación...")
                }

                is UbicacionUi.Error -> {
                    Text(
                        text = actual.mensaje,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 13.sp
                    )
                }

                UbicacionUi.Inicial -> {
                    Text(
                        "Pulsa el botón para obtener tu ubicación",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
