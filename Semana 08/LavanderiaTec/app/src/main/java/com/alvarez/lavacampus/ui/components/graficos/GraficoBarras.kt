
package com.alvarez.lavacampus.ui.components.graficos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.graphics.Paint
import java.util.Locale

@Composable
fun GraficoBarras(
    datos: List<DatoGrafico>,
    modifier: Modifier = Modifier
) {
    // Solo mostramos servicios con ingresos registrados.
    val datosValidos = datos.filter { it.valor > 0.0 }

    val coloresVioletas = listOf(
        Color(0xFF6D28D9),
        Color(0xFFA78BFA),
        Color(0xFFD8B4FE),
        Color(0xFF8B5CF6)
    )

    val valorMaximo = datosValidos
        .maxOfOrNull { it.valor }
        ?.coerceAtLeast(1.0)
        ?: 1.0

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (datosValidos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aún no hay ingresos registrados",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        } else {

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
            ) {
                val ancho = size.width
                val alto = size.height

                val espacioSuperior = 25.dp.toPx()
                val espacioInferior = 5.dp.toPx()

                val alturaDisponible =
                    (alto - espacioSuperior - espacioInferior)
                        .coerceAtLeast(0f)

                val anchoGrupo = ancho / datosValidos.size

                val anchoBarra = minOf(
                    42.dp.toPx(),
                    anchoGrupo * 0.50f
                )

                val pinturaTexto = Paint().apply {
                    color = android.graphics.Color.DKGRAY
                    textAlign = Paint.Align.CENTER
                    textSize = 10.sp.toPx()
                    isAntiAlias = true
                    isFakeBoldText = true
                }

                // Línea inferior del gráfico.
                drawLine(
                    color = Color(0xFFE5E7EB),
                    start = Offset(
                        x = 0f,
                        y = alto - espacioInferior
                    ),
                    end = Offset(
                        x = ancho,
                        y = alto - espacioInferior
                    ),
                    strokeWidth = 1.dp.toPx()
                )

                datosValidos.forEachIndexed { indice, dato ->

                    val centroX =
                        anchoGrupo * (indice + 0.5f)

                    val alturaBarra = (
                            dato.valor / valorMaximo
                            ).toFloat() * alturaDisponible

                    val izquierda =
                        centroX - anchoBarra / 2f

                    val superior =
                        alto - espacioInferior - alturaBarra

                    // Barras con tonos violetas.
                    drawRoundRect(
                        color = coloresVioletas[
                            indice % coloresVioletas.size
                        ],
                        topLeft = Offset(
                            x = izquierda,
                            y = superior
                        ),
                        size = Size(
                            width = anchoBarra,
                            height = alturaBarra
                        ),
                        cornerRadius = CornerRadius(
                            x = 6.dp.toPx(),
                            y = 6.dp.toPx()
                        )
                    )

                    // Importe sobre cada barra.
                    val importe = if (dato.valor % 1.0 == 0.0) {
                        String.format(
                            Locale.US,
                            "%.0f",
                            dato.valor
                        )
                    } else {
                        String.format(
                            Locale.US,
                            "%.2f",
                            dato.valor
                        )
                    }

                    drawIntoCanvas { canvas ->
                        canvas.nativeCanvas.drawText(
                            importe,
                            centroX,
                            (superior - 6.dp.toPx())
                                .coerceAtLeast(11.dp.toPx()),
                            pinturaTexto
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Nombres de servicios.
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                datosValidos.forEach { dato ->
                    Text(
                        text = dato.etiqueta,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 9.sp,
                        lineHeight = 11.sp,
                        color = Color(0xFF8B93A1),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
