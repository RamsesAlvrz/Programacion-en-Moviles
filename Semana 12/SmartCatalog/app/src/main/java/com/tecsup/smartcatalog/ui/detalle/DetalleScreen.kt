
package com.tecsup.smartcatalog.ui.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.tecsup.smartcatalog.ui.UiState
import com.tecsup.smartcatalog.ui.components.AppButton
import com.tecsup.smartcatalog.ui.components.SmartCatalogHeader
import com.tecsup.smartcatalog.ui.theme.AmarilloFavorito

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
        modifier = Modifier.fillMaxSize()
    ) {

        SmartCatalogHeader(
            titulo = "Detalle del celular",
            subtitulo = "Conoce más sobre este producto"
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            TextButton(onClick = onVolver) {
                Text("← Volver al catálogo")
            }

            when (val actual = estado) {

                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    Text("Cargando información...")
                }

                is UiState.Error -> {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "No se pudo cargar el producto",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = actual.mensaje,
                                color = MaterialTheme.colorScheme.error
                            )

                            AppButton(
                                texto = "Reintentar",
                                onClick = {
                                    viewModel.cargarDetalle(productoId)
                                }
                            )
                        }
                    }
                }

                is UiState.Success -> {

                    val producto = actual.data

                    // Imagen del celular
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .padding(20.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = producto.imagen,
                                contentDescription = producto.nombre,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    // Información principal
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {

                            Text(
                                text = producto.marca.uppercase(),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = producto.nombre,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )

                            HorizontalDivider()

                            Text(
                                text = "Precio",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = "$ ${producto.precio}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Descripción
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            Text(
                                text = "Descripción",
                                style = MaterialTheme.typography.titleLarge
                            )

                            Text(
                                text = producto.descripcion,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Botón de favoritos
                    Button(
                        onClick = {
                            viewModel.cambiarFavorito(producto)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (producto.favorito) {
                                AmarilloFavorito
                            } else {
                                MaterialTheme.colorScheme.primary
                            },
                            contentColor = if (producto.favorito) {
                                MaterialTheme.colorScheme.onTertiary
                            } else {
                                MaterialTheme.colorScheme.onPrimary
                            }
                        )
                    ) {
                        Text(
                            text = if (producto.favorito) {
                                "★ Quitar de favoritos"
                            } else {
                                "☆ Agregar a favoritos"
                            },
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier.padding(6.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
