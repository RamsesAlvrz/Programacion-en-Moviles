package com.alvarez.alvareztecstore.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.alvareztecstore.Producto

@Composable
fun TarjetaProducto(
    producto: Producto,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = producto.precio,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Box para vincular el DropdownMenu con el boton de 3 puntos
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones de producto"
                    )
                }

                // DropdownMenu basico con 3 opciones
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Favoritos") },
                        onClick = { expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Compartir") },
                        onClick = { expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Reportar") },
                        onClick = { expanded = false }
                    )
                }
            }
        }
    }
}