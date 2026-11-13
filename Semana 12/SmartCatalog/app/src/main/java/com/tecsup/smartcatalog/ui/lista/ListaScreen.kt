package com.tecsup.smartcatalog.ui.lista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SmartCatalogHeader(
            titulo = "SmartCatalog",
            subtitulo = "Descubre tu próximo celular"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Explora nuestro catálogo",
                style = MaterialTheme.typography.titleLarge
            )

            AppButton(
                texto = "★ Mis favoritos",
                onClick = onVerFavoritos
            )
        }

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
                        text = "Error: ${actual.mensaje}",
                        color = MaterialTheme.colorScheme.error
                    )

                    AppButton(
                        texto = "Reintentar",
                        onClick = { viewModel.cargarProductos() }
                    )
                }
            }

            is UiState.Success -> {

                if (actual.offline) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            ),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "Sin conexión: mostrando productos guardados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                if (actual.data.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No hay celulares disponibles")
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
                            items = actual.data,
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
