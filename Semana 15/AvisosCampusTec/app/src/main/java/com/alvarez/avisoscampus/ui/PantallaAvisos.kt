
package com.alvarez.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alvarez.avisoscampus.notificacion.Notificador
import com.alvarez.avisoscampus.ui.theme.GrisPendiente
import com.alvarez.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {

    val contexto = LocalContext.current

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            Notificador.mostrar(contexto, aviso)

            // TODO Paso 8: manejar permiso denegado
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AvisosCampus",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = "Centro de notificaciones",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = "Panel de avisos",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Gestiona tus notificaciones y eventos del dispositivo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            TarjetaAvisos(
                numero = "01",
                titulo = "Permiso de notificaciones"
            ) {

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = GrisPendiente.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "Sin pedir",
                        color = GrisPendiente,
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        )
                    )
                }

                Button(
                    onClick = {
                        // TODO Paso 2
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Permitir notificaciones")
                }
            }

            TarjetaAvisos(
                numero = "02",
                titulo = "Avisos"
            ) {

                Text(
                    text = "Genera una notificación inmediata.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Button(
                    onClick = {
                        // TODO Paso 3
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mostrar aviso")
                }
            }

            TarjetaAvisos(
                numero = "03",
                titulo = "Recordatorio"
            ) {

                Text(
                    text = "Programa un aviso en 10 segundos.",
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    Button(
                        onClick = {
                            // TODO Paso 5
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Recordar en 10 s")
                    }

                    OutlinedButton(
                        onClick = {
                            // TODO Paso 5
                        },
                        enabled = false
                    ) {
                        Text("Cancelar")
                    }
                }
            }

            TarjetaAvisos(
                numero = "04",
                titulo = "Receptor de anuncios"
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = "Escuchar el modo avión",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            text = "Detecta cambios del sistema.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = false,
                        onCheckedChange = {
                            // TODO Paso 6
                        }
                    )
                }
            }

            TarjetaAvisos(
                numero = "05",
                titulo = "Historial"
            ) {

                Text(
                    text = "Sin avisos todavía",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // TODO Paso 7: integrar HistorialAvisos
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun TarjetaAvisos(
    numero: String,
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = numero,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
                    .copy(alpha = 0.5f)
            )

            contenido()
        }
    }
}
