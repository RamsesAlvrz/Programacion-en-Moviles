package com.alvarez.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.model.Casilleros
import com.alvarez.lavacampus.data.model.EstadoPedido
import com.alvarez.lavacampus.viewmodel.GestionPedidoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val MoradoGestion = Color(0xFF7024DB)
private val FondoGestion: Color
    @Composable get() = MaterialTheme.colorScheme.background
private val BordeGestion: Color
    @Composable get() = MaterialTheme.colorScheme.outlineVariant
private val TextoGestion: Color
    @Composable get() = MaterialTheme.colorScheme.onSurface
private val GrisGestion: Color
    @Composable get() = MaterialTheme.colorScheme.onSurfaceVariant

@Composable
fun GestionPedidoScreen(
    pedidoId: Int,
    viewModel: GestionPedidoViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(pedidoId) {
        viewModel.cargarPedido(pedidoId)
    }

    val pedido by viewModel.pedido.collectAsState()
    val estudiante by viewModel.estudiante.collectAsState()
    val ocupados by viewModel.casillerosOcupados.collectAsState()
    val seleccionado by viewModel.casilleroElegido.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val procesando by viewModel.procesando.collectAsState()

    var confirmarCancelacion by remember { mutableStateOf(false) }

    if (confirmarCancelacion) {
        AlertDialog(
            onDismissRequest = { confirmarCancelacion = false },
            title = { Text("Cancelar pedido") },
            text = {
                Text("¿Seguro que deseas cancelar este pedido?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmarCancelacion = false
                        viewModel.cancelar()
                    }
                ) {
                    Text("Sí, cancelar", color = Color(0xFFE53935))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { confirmarCancelacion = false }
                ) {
                    Text("Volver")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FondoGestion)
    ) {
        // Encabezado
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 10.dp)
        ) {
            IconButton(
                onClick = onVolver,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoGestion
                )
            }

            Text(
                text = "Gestionar pedido #${pedidoId.toString().padStart(3, '0')}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextoGestion,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        if (pedido == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MoradoGestion)
            }
        } else {
            val actual = pedido!!
            val estado = EstadoPedido.desdeCodigo(actual.estado)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(11.dp)
            ) {

                TarjetaEstudianteGestion(
                    estudiante = estudiante,
                    estado = estado
                )

                SeguimientoPedido(
                    pedido = actual,
                    estado = estado
                )

                // Casilleros
                Text(
                    text = "Casillero para el recojo",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoGestion
                )

                BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                    val separacion = 10.dp
                    val anchoCasillero = (maxWidth - separacion * 3 - 4.dp) / 4

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(separacion),
                        contentPadding = PaddingValues(end = 4.dp)
                    ) {
                        items(Casilleros.todos) { casillero ->
                            val ocupado = casillero in ocupados
                            val elegido = seleccionado == casillero

                            val puedeElegir =
                                !ocupado &&
                                        !procesando &&
                                        estado in listOf(
                                    EstadoPedido.RECIBIDO,
                                    EstadoPedido.EN_PROCESO
                                )

                            Surface(
                                shape = RoundedCornerShape(50),
                                color = when {
                                    ocupado -> MaterialTheme.colorScheme.surfaceVariant
                                    elegido -> MoradoGestion
                                    else -> MaterialTheme.colorScheme.surface
                                },
                                border = if (!ocupado && !elegido) {
                                    BorderStroke(1.dp, BordeGestion)
                                } else {
                                    null
                                },
                                modifier = Modifier
                                    .width(anchoCasillero)
                                    .height(36.dp)
                                    .clickable(enabled = puedeElegir) {
                                        viewModel.elegirCasillero(casillero)
                                    }
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = casillero,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = when {
                                            ocupado -> GrisGestion
                                            elegido -> Color.White
                                            else -> TextoGestion
                                        }
                                    )
                                }
                            }
                        }
                    }

                }
                Text(
                    text = "Los casilleros en gris ya están ocupados",
                    fontSize = 10.sp,
                    color = GrisGestion
                )

                // Total y pago
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, BordeGestion),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = String.format(
                                    Locale.US,
                                    "Total S/ %.2f",
                                    actual.total
                                ),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextoGestion
                            )

                            Spacer(Modifier.height(3.dp))

                            Text(
                                text = "Se cobra al entregar",
                                fontSize = 11.sp,
                                color = GrisGestion
                            )
                        }

                        Text(
                            text = "Cobrado",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextoGestion
                        )

                        Spacer(Modifier.width(7.dp))

                        Switch(
                            checked = actual.pagado,
                            onCheckedChange = viewModel::marcarPagado,
                            enabled = !procesando &&
                                    estado != EstadoPedido.CANCELADO,
                            modifier = Modifier.height(32.dp),
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = MoradoGestion,
                                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                                uncheckedBorderColor = Color.Transparent
                            )
                        )
                    }
                }

                // Regla informativa
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(11.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = buildAnnotatedString {
                            append("Regla: no se entrega un pedido sin cobrar;\n")
                            withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSecondaryContainer, fontWeight = FontWeight.Bold)) {
                                append("ni se pasa a Listo sin casillero.")
                            }
                        },
                        fontSize = 11.sp,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 13.dp, vertical = 12.dp)
                    )
                }

                if (!mensaje.isNullOrBlank()) {
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(9.dp)
                    ) {
                        Text(
                            text = mensaje.orEmpty(),
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                val siguiente = estado?.siguiente()

                if (siguiente != null) {
                    Button(
                        onClick = { viewModel.avanzar() },
                        enabled = !procesando,
                        shape = RoundedCornerShape(9.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MoradoGestion
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Text(
                            text = when (siguiente) {
                                EstadoPedido.EN_PROCESO ->
                                    "Iniciar proceso"
                                EstadoPedido.LISTO ->
                                    "Marcar como Listo"
                                EstadoPedido.ENTREGADO ->
                                    "Entregar pedido"
                                else -> "Continuar"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (estado?.puedeCancelar() == true) {
                    OutlinedButton(
                        onClick = { confirmarCancelacion = true },
                        enabled = !procesando,
                        shape = RoundedCornerShape(9.dp),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFE53935)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)
                    ) {
                        Text(
                            text = "Cancelar pedido",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE53935)
                        )
                    }
                }

                if (estado == EstadoPedido.ENTREGADO ||
                    estado == EstadoPedido.CANCELADO
                ) {
                    Text(
                        text = if (estado == EstadoPedido.ENTREGADO) {
                            "Este pedido ya fue entregado."
                        } else {
                            "Este pedido fue cancelado."
                        },
                        fontSize = 12.sp,
                        color = GrisGestion,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun TarjetaEstudianteGestion(
    estudiante: Usuario?,
    estado: EstadoPedido?
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, BordeGestion),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val iniciales = estudiante?.nombre
                ?.trim()
                ?.split(Regex("\\s+"))
                ?.filter { it.isNotBlank() }
                ?.take(2)
                ?.joinToString("") { it.take(1).uppercase() }
                ?: "?"

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = iniciales,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Spacer(Modifier.width(11.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = estudiante?.nombre ?: "Estudiante",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoGestion
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = estudiante?.codigo ?: "Sin código",
                    fontSize = 10.sp,
                    color = GrisGestion
                )
            }

            val colorEstado = when (estado) {
                EstadoPedido.RECIBIDO -> Color(0xFFB78312)
                EstadoPedido.EN_PROCESO -> Color(0xFF1766D7)
                EstadoPedido.LISTO -> Color(0xFF249F68)
                EstadoPedido.CANCELADO -> Color(0xFFDC4747)
                else -> Color(0xFF777C89)
            }

            val fondoEstado = when (estado) {
                EstadoPedido.RECIBIDO -> Color(0xFFFFF3C9)
                EstadoPedido.EN_PROCESO -> Color(0xFFDCEAFF)
                EstadoPedido.LISTO -> Color(0xFFDCF9E9)
                EstadoPedido.CANCELADO -> Color(0xFFFFE7E7)
                else -> Color(0xFFF0F0F3)
            }

            Surface(
                color = fondoEstado,
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = estado?.etiqueta ?: "Sin estado",
                    color = colorEstado,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(
                        horizontal = 10.dp,
                        vertical = 4.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun SeguimientoPedido(
    pedido: Pedido,
    estado: EstadoPedido?
) {
    val pasos = listOf(
        EstadoPedido.RECIBIDO,
        EstadoPedido.EN_PROCESO,
        EstadoPedido.LISTO,
        EstadoPedido.ENTREGADO
    )
    val indiceActual = pasos.indexOf(estado)
    val cancelado = estado == EstadoPedido.CANCELADO

    // Mostrar únicamente las fechas que realmente existen en Room.
    // Los pedidos anteriores a la migración pueden no tener historial.
    val fechas = listOf(
        pedido.fechaCreacion,
        pedido.fechaInicioProceso,
        pedido.fechaListo,
        pedido.fechaEntregado
    )
    val formateador = remember {
        SimpleDateFormat("dd MMM HH:mm", Locale("es", "PE"))
    }

    val altoFila = 43.dp
    val tamanoCirculo = 21.dp
    val colorLinea = MaterialTheme.colorScheme.outlineVariant

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(15.dp),
        border = BorderStroke(1.dp, BordeGestion),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp, vertical = 10.dp)
        ) {
            // La línea se dibuja primero, por detrás de los círculos.
            Canvas(
                modifier = Modifier
                    .width(tamanoCirculo)
                    .height(altoFila * pasos.size)
            ) {
                val centroX = size.width / 2f
                val centroPrimerCirculo = altoFila.toPx() / 2f
                val centroUltimoCirculo = altoFila.toPx() * (pasos.size - 0.5f)
                drawLine(
                    color = colorLinea,
                    start = Offset(centroX, centroPrimerCirculo),
                    end = Offset(centroX, centroUltimoCirculo),
                    strokeWidth = 2.dp.toPx()
                )
                if (!cancelado && indiceActual > 0) {
                    drawLine(
                        color = MoradoGestion,
                        start = Offset(centroX, centroPrimerCirculo),
                        end = Offset(
                            centroX,
                            altoFila.toPx() * (indiceActual + 0.5f)
                        ),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }

            Column {
                pasos.forEachIndexed { indice, paso ->
                    val completado = !cancelado && indiceActual >= indice
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(altoFila),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(tamanoCirculo),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (completado) MoradoGestion else MaterialTheme.colorScheme.surface,
                                border = if (completado) null else BorderStroke(
                                    1.5.dp,
                                    MaterialTheme.colorScheme.outlineVariant
                                ),
                                modifier = Modifier.size(tamanoCirculo)
                            ) {
                                if (completado) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.width(11.dp))

                        Text(
                            text = paso.etiqueta,
                            fontSize = 12.sp,
                            fontWeight = if (completado) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (completado) TextoGestion else GrisGestion,
                            modifier = Modifier.weight(1f)
                        )

                        Text(
                            text = fechas[indice]?.let { fecha ->
                                formateador.format(Date(fecha))
                            } ?: "—",
                            fontSize = 10.sp,
                            color = GrisGestion
                        )
                    }
                }
            }
        }
    }
}
