
package com.alvarez.murocampus.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.murocampus.viewmodel.PostsViewModel
import com.alvarez.murocampus.viewmodel.UiState

private val RojoEliminar = Color(0xFFB3261E)
private val FondoEliminar = Color(0xFFFCE8E6)

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    modifier: Modifier = Modifier
) {

    val estado by
    vm.detalle.collectAsStateWithLifecycle()

    val eliminacion by
    vm.eliminacion.collectAsStateWithLifecycle()

    var mostrarConfirmacion by rememberSaveable(id) {
        mutableStateOf(false)
    }

    val eliminando = eliminacion == UiState.Cargando

    Box(
        modifier = modifier.fillMaxSize()
    ) {

        when (val actual = estado) {

            UiState.Cargando -> {

                Cargando()
            }

            is UiState.Error -> {

                ErrorConReintento(
                    mensaje = actual.mensaje,
                    onReintentar = { vm.abrir(id) }
                )
            }

            is UiState.Exito -> {

                val datos = actual.datos

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement =
                        Arrangement.spacedBy(10.dp)
                ) {

                    item {

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 2.dp
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {

                                Text(
                                    text = datos.post.title,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = Color(0xFF202B38)
                                )

                                Spacer(
                                    modifier = Modifier.height(10.dp)
                                )

                                Text(
                                    text = datos.post.body,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF64748B)
                                )

                                Spacer(
                                    modifier = Modifier.height(16.dp)
                                )

                                HorizontalDivider(
                                    color = Color(0xFFE2E8F0)
                                )

                                Spacer(
                                    modifier = Modifier.height(14.dp)
                                )

                                // Boton de eliminacion
                                Button(
                                    onClick = {
                                        vm.limpiarEliminacion()
                                        mostrarConfirmacion = true
                                    },
                                    enabled = !eliminando &&
                                            eliminacion !is UiState.Exito,
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = FondoEliminar,
                                        contentColor = RojoEliminar,
                                        disabledContainerColor =
                                            Color(0xFFE5E7EB),
                                        disabledContentColor =
                                            Color(0xFF64748B)
                                    )
                                ) {

                                    if (eliminando) {

                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            strokeWidth = 2.dp,
                                            color = RojoEliminar
                                        )

                                    } else {

                                        IconoEliminar(
                                            color = RojoEliminar,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.width(8.dp)
                                    )

                                    Text(
                                        text = if (eliminando) {
                                            "Eliminando..."
                                        } else {
                                            "Eliminar publicación"
                                        },
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                val resultado = eliminacion

                                if (resultado is UiState.Error) {

                                    Spacer(
                                        modifier = Modifier.height(12.dp)
                                    )

                                    Surface(
                                        modifier = Modifier.fillMaxWidth(),
                                        color = FondoEliminar,
                                        shape = RoundedCornerShape(10.dp)
                                    ) {

                                        Text(
                                            text = resultado.mensaje,
                                            modifier = Modifier.padding(12.dp),
                                            color = RojoEliminar,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {

                        Text(
                            text = "Comentarios (${datos.comentarios.size})",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    items(
                        items = datos.comentarios,
                        key = { it.id }
                    ) { comentario ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE4EEF5)
                            )
                        ) {

                            Column(
                                modifier = Modifier.padding(12.dp)
                            ) {

                                Text(
                                    text = comentario.email,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Spacer(
                                    modifier = Modifier.height(5.dp)
                                )

                                Text(
                                    text = comentario.body,
                                    color = Color(0xFF475569),
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialogo de confirmacion
    if (mostrarConfirmacion) {

        AlertDialog(
            onDismissRequest = {
                mostrarConfirmacion = false
            },
            icon = {

                Surface(
                    modifier = Modifier.size(54.dp),
                    shape = RoundedCornerShape(18.dp),
                    color = FondoEliminar
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        IconoEliminar(
                            color = RojoEliminar,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            },
            title = {

                Text(
                    text = "¿Eliminar publicación?",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF202B38)
                )
            },
            text = {

                Text(
                    text = "La publicación dejará de aparecer en el listado durante esta sesión. ¿Deseas continuar?",
                    color = Color(0xFF64748B)
                )
            },
            confirmButton = {

                Button(
                    onClick = {

                        mostrarConfirmacion = false

                        vm.eliminar(id)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = RojoEliminar,
                        contentColor = Color.White
                    )
                ) {

                    Text("Eliminar")
                }
            },
            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                    }
                ) {

                    Text("Cancelar")
                }
            },
            shape = RoundedCornerShape(24.dp),
            containerColor = Color.White
        )
    }
}

// Icono de papelera sin dependencias adicionales
@Composable
private fun IconoEliminar(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val w = size.width
        val h = size.height
        val grosor = size.minDimension * 0.075f

        drawLine(
            color = color,
            start = Offset(w * 0.15f, h * 0.25f),
            end = Offset(w * 0.85f, h * 0.25f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawRoundRect(
            color = color,
            topLeft = Offset(w * 0.24f, h * 0.30f),
            size = Size(w * 0.52f, h * 0.58f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                w * 0.06f
            ),
            style = Stroke(width = grosor)
        )

        drawLine(
            color = color,
            start = Offset(w * 0.38f, h * 0.12f),
            end = Offset(w * 0.62f, h * 0.12f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(w * 0.40f, h * 0.44f),
            end = Offset(w * 0.40f, h * 0.74f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(w * 0.60f, h * 0.44f),
            end = Offset(w * 0.60f, h * 0.74f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}
