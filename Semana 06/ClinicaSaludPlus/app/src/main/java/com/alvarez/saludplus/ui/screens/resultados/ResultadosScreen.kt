package com.alvarez.saludplus.ui.screens.resultados

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.ui.components.Encabezado
import com.alvarez.saludplus.ui.components.Tarjeta

@Composable
fun ResultadosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Encabezado(titulo = "Mis resultados")

        Text(
            text = "Consulta aquí tus resultados médicos.",
            style = MaterialTheme.typography.bodyLarge
        )

        Tarjeta(
            titulo = "Aún no tienes resultados",
            detalle = "Cuando tengas resultados disponibles, aparecerán en esta sección."
        )
    }
}