
package com.alvarez.avisoscampus.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alvarez.avisoscampus.model.PermisoUi
import com.alvarez.avisoscampus.notificacion.Notificador
import com.alvarez.avisoscampus.receiver.ModoAvionReceiver
import com.alvarez.avisoscampus.ui.theme.GrisPendiente
import com.alvarez.avisoscampus.ui.theme.VerdePermitido
import com.alvarez.avisoscampus.ui.theme.RojoDenegado
import com.alvarez.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {

    val contexto = LocalContext.current

    val permiso by vm.permiso.collectAsState()

    val faltan by vm.faltan.collectAsState()

    val escuchandoAvion by vm.escuchandoAvion.collectAsState()

    val avisos by vm.avisos.collectAsState()

    val mensaje by vm.mensaje.collectAsState()

    val launcherPermiso = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    val launcherAjustes = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        vm.permisoRespondido(
            Notificador.tienePermiso(contexto)
        )
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
    }

    LaunchedEffect(Unit) {

        vm.eventos.collect { aviso ->

            val mostrado = Notificador.mostrar(
                contexto,
                aviso
            )

            if (!mostrado) {
                vm.noSePudoMostrar()
            }
        }
    }

    DisposableEffect(escuchandoAvion, contexto) {

        if (escuchandoAvion) {

            val receptor = ModoAvionReceiver { activado ->
                vm.alCambiarModoAvion(activado)
            }

            val filtro = IntentFilter(
                Intent.ACTION_AIRPLANE_MODE_CHANGED
            )

            ContextCompat.registerReceiver(
                contexto,
                receptor,
                filtro,
                ContextCompat.RECEIVER_NOT_EXPORTED
            )

            onDispose {
                contexto.unregisterReceiver(receptor)
            }

        } else {

            onDispose { }
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

                val textoEstado = when (permiso) {
                    PermisoUi.SinPedir -> "Sin pedir"
                    PermisoUi.Concedido -> "Concedido"
                    PermisoUi.Denegado -> "Denegado"
                }

                val colorEstado = when (permiso) {
                    PermisoUi.SinPedir -> GrisPendiente
                    PermisoUi.Concedido -> VerdePermitido
                    PermisoUi.Denegado -> RojoDenegado
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text("Estado: ")

                    Text(
                        text = textoEstado,
                        color = colorEstado,
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    Button(
                        onClick = {

                            if (Notificador.tienePermiso(contexto)) {

                                vm.permisoRespondido(true)

                            } else if (
                                Build.VERSION.SDK_INT >=
                                Build.VERSION_CODES.TIRAMISU
                            ) {

                                launcherPermiso.launch(
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            }
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
                            vm.avisarAhora()
                        }
                    ) {
                        Text("Mostrar aviso")
                    }
                }
            }

            mensaje?.let { textoError ->

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.error
                                .copy(alpha = 0.12f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = textoError,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (permiso == PermisoUi.Denegado) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    OutlinedButton(
                        onClick = {

                            val intent = Intent(
                                Settings.ACTION_APP_NOTIFICATION_SETTINGS
                            ).apply {

                                putExtra(
                                    Settings.EXTRA_APP_PACKAGE,
                                    contexto.packageName
                                )
                            }

                            launcherAjustes.launch(intent)
                        },
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primary
                        )
                    ) {

                        Text("Abrir ajustes")
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
                            vm.recordarEn(10)
                        },
                        enabled = faltan == null
                    ) {
                        Text("Recordar en 10 s")
                    }

                    OutlinedButton(
                        onClick = {
                            vm.cancelarRecordatorio()
                        },
                        enabled = faltan != null,
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.primary.copy(
                                alpha = if (faltan != null) 1f else 0.5f
                            )
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            disabledContentColor = GrisPendiente
                        )
                    ) {
                        Text("Cancelar")
                    }
                }

                faltan?.let { segundos ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "Faltan $segundos s",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
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
                        checked = escuchandoAvion,
                        onCheckedChange = {
                            vm.cambiarEscucha(it)
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

                if (escuchandoAvion) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = "Activa o desactiva el modo avión del celular",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            TarjetaAvisos(
                titulo = "Historial"
            ) {

                HistorialAvisos(avisos = avisos)
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
