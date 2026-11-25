package com.alvarez.murocampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.murocampus.model.Post
import com.alvarez.murocampus.viewmodel.PostsViewModel
import com.alvarez.murocampus.viewmodel.UiState

private val AzulFormulario = Color(0xFF176B98)
private val AzulFondo = Color(0xFFE4EEF5)
private val TextoPrincipal = Color(0xFF202B38)
private val TextoSecundario = Color(0xFF64748B)

@Composable
fun PantallaEditar(
    vm: PostsViewModel,
    post: Post,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {

    val estado by
    vm.edicion.collectAsStateWithLifecycle()

    var titulo by rememberSaveable(post.id) {
        mutableStateOf(post.title)
    }

    val guardando = estado == UiState.Cargando

    val hayCambios =
        titulo.trim() != post.title

    val puedeGuardar =
        titulo.isNotBlank() &&
                hayCambios &&
                !guardando &&
                estado !is UiState.Exito

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = AzulFondo,
            shape = RoundedCornerShape(18.dp)
        ) {

            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Surface(
                    modifier = Modifier.size(48.dp),
                    color = Color.White,
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        IconoEditar(
                            color = AzulFormulario,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Text(
                    text = "Actualiza tu publicación",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoPrincipal
                )

                Text(
                    text = "Modifica el título sin cambiar el contenido original.",
                    fontSize = 14.sp,
                    color = TextoSecundario
                )

                Text(
                    text = "Publicación #${post.id} · Usuario ${post.userId}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulFormulario
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 2.dp
            )
        ) {

            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    text = "Título de la publicación",
                    fontWeight = FontWeight.SemiBold,
                    color = TextoPrincipal
                )

                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !guardando &&
                            estado !is UiState.Exito,
                    minLines = 2,
                    maxLines = 4,
                    isError = titulo.isBlank(),
                    placeholder = {

                        Text("Escribe un nuevo título")
                    },
                    supportingText = {

                        Text(
                            text = if (titulo.isBlank()) {
                                "El título no puede estar vacío."
                            } else if (!hayCambios) {
                                "Modifica el título para guardar."
                            } else {
                                "${titulo.trim().length} caracteres"
                            }
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AzulFormulario,
                        unfocusedBorderColor = Color(0xFFD1DCE5),
                        focusedTextColor = TextoPrincipal,
                        unfocusedTextColor = TextoPrincipal,
                        cursorColor = AzulFormulario
                    )
                )

                Text(
                    text = "Contenido original",
                    fontWeight = FontWeight.SemiBold,
                    color = TextoPrincipal
                )

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFF5F7FA),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Text(
                        text = post.body,
                        modifier = Modifier.padding(14.dp),
                        color = TextoSecundario,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = "El contenido se conservará sin modificaciones.",
                    fontSize = 12.sp,
                    color = TextoSecundario
                )

                HorizontalDivider(
                    color = Color(0xFFE2E8F0)
                )

                Button(
                    onClick = {
                        vm.editar(post.id, titulo)
                    },
                    enabled = puedeGuardar,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulFormulario,
                        contentColor = Color.White
                    )
                ) {

                    if (guardando) {

                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = AzulFormulario
                        )

                        Spacer(
                            modifier = Modifier.width(10.dp)
                        )
                    }

                    Text(
                        text = if (guardando) {
                            "Guardando cambios..."
                        } else {
                            "Guardar cambios"
                        },
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onCancelar,
                    enabled = !guardando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = AzulFormulario
                    )
                ) {

                    Text(
                        text = "Cancelar",
                        fontWeight = FontWeight.Medium
                    )
                }

                val resultado = estado

                if (resultado is UiState.Error) {

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFFFCE8E6),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Text(
                            text = resultado.mensaje,
                            modifier = Modifier.padding(14.dp),
                            color = Color(0xFFB3261E),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
