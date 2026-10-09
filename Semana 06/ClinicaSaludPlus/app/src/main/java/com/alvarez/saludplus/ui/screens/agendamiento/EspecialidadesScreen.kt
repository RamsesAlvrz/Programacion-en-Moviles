package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.CampoTexto
import com.alvarez.saludplus.ui.components.Encabezado
import com.alvarez.saludplus.ui.components.Tarjeta

@Composable
fun EspecialidadesScreen(
    onVolver: () -> Unit,
    onEspecialidad: (Int) -> Unit
) {
    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    val especialidades = Repositorio.buscarEspecialidades(busqueda)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Encabezado(
            titulo = "Especialidades",
            onVolver = onVolver
        )

        Text(
            text = "Selecciona la especialidad para tu consulta.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        CampoTexto(
            etiqueta = "Buscar especialidad",
            valor = busqueda,
            onCambio = {
                busqueda = it
            }
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (especialidades.isEmpty()) {
                item {
                    Text(
                        text = "No se encontraron especialidades.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            items(
                items = especialidades,
                key = { especialidad ->
                    especialidad.id
                }
            ) { especialidad ->

                Tarjeta(
                    titulo = especialidad.nombre,
                    detalle = especialidad.descripcion,
                    onClick = {
                        onEspecialidad(especialidad.id)
                    }
                )
            }
        }
    }
}