
package com.alvarez.murocampus.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.murocampus.viewmodel.PostsViewModel
import com.alvarez.murocampus.viewmodel.UiState

private val AzulBusqueda = Color(0xFF176B98)
private val AzulSuave = Color(0xFFE4EEF5)
private val FondoLista = Color(0xFFF5F7FA)
private val TextoOscuro = Color(0xFF202B38)
private val TextoGris = Color(0xFF64748B)

@Composable
fun PantallaLista(
    vm: PostsViewModel,
    onVerDetalle: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val estado by vm.lista.collectAsStateWithLifecycle()

    val usuarioFiltro by
    vm.usuarioFiltro.collectAsStateWithLifecycle()

    val busqueda by
    vm.busqueda.collectAsStateWithLifecycle()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        // Encabezado del listado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 12.dp
                )
        ) {

            Text(
                text = "Explorar publicaciones",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextoOscuro
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Encuentra contenido de nuestra comunidad",
                fontSize = 13.sp,
                color = TextoGris
            )

            Spacer(modifier = Modifier.height(16.dp))

            BuscadorPublicaciones(
                texto = busqueda,
                onTextoChange = vm::buscar,
                onLimpiar = vm::limpiarBusqueda
            )
        }

        // Filtro por usuario
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
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

        HorizontalDivider(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 6.dp
            ),
            color = Color(0xFFDDE5EC)
        )

        // Contador de resultados
        val estadoActual = estado

        if (estadoActual is UiState.Exito) {

            ContadorPublicaciones(
                cantidad = estadoActual.datos.size,
                buscando = busqueda.isNotBlank()
            )
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

                        EstadoSinResultados(
                            busqueda = busqueda,
                            onLimpiar = vm::limpiarBusqueda,
                            modifier = Modifier.align(
                                Alignment.Center
                            )
                        )

                    } else {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(
                                top = 4.dp,
                                bottom = 88.dp
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
private fun BuscadorPublicaciones(
    texto: String,
    onTextoChange: (String) -> Unit,
    onLimpiar: () -> Unit
) {

    OutlinedTextField(
        value = texto,
        onValueChange = onTextoChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        placeholder = {
            Text(
                text = "Buscar por título...",
                color = TextoGris,
                fontSize = 14.sp
            )
        },
        leadingIcon = {

            IconoBusqueda(
                color = AzulBusqueda,
                modifier = Modifier.size(22.dp)
            )
        },
        trailingIcon = {

            if (texto.isNotEmpty()) {

                IconButton(
                    onClick = onLimpiar,
                    modifier = Modifier.semantics {
                        contentDescription = "Limpiar búsqueda"
                    }
                ) {

                    IconoCerrar(
                        color = TextoGris,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = AzulBusqueda,
            unfocusedBorderColor = Color(0xFFD1DCE5),
            focusedTextColor = TextoOscuro,
            unfocusedTextColor = TextoOscuro,
            cursorColor = AzulBusqueda
        )
    )
}

@Composable
private fun ContadorPublicaciones(
    cantidad: Int,
    buscando: Boolean
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = if (buscando) {
                "Resultados de búsqueda"
            } else {
                "Publicaciones disponibles"
            },
            color = TextoGris,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Surface(
            color = AzulSuave,
            shape = RoundedCornerShape(20.dp)
        ) {

            Text(
                text = if (cantidad == 1) {
                    "1 publicación"
                } else {
                    "$cantidad publicaciones"
                },
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 6.dp
                ),
                color = AzulBusqueda,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun EstadoSinResultados(
    busqueda: String,
    onLimpiar: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Surface(
            modifier = Modifier.size(84.dp),
            color = AzulSuave,
            shape = RoundedCornerShape(28.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                IconoBusqueda(
                    color = AzulBusqueda,
                    modifier = Modifier.size(38.dp)
                )
            }
        }

        Text(
            text = if (busqueda.isNotBlank()) {
                "Sin coincidencias"
            } else {
                "Sin publicaciones"
            },
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextoOscuro
        )

        Text(
            text = if (busqueda.isNotBlank()) {
                "No encontramos publicaciones para \"$busqueda\". Intenta con otro título."
            } else {
                "No hay publicaciones disponibles para este usuario."
            },
            fontSize = 14.sp,
            color = TextoGris,
            textAlign = TextAlign.Center
        )

        if (busqueda.isNotBlank()) {

            OutlinedButton(
                onClick = onLimpiar,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AzulBusqueda
                )
            ) {

                Text(
                    text = "Limpiar búsqueda",
                    fontWeight = FontWeight.SemiBold
                )
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
            containerColor = FondoLista,
            labelColor = AzulBusqueda,
            selectedContainerColor = AzulBusqueda,
            selectedLabelColor = Color.White
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = seleccionado,
            borderColor = Color(0xFFB8D5E5),
            selectedBorderColor = AzulBusqueda
        )
    )
}

// Icono de lupa sin dependencias adicionales
@Composable
private fun IconoBusqueda(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val grosor = size.minDimension * 0.09f

        drawCircle(
            color = color,
            radius = size.minDimension * 0.29f,
            center = Offset(
                x = size.width * 0.39f,
                y = size.height * 0.39f
            ),
            style = Stroke(width = grosor)
        )

        drawLine(
            color = color,
            start = Offset(
                x = size.width * 0.61f,
                y = size.height * 0.61f
            ),
            end = Offset(
                x = size.width * 0.88f,
                y = size.height * 0.88f
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun IconoCerrar(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val grosor = size.minDimension * 0.10f

        drawLine(
            color = color,
            start = Offset(
                size.width * 0.25f,
                size.height * 0.25f
            ),
            end = Offset(
                size.width * 0.75f,
                size.height * 0.75f
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(
                size.width * 0.75f,
                size.height * 0.25f
            ),
            end = Offset(
                size.width * 0.25f,
                size.height * 0.75f
            ),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}
