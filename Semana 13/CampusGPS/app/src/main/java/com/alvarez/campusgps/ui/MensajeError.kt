package com.alvarez.campusgps.ui

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.campusgps.model.TipoError
import com.alvarez.campusgps.model.UbicacionUi

@Composable
fun MensajeError(
    error: UbicacionUi.Error,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        // Recuadro rosado

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color(0xFFFBE4E4),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = error.mensaje,
                color = Color(0xFFAD4444),
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                lineHeight = 17.sp,
                modifier = Modifier.widthIn(max = 270.dp)
            )
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
                    border = BorderStroke(
                        1.dp,
                        Color(0xFF176B9A)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF176B9A)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 0.dp
                    ),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Abrir ajustes",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            TipoError.GPS_APAGADO -> {
                OutlinedButton(
                    onClick = onReintentar,
                    border = BorderStroke(
                        1.dp,
                        Color(0xFF176B9A)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF176B9A)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 0.dp
                    ),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Reintentar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            TipoError.SIN_PLAY_SERVICES -> Unit

            TipoError.OTRO -> {
                OutlinedButton(
                    onClick = onReintentar,
                    border = BorderStroke(
                        1.dp,
                        Color(0xFF176B9A)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF176B9A)
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 0.dp
                    ),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Reintentar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
