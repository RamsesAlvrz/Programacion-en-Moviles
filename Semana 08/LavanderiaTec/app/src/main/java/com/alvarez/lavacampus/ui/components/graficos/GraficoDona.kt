package com.alvarez.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GraficoDona(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier
) {
    val estados = listOf(
        "Recibido" to Color(0xFFF59E0B),
        "En proceso" to Color(0xFF3B82F6),
        "Listo" to Color(0xFF22C55E),
        "Entregado" to Color(0xFF475569)
    )

    val datosOrdenados = estados.map { (nombre, color) ->
        val valor = datos
            .filter { it.etiqueta.equals(nombre, ignoreCase = true) }
            .sumOf { it.valor.coerceAtLeast(0.0) }

        DatoGrafico(etiqueta = nombre, valor = valor, color = color)
    }

    // Mantener estados adicionales, como Cancelado.
    val adicionales = datos.filter { dato ->
        estados.none { it.first.equals(dato.etiqueta, ignoreCase = true) }
    }
    val datosFinales = datosOrdenados + adicionales
    val total = datosFinales.sumOf { it.valor }

    val colorPrincipal = MaterialTheme.colorScheme.onSurface
    val colorSecundario = MaterialTheme.colorScheme.onSurfaceVariant
    val colorPista = MaterialTheme.colorScheme.outlineVariant

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        // Antes: 12.dp. Ahora hay más espacio entre dona y leyenda.
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Box(
            modifier = Modifier.size(112.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val grosor = 16.dp.toPx()

                drawArc(
                    color = colorPista,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = grosor, cap = StrokeCap.Butt)
                )

                if (total > 0.0) {
                    var anguloInicial = -90f
                    datosFinales.forEach { dato ->
                        val angulo = (dato.valor / total * 360.0).toFloat()
                        if (angulo > 0f) {
                            drawArc(
                                color = dato.color,
                                startAngle = anguloInicial,
                                sweepAngle = angulo,
                                useCenter = false,
                                style = Stroke(width = grosor, cap = StrokeCap.Butt)
                            )
                        }
                        anguloInicial += angulo
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = total.toInt().toString(),
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorPrincipal
                )
                Text(
                    text = "pedidos",
                    fontSize = 10.sp,
                    color = colorSecundario
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            datosFinales.forEach { dato ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(dato.color, RoundedCornerShape(2.dp))
                    )
                    Text(
                        text = dato.etiqueta,
                        modifier = Modifier.weight(1f),
                        fontSize = 10.sp,
                        color = colorSecundario,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = dato.valor.toInt().toString(),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorPrincipal
                    )
                }
            }
        }
    }
}