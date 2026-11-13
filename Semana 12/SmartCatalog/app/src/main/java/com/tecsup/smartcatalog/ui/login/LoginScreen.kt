
package com.tecsup.smartcatalog.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.smartcatalog.ui.components.AppButton

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

    val cargando = estado is LoginUiState.Loading

    LaunchedEffect(estado) {
        val actual = estado

        if (actual is LoginUiState.Success) {
            onLoginExitoso(actual.rol)
            viewModel.reiniciarEstado()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.primary,
            shadowElevation = 6.dp
        ) {
            Icon(
                imageVector = Icons.Default.Smartphone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(18.dp)
                    .size(42.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "SmartCatalog",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Tu catálogo de celulares",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(28.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(22.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    text = if (esRegistro) {
                        "Crear una cuenta"
                    } else {
                        "Bienvenido de nuevo"
                    },
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = if (esRegistro) {
                        "Regístrate para guardar tus celulares favoritos."
                    } else {
                        "Inicia sesión para explorar nuestro catálogo."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (esRegistro) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre completo") },
                        singleLine = true,
                        enabled = !cargando,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = correo,
                    onValueChange = { correo = it },
                    label = { Text("Correo electrónico") },
                    singleLine = true,
                    enabled = !cargando,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    enabled = !cargando,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                AppButton(
                    texto = if (esRegistro) {
                        "Crear cuenta"
                    } else {
                        "Iniciar sesión"
                    },
                    habilitado = !cargando,
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
                    }
                )

                if (cargando) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                val error = estado as? LoginUiState.Error

                if (error != null) {
                    Text(
                        text = error.mensaje,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                HorizontalDivider()

                TextButton(
                    onClick = {
                        esRegistro = !esRegistro
                        password = ""
                        viewModel.reiniciarEstado()
                    },
                    enabled = !cargando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (esRegistro) {
                            "¿Ya tienes cuenta? Inicia sesión"
                        } else {
                            "¿No tienes cuenta? Regístrate"
                        },
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "SmartCatalog · Catálogo de celulares",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}
