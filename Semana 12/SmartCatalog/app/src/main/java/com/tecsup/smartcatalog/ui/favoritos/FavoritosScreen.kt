
package com.tecsup.smartcatalog.ui.favoritos

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
import com.tecsup.smartcatalog.ui.components.ProductoCard
import com.tecsup.smartcatalog.ui.components.SmartCatalogHeader

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onVolver: () -> Unit,
    onVerDetalle: (Int) -> Unit
) {
    val favoritos by viewModel.favoritos.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // Encabezado personalizado
        SmartCatalogHeader(
            titulo = "Mis favoritos",
            subtitulo = "Tus celulares preferidos en un solo lugar"
        )

        // Botón para regresar al catálogo
        TextButton(
            onClick = onVolver,
            modifier = Modifier.padding(
                start = 12.dp,
                top = 8.dp,
                bottom = 4.dp
            )
        ) {
            Text(
                text = "← Volver al catálogo",
                style = MaterialTheme.typography.labelLarge
            )
        }

        // Título y contador de favoritos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Celulares guardados",
                style = MaterialTheme.typography.titleLarge
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = "${favoritos.size}",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 8.dp
                    )
                )
            }
        }

        if (favoritos.isEmpty()) {

            // Estado vacío
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "☆",
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Aún no tienes favoritos",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = "Explora el catálogo y guarda los celulares que más te gusten.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Button(onClick = onVolver) {
                            Text("Explorar catálogo")
                        }
                    }
                }
            }

        } else {

            // Lista de productos favoritos
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 8.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(
                    items = favoritos,
                    key = { it.id }
                ) { item ->

                    ProductoCard(
                        item = item,
                        onClick = {
                            onVerDetalle(item.id)
                        },
                        onFavorito = {
                            viewModel.quitarFavorito(item)
                        }
                    )
                }
            }
        }
    }
}
