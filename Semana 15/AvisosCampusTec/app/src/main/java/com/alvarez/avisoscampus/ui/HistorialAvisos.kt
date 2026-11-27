
package com.alvarez.avisoscampus.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.avisoscampus.model.Aviso
import com.alvarez.avisoscampus.model.Origen

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {

    if (avisos.isEmpty()) {

        Text(
            text = "Sin avisos todavía",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

    } else {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            avisos.take(20).forEach { aviso ->

                FilaAviso(aviso)
            }
        }
    }
}

@Composable
private fun FilaAviso(aviso: Aviso) {

    val colorOrigen = when (aviso.origen) {
        Origen.BOTON -> Color(0xFF287DA8)
        Origen.RECORDATORIO -> Color(0xFFF4B942)
        Origen.MODO_AVION -> Color(0xFF7655B5)
    }

    val colorTextoOrigen = when (aviso.origen) {
        Origen.RECORDATORIO -> Color(0xFF302300)
        else -> Color.White
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.outlineVariant
                    .copy(alpha = 0.45f),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .background(
                    color = colorOrigen,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(
                    horizontal = 7.dp,
                    vertical = 4.dp
                )
        ) {

            Text(
                text = aviso.origen.texto,
                color = colorTextoOrigen,
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = aviso.titulo,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = aviso.texto,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = aviso.hora,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
