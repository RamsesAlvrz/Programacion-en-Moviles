
package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Iron
import androidx.compose.material.icons.outlined.LocalLaundryService
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.repository.LavaRepository
import java.util.Locale

private val AzulTarifas = Color(0xFF0097B2)
private val FondoTarifas = Color(0xFFF1FBFD)
private val CelesteIcono = Color(0xFFD7F7FC)

@Composable
fun TarifasScreen(
    repository: LavaRepository,
    onVolver: () -> Unit
) {
    val servicios by remember(repository) {
        repository.observarServiciosActivos()
    }.collectAsState(initial = emptyList())

    Scaffold(
        containerColor = FondoTarifas,
        topBar = {
            Surface(color = FondoTarifas) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }

                    Text(
                        text = "Tarifas y servicios",
                        modifier = Modifier.weight(1f),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                top = 16.dp,
                bottom = 24.dp
            )
        ) {

            items(
                items = servicios,
                key = { it.id }
            ) { servicio ->
                TarjetaTarifa(servicio)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = AzulTarifas,
                        modifier = Modifier.size(22.dp)
                    )

                    Column {
                        Text(
                            text = "Horario de atención",
                            color = AzulTarifas,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Lunes a sábado · 8:00 a 18:00",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )

                        Text(
                            text = "Recojo en casilleros de pabellón A",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFF1BE),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "Mínimo 1 kg por servicio de lavado.",
                            fontSize = 12.sp,
                            color = Color(0xFF9B6500),
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = "Máximo 3 pedidos activos por estudiante.",
                            fontSize = 12.sp,
                            color = Color(0xFF9B6500),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaTarifa(servicio: Servicio) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                color = CelesteIcono,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(58.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    IconoTarifa(servicio)
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = servicio.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )

                Text(
                    text = servicio.textoPrecio(),
                    color = AzulTarifas,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Entrega en ${servicio.horasEntrega} h",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun IconoTarifa(servicio: Servicio) {
    val icono = when (servicio.unidad.uppercase()) {
        "KG" -> Icons.Outlined.LocalLaundryService
        "PRENDA" -> {
            if (servicio.nombre.contains("planch", ignoreCase = true)) {
                Icons.Outlined.Iron
            } else {
                Icons.Outlined.Checkroom
            }
        }
        else -> Icons.Outlined.LocalLaundryService
    }

    Icon(
        imageVector = icono,
        contentDescription = servicio.nombre,
        tint = AzulTarifas,
        modifier = Modifier.size(28.dp)
    )
}

// Convierte la unidad del servicio al texto solicitado por la guía.
fun Servicio.textoPrecio(): String {
    val unidadTexto = when (unidad.uppercase()) {
        "KG" -> "por kg"
        "PRENDA" -> "por prenda"
        "UNIDAD" -> "por unidad"
        else -> "por unidad"
    }

    return "S/ %.2f %s".format(
        Locale.US,
        precio,
        unidadTexto
    )
}
