package com.alvarez.lab04carritotecsup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.lab04carritotecsup.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito() {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    val productos = remember { mutableStateListOf<Producto>() }

    val subtotal = productos.sumOf { it.precio * it.cantidad }
    val igv = subtotal * 0.18
    val total = subtotal + igv

    val purpleThemeColor = Color(0xFF5A429B)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Mi Carrito TECSUP",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = purpleThemeColor)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // --- FORMULARIO DE INGRESO ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del producto") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = precio,
                        onValueChange = { precio = it },
                        label = { Text("Precio (S/)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = cantidad,
                        onValueChange = { cantidad = it },
                        label = { Text("Cantidad") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        val precioNum = precio.toDoubleOrNull() ?: 0.0
                        val cantidadNum = cantidad.toIntOrNull() ?: 0
                        if (nombre.isNotBlank() && precioNum > 0 && cantidadNum > 0) {
                            productos.add(Producto(nombre.trim(), precioNum, cantidadNum))
                            nombre = ""
                            precio = ""
                            cantidad = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = purpleThemeColor)
                ) {
                    Text("AGREGAR", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            }

            // --- CENTRO: ESTADO VACÍO O LAZYCOLUMN ---
            if (productos.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Tu carrito está vacío",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF616161)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Agrega tu primer producto",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF9E9E9E)
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(productos) { producto ->
                        TarjetaProducto(
                            producto = producto,
                            onEliminar = { productos.remove(producto) }
                        )
                    }
                }
            }

            // --- PANEL DE TOTALES INFERIOR ---
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color(0xFFF3EDF7)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Productos: ${productos.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )

                    if (productos.isNotEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Subtotal", color = Color(0xFF49454F))
                            Text("S/ %.2f".format(subtotal), color = Color(0xFF49454F))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("IGV (18%)", color = Color(0xFF49454F))
                            Text("S/ %.2f".format(igv), color = Color(0xFF49454F))
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1C1B1F)
                        )
                        Text(
                            text = "S/ %.2f".format(total),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = purpleThemeColor
                        )
                    }
                }
            }
        }
    }
}