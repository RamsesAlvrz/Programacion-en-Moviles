package com.alvarez.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.lavacampus.data.model.EstadoPedido
import com.alvarez.lavacampus.viewmodel.BandejaViewModel
import com.alvarez.lavacampus.viewmodel.PedidoBandeja
import java.util.Locale

// Colores originales de la bandeja.
private val FondoBandeja
    @Composable get() = MaterialTheme.colorScheme.background
private val MoradoBandeja = Color(0xFF7024DB)
private val BordeBandeja
    @Composable get() = MaterialTheme.colorScheme.outlineVariant
private val TextoPrincipal
    @Composable get() = MaterialTheme.colorScheme.onSurface
private val TextoSecundario
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

// COMMIT A: Colores para identificar pedidos atrasados.
private val RojoAtrasado
    @Composable get() = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFFFF8999) else Color(0xFFD92D40)
private val FondoAtrasado
    @Composable get() = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFF48242D) else Color(0xFFFFF0F1)
private val BordeAtrasado
    @Composable get() = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFFB95668) else Color(0xFFFFB8C0)

@Composable
fun BandejaScreen(
    viewModel: BandejaViewModel,
    onVolver: () -> Unit,
    onGestionarPedido: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    // Estados obtenidos desde BandejaViewModel.
    val pedidos by viewModel.pedidos.collectAsState()
    val busqueda by viewModel.busqueda.collectAsState()
    val filtro by viewModel.filtroEstado.collectAsState()

    // COMMIT A: Cantidad total de pedidos atrasados.
    val totalAtrasados by viewModel.totalPedidosAtrasados.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoBandeja)
            .padding(horizontal = 16.dp)
    ) {

        // Encabezado original.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
        ) {

            IconButton(
                onClick = onVolver,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoPrincipal
                )
            }

            Text(
                text = "Pedidos",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = TextoPrincipal,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Buscador original por estudiante o número de pedido.
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, BordeBandeja),
            shadowElevation = 1.dp,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 13.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = TextoSecundario,
                    modifier = Modifier.size(19.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                BasicTextField(
                    value = busqueda,
                    onValueChange = viewModel::actualizarBusqueda,
                    singleLine = true,
                    textStyle = androidx.compose.ui.text.TextStyle(
                        fontSize = 13.sp,
                        color = TextoPrincipal
                    ),
                    decorationBox = { innerTextField ->

                        Box {

                            if (busqueda.isBlank()) {
                                Text(
                                    text = "Buscar por estudiante o número",
                                    fontSize = 12.sp,
                                    color = TextoSecundario,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            innerTextField()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(13.dp))

        // COMMIT A: Resumen de pedidos atrasados.
        // Se muestra únicamente cuando existen pedidos vencidos.
        if (totalAtrasados > 0) {

            Surface(
                color = FondoAtrasado,
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, BordeAtrasado),
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 12.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Pedidos atrasados",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RojoAtrasado
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "Requieren atención prioritaria",
                            fontSize = 10.sp,
                            color = RojoAtrasado.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Surface(
                        color = RojoAtrasado,
                        shape = RoundedCornerShape(50)
                    ) {

                        Text(
                            text = totalAtrasados.toString(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFF3D1020) else Color.White,
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(13.dp))
        }

        // Filtros originales de la bandeja.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            val filtros = listOf(
                "TODOS" to "Todos",
                "RECIBIDO" to "Recibido",
                "EN_PROCESO" to "En proceso",
                "LISTO" to "Listo"
            )

            filtros.forEach { (codigo, etiqueta) ->

                val seleccionado = filtro == codigo

                Surface(
                    shape = RoundedCornerShape(50),
                    color = if (seleccionado) {
                        MoradoBandeja
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    border = if (seleccionado) {
                        null
                    } else {
                        BorderStroke(1.dp, BordeBandeja)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(38.dp)
                        .clickable {
                            viewModel.seleccionarFiltro(codigo)
                        }
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = etiqueta,
                            fontSize = 11.sp,
                            fontWeight = if (seleccionado) {
                                FontWeight.SemiBold
                            } else {
                                FontWeight.Medium
                            },
                            color = if (seleccionado) {
                                Color.White
                            } else {
                                TextoSecundario
                            },
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(15.dp))

        // Listado de pedidos.
        if (pedidos.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "No se encontraron pedidos",
                    color = TextoSecundario,
                    fontSize = 14.sp
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {

                items(
                    items = pedidos,
                    key = { it.pedido.id }
                ) { item ->

                    TarjetaPedidoBandeja(
                        item = item,
                        onAbrir = {
                            onGestionarPedido(item.pedido.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaPedidoBandeja(
    item: PedidoBandeja,
    onAbrir: () -> Unit
) {

    val pedido = item.pedido
    val estado = EstadoPedido.desdeCodigo(pedido.estado)

    // Colores originales según el estado.
    val colorEstado = when (estado) {
        EstadoPedido.RECIBIDO -> if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFFFFCE67) else Color(0xFFBD8310)
        EstadoPedido.EN_PROCESO -> if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFF9DC1FF) else Color(0xFF367EE6)
        EstadoPedido.LISTO -> if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFF79E3AE) else Color(0xFF24A76A)
        EstadoPedido.ENTREGADO -> if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFFBEC5D4) else Color(0xFF7C8290)
        EstadoPedido.CANCELADO -> if (MaterialTheme.colorScheme.background.luminance() < 0.5f) Color(0xFFFF9999) else Color(0xFFE24A4A)
        null -> TextoSecundario
    }

    val modoOscuro = MaterialTheme.colorScheme.background.luminance() < 0.5f

    val fondoEstado = when (estado) {
        EstadoPedido.RECIBIDO -> if (modoOscuro) Color(0xFF453719) else Color(0xFFFFF3C9)
        EstadoPedido.EN_PROCESO -> if (modoOscuro) Color(0xFF1E365B) else Color(0xFFE0ECFF)
        EstadoPedido.LISTO -> if (modoOscuro) Color(0xFF173E2C) else Color(0xFFDCF9E9)
        EstadoPedido.ENTREGADO -> if (modoOscuro) Color(0xFF333640) else Color(0xFFF0F0F3)
        EstadoPedido.CANCELADO -> if (modoOscuro) Color(0xFF4C2626) else Color(0xFFFFE7E7)
        null -> MaterialTheme.colorScheme.surfaceVariant
    }

    val textoBoton = when (estado) {
        EstadoPedido.RECIBIDO -> "Iniciar"
        EstadoPedido.EN_PROCESO -> "Marcar listo"
        EstadoPedido.LISTO -> "Entregar"
        else -> null
    }

    // COMMIT A: Se resalta la tarjeta cuando el pedido está atrasado.
    Surface(
        shape = RoundedCornerShape(15.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (item.estaAtrasado) 1.5.dp else 1.dp,
            color = if (item.estaAtrasado) {
                RojoAtrasado
            } else {
                BordeBandeja
            }
        ),
        shadowElevation = 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onAbrir)
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {

            // Icono de lavandería con fondo pastel.
            Box(
                modifier = Modifier.size(43.dp),
                contentAlignment = Alignment.Center
            ) {

                Surface(
                    shape = RoundedCornerShape(11.dp),
                    color = fondoEstado,
                    modifier = Modifier.fillMaxSize()
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.LocalLaundryService,
                            contentDescription = null,
                            tint = colorEstado,
                            modifier = Modifier.size(23.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(11.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                // Número de pedido y precio.
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Pedido #${pedido.id.toString().padStart(3, '0')}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoPrincipal
                    )

                    Text(
                        text = String.format(
                            Locale.US,
                            "S/ %.2f",
                            pedido.total
                        ),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoradoBandeja
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Nombre del estudiante.
                val nombreEstudiante =
                    item.estudiante?.nombre ?: "Estudiante"

                val nombreAbreviado = nombreEstudiante
                    .trim()
                    .split(Regex("\\s+"))
                    .filter { it.isNotBlank() }
                    .let { partes ->

                        when {
                            partes.isEmpty() -> "Estudiante"

                            partes.size == 1 -> partes.first()

                            else -> {
                                "${partes.first()} ${partes[1].first()}."
                            }
                        }
                    }

                val detallePedido = item.detalleResumen

                Text(
                    text = if (detallePedido.isBlank()) {
                        nombreAbreviado
                    } else {
                        "$nombreAbreviado · $detallePedido"
                    },
                    fontSize = 11.sp,
                    color = TextoSecundario,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

// Mostrar la fecha límite de entrega del pedido.
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Entrega: ${pedido.fechaEntrega}",
                    fontSize = 11.sp,
                    fontWeight = if (item.estaAtrasado) {
                        FontWeight.SemiBold
                    } else {
                        FontWeight.Normal
                    },
                    color = if (item.estaAtrasado) {
                        RojoAtrasado
                    } else {
                        TextoSecundario
                    }
                )

                Spacer(modifier = Modifier.height(7.dp))

                // Estado actual del pedido.
                Surface(
                    color = fondoEstado,
                    shape = RoundedCornerShape(50)
                ) {

                    Text(
                        text = estado?.etiqueta ?: pedido.estado,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorEstado,
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 4.dp
                        )
                    )
                }

                // COMMIT A: Etiqueta para pedidos atrasados.
                if (item.estaAtrasado) {

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        color = FondoAtrasado,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(
                            1.dp,
                            BordeAtrasado
                        )
                    ) {

                        Text(
                            text = "Pedido atrasado",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = RojoAtrasado,
                            modifier = Modifier.padding(
                                horizontal = 9.dp,
                                vertical = 4.dp
                            )
                        )
                    }
                }

                // Botón original para gestionar el pedido.
                if (textoBoton != null) {

                    Spacer(modifier = Modifier.height(7.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {

                        Button(
                            onClick = onAbrir,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MoradoBandeja
                            ),
                            shape = RoundedCornerShape(50),
                            contentPadding = PaddingValues(
                                horizontal = 10.dp,
                                vertical = 0.dp
                            ),
                            modifier = Modifier
                                .width(112.dp)
                                .height(36.dp)
                        ) {

                            Text(
                                text = textoBoton,
                                fontSize = 11.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}
