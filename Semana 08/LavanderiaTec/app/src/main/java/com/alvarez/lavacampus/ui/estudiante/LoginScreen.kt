
package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.viewmodel.SesionViewModel

// Pantalla de inicio de sesión de LavaCampus
@Composable
fun LoginScreen(
    viewModel: SesionViewModel,
    onLoginExitoso: (String) -> Unit
) {
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var rolSeleccionado by remember { mutableStateOf("CLIENTE") }

    val cargando by viewModel.cargando.collectAsStateWithLifecycle()
    val mensajeError by viewModel.mensajeError.collectAsStateWithLifecycle()

    val colorPrincipal = if (rolSeleccionado == "CLIENTE") {
        Color(0xFF0891B2)
    } else {
        Color(0xFF6D28D9)
    }

    val colorFondo = if (rolSeleccionado == "CLIENTE") {
        Color(0xFFF0FCFF)
    } else {
        Color(0xFFFAF8FF)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "LavaCampus",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = colorPrincipal
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Lavandería universitaria",
            fontSize = 16.sp,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Selecciona tu perfil",
            fontWeight = FontWeight.SemiBold,
            fontSize = 17.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf(
                "CLIENTE" to "Estudiante",
                "ADMIN" to "Administrador"
            ).forEach { (rol, etiqueta) ->

                val seleccionado = rolSeleccionado == rol

                OutlinedButton(
                    onClick = {
                        rolSeleccionado = rol
                        viewModel.limpiarError()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (seleccionado) {
                            colorPrincipal.copy(alpha = 0.12f)
                        } else {
                            Color.Transparent
                        },
                        contentColor = colorPrincipal
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        if (seleccionado) 2.dp else 1.dp,
                        colorPrincipal
                    )
                ) {
                    Text(
                        text = etiqueta,
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (mensajeError != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mensajeError.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                viewModel.iniciarSesion(
                    correo = correo,
                    clave = clave,
                    rol = rolSeleccionado
                ) { exitoso ->
                    if (exitoso) {
                        onLoginExitoso(rolSeleccionado)
                    }
                }
            },
            enabled = !cargando &&
                    correo.isNotBlank() &&
                    clave.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorPrincipal
            )
        ) {
            if (cargando) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Iniciar sesión",
                    fontSize = 16.sp
                )
            }
        }
    }
}
