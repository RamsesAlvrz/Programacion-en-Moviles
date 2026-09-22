package com.alvarez.lab04carritotecsup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ejemplo.lab04carritotecsup.Producto

@Composable
fun TarjetaProducto(producto: Producto, onEliminar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFD1C4E9)), // Borde morado suave
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1B1F)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "S/ %.2f  x  %d".format(producto.precio, producto.cantidad),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            val importeTotal = producto.precio * producto.cantidad
            Text(
                text = "S/ %.2f".format(importeTotal),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF5A429B), // Morado TECSUP
                modifier = Modifier.padding(end = 4.dp)
            )

            IconButton(onClick = onEliminar) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFB3261E)
                )
            }
        }
    }
}