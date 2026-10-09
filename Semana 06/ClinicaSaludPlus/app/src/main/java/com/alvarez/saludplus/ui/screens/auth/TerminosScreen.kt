package com.alvarez.saludplus.ui.screens.auth

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
import com.alvarez.saludplus.ui.components.Encabezado

@Composable
fun TerminosScreen(
    onVolver: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Encabezado(
            titulo = "Términos",
            onVolver = onVolver
        )

        Text(
            text = "Términos y condiciones",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "SaludPlus es una aplicación de práctica académica para simular el registro de pacientes y el agendamiento de citas médicas."
        )

        Text(
            text = "Las cuentas y las citas se almacenan temporalmente en memoria. No se envían a una clínica ni a un servicio externo."
        )

        Text(
            text = "Al finalizar el proceso de la aplicación, los datos registrados se pierden."
        )

        Text(
            text = "Utiliza datos de prueba. Las citas y los precios mostrados corresponden a ejemplos del laboratorio."
        )
    }
}