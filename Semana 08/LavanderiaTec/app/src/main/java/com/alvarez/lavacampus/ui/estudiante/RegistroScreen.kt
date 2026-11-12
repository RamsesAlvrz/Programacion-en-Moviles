package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
fun RegistroScreen(viewModel: SesionViewModel, onVolver: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    var confirmar by remember { mutableStateOf("") }
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()
    val error by viewModel.mensajeError.collectAsStateWithLifecycle()

    val turquesa = Color(0xFF0895B5)
    val texto = Color(0xFF202938)
    val secundario = Color(0xFF8A8E98)
    val borde = Color(0xFFE4E6EA)
    val campoColores = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = turquesa,
        unfocusedBorderColor = borde,
        focusedLabelColor = turquesa,
        cursorColor = turquesa
    )

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .padding(top = 42.dp, bottom = 24.dp)
    ) {
        Text("Crear cuenta", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = texto)
        Spacer(Modifier.height(4.dp))
        Text("Regístrate para utilizar LavaCampus", fontSize = 13.sp, color = secundario)
        Spacer(Modifier.height(26.dp))

        OutlinedTextField(
            value = nombre, onValueChange = { nombre = it },
            label = { Text("Nombre completo") },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = turquesa) },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            shape = RoundedCornerShape(10.dp), colors = campoColores
        )
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = codigo, onValueChange = { codigo = it },
            label = { Text("Código de estudiante") },
            leadingIcon = { Icon(Icons.Outlined.Badge, contentDescription = null, tint = turquesa) },
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            shape = RoundedCornerShape(10.dp), colors = campoColores
        )
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = correo, onValueChange = { correo = it },
            label = { Text("Correo electrónico") },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null, tint = turquesa) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            shape = RoundedCornerShape(10.dp), colors = campoColores
        )
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = clave, onValueChange = { clave = it },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = turquesa) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            shape = RoundedCornerShape(10.dp), colors = campoColores
        )
        Spacer(Modifier.height(14.dp))
        OutlinedTextField(
            value = confirmar, onValueChange = { confirmar = it },
            label = { Text("Confirmar contraseña") },
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null, tint = turquesa) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(), singleLine = true,
            shape = RoundedCornerShape(10.dp), colors = campoColores
        )

        if (!error.isNullOrBlank()) {
            Spacer(Modifier.height(12.dp))
            Text(error.orEmpty(), color = MaterialTheme.colorScheme.error, fontSize = 13.sp)
        }
        Spacer(Modifier.height(26.dp))
        Button(
            onClick = { viewModel.registrarEstudiante(nombre, codigo, correo, clave, confirmar) { } },
            enabled = !cargando,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = turquesa)
        ) {
            Text(if (cargando) "Registrando..." else "Crear cuenta", fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(14.dp))
        TextButton(
            onClick = { viewModel.limpiarError(); onVolver() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión", color = turquesa,
                fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center)
        }
    }
}
