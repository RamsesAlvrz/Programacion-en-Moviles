package com.tecsup.smartcatalog.ui.favoritos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.smartcatalog.ui.lista.ProductoCard

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

        TextButton(
            onClick = onVolver,
            modifier = Modifier.padding(8.dp)
        ) {
            Text("← Volver al catálogo")
        }

        Text(
            text = "Mis celulares favoritos",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )

        if (favoritos.isEmpty()) {

            Text(
                text = "Todavía no tienes celulares favoritos.",
                modifier = Modifier.padding(16.dp)
            )

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize()
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
