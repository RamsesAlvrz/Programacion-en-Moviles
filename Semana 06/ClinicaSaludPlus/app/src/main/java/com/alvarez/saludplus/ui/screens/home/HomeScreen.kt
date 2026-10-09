package com.alvarez.saludplus.ui.screens.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.IconoEspecialidad
import com.alvarez.saludplus.ui.components.IconoSaludPlus

private val FondoInicio = Color(0xFFF5FAFC)
private val TextoInicio = Color(0xFF17324F)
private val AzulInicio = Color(0xFF2378C9)

@Composable
fun HomeScreen(
    onEspecialidades: () -> Unit,
    onEspecialidad: (Int) -> Unit,
    onCitas: () -> Unit,
    onResultados: () -> Unit,
    onNotificaciones: () -> Unit
) {
    val usuario = Repositorio.usuarioActual
    val destacadas = Repositorio.especialidadesDestacadas()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoInicio)
    ) {
        // Adapta las tarjetas a la altura disponible.
        // En pantallas pequeñas se mantiene el desplazamiento.
        val altoAcceso = ((maxHeight - 220.dp) / 3.4f)
            .coerceAtLeast(136.dp)

        val altoEspecialidad = altoAcceso * 1.4f

        // Tres tarjetas completas en el ancho disponible.
        val anchoTarjeta = (maxWidth - 60.dp) / 3

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = usuario?.let {
                                "¡Hola, ${it.nombre}!"
                            } ?: "¡Hola!",
                            fontSize = 28.sp,
                            lineHeight = 30.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextoInicio
                        )

                        Text(
                            text = "¿Qué deseas hacer hoy?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoInicio
                        )
                    }

                    IconButton(
                        onClick = onNotificaciones
                    ) {
                        IconoSaludPlus(
                            tipo = "campana",
                            color = TextoInicio,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AccesoInicio(
                            titulo = "Agendar cita",
                            tipoIcono = "calendario",
                            fondo = Color(0xFFDCEEF9),
                            color = Color(0xFF287AB8),
                            onClick = onEspecialidades,
                            modifier = Modifier
                                .weight(1f)
                                .height(altoAcceso)
                        )

                        AccesoInicio(
                            titulo = "Mis citas",
                            tipoIcono = "calendario",
                            fondo = Color(0xFFD9F3E7),
                            color = Color(0xFF3B9569),
                            onClick = onCitas,
                            modifier = Modifier
                                .weight(1f)
                                .height(altoAcceso)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AccesoInicio(
                            titulo = "Mis citas",
                            tipoIcono = "persona",
                            fondo = Color(0xFFEBDFF7),
                            color = Color(0xFF995CC7),
                            onClick = onCitas,
                            modifier = Modifier
                                .weight(1f)
                                .height(altoAcceso)
                        )

                        AccesoInicio(
                            titulo = "Resultados",
                            tipoIcono = "documento",
                            fondo = Color(0xFFF9EACF),
                            color = Color(0xFFE39539),
                            onClick = onResultados,
                            modifier = Modifier
                                .weight(1f)
                                .height(altoAcceso)
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Especialidades destacadas",
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoInicio
                    )

                    TextButton(
                        onClick = onEspecialidades,
                        contentPadding = PaddingValues(
                            horizontal = 4.dp
                        )
                    ) {
                        Text(
                            text = "Ver todos",
                            color = AzulInicio,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 4.dp)
                ) {
                    items(
                        items = destacadas,
                        key = { it.id }
                    ) { especialidad ->
                        Card(
                            onClick = {
                                onEspecialidad(especialidad.id)
                            },
                            modifier = Modifier
                                .width(anchoTarjeta)
                                .height(altoEspecialidad),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = Color(0xFFD5E1E8)
                            ),
                            colors = CardDefaults.cardColors(
                                containerColor = FondoInicio
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(
                                        horizontal = 6.dp,
                                        vertical = 12.dp
                                    ),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(
                                    space = 12.dp,
                                    alignment = Alignment.CenterVertically
                                )
                            ) {
                                IconoEspecialidad(
                                    especialidadId = especialidad.id,
                                    tamano = 58.dp
                                )

                                Text(
                                    text = especialidad.nombre,
                                    fontSize = 14.sp,
                                    lineHeight = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoInicio,
                                    textAlign = TextAlign.Center,
                                    maxLines = 3,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AccesoInicio(
    titulo: String,
    tipoIcono: String,
    fondo: Color,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = fondo
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 8.dp,
                    vertical = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 12.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            IconoSaludPlus(
                tipo = tipoIcono,
                color = color,
                modifier = Modifier.size(48.dp)
            )

            Text(
                text = titulo,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}