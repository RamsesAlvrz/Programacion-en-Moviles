package com.alvarez.saludplus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconoEspecialidad(
    especialidadId: Int,
    tamano: Dp = 52.dp,
    modifier: Modifier = Modifier
) {
    val fondo = when (especialidadId) {
        1 -> Color(0xFFD9EFF7)
        2 -> Color(0xFFFFDDD8)
        3 -> Color(0xFFF0DDF3)
        4 -> Color(0xFFFFE8C8)
        5 -> Color(0xFFD5EEF7)
        else -> Color(0xFFDCE8FC)
    }

    val color = when (especialidadId) {
        1 -> Color(0xFF3289BE)
        2 -> Color(0xFFE46B6B)
        3 -> Color(0xFFAD69B7)
        4 -> Color(0xFFE79B3D)
        5 -> Color(0xFF2995BE)
        else -> Color(0xFF397DC0)
    }

    Box(
        modifier = modifier
            .size(tamano)
            .clip(RoundedCornerShape(12.dp))
            .background(fondo)
    ) {
        Canvas(Modifier.size(tamano)) {
            val w = size.width
            val h = size.height
            val trazo = Stroke(width = w * 0.055f)

            when (especialidadId) {
                // Medicina general: profesional de salud.
                1 -> {
                    drawCircle(
                        color = color,
                        radius = w * 0.13f,
                        center = Offset(w * 0.5f, h * 0.3f)
                    )

                    drawRoundRect(
                        color = color,
                        topLeft = Offset(w * 0.25f, h * 0.49f),
                        size = Size(w * 0.5f, h * 0.31f),
                        cornerRadius = CornerRadius(w * 0.12f)
                    )

                    drawLine(
                        color = Color.White,
                        start = Offset(w * 0.42f, h * 0.61f),
                        end = Offset(w * 0.58f, h * 0.61f),
                        strokeWidth = w * 0.045f
                    )

                    drawLine(
                        color = Color.White,
                        start = Offset(w * 0.5f, h * 0.53f),
                        end = Offset(w * 0.5f, h * 0.69f),
                        strokeWidth = w * 0.045f
                    )
                }

                // Cardiología: corazón y pulso.
                2 -> {
                    val corazon = Path().apply {
                        moveTo(w * 0.5f, h * 0.79f)
                        cubicTo(
                            w * 0.12f, h * 0.55f,
                            w * 0.13f, h * 0.24f,
                            w * 0.32f, h * 0.24f
                        )
                        cubicTo(
                            w * 0.42f, h * 0.24f,
                            w * 0.47f, h * 0.29f,
                            w * 0.5f, h * 0.36f
                        )
                        cubicTo(
                            w * 0.56f, h * 0.22f,
                            w * 0.75f, h * 0.18f,
                            w * 0.82f, h * 0.35f
                        )
                        cubicTo(
                            w * 0.91f, h * 0.55f,
                            w * 0.65f, h * 0.7f,
                            w * 0.5f, h * 0.79f
                        )
                        close()
                    }

                    drawPath(corazon, color)

                    val pulso = Path().apply {
                        moveTo(w * 0.27f, h * 0.49f)
                        lineTo(w * 0.4f, h * 0.49f)
                        lineTo(w * 0.46f, h * 0.4f)
                        lineTo(w * 0.54f, h * 0.61f)
                        lineTo(w * 0.6f, h * 0.49f)
                        lineTo(w * 0.74f, h * 0.49f)
                    }

                    drawPath(
                        path = pulso,
                        color = Color.White,
                        style = Stroke(w * 0.035f)
                    )
                }

                // Dermatología: mano con una marca en la piel.
                3 -> {
                    val mano = Path().apply {
                        moveTo(w * 0.36f, h * 0.83f)
                        lineTo(w * 0.31f, h * 0.64f)
                        lineTo(w * 0.19f, h * 0.49f)

                        quadraticBezierTo(
                            w * 0.14f, h * 0.39f,
                            w * 0.24f, h * 0.38f
                        )

                        lineTo(w * 0.34f, h * 0.48f)
                        lineTo(w * 0.34f, h * 0.25f)

                        quadraticBezierTo(
                            w * 0.34f, h * 0.16f,
                            w * 0.42f, h * 0.22f
                        )

                        lineTo(w * 0.44f, h * 0.43f)
                        lineTo(w * 0.46f, h * 0.18f)

                        quadraticBezierTo(
                            w * 0.49f, h * 0.1f,
                            w * 0.55f, h * 0.19f
                        )

                        lineTo(w * 0.55f, h * 0.43f)
                        lineTo(w * 0.59f, h * 0.24f)

                        quadraticBezierTo(
                            w * 0.63f, h * 0.16f,
                            w * 0.68f, h * 0.25f
                        )

                        lineTo(w * 0.65f, h * 0.46f)
                        lineTo(w * 0.7f, h * 0.34f)

                        quadraticBezierTo(
                            w * 0.76f, h * 0.29f,
                            w * 0.79f, h * 0.38f
                        )

                        lineTo(w * 0.73f, h * 0.66f)
                        lineTo(w * 0.67f, h * 0.83f)
                        close()
                    }

                    drawPath(
                        path = mano,
                        color = color
                    )

                    drawCircle(
                        color = Color.White,
                        radius = w * 0.07f,
                        center = Offset(w * 0.53f, h * 0.61f)
                    )

                    drawCircle(
                        color = color,
                        radius = w * 0.025f,
                        center = Offset(w * 0.53f, h * 0.61f)
                    )
                }

                // Pediatría: rostro de bebé.
                4 -> {
                    drawCircle(
                        color,
                        w * 0.08f,
                        Offset(w * 0.23f, h * 0.48f)
                    )

                    drawCircle(
                        color,
                        w * 0.08f,
                        Offset(w * 0.77f, h * 0.48f)
                    )

                    drawCircle(
                        color,
                        w * 0.28f,
                        Offset(w * 0.5f, h * 0.48f)
                    )

                    drawCircle(
                        Color.White,
                        w * 0.03f,
                        Offset(w * 0.4f, h * 0.45f)
                    )

                    drawCircle(
                        Color.White,
                        w * 0.03f,
                        Offset(w * 0.6f, h * 0.45f)
                    )

                    drawArc(
                        color = Color.White,
                        startAngle = 10f,
                        sweepAngle = 160f,
                        useCenter = false,
                        topLeft = Offset(w * 0.4f, h * 0.52f),
                        size = Size(w * 0.2f, h * 0.12f),
                        style = Stroke(w * 0.035f)
                    )

                    drawArc(
                        color = color,
                        startAngle = 180f,
                        sweepAngle = 230f,
                        useCenter = false,
                        topLeft = Offset(w * 0.43f, h * 0.12f),
                        size = Size(w * 0.16f, h * 0.17f),
                        style = trazo
                    )
                }

                // Traumatología: hueso.
                5 -> {
                    drawLine(
                        color,
                        Offset(w * 0.34f, h * 0.68f),
                        Offset(w * 0.65f, h * 0.34f),
                        strokeWidth = w * 0.17f
                    )

                    listOf(
                        Offset(w * 0.28f, h * 0.63f),
                        Offset(w * 0.39f, h * 0.75f),
                        Offset(w * 0.6f, h * 0.26f),
                        Offset(w * 0.73f, h * 0.38f)
                    ).forEach {
                        drawCircle(color, w * 0.11f, it)
                    }
                }

                // Oftalmología: ojo.
                else -> {
                    val ojo = Path().apply {
                        moveTo(w * 0.15f, h * 0.5f)
                        quadraticBezierTo(
                            w * 0.5f, h * 0.12f,
                            w * 0.85f, h * 0.5f
                        )
                        quadraticBezierTo(
                            w * 0.5f, h * 0.88f,
                            w * 0.15f, h * 0.5f
                        )
                        close()
                    }

                    drawPath(ojo, color)

                    drawCircle(
                        Color.White,
                        w * 0.14f,
                        Offset(w * 0.5f, h * 0.5f)
                    )

                    drawCircle(
                        color,
                        w * 0.075f,
                        Offset(w * 0.5f, h * 0.5f)
                    )
                }
            }
        }
    }
}