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
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.repository.LavaRepository
import java.util.Locale

private val AzulTarifas = Color(0xFF0097B2)
private val FondoClaroTarifas = Color(0xFFF0FBFD)
private val FondoIconoTarifas = Color(0xFFE0F7FA)
private val FondoAvisoTarifas = Color(0xFFFFF2C6)

@Composable
fun TarifasScreen(
    repository: LavaRepository,
    onVolver: () -> Unit
) {
    val servicios by remember(repository) {
        repository.observarServiciosActivos()
    }.collectAsState(initial = emptyList())

    val modoOscuro = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val fondoPantalla = if (modoOscuro) MaterialTheme.colorScheme.background else FondoClaroTarifas

    // Orden visual de la guía, sin alterar los datos ni el orden guardado en Room.
    val serviciosOrdenados = remember(servicios) {
        servicios.sortedWith(compareBy { servicio ->
            when {
                servicio.nombre.contains("lavado y secado", ignoreCase = true) -> 0
                servicio.nombre.contains("planch", ignoreCase = true) -> 1
                servicio.nombre.contains("lavado en seco", ignoreCase = true) -> 2
                else -> 3
            }
        })
    }

    Scaffold(
        containerColor = fondoPantalla,
        topBar = {
            Surface(color = fondoPantalla) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                    Text(
                        text = "Tarifas y servicios",
                        modifier = Modifier.weight(1f),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
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
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.spacedBy(13.dp),
            contentPadding = PaddingValues(top = 15.dp, bottom = 24.dp)
        ) {
            items(items = serviciosOrdenados, key = { it.id }) { servicio ->
                TarjetaTarifa(servicio, modoOscuro)
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Schedule,
                        contentDescription = null,
                        tint = AzulTarifas,
                        modifier = Modifier.size(19.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        Text(
                            text = "Horario de atención",
                            color = if (modoOscuro) MaterialTheme.colorScheme.primary else AzulTarifas,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Lunes a sábado · 8:00 a 18:00",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = "Recojo en casilleros de pabellón A",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (modoOscuro) Color(0xFF493B1F) else FondoAvisoTarifas,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 15.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        val colorAviso = if (modoOscuro) Color(0xFFFFDB8C) else Color(0xFF78571E)
                        Text(
                            text = "Mínimo 1 kg por servicio de lavado.",
                            fontSize = 11.sp,
                            color = colorAviso,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Máximo 3 pedidos activos por estudiante.",
                            fontSize = 11.sp,
                            color = colorAviso,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarjetaTarifa(servicio: Servicio, modoOscuro: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 17.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Surface(
                color = if (modoOscuro) MaterialTheme.colorScheme.secondaryContainer else FondoIconoTarifas,
                shape = RoundedCornerShape(11.dp),
                modifier = Modifier.size(58.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    IconoTarifa(servicio, modoOscuro)
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = servicio.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = servicio.textoPrecio(),
                    color = if (modoOscuro) MaterialTheme.colorScheme.primary else AzulTarifas,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Entrega en ${servicio.horasEntrega} h",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
private fun IconoTarifa(servicio: Servicio, modoOscuro: Boolean) {
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
        tint = if (modoOscuro) MaterialTheme.colorScheme.onSecondaryContainer else AzulTarifas,
        modifier = Modifier.size(29.dp)
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
    return "S/ %.2f %s".format(Locale.US, precio, unidadTexto)
}
