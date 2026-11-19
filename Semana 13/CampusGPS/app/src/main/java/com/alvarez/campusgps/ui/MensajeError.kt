package com.alvarez.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alvarez.campusgps.model.TipoError
import com.alvarez.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = colores.errorContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Atención",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = colores.onErrorContainer
                )

                Text(
                    text = error.mensaje,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colores.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
        }

        when (error.tipo) {
            TipoError.SIN_PERMISO -> {
                OutlinedButton(
                    onClick = {
                        val intent = Intent(
                            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        ).apply {
                            data = Uri.fromParts(
                                "package",
                                contexto.packageName,
                                null
                            )
                        }
                        contexto.startActivity(intent)
                    },
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Abrir ajustes")
                }
            }

            TipoError.GPS_APAGADO,
            TipoError.OTRO -> {
                OutlinedButton(
                    onClick = onReintentar,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text("Reintentar")
                }
            }

            TipoError.SIN_PLAY_SERVICES -> Unit
        }
    }
}
