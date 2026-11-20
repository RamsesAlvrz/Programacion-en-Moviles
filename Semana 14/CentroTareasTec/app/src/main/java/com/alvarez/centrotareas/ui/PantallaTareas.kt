
package com.alvarez.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alvarez.centrotareas.model.CargaUi
import com.alvarez.centrotareas.viewmodel.TareasViewModel

private val Azul = Color(0xFF2073A9)
private val VerdeFondo = Color(0xFFE5F5E7)
private val VerdeTexto = Color(0xFF256D35)
private val RojoFondo = Color(0xFFFBE4E4)
private val RojoTexto = Color(0xFFB33B3B)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(
    viewModel: TareasViewModel
) {
    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()

    val cargando = carga is CargaUi.Cargando

    Scaffold(
        containerColor = Color(0xFFFAF9FC),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "CentroTareas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
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
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // COMMIT 8: ERROR SIMULADO
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = "Dos cargas",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleSmall,
                        color = Azul,
                        fontWeight = FontWeight.Bold
                    )

                    // INTERRUPTOR SIMULAR ERROR
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Switch(
                            checked = simularError,
                            onCheckedChange = {
                                viewModel.cambiarSimularError(it)
                            },
                            enabled = !cargando
                        )

                        Text(
                            text = "Simular error",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    // BOTONES DE CARGA
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                viewModel.cargarSecuencial()
                            },
                            enabled = !cargando
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = {
                                viewModel.cargarParalelo()
                            },
                            enabled = !cargando,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Azul
                            )
                        ) {
                            Text("A la vez")
                        }
                    }

                    when (val estado = carga) {

                        is CargaUi.Inicial -> {
                            Text(
                                text = "Selecciona un tipo de carga",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        is CargaUi.Cargando -> {
                            Text(
                                text = "Cargando...",
                                color = Azul
                            )
                        }

                        is CargaUi.Exito -> {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = VerdeFondo
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(
                                        text = "Modo: ${estado.modo}",
                                        color = VerdeTexto,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Perfil: ${estado.perfil}",
                                        color = VerdeTexto
                                    )

                                    Text(
                                        text = "Pedidos: ${estado.pedidos}",
                                        color = VerdeTexto
                                    )

                                    Text(
                                        text = "Tiempo: ${estado.milisegundos} ms",
                                        color = VerdeTexto
                                    )
                                }
                            }

                            Text(
                                text = if (estado.modo == "A la vez") {
                                    "Todo por separado = 5000 ms"
                                } else {
                                    "En paralelo tarda aproximadamente 3000 ms"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = Azul,
                                textAlign = TextAlign.Center
                            )
                        }

                        is CargaUi.Error -> {

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = RojoFondo
                                )
                            ) {
                                Text(
                                    text = estado.mensaje.replace(
                                        "Sin conexion",
                                        "Sin conexión"
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    color = RojoTexto,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.reintentarCarga()
                                }
                            ) {
                                Text(
                                    text = "Reintentar",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
