
package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
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

    val esEstudiante = rolSeleccionado == "CLIENTE"

    val colorPrincipal = if (esEstudiante) {
        Color(0xFF0895B5)
    } else {
        Color(0xFF7024DF)
    }

    val colorTexto = Color(0xFF202938)
    val colorSecundario = Color(0xFF8A8E98)
    val colorBorde = Color(0xFFE4E6EA)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 42.dp, bottom = 24.dp)
    ) {

        // Encabezado
        Text(
            text = "Bienvenido",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Ingresa para continuar",
            fontSize = 13.sp,
            color = colorSecundario
        )

        Spacer(modifier = Modifier.height(26.dp))

        // Selector de perfil
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .background(
                    Color(0xFFF3F4F6),
                    RoundedCornerShape(12.dp)
                )
                .padding(3.dp),
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            listOf(
                "CLIENTE" to "Estudiante",
                "ADMIN" to "Administrador"
            ).forEach { (rol, etiqueta) ->

                val seleccionado = rolSeleccionado == rol

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(
                            if (seleccionado) colorPrincipal
                            else Color.Transparent,
                            RoundedCornerShape(9.dp)
                        )
                        .clickable {
                            rolSeleccionado = rol
                            viewModel.limpiarError()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = etiqueta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (seleccionado) {
                            Color.White
                        } else {
                            Color(0xFF737987)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Campo de correo
        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    "Correo",
                    fontSize = 12.sp
                )
            },
            placeholder = {
                Text(
                    "correo@ejemplo.com",
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "Correo",
                    tint = colorPrincipal,
                    modifier = Modifier.size(19.dp)
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorPrincipal,
                unfocusedBorderColor = colorBorde,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Campo de contraseña
        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(
                    "Contraseña",
                    fontSize = 12.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Lock,
                    contentDescription = "Contraseña",
                    tint = colorPrincipal,
                    modifier = Modifier.size(19.dp)
                )
            },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorPrincipal,
                unfocusedBorderColor = colorBorde,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Enlace visual de recuperación
        Text(
            text = "¿Olvidaste tu contraseña?",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.End,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorPrincipal
        )

        if (mensajeError != null) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = mensajeError.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Botón de ingreso
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
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colorPrincipal,
                disabledContainerColor = colorPrincipal.copy(
                    alpha = 0.65f
                ),
                disabledContentColor = Color.White
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
                    text = "Ingresar",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (esEstudiante) {
            // Enlace de registro mostrado en la guía.
            // Pendiente de conectar con una pantalla de registro.
            Text(
                text = "¿No tienes cuenta? Regístrate",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = colorPrincipal,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            // Información exclusiva del administrador
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF5F1FF),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(14.dp)
            ) {
                Text(
                    text = "Modo práctica",
                    color = colorPrincipal,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Puedes cambiar tu perfil desde ajustes para probar ambos roles.",
                    color = colorSecundario,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }
    }
}
