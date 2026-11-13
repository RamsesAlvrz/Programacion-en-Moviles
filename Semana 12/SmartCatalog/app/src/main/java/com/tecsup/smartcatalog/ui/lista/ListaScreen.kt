package com.tecsup.smartcatalog.ui.lista

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.tecsup.smartcatalog.domain.model.Item
import com.tecsup.smartcatalog.ui.UiState

@Composable
fun ListaScreen(
    viewModel: ListaViewModel,
    onVerDetalle: (Int) -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "SmartCatalog - Celulares",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        when (val actual = estado) {

            is UiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }

            is UiState.Error -> {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Error: ${actual.mensaje}")

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = { viewModel.cargarProductos() }) {
                        Text("Reintentar")
                    }
                }
            }

            is UiState.Success -> {
                if (actual.data.isEmpty()) {
                    Text(
                        "No hay celulares disponibles",
                        modifier = Modifier.padding(16.dp)
                    )
                } else {
                    LazyColumn {
                        items(
                            items = actual.data,
                            key = { it.id }
                        ) { item ->
                            ProductoCard(
                                item = item,
                                onClick = { onVerDetalle(item.id) },
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

@Composable
fun ProductoCard(
    item: Item,
    onClick: () -> Unit,
    onFavorito: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            AsyncImage(
                model = item.imagen,
                contentDescription = item.nombre,
                modifier = Modifier.size(90.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nombre,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(text = item.marca)

                Text(text = "$ ${item.precio}")

                TextButton(onClick = onFavorito) {
                    Text(
                        if (item.favorito)
                            "★ Quitar favorito"
                        else
                            "☆ Agregar favorito"
                    )
                }
            }
        }
    }
}
