
package com.alvarez.avisoscampus.ui

import androidx.compose.foundation.BorderStroke
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
                    Text(
                        text = "AvisosCampus",
                        style = MaterialTheme.typography.titleLarge
                    )
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
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            TarjetaAvisos(
                titulo = "Permiso de notificaciones"
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Estado: ")

                    Text(
                        text = "Sin pedir",
                        color = GrisPendiente,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Button(
                        onClick = {
                            // TODO Paso 2
                        }
                    ) {
                        Text("Pedir permiso")
                    }
                }
            }

            TarjetaAvisos(
                titulo = "Avisos"
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Button(
                        onClick = {
                            // TODO Paso 3
                        }
                    ) {
                        Text("Mostrar aviso")
                    }
                }
            }

            TarjetaAvisos(
                titulo = "Recordatorio"
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        onClick = {
                            // TODO Paso 5
                        }
                    ) {
                        Text("Recordar en 10 s")
                    }

                    OutlinedButton(
                        onClick = {
                            // TODO Paso 5
                        },
                        enabled = false,
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primary
                                .copy(alpha = 0.5f)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            disabledContentColor = GrisPendiente
                        )
                    ) {
                        Text("Cancelar")
                    }
                }

                // TODO Paso 5: mostrar segundos restantes
            }

            TarjetaAvisos(
                titulo = "Receptor de anuncios"
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Escuchar el modo avión",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Switch(
                        checked = false,
                        onCheckedChange = {
                            // TODO Paso 6
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor =
                                MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor =
                                MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = GrisPendiente,
                            uncheckedTrackColor =
                                MaterialTheme.colorScheme.surface,
                            uncheckedBorderColor = GrisPendiente
                        )
                    )
                }
            }

            TarjetaAvisos(
                titulo = "Historial"
            ) {

                Text(
                    text = "Sin avisos todavía",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // TODO Paso 7: integrar HistorialAvisos
            }
        }
    }
}

@Composable
private fun TarjetaAvisos(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {

    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = CardDefaults.outlinedCardBorder()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            contenido()
        }
    }
}
