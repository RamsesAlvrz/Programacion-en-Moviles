
package com.tecsup.smartcatalog.ui.favoritos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.smartcatalog.ui.components.ProductoCard
import com.tecsup.smartcatalog.ui.theme.*

@Composable
fun FavoritosScreen(
    viewModel: FavoritosViewModel,
    onVolver: () -> Unit,
    onVerDetalle: (Int) -> Unit
) {
    val favoritos by viewModel.favoritos.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPremium)
    ) {

        // ENCABEZADO
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = TextoPremium
                )
            }

            Column {
                Text(
                    text = "Mis favoritos",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextoPremium
                )

                Text(
                    text = "${favoritos.size} celulares guardados",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoPremiumSecundario
                )
            }
        }

        HorizontalDivider(
            color = BordePremium
        )

        if (favoritos.isEmpty()) {

            // ESTADO VACÍO
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = SuperficiePremium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = AzulBrillante,
                            modifier = Modifier.size(48.dp)
                        )

                        Text(
                            text = "Aún no tienes favoritos",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextoPremium
                        )

                        Text(
                            text = "Explora el catálogo y guarda los celulares que más te gusten.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoPremiumSecundario
                        )

                        Button(
                            onClick = onVolver,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AzulElectrico
                            )
                        ) {
                            Text("Explorar catálogo")
                        }
                    }
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
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
