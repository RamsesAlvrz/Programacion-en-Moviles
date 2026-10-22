package com.alvarez.lavacampus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Componente visual para mostrar el estado de un pedido
@Composable
fun EstadoPedidoChip(
    estado: String,
    modifier: Modifier = Modifier
) {

    // Determinar el color y el texto según el estado
    val (texto, colorTexto, colorFondo) = when (
        estado.uppercase().replace(" ", "_")
    ) {
        "RECIBIDO" -> Triple(
            "Recibido",
            Color(0xFFD97706),
            Color(0xFFFEF3C7)
        )

        "EN_PROCESO" -> Triple(
            "En proceso",
            Color(0xFF2563EB),
            Color(0xFFDBEAFE)
        )

        "LISTO" -> Triple(
            "Listo",
            Color(0xFF16A34A),
            Color(0xFFDCFCE7)
        )

        "ENTREGADO" -> Triple(
            "Entregado",
            Color(0xFF475569),
            Color(0xFFF1F5F9)
        )

        "CANCELADO" -> Triple(
            "Cancelado",
            Color(0xFFDC2626),
            Color(0xFFFEE2E2)
        )

        else -> Triple(
            estado,
            Color(0xFF475569),
            Color(0xFFF1F5F9)
        )
    }

    Text(
        text = texto,
        modifier = modifier
            .background(
                color = colorFondo,
                shape = RoundedCornerShape(50)
            )
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
        color = colorTexto,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold
    )
}
