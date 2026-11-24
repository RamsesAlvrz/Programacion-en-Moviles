package com.alvarez.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.murocampus.viewmodel.PostsViewModel
import com.alvarez.murocampus.viewmodel.UiState

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onVerDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val estado by vm.lista.collectAsStateWithLifecycle()

    val usuarioFiltro by
    vm.usuarioFiltro.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 12.dp,
                vertical = 4.dp
            ),
            horizontalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            item {
                ChipUsuario(
                    texto = "Todos",
                    seleccionado = usuarioFiltro == null,
                    onClick = {
                        vm.filtrar(null)
                    }
                )
            }

            items((1..10).toList()) { userId ->

                ChipUsuario(
                    texto = "Usuario $userId",
                    seleccionado = usuarioFiltro == userId,
                    onClick = {
                        vm.filtrar(userId)
                    }
                )
            }
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {

            when (val actual = estado) {

                UiState.Cargando -> {
                    Cargando()
                }

                is UiState.Error -> {
                    ErrorConReintento(
                        mensaje = actual.mensaje,
                        onReintentar = { vm.cargar() }
                    )
                }

                is UiState.Exito -> {

                    if (actual.datos.isEmpty()) {

                        Text(
                            text = "Sin publicaciones",
                            modifier = Modifier.align(
                                Alignment.Center
                            ),
                            color = MaterialTheme.colorScheme
                                .onSurfaceVariant
                        )

                    } else {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                vertical = 4.dp
                            )
                        ) {

                            items(
                                items = actual.datos,
                                key = { it.id }
                            ) { post ->

                                TarjetaPost(
                                    userId = post.userId,
                                    titulo = post.title,
                                    contenido = post.body,
                                    onClick = {
                                        onVerDetalle(post.id)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChipUsuario(
    texto: String,
    seleccionado: Boolean,
    onClick: () -> Unit
) {

    FilterChip(
        selected = seleccionado,
        onClick = onClick,
        label = {
            Text(
                text = texto,
                fontWeight = FontWeight.SemiBold
            )
        },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color(0xFFF5F7FA),
            labelColor = Color(0xFF176B98),
            selectedContainerColor = Color(0xFF176B98),
            selectedLabelColor = Color.White
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = seleccionado,
            borderColor = Color(0xFFB8D5E5),
            selectedBorderColor = Color(0xFF176B98)
        )
    )
}