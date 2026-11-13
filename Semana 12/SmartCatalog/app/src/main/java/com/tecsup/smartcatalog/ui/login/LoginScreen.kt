
package com.tecsup.smartcatalog.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.smartcatalog.R

private val FondoCampo = Color(0xFF152544)
private val AzulBorde = Color(0xFF385584)
private val AzulTexto = Color(0xFF93C5FD)
private val TextoClaro = Color(0xFFF8FAFC)
private val TextoSuave = Color(0xFFA5B4D0)

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginExitoso: (String) -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    var esRegistro by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mostrarPassword by remember { mutableStateOf(false) }

    val cargando = estado is LoginUiState.Loading

    LaunchedEffect(estado) {
        val actual = estado
        if (actual is LoginUiState.Success) {
            onLoginExitoso(actual.rol)
            viewModel.reiniciarEstado()
        }
    }

    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedTextColor = TextoClaro,
        unfocusedTextColor = TextoClaro,
        disabledTextColor = TextoSuave,
        focusedContainerColor = FondoCampo,
        unfocusedContainerColor = FondoCampo,
        focusedBorderColor = AzulTexto,
        unfocusedBorderColor = AzulBorde,
        focusedLabelColor = AzulTexto,
        unfocusedLabelColor = TextoSuave,
        focusedPlaceholderColor = TextoSuave,
        unfocusedPlaceholderColor = TextoSuave,
        focusedLeadingIconColor = AzulTexto,
        unfocusedLeadingIconColor = TextoSuave,
        focusedTrailingIconColor = AzulTexto,
        unfocusedTrailingIconColor = TextoSuave,
        cursorColor = AzulTexto
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050A18))
    ) {

        Image(
            painter = painterResource(R.drawable.login_smartcatalog),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Surface(
                color = Color(0xFF2563EB),
                shape = RoundedCornerShape(18.dp),
                shadowElevation = 8.dp
            ) {
                Icon(
                    imageVector = Icons.Default.Smartphone,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(14.dp)
                        .size(32.dp)
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = "SmartCatalog",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                text = "Tu experiencia tecnológica comienza aquí",
                style = MaterialTheme.typography.bodySmall,
                color = AzulTexto,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xF20D1B39),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp, AzulBorde
                ),
                shadowElevation = 10.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    Text(
                        text = if (esRegistro)
                            "Crea tu cuenta"
                        else
                            "¡Bienvenido de nuevo!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextoClaro
                    )

                    Text(
                        text = if (esRegistro)
                            "Regístrate para explorar SmartCatalog."
                        else
                            "Ingresa tus datos para continuar.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoSuave
                    )

                    if (esRegistro) {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre completo") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, null)
                            },
                            colors = coloresCampo,
                            shape = RoundedCornerShape(14.dp),
                            singleLine = true,
                            enabled = !cargando,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    OutlinedTextField(
                        value = correo,
                        onValueChange = { correo = it },
                        label = { Text("Correo electrónico") },
                        leadingIcon = {
                            Icon(Icons.Default.Email, null)
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        colors = coloresCampo,
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        enabled = !cargando,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, null)
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    mostrarPassword = !mostrarPassword
                                }
                            ) {
                                Icon(
                                    imageVector = if (mostrarPassword)
                                        Icons.Default.VisibilityOff
                                    else
                                        Icons.Default.Visibility,
                                    contentDescription = if (mostrarPassword)
                                        "Ocultar contraseña"
                                    else
                                        "Mostrar contraseña"
                                )
                            }
                        },
                        visualTransformation = if (mostrarPassword)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        colors = coloresCampo,
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        enabled = !cargando,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            if (esRegistro) {
                                viewModel.registrar(
                                    nombre = nombre,
                                    correo = correo,
                                    password = password
                                )
                            } else {
                                viewModel.iniciarSesion(
                                    correo = correo,
                                    password = password
                                )
                            }
                        },
                        enabled = !cargando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2563EB)
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
                                if (esRegistro)
                                    "Crear cuenta"
                                else
                                    "Iniciar sesión",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    val error = estado as? LoginUiState.Error
                    if (error != null) {
                        Text(
                            text = error.mensaje,
                            color = Color(0xFFFCA5A5)
                        )
                    }

                    HorizontalDivider(
                        color = AzulBorde
                    )

                    TextButton(
                        onClick = {
                            esRegistro = !esRegistro
                            password = ""
                            mostrarPassword = false
                            viewModel.reiniciarEstado()
                        },
                        enabled = !cargando,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (esRegistro)
                                "¿Ya tienes cuenta? Inicia sesión"
                            else
                                "¿No tienes cuenta? Regístrate",
                            color = AzulTexto,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "SMARTCATALOG 2.0",
                style = MaterialTheme.typography.labelSmall,
                color = AzulTexto
            )
        }
    }
}
