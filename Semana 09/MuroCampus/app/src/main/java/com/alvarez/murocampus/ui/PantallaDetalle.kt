
package com.alvarez.murocampus.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.murocampus.model.Comentario
import com.alvarez.murocampus.model.Post
import com.alvarez.murocampus.ui.theme.*
import com.alvarez.murocampus.viewmodel.PostsViewModel
import com.alvarez.murocampus.viewmodel.UiState

@Composable
fun PantallaDetalle(
    vm: PostsViewModel,
    id: Int,
    onEditar: () -> Unit,
    modifier: Modifier = Modifier
) {

    val estado by vm.detalle.collectAsStateWithLifecycle()
    val eliminacion by vm.eliminacion.collectAsStateWithLifecycle()
    val edicion by vm.edicion.collectAsStateWithLifecycle()

    var mostrarConfirmacion by rememberSaveable(id) {
        mutableStateOf(false)
    }

    val eliminando = eliminacion == UiState.Cargando

    val ocupado = eliminando ||
            edicion == UiState.Cargando ||
            eliminacion is UiState.Exito

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
                    onReintentar = {
                        vm.abrir(id)
                    }
                )
            }

            is UiState.Exito -> {

                val datos = actual.datos

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 24.dp
                    ),
                    verticalArrangement =
                        Arrangement.spacedBy(12.dp)
                ) {

                    // Publicacion principal
                    item {

                        TarjetaDetallePublicacion(
                            post = datos.post,
                            cantidadComentarios =
                                datos.comentarios.size,
                            ocupado = ocupado,
                            eliminando = eliminando,
                            errorEliminacion =
                                (eliminacion as? UiState.Error)
                                    ?.mensaje,
                            onEditar = onEditar,
                            onEliminar = {

                                vm.limpiarEliminacion()

                                mostrarConfirmacion = true
                            }
                        )
                    }

                    // Encabezado de comentarios
                    item {

                        EncabezadoComentarios(
                            cantidad = datos.comentarios.size
                        )
                    }

                    // Estado sin comentarios
                    if (datos.comentarios.isEmpty()) {

                        item {
                            SinComentarios()
                        }
                    }

                    // Lista de comentarios
                    itemsIndexed(
                        items = datos.comentarios,
                        key = { _, comentario ->
                            comentario.id
                        }
                    ) { indice, comentario ->

                        TarjetaComentario(
                            comentario = comentario,
                            numero = indice + 1
                        )
                    }

                    // Final de los comentarios
                    if (datos.comentarios.isNotEmpty()) {

                        item {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                HorizontalDivider(
                                    modifier = Modifier.weight(1f),
                                    color = BordeSuave
                                )

                                Text(
                                    text = "Fin de los comentarios",
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp
                                    ),
                                    color = TextoSecundario,
                                    style =
                                        MaterialTheme.typography.bodySmall
                                )

                                HorizontalDivider(
                                    modifier = Modifier.weight(1f),
                                    color = BordeSuave
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarConfirmacion) {

        DialogoEliminarPublicacion(
            onCancelar = {
                mostrarConfirmacion = false
            },
            onConfirmar = {

                mostrarConfirmacion = false

                vm.eliminar(id)
            }
        )
    }
}

// ======================================================
// TARJETA PRINCIPAL DE LA PUBLICACION
// ======================================================

@Composable
private fun TarjetaDetallePublicacion(
    post: Post,
    cantidadComentarios: Int,
    ocupado: Boolean,
    eliminando: Boolean,
    errorEliminacion: String?,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        border = BorderStroke(1.dp, BordeSuave),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Identificacion del autor
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                AvatarUsuarioCampus(
                    userId = post.userId
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = "Usuario ${post.userId}",
                        color = TextoPrincipal,
                        style =
                            MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Autor de la publicación",
                        color = TextoSecundario,
                        style =
                            MaterialTheme.typography.bodySmall
                    )
                }

                Surface(
                    color = AzulClaro,
                    shape = RoundedCornerShape(8.dp)
                ) {

                    Text(
                        text = "#${post.id}",
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        color = AzulOscuro,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 12.sp
                    )
                }
            }

            HorizontalDivider(
                color = BordeSuave
            )

            // Titulo y contenido de la publicacion
            Column(
                verticalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    text = post.title,
                    color = TextoPrincipal,
                    style =
                        MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = post.body,
                    color = TextoSecundario,
                    style =
                        MaterialTheme.typography.bodyLarge
                )
            }

            // Cantidad de comentarios
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconoComentario(
                    color = AzulPrincipal,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(7.dp))

                Text(
                    text = if (cantidadComentarios == 1) {
                        "1 comentario"
                    } else {
                        "$cantidadComentarios comentarios"
                    },
                    color = AzulPrincipal,
                    style =
                        MaterialTheme.typography.labelMedium
                )
            }

            HorizontalDivider(
                color = BordeSuave
            )

            Text(
                text = "ACCIONES",
                color = TextoSecundario,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 1.sp
            )

            // Botones de editar y eliminar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(10.dp)
            ) {

                OutlinedButton(
                    onClick = onEditar,
                    enabled = !ocupado,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        1.dp,
                        if (ocupado) {
                            BordeSuave
                        } else {
                            AzulPrincipal
                        }
                    ),
                    colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = AzulPrincipal
                        ),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 10.dp
                    )
                ) {

                    IconoEditar(
                        color = if (ocupado) {
                            TextoSecundario
                        } else {
                            AzulPrincipal
                        },
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = "Editar",
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Button(
                    onClick = onEliminar,
                    enabled = !ocupado,
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = FondoError,
                        contentColor = RojoError,
                        disabledContainerColor =
                            SuperficieSuave,
                        disabledContentColor =
                            TextoSecundario
                    ),
                    contentPadding = PaddingValues(
                        horizontal = 8.dp,
                        vertical = 10.dp
                    )
                ) {

                    if (eliminando) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(17.dp),
                            strokeWidth = 2.dp,
                            color = RojoError
                        )

                    } else {

                        IconoEliminar(
                            color = if (ocupado) {
                                TextoSecundario
                            } else {
                                RojoError
                            },
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(7.dp))

                    Text(
                        text = if (eliminando) {
                            "Eliminando"
                        } else {
                            "Eliminar"
                        },
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                }
            }

            // Mensaje de error al eliminar
            if (errorEliminacion != null) {

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = FondoError,
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = errorEliminacion,
                        modifier = Modifier.padding(12.dp),
                        color = RojoError,
                        style =
                            MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// ======================================================
// ENCABEZADO DE COMENTARIOS
// ======================================================

@Composable
private fun EncabezadoComentarios(
    cantidad: Int
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 10.dp,
                bottom = 4.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            modifier = Modifier.size(44.dp),
            color = AzulClaro,
            shape = RoundedCornerShape(14.dp)
        ) {

            Box(
                contentAlignment = Alignment.Center
            ) {

                IconoComentario(
                    color = AzulPrincipal,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Comentarios",
                color = TextoPrincipal,
                style =
                    MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Conversación de la comunidad",
                color = TextoSecundario,
                style =
                    MaterialTheme.typography.bodySmall
            )
        }

        Surface(
            color = AzulOscuro,
            shape = CircleShape
        ) {

            Text(
                text = cantidad.toString(),
                modifier = Modifier.padding(
                    horizontal = 13.dp,
                    vertical = 7.dp
                ),
                color = Blanco,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
    }
}

// ======================================================
// TARJETA INDIVIDUAL DE COMENTARIO
// ======================================================

@Composable
private fun TarjetaComentario(
    comentario: Comentario,
    numero: Int
) {

    val iniciales = obtenerIniciales(
        comentario.email
    )

    val indiceColor = ((numero - 1) % 10) + 1

    val colorAvatar =
        colorUsuarioCampus(indiceColor)

    val fondoAvatar =
        fondoUsuarioCampus(indiceColor)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        border = BorderStroke(1.dp, BordeSuave),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Identificacion del comentarista
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(44.dp),
                    color = fondoAvatar,
                    shape = CircleShape
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Text(
                            text = iniciales,
                            color = colorAvatar,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement =
                        Arrangement.spacedBy(3.dp)
                ) {

                    Text(
                        text = comentario.email
                            .substringBefore("@")
                            .replace(".", " ")
                            .replace("_", " "),
                        color = TextoPrincipal,
                        fontWeight = FontWeight.SemiBold,
                        style =
                            MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = comentario.email,
                        color = TextoSecundario,
                        style =
                            MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    color = SuperficieSuave,
                    shape = RoundedCornerShape(8.dp)
                ) {

                    Text(
                        text = "#$numero",
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 5.dp
                        ),
                        color = TextoSecundario,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.sp
                    )
                }
            }

            HorizontalDivider(
                color = BordeSuave.copy(alpha = 0.7f)
            )

            // Titulo del comentario
            Text(
                text = comentario.name,
                color = TextoPrincipal,
                fontWeight = FontWeight.SemiBold,
                style =
                    MaterialTheme.typography.titleSmall
            )

            // Contenido del comentario
            Text(
                text = comentario.body,
                color = TextoSecundario,
                style =
                    MaterialTheme.typography.bodyMedium
            )
        }
    }
}

