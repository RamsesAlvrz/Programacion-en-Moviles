package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.viewmodel.InicioViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val TurquesaInicio = Color(0xFF0895B5)

@Composable
fun InicioScreen(
    viewModel: InicioViewModel,
    usuarioId: Int,
    onNuevoPedido: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    onMisPedidos: () -> Unit
) {
    val TextoPrincipal = MaterialTheme.colorScheme.onBackground
    val TextoSecundario = MaterialTheme.colorScheme.onSurfaceVariant
    val FondoInicio = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) {
        MaterialTheme.colorScheme.background
    } else {
        Color(0xFFF0FBFD)
    }

    val estudiante by viewModel.estudiante.collectAsStateWithLifecycle()
    val pedidos by viewModel.pedidos.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(usuarioId) {
        viewModel.cargarDatos(usuarioId)
    }

    val pedidosOrdenados = pedidos.sortedByDescending { it.fechaCreacion }

    val pedidoActivo = pedidosOrdenados.firstOrNull {
        it.estado.uppercase() in listOf(
            "RECIBIDO", "EN_PROCESO", "LISTO"
        )
    }

    val ultimosPedidos = pedidosOrdenados.take(3)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoInicio),
        contentPadding = PaddingValues(
            start = 20.dp,
            end = 20.dp,
            top = 22.dp,
            bottom = 28.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // Encabezado como en la guía
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Hola, ${
                            estudiante?.nombre?.substringBefore(" ")
                                ?: "estudiante"
                        }",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )

                    Spacer(Modifier.height(3.dp))

                    Text(
                        text = "${estudiante?.codigo.orEmpty()} · Tecsup",
                        fontSize = 12.sp,
                        color = TextoSecundario
                    )
                }

                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Notificaciones",
                    tint = TextoPrincipal,
                    modifier = Modifier.size(23.dp)
                )
            }
        }

        if (cargando) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(30.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = TurquesaInicio)
                }
            }
        } else if (error != null) {
            item {
                Text(
                    text = error.orEmpty(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else {

            // Tarjeta principal del pedido activo
            item {
                if (pedidoActivo != null) {
                    TarjetaPedidoActivo(
                        pedido = pedidoActivo,
                        onVerDetalle = {
                            onVerDetalle(pedidoActivo.id)
                        }
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = TurquesaInicio
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {
                            Text(
                                text = "Tus pedidos",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )

                            Spacer(Modifier.height(6.dp))

                            Text(
                                text = "No tienes pedidos activos",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 19.sp
                            )

                            Spacer(Modifier.height(5.dp))

                            Text(
                                text = "¡Registra un nuevo pedido para comenzar!",
                                color = Color.White,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Nuevo pedido
            item {
                Button(
                    onClick = onNuevoPedido,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TurquesaInicio
                    )
                ) {
                    Text(
                        text = "Nuevo pedido",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Accesos rápidos
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AccesoRapido(
                        titulo = "Mis pedidos",
                        descripcion = "Ver estados",
                        icono = {
                            Icon(
                                Icons.Outlined.List,
                                contentDescription = null,
                                tint = TurquesaInicio
                            )
                        },
                        modifier = Modifier.weight(1f),
                        onClick = onMisPedidos
                    )

                    AccesoRapido(
                        titulo = "Tarifas",
                        descripcion = "Precios por kg",
                        icono = {
                            Icon(
                                Icons.Outlined.LocalOffer,
                                contentDescription = null,
                                tint = TurquesaInicio
                            )
                        },
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // Se conectará a TarifasScreen
                            // cuando implementemos su navegación.
                        }
                    )
                }
            }

            // Título de últimos pedidos
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Últimos pedidos",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )

                    Text(
                        text = "Ver todos",
                        color = TurquesaInicio,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            onMisPedidos()
                        }
                    )
                }
            }

            // Pedidos recientes
            if (ultimosPedidos.isEmpty()) {
                item {
                    Text(
                        text = "Todavía no tienes pedidos registrados.",
                        color = TextoSecundario,
                        fontSize = 13.sp
                    )
                }
            } else {
                items(ultimosPedidos.size) { indice ->
                    PedidoRecienteItem(
                        pedido = ultimosPedidos[indice],
                        onClick = {
                            onVerDetalle(ultimosPedidos[indice].id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaPedidoActivo(
    pedido: Pedido,
    onVerDetalle: () -> Unit
) {
    val estado = pedido.estado.uppercase()
    val nombreEstado = when (estado) {
        "RECIBIDO" -> "Recibido"
        "EN_PROCESO" -> "En proceso"
        "LISTO" -> "Listo"
        "ENTREGADO" -> "Entregado"
        else -> estado
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(TurquesaInicio)
            .clickable(onClick = onVerDetalle)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tu pedido activo",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.85f)
            )

            Text(
                text = "#${pedido.id.toString().padStart(3, '0')}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(Modifier.height(4.dp))

        Text(
            text = nombreEstado,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Entrega estimada: ${formatearFechaCorta(pedido.fechaEntrega)}",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.9f)
        )

        Spacer(Modifier.height(14.dp))

        SeguimientoEnTarjeta(estado)
    }
}


@Composable
private fun SeguimientoEnTarjeta(estado: String) {
    val TextoPrincipal = MaterialTheme.colorScheme.onSurface
    val TextoSecundario = MaterialTheme.colorScheme.onSurfaceVariant
    val etapas = listOf(
        "RECIBIDO",
        "EN_PROCESO",
        "LISTO",
        "ENTREGADO"
    )

    val etiquetas = listOf(
        "Recibido",
        "En proceso",
        "Listo",
        "Entregado"
    )

    val etapaActual = etapas.indexOf(estado.uppercase())
    val colorPendiente = MaterialTheme.colorScheme.outline

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(11.dp)
            )
            .padding(horizontal = 9.dp, vertical = 9.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            etapas.forEachIndexed { indice, _ ->
                val completado = etapaActual >= 0 &&
                        indice <= etapaActual

                Box(
                    modifier = Modifier
                        .size(21.dp)
                        .background(
                            if (completado) TurquesaInicio
                            else colorPendiente,
                            RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (completado) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
                        )
                    }
                }

                if (indice < etapas.lastIndex) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(2.dp)
                            .background(
                                if (etapaActual > indice) {
                                    TurquesaInicio
                                } else {
                                    colorPendiente
                                }
                            )
                    )
                }
            }
        }

        Spacer(Modifier.height(6.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            etiquetas.forEachIndexed { indice, etiqueta ->
                Text(
                    text = etiqueta,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 9.sp,
                    fontWeight = if (
                        etapaActual >= 0 && indice <= etapaActual
                    ) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (
                        etapaActual >= 0 && indice <= etapaActual
                    ) TextoPrincipal else TextoSecundario,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun AccesoRapido(
    titulo: String,
    descripcion: String,
    icono: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val TextoPrincipal = MaterialTheme.colorScheme.onSurface
    val TextoSecundario = MaterialTheme.colorScheme.onSurfaceVariant
    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(37.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                icono()
            }

            Column {
                Text(
                    text = titulo,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal,
                    maxLines = 1
                )

                Text(
                    text = descripcion,
                    fontSize = 10.sp,
                    color = TextoSecundario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PedidoRecienteItem(
    pedido: Pedido,
    onClick: () -> Unit
) {
    val TextoPrincipal = MaterialTheme.colorScheme.onSurface
    val TextoSecundario = MaterialTheme.colorScheme.onSurfaceVariant
    val estado = pedido.estado.uppercase()

    val estadoTexto = when (estado) {
        "RECIBIDO" -> "Recibido"
        "EN_PROCESO" -> "En proceso"
        "LISTO" -> "Listo"
        "ENTREGADO" -> "Entregado"
        "CANCELADO" -> "Cancelado"
        else -> estado
    }

    val colorEstado = when (estado) {
        "RECIBIDO" -> Color(0xFF3B82F6)
        "EN_PROCESO" -> Color(0xFF0895B5)
        "LISTO" -> Color(0xFF16A34A)
        "ENTREGADO" -> Color(0xFF64748B)
        "CANCELADO" -> Color(0xFFDC2626)
        else -> Color.Gray
    }

    val fondoEstado = MaterialTheme.colorScheme.surfaceVariant

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(13.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(13.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(
                        fondoEstado,
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Inventory2,
                    contentDescription = null,
                    tint = colorEstado,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Pedido #${pedido.id.toString().padStart(3, '0')}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )

                    Text(
                        text = "S/ %.2f".format(pedido.total),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TurquesaInicio
                    )
                }

                Text(
                    text = "Registrado: ${formatearFechaCorta(pedido.fechaRegistro)}",                    fontSize = 11.sp,
                    color = TextoSecundario
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = estadoTexto,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorEstado
                    )

                    if (!pedido.casillero.isNullOrBlank()) {
                        Text(
                            text = "Casillero ${pedido.casillero}",
                            fontSize = 10.sp,
                            color = TurquesaInicio
                        )
                    }
                }
            }
        }
    }
}

// Convierte fechas de Room (yyyy-MM-dd) al formato de la guía (mié 21 oct).

private fun formatearFechaCorta(fecha: String): String {

    val idioma = Locale("es", "PE")

    // Reconoce los distintos formatos guardados en Room.
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
            // Si el formato no coincide, prueba el siguiente.
        }
    }

    return fecha
}
