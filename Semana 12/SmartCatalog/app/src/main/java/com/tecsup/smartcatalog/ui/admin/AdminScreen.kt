package com.tecsup.smartcatalog.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Panel de administrador",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onCerrarSesion
        ) {
            Text("Cerrar sesión")
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (val actual = estado) {

            is AdminUiState.Loading -> {
                CircularProgressIndicator()
            }

            is AdminUiState.Error -> {
                Text(
                    text = "Error: ${actual.mensaje}",
                    color = MaterialTheme.colorScheme.error
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = { viewModel.cargarDatos() }) {
                    Text("Reintentar")
                }
            }

            is AdminUiState.Success -> {

                Text(
                    text = "Resumen general",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                EstadisticaCard(
                    titulo = "Productos registrados",
                    valor = actual.stats.totalProductos.toString()
                )

                EstadisticaCard(
                    titulo = "Usuarios registrados",
                    valor = actual.stats.totalUsuarios.toString()
                )

                EstadisticaCard(
                    titulo = "Mis favoritos",
                    valor = actual.stats.totalFavoritos.toString()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Usuarios registrados",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (actual.usuarios.isEmpty()) {
                    Text("No hay usuarios registrados")
                } else {
                    LazyColumn {
                        items(
                            items = actual.usuarios,
                            key = { it.uid }
                        ) { usuario ->

                            UsuarioCard(usuario)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EstadisticaCard(
    titulo: String,
    valor: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = valor,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun UsuarioCard(usuario: UsuarioAdmin) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = usuario.nombre,
                style = MaterialTheme.typography.titleMedium
            )

            Text("Correo: ${usuario.correo}")

            Text("Rol: ${usuario.rol}")
        }
    }
}