// Obtener iniciales desde el correo electronico

private fun obtenerIniciales(
    email: String
): String {

    val nombre = email.substringBefore("@")

    val partes = nombre.split(
        ".", "_", "-"
    ).filter {
        it.isNotBlank()
    }

    return if (partes.size >= 2) {

        (
                partes[0].take(1) +
                        partes[1].take(1)
                ).uppercase()

    } else {

        nombre.take(2)
            .uppercase()
            .ifBlank { "U" }
    }
}

// ======================================================
// ESTADO SIN COMENTARIOS
// ======================================================

@Composable
private fun SinComentarios() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Blanco
        ),
        border = BorderStroke(1.dp, BordeSuave)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            IconoComentario(
                color = AzulPrincipal,
                modifier = Modifier.size(38.dp)
            )

            Text(
                text = "Todavía no hay comentarios",
                color = TextoPrincipal,
                style =
                    MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Esta publicación aún no tiene respuestas.",
                color = TextoSecundario,
                style =
                    MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

// ======================================================
// CONFIRMACION DE ELIMINACION
// ======================================================

@Composable
private fun DialogoEliminarPublicacion(
    onCancelar: () -> Unit,
    onConfirmar: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onCancelar,

        icon = {

            Surface(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(20.dp),
                color = FondoError
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    IconoEliminar(
                        color = RojoError,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        },

        title = {

            Text(
                text = "¿Eliminar publicación?",
                color = TextoPrincipal,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },

        text = {

            Text(
                text = "La eliminación se simulará mediante el servicio JSONPlaceholder. ¿Deseas continuar?",
                color = TextoSecundario,
                style =
                    MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        },

        confirmButton = {

            Button(
                onClick = onConfirmar,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RojoError,
                    contentColor = Blanco
                )
            ) {

                Text("Eliminar")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onCancelar
            ) {

                Text(
                    text = "Cancelar",
                    color = TextoSecundario
                )
            }
        },

        shape = RoundedCornerShape(26.dp),
        containerColor = Blanco
    )
}

// ======================================================
// ICONOS PERSONALIZADOS
// ======================================================

@Composable
private fun IconoComentario(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val w = size.width
        val h = size.height
        val grosor = size.minDimension * 0.075f

        val burbuja = Path().apply {

            moveTo(w * 0.25f, h * 0.12f)

            lineTo(w * 0.75f, h * 0.12f)

            quadraticBezierTo(
                w * 0.90f,
                h * 0.12f,
                w * 0.90f,
                h * 0.28f
            )

            lineTo(w * 0.90f, h * 0.60f)

            quadraticBezierTo(
                w * 0.90f,
                h * 0.75f,
                w * 0.75f,
                h * 0.75f
            )

            lineTo(w * 0.40f, h * 0.75f)

            lineTo(w * 0.16f, h * 0.90f)

            lineTo(w * 0.20f, h * 0.73f)

            quadraticBezierTo(
                w * 0.10f,
                h * 0.70f,
                w * 0.10f,
                h * 0.58f
            )

            lineTo(w * 0.10f, h * 0.28f)

            quadraticBezierTo(
                w * 0.10f,
                h * 0.12f,
                w * 0.25f,
                h * 0.12f
            )

            close()
        }

        drawPath(
            path = burbuja,
            color = color,
            style = Stroke(
                width = grosor,
                cap = StrokeCap.Round
            )
        )

        for (i in 0..2) {

            drawCircle(
                color = color,
                radius = w * 0.045f,
                center = Offset(
                    w * (0.32f + i * 0.18f),
                    h * 0.44f
                )
            )
        }
    }
}

@Composable
internal fun IconoEditar(
    color: Color,
    modifier: Modifier = Modifier
) {

    Canvas(modifier = modifier) {

        val w = size.width
        val h = size.height
        val grosor = size.minDimension * 0.09f

        drawLine(
            color = color,
            start = Offset(w * 0.25f, h * 0.75f),
            end = Offset(w * 0.78f, h * 0.22f),
            strokeWidth = grosor * 2,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(w * 0.15f, h * 0.85f),
            end = Offset(w * 0.35f, h * 0.80f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(w * 0.15f, h * 0.85f),
            end = Offset(w * 0.20f, h * 0.65f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )

        drawLine(
            color = color,
            start = Offset(w * 0.50f, h * 0.85f),
            end = Offset(w * 0.88f, h * 0.85f),
            strokeWidth = grosor,
            cap = StrokeCap.Round
        )
    }
}

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
            cornerRadius = CornerRadius(w * 0.06f),
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
