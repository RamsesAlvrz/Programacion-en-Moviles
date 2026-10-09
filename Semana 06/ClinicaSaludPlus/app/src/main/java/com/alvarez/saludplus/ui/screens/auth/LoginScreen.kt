package com.alvarez.saludplus.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.BotonPrimario
import com.alvarez.saludplus.ui.components.CampoTexto
import com.alvarez.saludplus.ui.components.Encabezado

@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onAcceso: () -> Unit,
    onRegistro: () -> Unit
) {
    var correo by rememberSaveable {
        mutableStateOf("")
    }

    var contrasena by rememberSaveable {
        mutableStateOf("")
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Encabezado(
            titulo = "Iniciar sesión",
            onVolver = onVolver
        )

        Text(
            text = "Bienvenido a SaludPlus",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Ingresa con el correo y la contraseña de tu cuenta.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CampoTexto(
            etiqueta = "Correo electrónico",
            valor = correo,
            onCambio = {
                correo = it
                error = ""
            },
            teclado = KeyboardType.Email
        )

        CampoTexto(
            etiqueta = "Contraseña",
            valor = contrasena,
            onCambio = {
                contrasena = it
                error = ""
            },
            teclado = KeyboardType.Password,
            secreto = true
        )

        if (error.isNotBlank()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        BotonPrimario(
            texto = "Ingresar",
            onClick = {
                val correoLimpio = correo.trim()

                error = when {
                    correoLimpio.isBlank() || contrasena.isBlank() ->
                        "Completa el correo y la contraseña."

                    !Patterns.EMAIL_ADDRESS
                        .matcher(correoLimpio)
                        .matches() ->
                        "Ingresa un correo válido."

                    else -> ""
                }

                if (error.isEmpty()) {
                    val accesoCorrecto = Repositorio.iniciarSesion(
                        correo = correoLimpio,
                        contrasena = contrasena
                    )

                    if (accesoCorrecto) {
                        onAcceso()
                    } else {
                        error = "Correo o contraseña incorrectos."
                    }
                }
            }
        )

        TextButton(
            onClick = onRegistro
        ) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}