package com.tecsup.smartcatalog.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.smartcatalog.ui.components.AppButton
import com.tecsup.smartcatalog.ui.components.SmartCatalogHeader
import com.tecsup.smartcatalog.ui.components.StatCard

@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        SmartCatalogHeader(
            titulo = "Administración",
            subtitulo = "Gestiona y supervisa SmartCatalog"
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    text = "Resumen general",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            when (val actual = estado) {

                is AdminUiState.Loading -> {
                    item {
                        CircularProgressIndicator()
                    }
                }

                is AdminUiState.Error -> {
                    item {
                        Text(
                            text = actual.mensaje,
                            color = MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        AppButton(
                            texto = "Reintentar",
                            onClick = { viewModel.cargarDatos() }
                        )
                    }
                }

                is AdminUiState.Success -> {

                    item {
                        StatCard(
                            titulo = "Productos registrados",
                            valor = actual.stats.totalProductos.toString()
                        )
                    }

                    item {
                        StatCard(
                            titulo = "Usuarios registrados",
                            valor = actual.stats.totalUsuarios.toString()
                        )
                    }

                    item {
                        StatCard(
                            titulo = "Mis favoritos",
                            valor = actual.stats.totalFavoritos.toString()
                        )
                    }

                    item {
                        Text(
                            text = "Usuarios registrados",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    items(
                        items = actual.usuarios,
                        key = { it.uid }
                    ) { usuario ->
                        UsuarioCard(usuario)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                AppButton(
                    texto = "Cerrar sesión",
                    onClick = onCerrarSesion
                )
            }
        }
    }
}

@Composable
fun UsuarioCard(usuario: UsuarioAdmin) {

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Text(
                text = usuario.nombre,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = usuario.correo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "Rol: ${usuario.rol}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
