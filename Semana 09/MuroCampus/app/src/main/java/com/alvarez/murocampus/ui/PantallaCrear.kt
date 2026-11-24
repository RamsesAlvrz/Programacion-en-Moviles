package com.alvarez.murocampus.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.murocampus.viewmodel.PostsViewModel
import com.alvarez.murocampus.viewmodel.UiState

@Composable
fun PantallaCrear(
    vm: PostsViewModel,
    modifier: Modifier = Modifier
) {

    var titulo by rememberSaveable {
        mutableStateOf("")
    }

    var contenido by rememberSaveable {
        mutableStateOf("")
    }

    val estado by
    vm.publicacion.collectAsStateWithLifecycle()

    val publicando = estado == UiState.Cargando

    LaunchedEffect(estado) {
        if (estado is UiState.Exito) {
            titulo = ""
            contenido = ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
            },
            label = {
                Text("Título")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = !publicando,
            shape = RoundedCornerShape(10.dp)
        )

        OutlinedTextField(
            value = contenido,
            onValueChange = {
                contenido = it
            },
            label = {
                Text("Contenido")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            enabled = !publicando,
            shape = RoundedCornerShape(10.dp)
        )

        Button(
            onClick = {
                vm.publicar(titulo, contenido)
            },
            enabled = titulo.isNotBlank() &&
                    contenido.isNotBlank() &&
                    !publicando,
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(
                horizontal = 30.dp,
                vertical = 12.dp
            )
        ) {

            Text(
                text = if (publicando) {
                    "Publicando..."
                } else {
                    "Publicar"
                },
                fontWeight = FontWeight.Bold
            )
        }

        when (val actual = estado) {

            null -> Unit

            UiState.Cargando -> {

                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp)
                )
            }

            is UiState.Exito -> {

                MensajePublicado(
                    mensaje = "Publicado con id ${actual.datos.id}"
                )
            }

            is UiState.Error -> {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFCE8E6)
                    )
                ) {

                    Text(
                        text = actual.mensaje,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        color = Color(0xFFB3261E),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun MensajePublicado(
    mensaje: String
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFE8F5E9),
                shape = RoundedCornerShape(10.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.matchParentSize()
        ) {

            val grosor = 2.dp.toPx()

            drawRoundRect(
                color = Color(0xFF287D46),
                topLeft = Offset(
                    grosor / 2,
                    grosor / 2
                ),
                size = Size(
                    size.width - grosor,
                    size.height - grosor
                ),
                cornerRadius = CornerRadius(
                    10.dp.toPx()
                ),
                style = Stroke(
                    width = grosor,
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(
                            6.dp.toPx(),
                            4.dp.toPx()
                        )
                    )
                )
            )
        }

        Text(
            text = mensaje,
            modifier = Modifier.padding(14.dp),
            color = Color(0xFF287D46),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}