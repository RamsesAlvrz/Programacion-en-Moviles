package com.alvarez.saludplus.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.BotonPrimario
import com.alvarez.saludplus.ui.components.CampoTexto
import com.alvarez.saludplus.ui.components.Encabezado

@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onRegistrado: () -> Unit,
    onTerminos: () -> Unit
) {
    var nombre by rememberSaveable {
        mutableStateOf("")
    }

    var apellido by rememberSaveable {
        mutableStateOf("")
    }

    var correo by rememberSaveable {
        mutableStateOf("")
    }

    var telefono by rememberSaveable {
        mutableStateOf("")
    }

    var contrasena by rememberSaveable {
        mutableStateOf("")
    }

    var confirmarContrasena by rememberSaveable {
        mutableStateOf("")
    }

    var aceptaTerminos by rememberSaveable {
        mutableStateOf(false)
    }

    var error by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Encabezado(
            titulo = "Crear cuenta",
            onVolver = onVolver
        )

        Text(
            text = "Regístrate para reservar tus citas.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CampoTexto(
            etiqueta = "Nombre",
            valor = nombre,
            onCambio = {
                nombre = it
                error = ""
            }
        )

        CampoTexto(
            etiqueta = "Apellido",
            valor = apellido,
            onCambio = {
                apellido = it
                error = ""
            }
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
            etiqueta = "Teléfono",
            valor = telefono,
            onCambio = {
                telefono = it
                error = ""
            },
            teclado = KeyboardType.Phone
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

        CampoTexto(
            etiqueta = "Confirmar contraseña",
            valor = confirmarContrasena,
            onCambio = {
                confirmarContrasena = it
                error = ""
            },
            teclado = KeyboardType.Password,
            secreto = true
        )

        Text(
            text = "La contraseña debe tener al menos 6 caracteres.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = aceptaTerminos,
                onCheckedChange = {
                    aceptaTerminos = it
                    error = ""
                }
            )

            TextButton(
                onClick = onTerminos,
                modifier = Modifier.weight(1f)
            ) {
                Text("Acepto los términos y condiciones")
            }
        }

        if (error.isNotBlank()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error
            )
        }

        BotonPrimario(
            texto = "Registrarme",
            onClick = {
                val correoLimpio = correo.trim()
                val telefonoLimpio = telefono.trim()

                error = when {
                    nombre.isBlank() || apellido.isBlank() ->
                        "Completa tu nombre y apellido."

                    !Patterns.EMAIL_ADDRESS
                        .matcher(correoLimpio)
                        .matches() ->
                        "Ingresa un correo válido."

                    telefonoLimpio.length != 9 ||
                            !telefonoLimpio.all { it.isDigit() } ->
                        "El teléfono debe tener 9 dígitos."

                    contrasena.length < 6 ->
                        "La contraseña debe tener al menos 6 caracteres."

                    contrasena != confirmarContrasena ->
                        "Las contraseñas no coinciden."

                    !aceptaTerminos ->
                        "Debes aceptar los términos y condiciones."

                    else -> ""
                }

                if (error.isEmpty()) {
                    val registrado = Repositorio.registrarUsuario(
                        nombre = nombre,
                        apellido = apellido,
                        correo = correoLimpio,
                        contrasena = contrasena,
                        telefono = telefonoLimpio
                    )

                    if (registrado) {
                        onRegistrado()
                    } else {
                        error = "No se pudo registrar. Revisa los datos o utiliza otro correo."
                    }
                }
            }
        )
    }
}