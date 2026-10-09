package com.alvarez.saludplus.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio

private val AzulRegistro = Color(0xFF2378C9)
private val FondoRegistro = Color(0xFFF5FAFC)
private val CelesteRegistro = Color(0xFFE3F1F7)
private val BordeRegistro = Color(0xFFCEDCE4)
private val TextoRegistro = Color(0xFF17324F)

@Composable
fun RegistroScreen(
    onVolver: () -> Unit,
    onRegistrado: () -> Unit,
    onTerminos: () -> Unit,
    onLogin: () -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var apellido by rememberSaveable { mutableStateOf("") }
    var correo by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var confirmacion by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoRegistro)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        TextButton(
            onClick = onVolver,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "‹ Volver",
                color = AzulRegistro
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextoRegistro
            )

            Text(
                text = "Regístrate para agendar tus citas",
                style = MaterialTheme.typography.bodyMedium,
                color = TextoRegistro,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            IconoCampoRegistro(tipo = "persona")

            CampoRegistro(
                etiqueta = "Nombres",
                valor = nombre,
                onCambio = { nombre = it },
                modifier = Modifier.weight(1f)
            )

            CampoRegistro(
                etiqueta = "Apellidos",
                valor = apellido,
                onCambio = { apellido = it },
                modifier = Modifier.weight(1f)
            )
        }

        FilaCampoRegistro(
            tipo = "telefono",
            etiqueta = "Teléfono",
            valor = telefono,
            onCambio = { telefono = it },
            teclado = KeyboardType.Phone
        )

        FilaCampoRegistro(
            tipo = "correo",
            etiqueta = "Correo electrónico",
            valor = correo,
            onCambio = { correo = it },
            teclado = KeyboardType.Email
        )

        FilaCampoRegistro(
            tipo = "clave",
            etiqueta = "Contraseña",
            valor = contrasena,
            onCambio = { contrasena = it },
            secreto = true,
            teclado = KeyboardType.Password
        )

        FilaCampoRegistro(
            tipo = "clave",
            etiqueta = "Confirmar contraseña",
            valor = confirmacion,
            onCambio = { confirmacion = it },
            secreto = true,
            teclado = KeyboardType.Password
        )

        Text(
            text = "La contraseña debe tener al menos 6 caracteres.",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF52677C)
        )

        if (error.isNotBlank()) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                error = ""

                when {
                    nombre.isBlank() || apellido.isBlank() -> {
                        error = "Completa tus nombres y apellidos."
                    }

                    !Patterns.EMAIL_ADDRESS
                        .matcher(correo.trim())
                        .matches() -> {
                        error = "Ingresa un correo electrónico válido."
                    }

                    telefono.trim().length != 9 ||
                            !telefono.trim().all { it.isDigit() } -> {
                        error = "El teléfono debe tener 9 dígitos."
                    }

                    contrasena.length < 6 -> {
                        error = "La contraseña debe tener al menos 6 caracteres."
                    }

                    contrasena != confirmacion -> {
                        error = "Las contraseñas no coinciden."
                    }

                    else -> {
                        val registrado = Repositorio.registrarUsuario(
                            nombre = nombre,
                            apellido = apellido,
                            correo = correo,
                            contrasena = contrasena,
                            telefono = telefono
                        )

                        if (registrado) {
                            onRegistrado()
                        } else {
                            error = "No se pudo registrar. Revisa los datos o si el correo ya está registrado."
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulRegistro,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Registrarme",
                fontWeight = FontWeight.SemiBold
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Al registrarte aceptas nuestros",
                style = MaterialTheme.typography.bodySmall,
                color = TextoRegistro,
                textAlign = TextAlign.Center
            )

            TextButton(
                onClick = onTerminos,
                contentPadding = PaddingValues(
                    horizontal = 8.dp,
                    vertical = 0.dp
                )
            ) {
                Text(
                    text = "Términos y Condiciones",
                    color = AzulRegistro,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        HorizontalDivider(
            color = BordeRegistro,
            modifier = Modifier.padding(top = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿Ya tienes cuenta?",
                style = MaterialTheme.typography.bodySmall,
                color = TextoRegistro,
                fontWeight = FontWeight.SemiBold
            )

            TextButton(
                onClick = onLogin,
                contentPadding = PaddingValues(horizontal = 6.dp)
            ) {
                Text(
                    text = "Iniciar sesión",
                    color = AzulRegistro,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun FilaCampoRegistro(
    tipo: String,
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    teclado: KeyboardType = KeyboardType.Text,
    secreto: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        IconoCampoRegistro(tipo)

        CampoRegistro(
            etiqueta = etiqueta,
            valor = valor,
            onCambio = onCambio,
            teclado = teclado,
            secreto = secreto,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CampoRegistro(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    modifier: Modifier = Modifier,
    teclado: KeyboardType = KeyboardType.Text,
    secreto: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = TextoRegistro
        )

        OutlinedTextField(
            value = valor,
            onValueChange = onCambio,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            textStyle = MaterialTheme.typography.bodyMedium,
            keyboardOptions = KeyboardOptions(
                keyboardType = teclado
            ),
            visualTransformation = if (secreto) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AzulRegistro,
                unfocusedBorderColor = BordeRegistro,
                focusedContainerColor = FondoRegistro,
                unfocusedContainerColor = FondoRegistro,
                focusedTextColor = TextoRegistro,
                unfocusedTextColor = TextoRegistro
            )
        )
    }
}

@Composable
private fun IconoCampoRegistro(tipo: String) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .background(
                color = CelesteRegistro,
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(24.dp)) {
            val ancho = size.width
            val alto = size.height
            val grosor = 2.dp.toPx()
            val trazo = Stroke(width = grosor)

            when (tipo) {
                "persona" -> {
                    drawCircle(
                        color = AzulRegistro,
                        radius = ancho * 0.16f,
                        center = Offset(ancho * 0.50f, alto * 0.28f)
                    )

                    drawArc(
                        color = AzulRegistro,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(ancho * 0.18f, alto * 0.53f),
                        size = Size(ancho * 0.64f, alto * 0.52f),
                        style = trazo
                    )
                }

                "telefono" -> {
                    drawRoundRect(
                        color = AzulRegistro,
                        topLeft = Offset(ancho * 0.28f, alto * 0.10f),
                        size = Size(ancho * 0.44f, alto * 0.80f),
                        style = trazo
                    )

                    drawLine(
                        color = AzulRegistro,
                        start = Offset(ancho * 0.42f, alto * 0.76f),
                        end = Offset(ancho * 0.58f, alto * 0.76f),
                        strokeWidth = grosor
                    )
                }

                "correo" -> {
                    drawRoundRect(
                        color = AzulRegistro,
                        topLeft = Offset(ancho * 0.12f, alto * 0.24f),
                        size = Size(ancho * 0.76f, alto * 0.52f),
                        style = trazo
                    )

                    drawLine(
                        color = AzulRegistro,
                        start = Offset(ancho * 0.14f, alto * 0.27f),
                        end = Offset(ancho * 0.50f, alto * 0.51f),
                        strokeWidth = grosor
                    )

                    drawLine(
                        color = AzulRegistro,
                        start = Offset(ancho * 0.50f, alto * 0.51f),
                        end = Offset(ancho * 0.86f, alto * 0.27f),
                        strokeWidth = grosor
                    )
                }

                "clave" -> {
                    drawArc(
                        color = AzulRegistro,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(ancho * 0.30f, alto * 0.12f),
                        size = Size(ancho * 0.40f, alto * 0.50f),
                        style = trazo
                    )

                    drawRoundRect(
                        color = AzulRegistro,
                        topLeft = Offset(ancho * 0.22f, alto * 0.42f),
                        size = Size(ancho * 0.56f, alto * 0.44f)
                    )

                    drawCircle(
                        color = Color.White,
                        radius = ancho * 0.05f,
                        center = Offset(ancho * 0.50f, alto * 0.62f)
                    )
                }
            }
        }
    }
}