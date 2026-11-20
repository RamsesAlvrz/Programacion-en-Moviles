package com.alvarez.centrotareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.centrotareas.viewmodel.TareasViewModel

@Composable
fun PantallaTareas(
    viewModel: TareasViewModel
) {
    var toques by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "CentroTareas",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Laboratorio 14 - Hilos y corrutinas",
                style = MaterialTheme.typography.bodyMedium
            )

            Button(
                onClick = { toques++ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Tócame: $toques")
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Tarea con progreso",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Estado: Listo"
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Iniciar tarea")
                    }

                    OutlinedButton(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Contar números primos",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Cálculo pendiente"
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Contar primos")
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Dos cargas",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Comparación pendiente"
                    )

                    Spacer(Modifier.height(12.dp))

                    Button(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Carga secuencial")
                    }

                    OutlinedButton(
                        onClick = {},
                        enabled = false,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Carga paralela")
                    }
                }
            }
        }
    }
}
