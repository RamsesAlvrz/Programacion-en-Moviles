
package com.tecsup.smartcatalog.ui.lista

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.smartcatalog.ui.UiState
import com.tecsup.smartcatalog.ui.components.AppButton
import com.tecsup.smartcatalog.ui.components.ProductoCard
import com.tecsup.smartcatalog.ui.components.SmartCatalogHeader

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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        SmartCatalogHeader(
            titulo = "SmartCatalog",
            subtitulo = "Encuentra el celular perfecto para ti"
        )

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
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = actual.mensaje,
                        color = MaterialTheme.colorScheme.error
                    )

                    AppButton(
                        texto = "Reintentar",
                        onClick = { viewModel.cargarProductos() }
                    )
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    Text(
                        text = "Explora nuestro catálogo",
                        style = MaterialTheme.typography.titleLarge
                    )

                    OutlinedTextField(
                        value = busqueda,
                        onValueChange = {
                            viewModel.actualizarBusqueda(it)
                        },
                        label = {
                            Text("Buscar celular o marca")
                        },
                        placeholder = {
                            Text("Ejemplo: Samsung, iPhone...")
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = "Filtrar por marca",
                        style = MaterialTheme.typography.titleSmall
                    )

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
                                label = {
                                    Text(marca)
                                }
                            )
                        }
                    }

                    var menuExpandido =
                        androidx.compose.runtime.remember {
                            androidx.compose.runtime.mutableStateOf(false)
                        }

                    Box {
                        OutlinedButton(
                            onClick = {
                                menuExpandido.value = true
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ordenar: $ordenPrecio")
                        }

                        DropdownMenu(
                            expanded = menuExpandido.value,
                            onDismissRequest = {
                                menuExpandido.value = false
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
                                        menuExpandido.value = false
                                    }
                                )
                            }
                        }
                    }

                    AppButton(
                        texto = "★ Mis favoritos",
                        onClick = onVerFavoritos
                    )

                    Text(
                        text = "${productosFiltrados.size} celulares encontrados",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (actual.offline) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
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

                if (productosFiltrados.isEmpty()) {

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (productos.isEmpty()) {
                                "No hay celulares disponibles."
                            } else {
                                "No encontramos celulares con esos filtros."
                            },
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                } else {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 20.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = productosFiltrados,
                            key = { it.id }
                        ) { item ->

                            ProductoCard(
                                item = item,
                                onClick = {
                                    onVerDetalle(item.id)
                                },
                                onFavorito = {
                                    viewModel.cambiarFavorito(item)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
