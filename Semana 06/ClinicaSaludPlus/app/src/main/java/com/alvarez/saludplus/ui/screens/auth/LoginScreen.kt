package com.alvarez.saludplus.ui.screens.auth

import android.util.Patterns
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.R
import com.alvarez.saludplus.repository.Repositorio

private val AzulLogin = Color(0xFF2378C9)
private val FondoLogin = Color(0xFFF5FAFC)
private val CelesteLogin = Color(0xFFE3F1F7)
private val BordeLogin = Color(0xFFCEDCE4)
private val TextoLogin = Color(0xFF17324F)

@Composable
fun LoginScreen(
    onVolver: () -> Unit,
    onAcceso: () -> Unit,
    onRegistro: () -> Unit
) {
    var correo by rememberSaveable { mutableStateOf("") }
    var contrasena by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }
    var mostrarContrasena by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoLogin)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextButton(
            onClick = onVolver,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                text = "‹ Volver",
                color = AzulLogin
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logo_saludplus),
                contentDescription = "Logo de Clínica SaludPlus",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextoLogin
            )

            Text(
                text = "Bienvenido a SaludPlus",
                style = MaterialTheme.typography.titleSmall,
                color = TextoLogin
            )

            Text(
                text = "Ingresa para gestionar tus citas médicas.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF52677C),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        CampoLogin(
            etiqueta = "Correo electrónico",
            valor = correo,
            onCambio = { correo = it },
            tipo = "correo",
            teclado = KeyboardType.Email
        )

        CampoLogin(
            etiqueta = "Contraseña",
            valor = contrasena,
            onCambio = { contrasena = it },
            tipo = "clave",
            teclado = KeyboardType.Password,
            ocultarTexto = !mostrarContrasena,
            accionFinal = {
                TextButton(
                    onClick = {
                        mostrarContrasena = !mostrarContrasena
                    },
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Text(
                        text = if (mostrarContrasena) {
                            "Ocultar"
                        } else {
                            "Ver"
                        },
                        color = AzulLogin,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
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
                    correo.isBlank() || contrasena.isBlank() -> {
                        error = "Ingresa tu correo y contraseña."
                    }

                    !Patterns.EMAIL_ADDRESS
                        .matcher(correo.trim())
                        .matches() -> {
                        error = "Ingresa un correo electrónico válido."
                    }

                    else -> {
                        val accesoCorrecto = Repositorio.iniciarSesion(
                            correo = correo,
                            contrasena = contrasena
                        )

                        if (accesoCorrecto) {
                            onAcceso()
                        } else {
                            error = "Correo o contraseña incorrectos."
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AzulLogin,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Ingresar",
                fontWeight = FontWeight.SemiBold
            )
        }

        HorizontalDivider(
            color = BordeLogin,
            modifier = Modifier.padding(top = 4.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿No tienes cuenta?",
                style = MaterialTheme.typography.bodySmall,
                color = TextoLogin,
                fontWeight = FontWeight.SemiBold
            )

            TextButton(
                onClick = onRegistro,
                contentPadding = PaddingValues(horizontal = 6.dp)
            ) {
                Text(
                    text = "Regístrate",
                    color = AzulLogin,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun CampoLogin(
    etiqueta: String,
    valor: String,
    onCambio: (String) -> Unit,
    tipo: String,
    teclado: KeyboardType,
    ocultarTexto: Boolean = false,
    accionFinal: (@Composable () -> Unit)? = null
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        IconoLogin(tipo)

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = etiqueta,
                style = MaterialTheme.typography.labelMedium,
                color = TextoLogin
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
                visualTransformation = if (ocultarTexto) {
                    PasswordVisualTransformation()
                } else {
                    VisualTransformation.None
                },
                trailingIcon = accionFinal,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulLogin,
                    unfocusedBorderColor = BordeLogin,
                    focusedContainerColor = FondoLogin,
                    unfocusedContainerColor = FondoLogin,
                    focusedTextColor = TextoLogin,
                    unfocusedTextColor = TextoLogin
                )
            )
        }
    }
}

@Composable
private fun IconoLogin(tipo: String) {
    Box(
        modifier = Modifier
            .size(52.dp)
            .background(
                color = CelesteLogin,
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
                "correo" -> {
                    drawRoundRect(
                        color = AzulLogin,
                        topLeft = Offset(ancho * 0.12f, alto * 0.24f),
                        size = Size(ancho * 0.76f, alto * 0.52f),
                        style = trazo
                    )

                    drawLine(
                        color = AzulLogin,
                        start = Offset(ancho * 0.14f, alto * 0.27f),
                        end = Offset(ancho * 0.50f, alto * 0.51f),
                        strokeWidth = grosor
                    )

                    drawLine(
                        color = AzulLogin,
                        start = Offset(ancho * 0.50f, alto * 0.51f),
                        end = Offset(ancho * 0.86f, alto * 0.27f),
                        strokeWidth = grosor
                    )
                }

                "clave" -> {
                    drawArc(
                        color = AzulLogin,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(ancho * 0.30f, alto * 0.12f),
                        size = Size(ancho * 0.40f, alto * 0.50f),
                        style = trazo
                    )

                    drawRoundRect(
                        color = AzulLogin,
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