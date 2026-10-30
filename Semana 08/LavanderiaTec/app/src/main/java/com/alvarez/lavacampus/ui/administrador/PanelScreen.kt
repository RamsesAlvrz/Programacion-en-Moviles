package com.alvarez.lavacampus.ui.administrador

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.ui.components.graficos.GraficoBarras
import com.alvarez.lavacampus.ui.components.graficos.GraficoDona
import com.alvarez.lavacampus.viewmodel.PanelViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val Morado = Color(0xFF6D28D9)
private val Verde = Color(0xFF16A34A)

// COMMIT A: Colores para los pedidos atrasados.
private val RojoAtrasado = Color(0xFFDC2626)
private val FondoAtrasado = Color(0xFFFEE2E2)

@Composable
fun PanelScreen(
    viewModel: PanelViewModel,
    onVerPedidosListos: () -> Unit
) {

    // Se consulta el tema de MaterialTheme, no el tema del sistema,
    // para respetar el interruptor de modo oscuro de la aplicación.
    val oscuro = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val textoPrincipal = MaterialTheme.colorScheme.onBackground
    val textoSecundario = MaterialTheme.colorScheme.onSurfaceVariant
    val superficieGrafico = MaterialTheme.colorScheme.surface

    val pedidosHoy by viewModel.pedidosHoy.collectAsStateWithLifecycle()
    val enProceso by viewModel.enProceso.collectAsStateWithLifecycle()
    val porRecoger by viewModel.porRecoger.collectAsStateWithLifecycle()

    // COMMIT A: Nuevo indicador de pedidos atrasados.
    val pedidosAtrasados by viewModel.pedidosAtrasados.collectAsStateWithLifecycle()

    val ingresosSemana by viewModel.ingresosSemana.collectAsStateWithLifecycle()

    val pedidosPagadosSemana by viewModel.pedidosPagadosSemana
        .collectAsStateWithLifecycle()

    val porEstado by viewModel.porEstado.collectAsStateWithLifecycle()
    val porServicio by viewModel.porServicio.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()
    val mensajeError by viewModel.mensajeError.collectAsStateWithLifecycle()

    val fecha = remember {
        LocalDate.now().format(
            DateTimeFormatter.ofPattern(
                "EEE, d MMM",
                Locale("es", "PE")
            )
        ).replaceFirstChar { it.uppercase() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // ENCABEZADO Y FECHA
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {

            Text(
                text = "Panel de la lavandería",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textoPrincipal
            )

            Text(
                text = "Hoy · $fecha",
                fontSize = 12.sp,
                color = textoSecundario
            )
        }

        if (cargando) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            // TRES CONTADORES ORIGINALES
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                TarjetaContador(
                    titulo = "Pedidos hoy",
                    cantidad = pedidosHoy,
                    fondo = if (oscuro) Color(0xFF332451) else Color(0xFFF0ECFF),
                    colorTexto = if (oscuro) Color(0xFFD8B4FE) else Color(0xFF7C3AED),
                    modifier = Modifier.weight(1f)
                )

                TarjetaContador(
                    titulo = "En proceso",
                    cantidad = enProceso,
                    fondo = if (oscuro) Color(0xFF1B3152) else Color(0xFFE8EFFF),
                    colorTexto = if (oscuro) Color(0xFF93C5FD) else Color(0xFF2563EB),
                    modifier = Modifier.weight(1f)
                )

                TarjetaContador(
                    titulo = "Por recoger",
                    cantidad = porRecoger,
                    fondo = if (oscuro) Color(0xFF173D2D) else Color(0xFFDCFCE7),
                    colorTexto = if (oscuro) Color(0xFF86EFAC) else Color(0xFF16A34A),
                    modifier = Modifier.weight(1f)
                )
            }

            // COMMIT A: NUEVA TARJETA DE PEDIDOS ATRASADOS
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(13.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (oscuro) Color(0xFF4B2026) else FondoAtrasado
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {

                        Text(
                            text = "Pedidos atrasados",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (oscuro) Color(0xFFFCA5A5) else RojoAtrasado
                        )

                        Text(
                            text = if (pedidosAtrasados > 0) {
                                "Requieren atención prioritaria"
                            } else {
                                "No hay pedidos atrasados"
                            },
                            fontSize = 11.sp,
                            color = if (oscuro) Color(0xFFFCA5A5) else RojoAtrasado.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = pedidosAtrasados.toString(),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (oscuro) Color(0xFFFCA5A5) else RojoAtrasado
                    )
                }
            }

            // INGRESOS SEMANALES
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Morado
                )
            ) {

                Column(
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    Text(
                        text = "Ingresos de la semana",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = String.format(
                                Locale.US,
                                "S/ %,.2f",
                                ingresosSemana
                            ),
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "$pedidosPagadosSemana pedidos",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White.copy(alpha = 0.90f)
                        )
                    }
                }
            }

            // GRÁFICO DE DONA
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = "Pedidos por estado",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textoPrincipal
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = superficieGrafico
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {

                    Box(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        GraficoDona(datos = porEstado)
                    }
                }
            }

            // GRÁFICO DE BARRAS
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {

                Text(
                    text = "Ingresos por servicio (S/)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textoPrincipal
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = superficieGrafico
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 1.dp
                    )
                ) {

                    Box(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        GraficoBarras(datos = porServicio)
                    }
                }
            }

            // ALERTA ORIGINAL DE PEDIDOS LISTOS
            if (porRecoger > 0) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (oscuro) Color(0xFF173D2D) else Color(0xFFDCFCE7)
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = if (oscuro) Color(0xFF86EFAC) else Verde,
                            modifier = Modifier.size(20.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = "$porRecoger pedidos esperan recojo",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (oscuro) Color(0xFFBBF7D0) else Color(0xFF15803D)
                            )

                            Text(
                                text = "Revisa a los estudiantes",
                                fontSize = 10.sp,
                                color = if (oscuro) Color(0xFF86EFAC) else Color(0xFF16A34A)
                            )
                        }

                        Button(
                            onClick = onVerPedidosListos,
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(
                                horizontal = 15.dp,
                                vertical = 0.dp
                            ),
                            modifier = Modifier.height(34.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Verde
                            )
                        ) {

                            Text(
                                text = "Ver",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }

        if (mensajeError != null) {

            Text(
                text = mensajeError.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun TarjetaContador(
    titulo: String,
    cantidad: Int,
    fondo: Color,
    colorTexto: Color,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(13.dp),
        colors = CardDefaults.cardColors(
            containerColor = fondo
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = cantidad.toString(),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = colorTexto
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = titulo,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorTexto,
                maxLines = 1
            )
        }
    }
}
