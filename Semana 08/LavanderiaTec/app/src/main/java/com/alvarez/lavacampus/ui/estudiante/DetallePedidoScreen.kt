
package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.data.local.entity.DetallePedido
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.viewmodel.DetallePedidoViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private val TurquesaDetalle = Color(0xFF0895B5)
private val FondoDetalle = Color(0xFFF0FCFF)
private val TextoDetalle = Color(0xFF263341)
private val GrisDetalle = Color(0xFF8B95A2)
private val BordeDetalle = Color(0xFFE4E9ED)
private val AmarilloDetalle = Color(0xFFFFF4C9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetallePedidoScreen(
    pedidoId: Int,
    viewModel: DetallePedidoViewModel,
    onVolver: () -> Unit
) {
    val pedidoConDetalle by viewModel.pedido.collectAsStateWithLifecycle()
    val servicios by viewModel.servicios.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()
    val mensajeError by viewModel.mensajeError.collectAsStateWithLifecycle()

    var mostrarConfirmacion by remember { mutableStateOf(false) }

    LaunchedEffect(pedidoId) {
        viewModel.observarPedido(pedidoId)
    }

    val pedido = pedidoConDetalle?.pedido
    val puedeCancelar = pedido?.estado == "RECIBIDO"

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            title = {
                Text("Cancelar pedido")
            },
            text = {
                Text("¿Estás seguro de que deseas cancelar este pedido?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                        viewModel.cancelar()
                    }
                ) {
                    Text("Sí, cancelar", color = Color(0xFFD94A4A))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarConfirmacion = false }
                ) {
                    Text("Volver")
                }
            }
        )
    }

    Scaffold(
        containerColor = FondoDetalle,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Pedido #${pedidoId.toString().padStart(3, '0')}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoDetalle
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextoDetalle
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = FondoDetalle
                )
            )
        }
    ) { paddingValues ->

        if (cargando && pedidoConDetalle == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = TurquesaDetalle)
            }
        } else if (pedidoConDetalle == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mensajeError ?: "No se encontró el pedido",
                    color = GrisDetalle
                )
            }
        } else {
            val datos = pedidoConDetalle!!

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 4.dp,
                    bottom = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // TARJETA SUPERIOR
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(13.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(1.dp, BordeDetalle)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .background(
                                        Color(0xFFDFF5FF),
                                        RoundedCornerShape(11.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.LocalLaundryService,
                                    contentDescription = null,
                                    tint = Color(0xFF477DF0),
                                    modifier = Modifier.size(28.dp)
                                )
                            }

                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Registrado el ${
                                        fechaCortaDetalle(datos.pedido.fechaRegistro)
                                    }",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoDetalle
                                )

                                Text(
                                    text = "Entrega estimada: ${
                                        fechaCortaDetalle(datos.pedido.fechaEntrega)
                                    }",
                                    fontSize = 10.sp,
                                    color = GrisDetalle
                                )

                                EstadoDetalleChip(datos.pedido.estado)
                            }
                        }
                    }
                }

                // SEGUIMIENTO
                item {
                    Text(
                        text = "Seguimiento",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoDetalle
                    )
                }

                item {
                    SeguimientoVertical(
                        estado = datos.pedido.estado,
                        fechaRegistro = datos.pedido.fechaRegistro,
                        fechaEntrega = datos.pedido.fechaEntrega
                    )
                }

                // DETALLE DE SERVICIOS
                item {
                    Text(
                        text = "Detalle",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoDetalle
                    )
                }

                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(1.dp, BordeDetalle)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            datos.detalles.forEach { detalle ->
                                val servicio = servicios.find {
                                    it.id == detalle.servicioId
                                }

                                FilaServicioDetalle(
                                    detalle = detalle,
                                    servicio = servicio
                                )
                            }

                            HorizontalDivider(color = BordeDetalle)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoDetalle
                                )

                                Text(
                                    text = monedaDetalle(datos.pedido.total),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TurquesaDetalle
                                )
                            }
                        }
                    }
                }

                // PAGO Y CASILLERO
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(AmarilloDetalle)
                            .padding(13.dp),
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = if (datos.pedido.pagado) {
                                "Pagado"
                            } else {
                                "Pago pendiente · se paga al recoger"
                            },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFAA7510)
                        )

                        Text(
                            text = "Casillero: ${
                                datos.pedido.casillero
                                    ?: "Se asignará al estar listo"
                            }",
                            fontSize = 11.sp,
                            color = Color(0xFFAA7510)
                        )
                    }
                }

                // ERROR DE CANCELACIÓN
                if (mensajeError != null) {
                    item {
                        Text(
                            text = mensajeError.orEmpty(),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                // BOTÓN DE CANCELACIÓN
                item {
                    OutlinedButton(
                        onClick = { mostrarConfirmacion = true },
                        enabled = puedeCancelar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(11.dp),
                        border = BorderStroke(
                            1.dp,
                            if (puedeCancelar) {
                                TurquesaDetalle
                            } else {
                                GrisDetalle
                            }
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = TurquesaDetalle,
                            disabledContentColor = GrisDetalle
                        )
                    ) {
                        Text(
                            text = "Cancelar pedido",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                item {
                    Text(
                        text = "Solo se cancela antes de empezar el lavado",
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 10.sp,
                        color = GrisDetalle,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun EstadoDetalleChip(estado: String) {
    val color = when (estado) {
        "RECIBIDO" -> Color(0xFF0895B5)
        "EN_PROCESO" -> Color(0xFF477DF0)
        "LISTO" -> Color(0xFF159B73)
        "ENTREGADO" -> Color(0xFF159B73)
        "CANCELADO" -> Color(0xFFD94A4A)
        else -> GrisDetalle
    }

    val etiqueta = when (estado) {
        "RECIBIDO" -> "Recibido"
        "EN_PROCESO" -> "En proceso"
        "LISTO" -> "Listo"
        "ENTREGADO" -> "Entregado"
        "CANCELADO" -> "Cancelado"
        else -> estado
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            text = etiqueta,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
private fun SeguimientoVertical(
    estado: String,
    fechaRegistro: String,
    fechaEntrega: String
) {
    val pasos = listOf(
        "Recibido",
        "En proceso",
        "Listo",
        "Entregado"
    )

    val indiceActual = when (estado) {
        "RECIBIDO" -> 0
        "EN_PROCESO" -> 1
        "LISTO" -> 2
        "ENTREGADO" -> 3
        else -> -1
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(1.dp, BordeDetalle)
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 13.dp,
                vertical = 12.dp
            )
        ) {
            pasos.forEachIndexed { index, paso ->
                val completado = index <= indiceActual

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(43.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(22.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(21.dp)
                                .background(
                                    if (completado) {
                                        TurquesaDetalle
                                    } else {
                                        Color.White
                                    },
                                    CircleShape
                                )
                                .then(
                                    if (!completado) {
                                        Modifier.background(
                                            Color.Transparent,
                                            CircleShape
                                        )
                                    } else {
                                        Modifier
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            if (completado) {
                                Text(
                                    text = "✓",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .background(
                                            Color.White,
                                            CircleShape
                                        )
                                        .then(
                                            Modifier
                                                .padding(1.dp)
                                                .background(
                                                    Color(0xFFF1F3F5),
                                                    CircleShape
                                                )
                                        )
                                )
                            }
                        }

                        if (index < pasos.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(22.dp)
                                    .background(
                                        if (index < indiceActual) {
                                            TurquesaDetalle
                                        } else {
                                            BordeDetalle
                                        }
                                    )
                            )
                        }
                    }

                    Spacer(Modifier.width(9.dp))

                    Text(
                        text = paso,
                        modifier = Modifier.weight(1f),
                        fontSize = 12.sp,
                        fontWeight = if (index == indiceActual) {
                            FontWeight.Bold
                        } else {
                            FontWeight.Normal
                        },
                        color = if (completado) {
                            TextoDetalle
                        } else {
                            GrisDetalle
                        }
                    )

                    Text(
                        text = when {
                            index == 0 -> fechaCortaDetalle(fechaRegistro)
                            index == 3 && estado == "ENTREGADO" ->
                                fechaCortaDetalle(fechaEntrega)
                            else -> "Pendiente"
                        },
                        fontSize = 10.sp,
                        color = GrisDetalle
                    )
                }
            }
        }
    }
}

@Composable
private fun FilaServicioDetalle(
    detalle: DetallePedido,
    servicio: Servicio?
) {
    val unidad = when (servicio?.unidad) {
        "KG" -> "kg"
        "PRENDA" -> "prendas"
        else -> "unidades"
    }

    val cantidad = if (servicio?.unidad == "KG") {
        String.format(Locale.US, "%.1f", detalle.cantidad)
    } else {
        detalle.cantidad.toInt().toString()
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${servicio?.nombre ?: "Servicio #${detalle.servicioId}"}" +
                    " · $cantidad $unidad × ${monedaDetalle(detalle.precioUnitario)}",
            modifier = Modifier.weight(1f),
            fontSize = 10.sp,
            color = GrisDetalle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = monedaDetalle(detalle.subtotal),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextoDetalle
        )
    }
}

private fun monedaDetalle(valor: Double): String {
    return "S/ " + String.format(Locale.US, "%.2f", valor)
}


private fun fechaCortaDetalle(fecha: String): String {

    val idioma = Locale("es", "PE")

    // Formatos de fecha que pueden existir en Room.
    val formatosEntrada = listOf(
        "yyyy-MM-dd",
        "dd/MM/yyyy HH:mm",
        "dd/MM/yyyy",
        "yyyy-MM-dd HH:mm",
        "yyyy-MM-dd'T'HH:mm:ss"
    )

    val formatoSalida = SimpleDateFormat(
        "EEE d MMM",
        idioma
    )

    for (formato in formatosEntrada) {
        try {
            val entrada = SimpleDateFormat(
                formato,
                Locale.US
            ).apply {
                isLenient = false
            }

            val fechaConvertida = entrada.parse(fecha)

            if (fechaConvertida != null) {
                return formatoSalida
                    .format(fechaConvertida)
                    .replace(".", "")
                    .lowercase(idioma)
            }

        } catch (e: java.text.ParseException) {
            // Si no coincide, probar el siguiente formato.
        }
    }

    // Si no reconoce la fecha, conserva el valor original.
    return fecha
}


