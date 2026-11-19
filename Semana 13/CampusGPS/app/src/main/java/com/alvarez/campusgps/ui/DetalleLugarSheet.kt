package com.alvarez.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alvarez.campusgps.model.EstadoRuta
import com.alvarez.campusgps.model.LugarConDistancia
import com.alvarez.campusgps.util.formatearDistancia
import kotlin.math.ceil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleLugarSheet(
    item: LugarConDistancia,
    estadoRuta: EstadoRuta,
    tieneUbicacion: Boolean,
    onCerrar: () -> Unit,
    onComoLlegar: () -> Unit,
    onLimpiarRuta: () -> Unit
) {
    val colores = MaterialTheme.colorScheme
    val rutaDelLugar = (estadoRuta as? EstadoRuta.Lista)
        ?.takeIf { it.destinoId == item.lugar.id }
        ?.ruta

    ModalBottomSheet(
        onDismissRequest = onCerrar,
        containerColor = colores.surface
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(start = 22.dp, end = 22.dp, bottom = 26.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "DETALLES DEL LUGAR",
                style = MaterialTheme.typography.labelMedium,
                color = colores.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                item.lugar.nombre,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Categoría: ${item.lugar.categoria}",
                color = colores.onSurfaceVariant
            )
            HorizontalDivider()
            Text(
                "Distancia directa: " +
                        (item.metros?.let { formatearDistancia(it) } ?: "Sin calcular")
            )
            if (rutaDelLugar != null) {
                Text(
                    "Recorrido a pie: " +
                            formatearDistancia(rutaDelLugar.distanciaMetros.toFloat()),
                    fontWeight = FontWeight.SemiBold,
                    color = colores.primary
                )
                val minutos = ceil(rutaDelLugar.duracionSegundos / 60.0).toInt()
                Text("Duración estimada: $minutos min")
            }
            Text(
                "Coordenadas: ${item.lugar.latitud}, ${item.lugar.longitud}",
                style = MaterialTheme.typography.bodySmall,
                color = colores.onSurfaceVariant
            )
            if (!tieneUbicacion) {
                Text(
                    "Obtén tu ubicación antes de solicitar indicaciones.",
                    style = MaterialTheme.typography.bodySmall,
                    color = colores.error
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onComoLlegar,
                    enabled = tieneUbicacion && estadoRuta !is EstadoRuta.Cargando,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Cómo llegar") }
                OutlinedButton(
                    onClick = onLimpiarRuta,
                    enabled = estadoRuta !is EstadoRuta.SinRuta,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) { Text("Limpiar ruta") }
            }
            TextButton(
                onClick = onCerrar,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Cerrar") }
        }
    }
}
