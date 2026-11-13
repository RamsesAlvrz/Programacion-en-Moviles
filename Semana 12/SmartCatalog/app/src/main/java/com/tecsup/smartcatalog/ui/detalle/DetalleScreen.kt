
package com.tecsup.smartcatalog.ui.detalle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.tecsup.smartcatalog.ui.UiState
import com.tecsup.smartcatalog.ui.theme.*

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
            .background(FondoPremium)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        // ENCABEZADO
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver al catálogo",
                    tint = TextoPremium
                )
            }

            Text(
                text = "Detalle del celular",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextoPremium
            )
        }

        when (val actual = estado) {

            is UiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = AzulBrillante
                    )
                }
            }

            is UiState.Error -> {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = SuperficiePremium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = actual.mensaje,
                            color = Color(0xFFFCA5A5)
                        )

                        Button(
                            onClick = {
                                viewModel.cargarDetalle(productoId)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AzulElectrico
                            )
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            is UiState.Success -> {
                val producto = actual.data

                // IMAGEN PRINCIPAL
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(310.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFF1F5F9)
                ) {
                    AsyncImage(
                        model = producto.imagen,
                        contentDescription = producto.nombre,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // INFORMACIÓN DEL CELULAR
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    color = SuperficiePremium,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        BordePremium
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = producto.marca.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            color = AzulBrillante,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = producto.nombre,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextoPremium
                        )

                        Text(
                            text = "$ ${producto.precio}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = AzulBrillante
                        )

                        HorizontalDivider(
                            color = BordePremium
                        )

                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextoPremium
                        )

                        Text(
                            text = producto.descripcion,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoPremiumSecundario
                        )
                    }
                }

                // BOTÓN DE FAVORITOS
                Button(
                    onClick = {
                        viewModel.cambiarFavorito(producto)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulElectrico
                    )
                ) {
                    Icon(
                        imageVector = if (producto.favorito) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = null
                    )

                    Spacer(Modifier.width(10.dp))

                    Text(
                        text = if (producto.favorito) {
                            "Quitar de favoritos"
                        } else {
                            "Agregar a favoritos"
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
