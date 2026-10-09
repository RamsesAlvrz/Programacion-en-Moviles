package com.alvarez.saludplus.ui.screens.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.BotonPrimario
import com.alvarez.saludplus.ui.components.Encabezado
import com.alvarez.saludplus.ui.components.Tarjeta

@Composable
fun PerfilScreen(
    onCerrarSesion: () -> Unit
) {
    val usuario = Repositorio.usuarioActual

    val cantidadCitas = usuario?.let {
        Repositorio.citasDelUsuario(it.id).size
    } ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Encabezado(
            titulo = "Mi perfil"
        )

        if (usuario != null) {
            Text(
                text = "Datos del paciente",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Tarjeta(
                titulo = "Nombre completo",
                detalle = "${usuario.nombre} ${usuario.apellido}"
            )

            Tarjeta(
                titulo = "Correo electrónico",
                detalle = usuario.correo
            )

            Tarjeta(
                titulo = "Teléfono",
                detalle = usuario.telefono
            )

            Tarjeta(
                titulo = "Mis citas",
                detalle = "$cantidadCitas cita(s) reservada(s)"
            )
        } else {
            Text(
                text = "No hay una sesión activa.",
                color = MaterialTheme.colorScheme.error
            )
        }

        BotonPrimario(
            texto = "Cerrar sesión",
            onClick = {
                Repositorio.cerrarSesion()
                onCerrarSesion()
            }
        )
    }
}