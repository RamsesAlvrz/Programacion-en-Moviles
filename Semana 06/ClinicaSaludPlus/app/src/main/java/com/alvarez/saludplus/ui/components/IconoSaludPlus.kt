package com.alvarez.saludplus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun IconoSaludPlus(
    tipo: String,
    color: Color,
    modifier: Modifier = Modifier.size(28.dp)
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val grosor = size.minDimension * 0.075f
        val trazo = Stroke(width = grosor)

        when (tipo) {
            "calendario" -> {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.18f, h * 0.22f),
                    size = Size(w * 0.64f, h * 0.62f),
                    style = trazo
                )

                drawLine(
                    color = color,
                    start = Offset(w * 0.18f, h * 0.42f),
                    end = Offset(w * 0.82f, h * 0.42f),
                    strokeWidth = grosor
                )

                listOf(0.34f, 0.66f).forEach {
                    drawLine(
                        color = color,
                        start = Offset(w * it, h * 0.12f),
                        end = Offset(w * it, h * 0.30f),
                        strokeWidth = grosor
                    )
                }

                listOf(0.52f, 0.65f).forEach {
                    drawLine(
                        color = color,
                        start = Offset(w * 0.35f, h * it),
                        end = Offset(w * 0.65f, h * it),
                        strokeWidth = grosor
                    )
                }
            }

            "persona" -> {
                drawCircle(
                    color = color,
                    radius = w * 0.17f,
                    center = Offset(w * 0.50f, h * 0.28f)
                )

                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.20f, h * 0.53f),
                    size = Size(w * 0.60f, h * 0.32f),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        w * 0.14f,
                        h * 0.14f
                    )
                )
            }

            "documento" -> {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.24f, h * 0.12f),
                    size = Size(w * 0.52f, h * 0.76f),
                    style = trazo
                )

                listOf(0.35f, 0.50f, 0.65f).forEach {
                    drawLine(
                        color = color,
                        start = Offset(w * 0.36f, h * it),
                        end = Offset(w * 0.64f, h * it),
                        strokeWidth = grosor
                    )
                }
            }

            "inicio" -> {
                val casa = Path().apply {
                    moveTo(w * 0.12f, h * 0.45f)
                    lineTo(w * 0.50f, h * 0.12f)
                    lineTo(w * 0.88f, h * 0.45f)
                    lineTo(w * 0.78f, h * 0.45f)
                    lineTo(w * 0.78f, h * 0.87f)
                    lineTo(w * 0.59f, h * 0.87f)
                    lineTo(w * 0.59f, h * 0.62f)
                    lineTo(w * 0.41f, h * 0.62f)
                    lineTo(w * 0.41f, h * 0.87f)
                    lineTo(w * 0.22f, h * 0.87f)
                    lineTo(w * 0.22f, h * 0.45f)
                    close()
                }

                drawPath(casa, color)
            }

            "campana" -> {
                val campana = Path().apply {
                    moveTo(w * 0.22f, h * 0.70f)
                    lineTo(w * 0.28f, h * 0.58f)
                    lineTo(w * 0.28f, h * 0.38f)
                    cubicTo(
                        w * 0.28f, h * 0.08f,
                        w * 0.72f, h * 0.08f,
                        w * 0.72f, h * 0.38f
                    )
                    lineTo(w * 0.72f, h * 0.58f)
                    lineTo(w * 0.78f, h * 0.70f)
                    close()
                }

                drawPath(
                    path = campana,
                    color = color,
                    style = trazo
                )

                drawCircle(
                    color = color,
                    radius = w * 0.07f,
                    center = Offset(w * 0.50f, h * 0.85f)
                )
            }

            "corazon" -> {
                val corazon = Path().apply {
                    moveTo(w * 0.50f, h * 0.86f)
                    cubicTo(
                        w * 0.02f, h * 0.52f,
                        w * 0.04f, h * 0.10f,
                        w * 0.30f, h * 0.15f
                    )
                    cubicTo(
                        w * 0.40f, h * 0.16f,
                        w * 0.46f, h * 0.23f,
                        w * 0.50f, h * 0.30f
                    )
                    cubicTo(
                        w * 0.54f, h * 0.23f,
                        w * 0.60f, h * 0.16f,
                        w * 0.70f, h * 0.15f
                    )
                    cubicTo(
                        w * 0.96f, h * 0.10f,
                        w * 0.98f, h * 0.52f,
                        w * 0.50f, h * 0.86f
                    )
                    close()
                }

                drawPath(corazon, color)
            }

            else -> {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.38f, h * 0.12f),
                    size = Size(w * 0.24f, h * 0.76f)
                )

                drawRoundRect(
                    color = color,
                    topLeft = Offset(w * 0.12f, h * 0.38f),
                    size = Size(w * 0.76f, h * 0.24f)
                )
            }
        }
    }
}