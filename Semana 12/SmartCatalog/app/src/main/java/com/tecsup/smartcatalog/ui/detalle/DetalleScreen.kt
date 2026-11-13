package com.tecsup.smartcatalog.ui.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.tecsup.smartcatalog.ui.UiState

@Composable
fun DetalleScreen(
    productoId: Int,
    viewModel: DetalleViewModel,
    onVolver: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(productoId) {
        viewModel.cargarDetalle(productoId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        TextButton(onClick = onVolver) {
            Text("← Volver al catálogo")
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (val actual = estado) {
            is UiState.Loading -> {
                CircularProgressIndicator()
                Text("Cargando información...")
            }

            is UiState.Error -> {
                Text(
                    text = "Error: ${actual.mensaje}",
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.cargarDetalle(productoId)
                    }
                ) {
                    Text("Reintentar")
                }
            }

            is UiState.Success -> {
                val producto = actual.data

                AsyncImage(
                    model = producto.imagen,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Marca: ${producto.marca}",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Precio: $ ${producto.precio}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Descripción",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = producto.descripcion)

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.cambiarFavorito(producto)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (producto.favorito)
                            "★ Quitar de favoritos"
                        else
                            "☆ Agregar a favoritos"
                    )
                }
            }
        }
    }
}
