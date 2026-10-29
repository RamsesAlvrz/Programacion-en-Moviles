package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.DryCleaning
import androidx.compose.material.icons.outlined.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.model.LineaPedido
import com.alvarez.lavacampus.viewmodel.NuevoPedidoViewModel
import java.util.Locale
import java.text.SimpleDateFormat
import androidx.compose.ui.graphics.Color

private val TurquesaPedido = Color(0xFF0895B5)
private val FondoPedido = Color(0xFFF0FCFF)
private val TextoPedido = Color(0xFF263341)
private val GrisPedido = Color(0xFF8B95A2)
private val BordePedido = Color(0xFFE4E9ED)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NuevoPedidoScreen(
    viewModel: NuevoPedidoViewModel,
    usuarioId: Int,
    onVolver: () -> Unit,
    onPedidoRegistrado: (Long) -> Unit
) {
    val lineas by viewModel.lineas.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()
    val guardando by viewModel.guardando.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    // COMMIT 14: Estado de recepción de pedidos guardado en DataStore.
    val recibirPedidos by viewModel.recibirPedidos.collectAsStateWithLifecycle(
        initialValue = true
    )

    var nota by rememberSaveable { mutableStateOf("") }

    // La carga de servicios se realiza desde MainActivity para distinguir
    // entre un pedido nuevo y la repetición de uno anterior.

// Servicios seleccionados por el estudiante.
    val lineasSeleccionadas = lineas.filter { it.seleccionado }

// MEJORA B: Calcular el subtotal, descuento y total final.
    val subtotal = viewModel.calcularSubtotal()
    val descuento = viewModel.calcularDescuento()
    val total = viewModel.calcularTotal()

    val haySeleccion = lineasSeleccionadas.isNotEmpty()

    Scaffold(
        containerColor = FondoPedido,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Nuevo pedido",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPedido
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = TextoPedido
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = FondoPedido
                )
            )
        }
    ) { paddingValues ->

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
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            item {
                Text(
                    text = "¿Qué necesitas?",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPedido
                )
            }

            // COMMIT 14: Avisar cuando la lavandería no acepta pedidos.
            if (!recibirPedidos) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFF1F0),
                        border = BorderStroke(1.dp, Color(0xFFF5B7B1))
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "Servicio temporalmente no disponible",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFFB42318)
                            )
                            Text(
                                text = "La lavandería no está recibiendo nuevos pedidos en este momento. Inténtalo más tarde.",
                                fontSize = 12.sp,
                                color = Color(0xFF7A271A)
                            )
                        }
                    }
                }
            }

            if (cargando) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = TurquesaPedido
                        )
                    }
                }
            } else {
                items(
                    items = lineas,
                    key = { it.servicio.id }
                ) { linea ->
                    ServicioSeleccionCard(
                        linea = linea,
                        habilitado = !guardando && recibirPedidos,
                        onAumentar = {
                            viewModel.aumentarCantidad(
                                linea.servicio.id
                            )
                        },
                        onDisminuir = {
                            viewModel.disminuirCantidad(
                                linea.servicio.id
                            )
                        }
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White,
                            RoundedCornerShape(11.dp)
                        )
                        .padding(
                            horizontal = 12.dp,
                            vertical = 9.dp
                        )
                ) {
                    Text(
                        text = "Nota (opcional)",
                        fontSize = 11.sp,
                        color = GrisPedido
                    )

                    Spacer(Modifier.height(5.dp))

                    BasicTextField(
                        value = nota,
                        onValueChange = { nota = it },
                        enabled = !guardando && recibirPedidos,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 13.sp,
                            color = TextoPedido
                        ),
                        minLines = 1,
                        maxLines = 3,
                        decorationBox = { innerTextField ->
                            Box {
                                innerTextField()
                            }
                        }
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFFE9FCFF),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(13.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Resumen",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TurquesaPedido
                    )

                    if (lineasSeleccionadas.isEmpty()) {
                        Text(
                            text = "Selecciona al menos un servicio",
                            fontSize = 12.sp,
                            color = GrisPedido
                        )
                    } else {
                        lineasSeleccionadas.forEach { linea ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${linea.servicio.nombre}: ${
                                        if (linea.servicio.unidad == "KG") {
                                            String.format(
                                                Locale.US,
                                                "%.1f kg",
                                                linea.cantidad
                                            )
                                        } else {
                                            "${linea.cantidad.toInt()} ${
                                                if (linea.cantidad == 1.0) {
                                                    "unidad"
                                                } else {
                                                    "unidades"
                                                }
                                            }"
                                        }
                                    }",
                                    modifier = Modifier.weight(1f),
                                    fontSize = 11.sp,
                                    color = GrisPedido,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(Modifier.width(8.dp))

                                Text(
                                    text = "S/ ${
                                        String.format(
                                            Locale.US,
                                            "%.2f",
                                            linea.subtotal
                                        )
                                    }",
                                    fontSize = 11.sp,
                                    color = TextoPedido,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    // MEJORA B: Mostrar el descuento por volumen.
                    if (descuento > 0.0) {

                        // Subtotal original antes de aplicar el descuento.
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Subtotal",
                                fontSize = 12.sp,
                                color = GrisPedido
                            )

                            Text(
                                text = "S/ ${String.format(Locale.US, "%.2f", subtotal)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = TextoPedido
                            )
                        }

                        // Descuento aplicado únicamente al lavado por volumen.
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Descuento por volumen (10 %)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF168A58)
                            )

                            Text(
                                text = "-S/ ${String.format(Locale.US, "%.2f", descuento)}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF168A58)
                            )
                        }

                        Text(
                            text = "Descuento aplicado por superar los 5 kg de lavado.",
                            fontSize = 10.sp,
                            color = Color(0xFF168A58)
                        )
                    }

                    HorizontalDivider(
                        color = Color(0xFFD2EAF0)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoPedido
                        )

                        Text(
                            text = "S/ ${
                                String.format(
                                    Locale.US,
                                    "%.2f",
                                    total
                                )
                            }",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = TurquesaPedido
                        )
                    }
                }
            }

            item {
                Text(
                    text = if (haySeleccion) {
                        "Entrega estimada: ${
                            formatearFechaEntrega(
                                viewModel.calcularFechaEntrega()
                            )
                        }"
                    } else {
                        "Entrega estimada: Selecciona un servicio"
                    },
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp,
                    color = GrisPedido
                )
            }

            if (error != null) {
                item {
                    Text(
                        text = error.orEmpty(),
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        viewModel.registrarPedido(
                            usuarioId = usuarioId,
                            nota = nota
                        ) { pedidoId ->
                            if (pedidoId != null) {
                                onPedidoRegistrado(pedidoId)
                            }
                        }
                    },
                    enabled = haySeleccion &&
                            recibirPedidos &&
                            !guardando &&
                            !cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TurquesaPedido
                    ),
                    shape = RoundedCornerShape(11.dp)
                ) {
                    if (guardando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (recibirPedidos) "Confirmar pedido" else "Pedidos no disponibles",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ServicioSeleccionCard(
    linea: LineaPedido,
    habilitado: Boolean,
    onAumentar: () -> Unit,
    onDisminuir: () -> Unit
) {
    val servicio = linea.servicio
    val esKilogramo = servicio.unidad == "KG"
    val cantidadMaxima = if (esKilogramo) 10.0 else 30.0
    val seleccionado = linea.seleccionado

    val cantidadTexto = if (esKilogramo) {
        String.format(
            Locale.US,
            "%.1f kg",
            linea.cantidad
        )
    } else {
        linea.cantidad.toInt().toString()
    }

    val icono = when {
        servicio.nombre.contains(
            "planch",
            ignoreCase = true
        ) -> Icons.Outlined.DryCleaning

        servicio.nombre.contains(
            "seco",
            ignoreCase = true
        ) -> Icons.Outlined.Checkroom

        else -> Icons.Outlined.LocalLaundryService
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(13.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = if (seleccionado) 1.5.dp else 1.dp,
            color = if (seleccionado) {
                TurquesaPedido
            } else {
                BordePedido
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(39.dp)
                    .background(
                        if (seleccionado) {
                            Color(0xFFD8F7FB)
                        } else {
                            Color(0xFFF2F4F6)
                        },
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = if (seleccionado) {
                        TurquesaPedido
                    } else {
                        GrisPedido
                    },
                    modifier = Modifier.size(21.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = servicio.nombre,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPedido
                )

                Text(
                    text = "S/ ${
                        String.format(
                            Locale.US,
                            "%.2f",
                            servicio.precio
                        )
                    } por ${
                        when (servicio.unidad) {
                            "KG" -> "kg"
                            "PRENDA" -> "prenda"
                            else -> "unidad"
                        }
                    } · ${servicio.horasEntrega} h",
                    fontSize = 11.sp,
                    color = GrisPedido
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BotonCantidad(
                        simbolo = "−",
                        habilitado = habilitado &&
                                linea.cantidad > 0.0,
                        onClick = onDisminuir
                    )

                    Text(
                        text = cantidadTexto,
                        modifier = Modifier
                            .widthIn(min = 44.dp)
                            .padding(horizontal = 5.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPedido
                    )

                    BotonCantidad(
                        simbolo = "+",
                        habilitado = habilitado &&
                                linea.cantidad < cantidadMaxima,
                        onClick = onAumentar
                    )
                }
            }
        }
    }
}

@Composable
private fun BotonCantidad(
    simbolo: String,
    habilitado: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(25.dp)
            .clip(CircleShape)
            .background(
                if (habilitado) {
                    TurquesaPedido
                } else {
                    Color(0xFFE5E7EB)
                }
            )
            .clickable(
                enabled = habilitado,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = simbolo,
            color = if (habilitado) {
                Color.White
            } else {
                GrisPedido
            },
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatearFechaEntrega(fecha: String): String {
    return try {
        val formatoEntrada = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale("es", "PE")
        )
        formatoEntrada.isLenient = false

        val formatoSalida = SimpleDateFormat(
            "EEE d MMM",
            Locale("es", "PE")
        )

        val fechaConvertida = formatoEntrada.parse(fecha)

        if (fechaConvertida != null) {
            formatoSalida.format(fechaConvertida)
                .replace(".", "")
                .lowercase(Locale("es", "PE"))
        } else {
            fecha
        }
    } catch (e: Exception) {
        fecha
    }
}
