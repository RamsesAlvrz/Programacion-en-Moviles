package com.alvarez.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.campusgps.model.UbicacionUi
import com.alvarez.campusgps.viewmodel.MapaViewModel

@Composable
fun PantallaMapa(vm: MapaViewModel) {

    val estado by vm.estado.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Mi ubicación",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF163B52)
            )

            Spacer(Modifier.height(20.dp))

            Button(
                onClick = { vm.buscar() },
                enabled = false,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF176B9A)
                )
            ) {
                Text("Obtener mi ubicación")
            }

            Spacer(Modifier.height(16.dp))

            when (val actual = estado) {
                is UbicacionUi.Lista -> {
                    Text("Latitud: ${actual.posicion.latitud}")
                    Text("Longitud: ${actual.posicion.longitud}")
                }

                UbicacionUi.Buscando -> {
                    CircularProgressIndicator()
                }

                is UbicacionUi.Error -> {
                    Text(
                        text = actual.mensaje,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                UbicacionUi.Inicial -> {
                    Text(
                        "Activa la ubicación en los siguientes pasos",
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
