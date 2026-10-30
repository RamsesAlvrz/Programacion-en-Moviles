package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.viewmodel.MisPedidosViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private val AzulPedidos = Color(0xFF0097B2)
private val FondoEstudiante = Color(0xFFF0FBFD)
private val VerdePedidos = Color(0xFF20A35A)

@Composable
fun MisPedidosScreen(
    viewModel: MisPedidosViewModel,
    usuarioId: Int,
    onVolver: () -> Unit,
    onNuevoPedido: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    onRepetirPedido: (Int) -> Unit
) {
    val activos by viewModel.activos.collectAsState()
    val historial by viewModel.historial.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    val mensajeError by viewModel.mensajeError.collectAsState()

    var pestanaSeleccionada by remember { mutableIntStateOf(0) }
    var pedidoParaCancelar by remember { mutableStateOf<Pedido?>(null) }

    LaunchedEffect(usuarioId) {
        viewModel.cargarPedidos(usuarioId)
    }

    val pedidosVisibles =
        if (pestanaSeleccionada == 0) activos else historial

    val fondoPantalla = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
        MaterialTheme.colorScheme.background
    } else {
        FondoEstudiante
    }

    Scaffold(
        containerColor = fondoPantalla,
        topBar = {
            Surface(color = fondoPantalla) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }

                    Text(
                        text = "Mis pedidos",
                        modifier = Modifier.weight(1f),
                        fontWeight = FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        fontSize = 18.sp
                    )

                    Spacer(modifier = Modifier.width(48.dp))
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoPedido,
                containerColor = AzulPedidos,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nuevo pedido"
                )
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            // Pestañas Activos e Historial
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Activos", "Historial").forEachIndexed { index, titulo ->
                    val seleccionado = pestanaSeleccionada == index

                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { pestanaSeleccionada = index },
                        shape = RoundedCornerShape(10.dp),
                        color = if (seleccionado) AzulPedidos else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = titulo,
                                color = if (seleccionado) Color.White
                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (seleccionado)
                                    FontWeight.Bold else FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (cargando) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = AzulPedidos)
                }
            } else if (pedidosVisibles.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (pestanaSeleccionada == 0)
                            "No tienes pedidos activos."
                        else
                            "Todavía no tienes pedidos en el historial.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 90.dp)
                ) {
                    items(
                        items = pedidosVisibles,
                        key = { it.id }
                    ) { pedido ->
                        TarjetaMisPedidos(
                            pedido = pedido,
                            onVerDetalle = { onVerDetalle(pedido.id) },
                            onCancelar = { pedidoParaCancelar = pedido },
                            onRepetir = { onRepetirPedido(pedido.id) }
                        )
                    }

                    item {
                        Text(
                            text = "Aviso",
                            color = AzulPedidos,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Puedes consultar el estado y los detalles de tus pedidos en cualquier momento.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }

    // Confirmación antes de cancelar un pedido.
    pedidoParaCancelar?.let { pedido ->
        AlertDialog(
            onDismissRequest = { pedidoParaCancelar = null },
            title = { Text("Cancelar pedido") },
            text = {
                Text(
                    "¿Estás seguro de que deseas cancelar el pedido " +
                            "#${pedido.id.toString().padStart(3, '0')}?"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.cancelar(pedido)
                        pedidoParaCancelar = null
                    }
                ) {
                    Text("Sí, cancelar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { pedidoParaCancelar = null }) {
                    Text("Volver")
                }
            }
        )
    }

    if (mensajeError != null) {
        AlertDialog(
            onDismissRequest = viewModel::limpiarError,
            title = { Text("Aviso") },
            text = { Text(mensajeError.orEmpty()) },
            confirmButton = {
                TextButton(onClick = viewModel::limpiarError) {
                    Text("Entendido")
                }
            }
        )
    }
}

@Composable
private fun TarjetaMisPedidos(
    pedido: Pedido,
    onVerDetalle: () -> Unit,
    onCancelar: () -> Unit,
    onRepetir: () -> Unit
) {
    val estaListo = pedido.estado == "LISTO"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onVerDetalle),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = if (estaListo)
            BorderStroke(1.dp, VerdePedidos)
        else
            null,
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            Row(verticalAlignment = Alignment.Top) {
                Surface(
                    color = when (pedido.estado) {
                        "LISTO" -> Color(0xFFDDF7E6)
                        "RECIBIDO" -> Color(0xFFFFF0C9)
                        else -> Color(0xFFE2EDFF)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalLaundryService,
                        contentDescription = null,
                        tint = when (pedido.estado) {
                            "LISTO" -> VerdePedidos
                            "RECIBIDO" -> Color(0xFFE6A52C)
                            else -> Color(0xFF5588E8)
                        },
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Pedido #${pedido.id.toString().padStart(3, '0')}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = formatearFechaMisPedidos(pedido.fechaRegistro),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    EstadoPedidoChipMisPedidos(pedido.estado)
                }

                Text(
                    text = "S/ %.2f".format(Locale.US, pedido.total),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            if (estaListo && !pedido.casillero.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = VerdePedidos,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "Casillero ${pedido.casillero}",
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 7.dp
                        ),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (pedido.estado == "RECIBIDO") {
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onCancelar,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color(0xFFE35D5D),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // MEJORA C: Repetir pedidos finalizados o cancelados.
            if (pedido.estado == "ENTREGADO" || pedido.estado == "CANCELADO") {
                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onRepetir,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Repetir pedido",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun EstadoPedidoChipMisPedidos(estado: String) {
    val (texto, fondo, colorTexto) = when (estado) {
        "RECIBIDO" -> Triple(
            "Recibido", Color(0xFFFFF0C9), Color(0xFFAF7900)
        )
        "EN_PROCESO" -> Triple(
            "En proceso", Color(0xFFE3ECFF), Color(0xFF4875D6)
        )
        "LISTO" -> Triple(
            "Listo", Color(0xFFDDF7E6), Color(0xFF20894D)
        )
        "ENTREGADO" -> Triple(
            "Entregado", Color(0xFFDDF7E6), Color(0xFF20894D)
        )
        "CANCELADO" -> Triple(
            "Cancelado", Color(0xFFFFE6E6), Color(0xFFCF4C4C)
        )
        else -> Triple(
            estado, Color.LightGray, Color.DarkGray
        )
    }

    Surface(
        color = fondo,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = texto,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 5.dp
            ),
            color = colorTexto,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatearFechaMisPedidos(fecha: String): String {
    val formatos = listOf(
        "yyyy-MM-dd",
        "dd/MM/yyyy HH:mm",
        "dd/MM/yyyy",
        "yyyy-MM-dd HH:mm",
        "yyyy-MM-dd'T'HH:mm:ss"
    )

    for (formato in formatos) {
        try {
            val entrada = SimpleDateFormat(formato, Locale.US).apply {
                isLenient = false
            }
            val fechaConvertida = entrada.parse(fecha) ?: continue

            return SimpleDateFormat(
                "EEE d MMM",
                Locale("es", "PE")
            ).format(fechaConvertida).replace(".", "")
        } catch (_: java.text.ParseException) {
            // Intentar con el siguiente formato.
        }
    }

    return fecha
}
