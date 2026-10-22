package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.ui.components.PedidoCard
import com.alvarez.lavacampus.viewmodel.InicioViewModel

private val Turquesa = Color(0xFF0891B2)
private val FondoEstudiante = Color(0xFFF0FCFF)

@Composable
fun InicioScreen(
    viewModel: InicioViewModel,
    usuarioId: Int,
    onNuevoPedido: () -> Unit,
    onVerDetalle: (Int) -> Unit,
    onMisPedidos: () -> Unit
) {
    val estudiante by viewModel.estudiante.collectAsStateWithLifecycle()
    val pedidos by viewModel.pedidos.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(usuarioId) {
        viewModel.cargarDatos(usuarioId)
    }

    val pedidoReciente = pedidos.maxByOrNull {
        it.fechaCreacion
    }

    val pedidosActivos = pedidos.count {
        it.estado.uppercase() !in listOf(
            "ENTREGADO",
            "CANCELADO"
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoEstudiante),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // Encabezado de bienvenida
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Turquesa,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(22.dp)
            ) {
                Text(
                    text = "LAVACAMPUS · ESTUDIANTE",
                    color = Color(0xFFCFFAFE),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(10.dp))

                Text(
                    text = "¡Hola, ${
                        estudiante?.nombre?.substringBefore(" ")
                            ?: "estudiante"
                    }!",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Tu ropa limpia, sin complicaciones.",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        // Resumen de pedidos
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ResumenPedidoCard(
                    titulo = "Pedidos activos",
                    cantidad = pedidosActivos,
                    modifier = Modifier.weight(1f)
                )

                ResumenPedidoCard(
                    titulo = "Total de pedidos",
                    cantidad = pedidos.size,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Botón para registrar un nuevo pedido
        item {
            Button(
                onClick = onNuevoPedido,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Turquesa
                )
            ) {
                Text(
                    text = "+ Registrar nuevo pedido",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Encabezado de pedidos
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mi pedido reciente",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = onMisPedidos) {
                    Text(
                        text = "Ver todos",
                        color = Turquesa
                    )
                }
            }
        }

        // Contenido según el estado de carga
        if (cargando) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Turquesa
                    )
                }
            }
        } else if (error != null) {
            item {
                Text(
                    text = error.orEmpty(),
                    color = MaterialTheme.colorScheme.error
                )
            }
        } else if (pedidoReciente == null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Aún no tienes pedidos",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 17.sp
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Registra tu primer pedido para comenzar.",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        } else {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    PedidoCard(
                        pedido = pedidoReciente,
                        onVerDetalle = onVerDetalle
                    )

                    SeguimientoPedido(
                        estado = pedidoReciente.estado
                    )
                }
            }
        }
    }
}

// Tarjeta que muestra la cantidad de pedidos
@Composable
private fun ResumenPedidoCard(
    titulo: String,
    cantidad: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = cantidad.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Turquesa
            )
        }
    }
}

// Seguimiento visual de las etapas del pedido
@Composable
private fun SeguimientoPedido(
    estado: String
) {
    val etapas = listOf(
        "RECIBIDO",
        "EN_PROCESO",
        "LISTO",
        "ENTREGADO"
    )

    val estadoNormalizado = estado
        .uppercase()
        .replace(" ", "_")

    val etapaActual = etapas.indexOf(estadoNormalizado)

    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Seguimiento del pedido",
                fontWeight = FontWeight.SemiBold
            )

            if (estadoNormalizado == "CANCELADO") {
                Text(
                    text = "Este pedido fue cancelado.",
                    color = Color(0xFFDC2626)
                )
            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    etapas.forEachIndexed { index, _ ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(8.dp)
                                .background(
                                    color = if (index <= etapaActual) {
                                        Turquesa
                                    } else {
                                        Color(0xFFE2E8F0)
                                    },
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    }
                }

                Text(
                    text = "Recibido → En proceso → Listo → Entregado",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
