
package com.tecsup.smartcatalog.ui.lista

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.smartcatalog.ui.UiState
import com.tecsup.smartcatalog.ui.components.CatalogoBanner
import com.tecsup.smartcatalog.ui.components.ProductoGridCard

@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onVerDetalle: (Int) -> Unit,
    onVerFavoritos: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val marcaSeleccionada by viewModel.marcaSeleccionada.collectAsStateWithLifecycle()
    val ordenPrecio by viewModel.ordenPrecio.collectAsStateWithLifecycle()

    var menuExpandido by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        when (val actual = estado) {

            is UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is UiState.Error -> {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = actual.mensaje,
                        color = MaterialTheme.colorScheme.error
                    )

                    Button(
                        onClick = { viewModel.cargarProductos() }
                    ) {
                        Text("Reintentar")
                    }
                }
            }

            is UiState.Success -> {

                val productos = actual.data

                val marcas = listOf("Todas") +
                        productos.map { it.marca }
                            .distinct()
                            .sorted()

                val productosFiltrados = productos
                    .filter { producto ->
                        val coincideBusqueda =
                            producto.nombre.contains(
                                busqueda,
                                ignoreCase = true
                            ) ||
                                    producto.marca.contains(
                                        busqueda,
                                        ignoreCase = true
                                    )

                        val coincideMarca =
                            marcaSeleccionada == "Todas" ||
                                    producto.marca == marcaSeleccionada

                        coincideBusqueda && coincideMarca
                    }
                    .let { lista ->
                        when (ordenPrecio) {
                            "Menor precio" ->
                                lista.sortedBy { it.precio }

                            "Mayor precio" ->
                                lista.sortedByDescending { it.precio }

                            else -> lista
                        }
                    }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 14.dp,
                        end = 14.dp,
                        top = 12.dp,
                        bottom = 28.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    // Encabezado dentro del scroll
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Smartphone,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier
                                            .padding(10.dp)
                                            .size(24.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "SmartCatalog",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Text(
                                        text = "Tecnología a tu alcance",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            IconButton(onClick = onVerFavoritos) {
                                Icon(
                                    imageVector = Icons.Default.FavoriteBorder,
                                    contentDescription = "Mis favoritos",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Buscador
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        OutlinedTextField(
                            value = busqueda,
                            onValueChange = {
                                viewModel.actualizarBusqueda(it)
                            },
                            placeholder = {
                                Text("Buscar celulares o marcas...")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Banner
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        CatalogoBanner(
                            onExplorar = {
                                viewModel.actualizarBusqueda("")
                                viewModel.seleccionarMarca("Todas")
                                viewModel.seleccionarOrden("Normal")
                            }
                        )
                    }

                    // Título de categorías
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = "Explorar marcas",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Filtros de marca
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            marcas.forEach { marca ->
                                FilterChip(
                                    selected = marcaSeleccionada == marca,
                                    onClick = {
                                        viewModel.seleccionarMarca(marca)
                                    },
                                    label = { Text(marca) }
                                )
                            }
                        }
                    }

                    // Contador y ordenamiento
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Nuestros celulares",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    text = "${productosFiltrados.size} productos",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Box {
                                TextButton(
                                    onClick = { menuExpandido = true }
                                ) {
                                    Text("Ordenar")
                                }

                                DropdownMenu(
                                    expanded = menuExpandido,
                                    onDismissRequest = {
                                        menuExpandido = false
                                    }
                                ) {
                                    listOf(
                                        "Normal",
                                        "Menor precio",
                                        "Mayor precio"
                                    ).forEach { orden ->
                                        DropdownMenuItem(
                                            text = { Text(orden) },
                                            onClick = {
                                                viewModel.seleccionarOrden(orden)
                                                menuExpandido = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (actual.offline) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "Sin conexión: mostrando productos guardados",
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }
                    }

                    if (productosFiltrados.isEmpty()) {
                        item(span = { GridItemSpan(maxLineSpan) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "No encontramos celulares con esos filtros.",
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    } else {
                        items(
                            items = productosFiltrados,
                            key = { it.id }
                        ) { producto ->

                            ProductoGridCard(
                                item = producto,
                                onClick = {
                                    onVerDetalle(producto.id)
                                },
                                onFavorito = {
                                    viewModel.cambiarFavorito(producto)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
