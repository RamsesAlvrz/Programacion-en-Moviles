
package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.model.LineaPedido
import com.alvarez.lavacampus.viewmodel.NuevoPedidoViewModel
import java.util.Locale

private val TurquesaPedido = Color(0xFF0891B2)
private val FondoPedido = Color(0xFFF0FCFF)

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

    var nota by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.cargarServicios()
    }

    val total = lineas.sumOf { it.subtotal }
    val haySeleccion = lineas.any { it.seleccionado }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Nuevo pedido")
                },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text(
                            "Volver",
                            color = TurquesaPedido
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FondoPedido
                )
            )
        },
        containerColor = FondoPedido
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text(
                    text = "Selecciona tus servicios",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Indica la cantidad de prendas o kilos que deseas enviar.",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
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
                        habilitado = !guardando,
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
                OutlinedTextField(
                    value = nota,
                    onValueChange = { nota = it },
                    label = {
                        Text("Indicaciones adicionales")
                    },
                    placeholder = {
                        Text("Ejemplo: tratar con cuidado las prendas delicadas")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    maxLines = 4,
                    enabled = !guardando
                )
            }

            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = "Resumen del pedido",
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )

                        HorizontalDivider()

                        Text(
                            text = "Servicios seleccionados: ${
                                lineas.count { it.seleccionado }
                            }"
                        )

                        Text(
                            text = "Entrega estimada: ${
                                viewModel.calcularFechaEntrega()
                            }",
                            fontSize = 13.sp,
                            color = Color.DarkGray
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total",
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "S/ ${
                                    String.format(
                                        Locale.US,
                                        "%.2f",
                                        total
                                    )
                                }",
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                                color = TurquesaPedido
                            )
                        }
                    }
                }
            }

            if (error != null) {
                item {
                    Text(
                        text = error.orEmpty(),
                        color = MaterialTheme.colorScheme.error
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
                            !guardando &&
                            !cargando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TurquesaPedido
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (guardando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Confirmar pedido",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

// Tarjeta que permite seleccionar la cantidad de un servicio
@Composable
private fun ServicioSeleccionCard(
    linea: LineaPedido,
    habilitado: Boolean,
    onAumentar: () -> Unit,
    onDisminuir: () -> Unit
) {

    val esKilogramo = linea.servicio.unidad == "KG"

    // Cantidad máxima permitida según la guía
    val cantidadMaxima = if (esKilogramo) {
        10.0
    } else {
        30.0
    }

    // Mostrar decimales solamente para los kilogramos
    val cantidadTexto = if (esKilogramo) {
        String.format(
            Locale.US,
            "%.1f kg",
            linea.cantidad
        )
    } else {
        linea.cantidad.toInt().toString()
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Text(
                text = linea.servicio.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Text(
                text = linea.servicio.descripcion,
                fontSize = 13.sp,
                color = Color.Gray
            )

            Text(
                text = "S/ ${
                    String.format(
                        Locale.US,
                        "%.2f",
                        linea.servicio.precio
                    )
                } por ${linea.servicio.unidad}",
                color = TurquesaPedido,
                fontWeight = FontWeight.Medium
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedButton(
                        onClick = onDisminuir,
                        enabled = habilitado &&
                                linea.cantidad > 0.0,
                        contentPadding = PaddingValues(
                            horizontal = 12.dp
                        )
                    ) {
                        Text("−")
                    }

                    Text(
                        text = cantidadTexto,
                        modifier = Modifier.padding(
                            horizontal = 10.dp
                        ),
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedButton(
                        onClick = onAumentar,
                        enabled = habilitado &&
                                linea.cantidad < cantidadMaxima,
                        contentPadding = PaddingValues(
                            horizontal = 12.dp
                        )
                    ) {
                        Text("+")
                    }
                }

                Text(
                    text = "S/ ${
                        String.format(
                            Locale.US,
                            "%.2f",
                            linea.subtotal
                        )
                    }",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
