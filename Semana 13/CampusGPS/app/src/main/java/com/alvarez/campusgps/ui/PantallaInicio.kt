
package com.alvarez.campusgps.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.campusgps.R
import com.alvarez.campusgps.model.LugarConDistancia
import com.alvarez.campusgps.util.formatearDistancia

@Composable
fun PantallaInicio(
    ultimoLugar: LugarConDistancia?,
    mostrarUltimoLugar: Boolean,
    onExplorarMapa: () -> Unit,
    onAbrirRutas: () -> Unit,
    onAbrirCercanos: () -> Unit,
    onVerUltimoLugar: () -> Unit
) {
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
            .verticalScroll(rememberScrollState())
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = colores.primary,
            shape = RoundedCornerShape(
                bottomStart = 26.dp,
                bottomEnd = 26.dp
            ),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 18.dp,
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Text(
                    text = "CAMPUSGPS  2.0",
                    color = Color(0xFF9DF1DD),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    fontSize = 11.sp
                )

                Text(
                    text = if (mostrarUltimoLugar) {
                        "Tu último destino"
                    } else {
                        "Tu campus,\nsiempre cerca."
                    },
                    color = Color.White,
                    fontSize = 29.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = if (mostrarUltimoLugar) {
                        "Consulta el último lugar que seleccionaste."
                    } else {
                        "Explora, descubre lugares y encuentra " +
                                "rutas para llegar caminando."
                    },
                    color = Color.White.copy(alpha = 0.88f),
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )

                if (!mostrarUltimoLugar) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.campus_ilustracion
                        ),
                        contentDescription =
                            "Ilustración del campus TECSUP y navegación GPS",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(155.dp)
                            .clip(RoundedCornerShape(18.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {
            if (mostrarUltimoLugar) {
                Text(
                    "Último lugar seleccionado",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                if (ultimoLugar != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = colores.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(11.dp)
                        ) {
                            Text(
                                "DESTINO RECIENTE",
                                color = colores.primary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                ultimoLugar.lugar.nombre,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Categoría: ${ultimoLugar.lugar.categoria}",
                                color = colores.onSurfaceVariant
                            )

                            Text(
                                "Distancia directa: " +
                                        (ultimoLugar.metros?.let {
                                            formatearDistancia(it)
                                        } ?: "Sin calcular"),
                                color = colores.primary
                            )

                            Button(
                                onClick = onExplorarMapa,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Abrir mapa")
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = colores.surface
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            "No hay un destino seleccionado. " +
                                    "Abre el mapa y selecciona un lugar.",
                            modifier = Modifier.padding(20.dp),
                            color = colores.onSurfaceVariant
                        )
                    }
                }
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Descubre CampusGPS",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold
                        )

                        Text(
                            "Elige lo que quieres hacer",
                            color = colores.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }

                    Surface(
                        color = colores.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "4 opciones",
                            modifier = Modifier.padding(
                                horizontal = 10.dp,
                                vertical = 7.dp
                            ),
                            color = colores.primary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }

                AccesoCampus(
                    numero = "01",
                    simbolo = "⌖",
                    titulo = "Explorar el mapa",
                    descripcion =
                        "Visualiza todos los lugares del campus.",
                    onClick = onExplorarMapa
                )

                AccesoCampus(
                    numero = "02",
                    simbolo = "↝",
                    titulo = "Rutas a pie",
                    descripcion =
                        "Selecciona un destino y calcula tu recorrido.",
                    onClick = onAbrirRutas
                )

                AccesoCampus(
                    numero = "03",
                    simbolo = "◎",
                    titulo = "Lugares cercanos",
                    descripcion =
                        "Filtra lugares dentro de un radio de 200 m.",
                    onClick = onAbrirCercanos
                )

                AccesoCampus(
                    numero = "04",
                    simbolo = "◉",
                    titulo = "Último lugar",
                    descripcion =
                        "Consulta tu destino seleccionado anteriormente.",
                    onClick = onVerUltimoLugar
                )

                Text(
                    "CAMPUSGPS · OPENSTREETMAP",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    color = colores.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun AccesoCampus(
    numero: String,
    simbolo: String,
    titulo: String,
    descripcion: String,
    onClick: () -> Unit
) {
    val colores = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colores.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                color = colores.primaryContainer,
                shape = RoundedCornerShape(14.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        simbolo,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.primary
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = colores.onSurface
                )

                Text(
                    descripcion,
                    fontSize = 11.sp,
                    lineHeight = 16.sp,
                    color = colores.onSurfaceVariant
                )
            }

            Text(
                "›",
                fontSize = 26.sp,
                color = colores.primary
            )
        }
    }
}
