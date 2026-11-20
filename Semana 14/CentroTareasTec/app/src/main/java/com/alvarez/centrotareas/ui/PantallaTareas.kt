package com.alvarez.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alvarez.centrotareas.model.PrimosUi
import com.alvarez.centrotareas.viewmodel.TareasViewModel

private val Azul = Color(0xFF2073A9)
private val VerdeFondo = Color(0xFFE5F5E7)
private val VerdeTexto = Color(0xFF256D35)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(
    viewModel: TareasViewModel
) {
    var hasta by rememberSaveable {
        mutableStateOf("")
    }

    val mensaje by viewModel.mensaje.collectAsState()
    val progreso by viewModel.progreso.collectAsState()
    val corriendo by viewModel.corriendo.collectAsState()
    val primos by viewModel.primos.collectAsState()

    val calculando = primos is PrimosUi.Calculando

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
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            // TARJETA 1: PROGRESO
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
                        text = "Tarea con progreso",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleSmall,
                        color = Azul,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { viewModel.tareaLarga() },
                            enabled = !corriendo,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Azul
                            )
                        ) {
                            Text("Iniciar")
                        }

                        OutlinedButton(
                            onClick = { viewModel.cancelarTarea() },
                            enabled = corriendo
                        ) {
                            Text("Cancelar")
                        }
                    }

                    LinearProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxWidth(),
                        color = Azul,
                        trackColor = Color(0xFFDDE2E7)
                    )

                    Text(
                        text = "${(progreso * 100).toInt()} %",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Text(
                        text = mensaje,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray
                    )
                }
            }

            // TARJETA 2: NUMEROS PRIMOS
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
                        text = "Contar números primos",
                        modifier = Modifier.fillMaxWidth(),
                        style = MaterialTheme.typography.titleSmall,
                        color = Azul,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = hasta,
                        onValueChange = { nuevo ->
                            if (nuevo.all { it.isDigit() }) {
                                hasta = nuevo
                            }
                        },
                        label = {
                            Text("Hasta el número")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                hasta.toIntOrNull()?.let {
                                    viewModel.contarPrimos(it)
                                }
                            },
                            enabled = !calculando &&
                                    (hasta.toIntOrNull() ?: 0) >= 2,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Azul
                            )
                        ) {
                            Text("Contar")
                        }

                        OutlinedButton(
                            onClick = {
                                viewModel.cancelarPrimos()
                            },
                            enabled = calculando
                        ) {
                            Text("Cancelar")
                        }
                    }

                    when (val estado = primos) {
                        is PrimosUi.Inicial -> {
                            Text(
                                text = "Ingresa un número para comenzar",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        is PrimosUi.Calculando -> {
                            Text("Calculando números primos...")
                        }

                        is PrimosUi.Listo -> {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = VerdeFondo
                                ),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Primos encontrados: ${estado.cantidad}",
                                        color = VerdeTexto,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )

                                    Text(
                                        text = "Tiempo: ${estado.milisegundos} ms",
                                        color = VerdeTexto,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        is PrimosUi.Cancelado -> {
                            Text(
                                text = "Cálculo cancelado",
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}
