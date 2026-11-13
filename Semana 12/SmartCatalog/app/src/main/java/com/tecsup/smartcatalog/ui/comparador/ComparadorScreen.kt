package com.tecsup.smartcatalog.ui.comparador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.tecsup.smartcatalog.domain.model.Item
import com.tecsup.smartcatalog.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparadorScreen(
    productos: List<Item>,
    onVolver: () -> Unit
) {
    var productoUno by remember { mutableStateOf<Item?>(null) }
    var productoDos by remember { mutableStateOf<Item?>(null) }

    var menuUno by remember { mutableStateOf(false) }
    var menuDos by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPremium)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoPremium
                )
            }

            Text(
                text = "Comparador de celulares",
                style = MaterialTheme.typography.titleLarge,
                color = TextoPremium,
                fontWeight = FontWeight.Bold
            )
        }

        Surface(
            color = SuperficiePremium,
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                modifier = Modifier.padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CompareArrows,
                    contentDescription = null,
                    tint = AzulBrillante,
                    modifier = Modifier.size(32.dp)
                )

                Column {
                    Text(
                        text = "Encuentra tu celular ideal",
                        color = TextoPremium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Selecciona dos modelos y compara sus características.",
                        color = TextoPremiumSecundario,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        // SELECTOR DEL PRIMER CELULAR
        ExposedDropdownMenuBox(
            expanded = menuUno,
            onExpandedChange = { menuUno = it }
        ) {
            OutlinedTextField(
                value = productoUno?.nombre ?: "Seleccionar primer celular",
                onValueChange = {},
                readOnly = true,
                label = { Text("Celular 1") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = menuUno
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextoPremium,
                    unfocusedTextColor = TextoPremium,
                    focusedBorderColor = AzulBrillante,
                    unfocusedBorderColor = BordePremium,
                    focusedContainerColor = SuperficiePremium,
                    unfocusedContainerColor = SuperficiePremium
                )
            )

            ExposedDropdownMenu(
                expanded = menuUno,
                onDismissRequest = { menuUno = false }
            ) {
                productos.forEach { producto ->
                    DropdownMenuItem(
                        text = { Text(producto.nombre) },
                        onClick = {
                            productoUno = producto
                            menuUno = false
                        }
                    )
                }
            }
        }

        // SELECTOR DEL SEGUNDO CELULAR
        ExposedDropdownMenuBox(
            expanded = menuDos,
            onExpandedChange = { menuDos = it }
        ) {
            OutlinedTextField(
                value = productoDos?.nombre ?: "Seleccionar segundo celular",
                onValueChange = {},
                readOnly = true,
                label = { Text("Celular 2") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = menuDos
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextoPremium,
                    unfocusedTextColor = TextoPremium,
                    focusedBorderColor = AzulBrillante,
                    unfocusedBorderColor = BordePremium,
                    focusedContainerColor = SuperficiePremium,
                    unfocusedContainerColor = SuperficiePremium
                )
            )

            ExposedDropdownMenu(
                expanded = menuDos,
                onDismissRequest = { menuDos = false }
            ) {
                productos.forEach { producto ->
                    DropdownMenuItem(
                        text = { Text(producto.nombre) },
                        onClick = {
                            productoDos = producto
                            menuDos = false
                        }
                    )
                }
            }
        }

        if (productoUno != null && productoDos != null) {

            val primero = productoUno!!
            val segundo = productoDos!!

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ComparadorProductoCard(
                    producto = primero,
                    modifier = Modifier.weight(1f)
                )

                ComparadorProductoCard(
                    producto = segundo,
                    modifier = Modifier.weight(1f)
                )
            }

            Surface(
                color = SuperficiePremium,
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Resultado de la comparación",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextoPremium
                    )

                    HorizontalDivider(color = BordePremium)

                    val diferencia = kotlin.math.abs(
                        primero.precio - segundo.precio
                    )

                    val resultado = when {
                        primero.precio < segundo.precio ->
                            "${primero.nombre} es más económico"

                        segundo.precio < primero.precio ->
                            "${segundo.nombre} es más económico"

                        else -> "Ambos celulares tienen el mismo precio"
                    }

                    Text(
                        text = resultado,
                        color = AzulBrillante,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Diferencia de precio: $ %.2f".format(diferencia),
                        color = TextoPremiumSecundario
                    )
                }
            }
        }
    }
}

@Composable
private fun ComparadorProductoCard(
    producto: Item,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = SuperficiePremium,
        shape = RoundedCornerShape(18.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            BordePremium
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                color = Color(0xFF243653),
                shape = RoundedCornerShape(12.dp)
            ) {
                AsyncImage(
                    model = producto.imagen,
                    contentDescription = producto.nombre,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(12.dp)
                )
            }

            Text(
                text = producto.nombre,
                color = TextoPremium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = producto.marca,
                color = TextoPremiumSecundario
            )

            Text(
                text = "$ ${producto.precio}",
                color = AzulBrillante,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
