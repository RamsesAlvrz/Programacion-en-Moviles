
package com.alvarez.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.murocampus.ui.theme.*

@Composable
fun Cargando() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Surface(
            modifier = Modifier.size(88.dp),
            shape = RoundedCornerShape(28.dp),
            color = AzulClaro
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    modifier = Modifier.size(42.dp),
                    color = AzulPrincipal,
                    strokeWidth = 4.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Text(
            text = "Cargando información",
            style = MaterialTheme.typography.titleMedium,
            color = TextoPrincipal
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Espera un momento, por favor.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextoSecundario,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ErrorConReintento(
    mensaje: String,
    onReintentar: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Blanco
            ),
            border = BorderStroke(1.dp, BordeSuave),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Surface(
                    modifier = Modifier.size(76.dp),
                    color = FondoError,
                    shape = RoundedCornerShape(24.dp)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        IconoAlertaCampus(
                            color = RojoError,
                            modifier = Modifier.size(38.dp)
                        )
                    }
                }

                Text(
                    text = "Algo salió mal",
                    style = MaterialTheme.typography.titleLarge,
                    color = TextoPrincipal,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = mensaje,
                    color = TextoSecundario,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = onReintentar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPrincipal
                    )
                ) {

                    Text(
                        text = "Reintentar",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

// Colores compartidos entre listado y detalle

internal fun colorUsuarioCampus(userId: Int): Color {

    return when (userId) {

        1 -> Color(0xFF176B98)
        2 -> Color(0xFF926000)
        3 -> Color(0xFF287D46)
        4 -> Color(0xFF7B4BB7)
        5 -> Color(0xFFB3261E)
        6 -> Color(0xFF00796B)
        7 -> Color(0xFFC2410C)
        8 -> Color(0xFFAD356D)
        9 -> Color(0xFF475569)
        10 -> Color(0xFF795548)

        else -> AzulPrincipal
    }
}

internal fun fondoUsuarioCampus(userId: Int): Color {

    return when (userId) {

        1 -> Color(0xFFDCECF9)
        2 -> Color(0xFFFFF1C2)
        3 -> Color(0xFFE2F3E7)
        4 -> Color(0xFFF0E5FC)
        5 -> Color(0xFFFCE8E6)
        6 -> Color(0xFFDDF3EF)
        7 -> Color(0xFFFFE9DB)
        8 -> Color(0xFFFCE7F1)
        9 -> Color(0xFFE2E8F0)
        10 -> Color(0xFFEEE5DF)

        else -> AzulClaro
    }
}

// Avatar reutilizable

@Composable
internal fun AvatarUsuarioCampus(
    userId: Int,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier.size(46.dp),
        color = fondoUsuarioCampus(userId),
        shape = CircleShape
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "U$userId",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = colorUsuarioCampus(userId)
            )
        }
    }
}

// Tarjeta de publicación

@Composable
fun TarjetaPost(
    userId: Int,
    titulo: String,
    contenido: String,
    onClick: () -> Unit = {}
) {

    // La tarjeta completa ya no recibe el evento onClick.

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 6.dp
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        border = BorderStroke(1.dp, BordeSuave),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Identificación del usuario

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AvatarUsuarioCampus(
                    userId = userId
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Usuario $userId",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextoPrincipal
                    )

                    Text(
                        text = "Comunidad universitaria",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSecundario
                    )
                }

                Surface(
                    color = SuperficieSuave,
                    shape = RoundedCornerShape(8.dp)
                ) {

                    Text(
                        text = "Publicación",
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 5.dp
                        ),
                        color = AzulOscuro,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            HorizontalDivider(
                color = BordeSuave.copy(alpha = 0.7f)
            )

            // Título de la publicación

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = TextoPrincipal,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Contenido de la publicación

            Text(
                text = contenido,
                style = MaterialTheme.typography.bodyMedium,
                color = TextoSecundario,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(
                color = BordeSuave
            )

            // Única sección pulsable de la tarjeta

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable(
                        role = Role.Button,
                        onClick = onClick
                    )
                    .padding(
                        horizontal = 8.dp,
                        vertical = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Ver publicación",
                    color = AzulPrincipal,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )

                IconoFlechaCampus(
                    color = AzulPrincipal,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

// Icono de flecha hacia la derecha

@Composable
private fun IconoFlechaCampus(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val w = size.width
        val h = size.height
        val grosor = size.minDimension * 0.085f

        drawLine(
            color = color,
            start = Offset(w * 0.15f, h * 0.50f),
            end = Offset(w * 0.85f, h * 0.50f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(w * 0.55f, h * 0.20f),
            end = Offset(w * 0.85f, h * 0.50f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(w * 0.55f, h * 0.80f),
            end = Offset(w * 0.85f, h * 0.50f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}

// Icono de alerta

@Composable
private fun IconoAlertaCampus(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val w = size.width
        val h = size.height
        val grosor = size.minDimension * 0.075f

        drawCircle(
            color = color,
            radius = size.minDimension * 0.43f,
            style = Stroke(width = grosor)
        )

        drawLine(
            color = color,
            start = Offset(w * 0.50f, h * 0.26f),
            end = Offset(w * 0.50f, h * 0.55f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawCircle(
            color = color,
            radius = grosor * 0.65f,
            center = Offset(
                w * 0.50f,
                h * 0.72f
            )
        )
    }
}
