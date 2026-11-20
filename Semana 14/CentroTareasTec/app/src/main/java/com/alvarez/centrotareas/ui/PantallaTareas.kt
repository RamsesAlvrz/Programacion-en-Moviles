package com.alvarez.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alvarez.centrotareas.model.CargaUi
import com.alvarez.centrotareas.viewmodel.TareasViewModel

private val Azul = Color(0xFF176B9A)
private val Fondo = Color(0xFFF3F6FA)
private val Verde = Color(0xFF167448)
private val Rojo = Color(0xFFB42332)
private val Naranja = Color(0xFFB76B12)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {

    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()
    val limiteActivo by viewModel.limiteActivo.collectAsState()
    val reintentoAutomatico by viewModel.reintentoAutomatico.collectAsState()
    val intentoActual by viewModel.intentoActual.collectAsState()
    val mensajeReintento by viewModel.mensajeReintento.collectAsState()

    val cargando = carga is CargaUi.Cargando

    Scaffold(
        containerColor = Fondo,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "CentroTareas Tec",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Panel de operaciones",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Azul
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                colors = CardDefaults.cardColors(containerColor = Azul),
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "CENTRO DE CONTROL",
                        color = Color.White.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.labelMedium
                    )

                    Text(
                        text = "Gestión inteligente de tareas",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Supervisa tus operaciones y recupera cargas fallidas.",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Configuración",
                        color = Azul,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Límite de tiempo",
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Cancelar después de 2 segundos",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Switch(
                            checked = limiteActivo,
                            onCheckedChange = viewModel::cambiarLimiteActivo,
                            enabled = !cargando
                        )
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Simular error",
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Probar fallos de conexión",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Switch(
                            checked = simularError,
                            onCheckedChange = viewModel::cambiarSimularError,
                            enabled = !cargando
                        )
                    }

                    HorizontalDivider()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Reintento automático",
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Hasta 3 intentos con espera progresiva",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Switch(
                            checked = reintentoAutomatico,
                            onCheckedChange =
                                viewModel::cambiarReintentoAutomatico,
                            enabled = !cargando
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Ejecución de cargas",
                        color = Azul,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Selecciona cómo procesar el perfil y los pedidos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = viewModel::cargarSecuencial,
                            enabled = !cargando,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = viewModel::cargarParalelo,
                            enabled = !cargando,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Azul
                            )
                        ) {
                            Text("A la vez")
                        }
                    }

                    if (mensajeReintento.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFFFFF5E7),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "ESTADO DE INTENTOS",
                                color = Naranja,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium
                            )

                            Text(
                                text = mensajeReintento,
                                color = Naranja,
                                style = MaterialTheme.typography.bodyMedium
                            )

                            if (reintentoAutomatico && intentoActual > 0) {
                                Text(
                                    text = "Intento $intentoActual de 3",
                                    color = Naranja,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    when (val estado = carga) {
                        CargaUi.Inicial -> {
                            Text(
                                text = "Listo para iniciar una operación.",
                                color = Color.Gray
                            )
                        }

                        CargaUi.Cargando -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CircularProgressIndicator(color = Azul)
                                Text(
                                    text = "Procesando información...",
                                    color = Azul,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        is CargaUi.Exito -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color(0xFFE7F6EE),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "OPERACIÓN COMPLETADA",
                                    color = Verde,
                                    fontWeight = FontWeight.Bold
                                )

                                Text("Modo: ${estado.modo}")
                                Text("Perfil: ${estado.perfil}")
                                Text("Pedidos: ${estado.pedidos}")
                                Text("Duración total: ${estado.milisegundos} ms")
                            }
                        }

                        is CargaUi.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color(0xFFFFECEE),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "OPERACIÓN INTERRUMPIDA",
                                    color = Rojo,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = estado.mensaje.replace(
                                        "Sin conexion",
                                        "Sin conexión"
                                    ),
                                    color = Rojo
                                )

                                OutlinedButton(
                                    onClick = viewModel::reintentarCarga
                                ) {
                                    Text("Reintentar operación")
                                }
                            }
                        }
                    }
                }
            }

            Text(
                text = "CentroTareasTec · Fase 2 · Mejora con IA",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}
