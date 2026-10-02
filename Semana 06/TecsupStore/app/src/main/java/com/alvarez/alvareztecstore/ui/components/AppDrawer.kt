package com.alvarez.alvareztecstore.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ItemNavegacion(val titulo: String)

val opcionesDrawer = listOf(
    ItemNavegacion("Inicio"),
    ItemNavegacion("Mis pedidos"),
    ItemNavegacion("Favoritos"),
    ItemNavegacion("Perfil"),
    ItemNavegacion("Cerrar sesion")
)

@Composable
fun AppDrawer(
    destinoSeleccionado: String,
    onDestinoSeleccionado: (String) -> Unit
) {
    ModalDrawerSheet {
        Text(
            text = "Menú de Navegación",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleMedium
        )
        HorizontalDivider()

        opcionesDrawer.forEach { item ->
            NavigationDrawerItem(
                label = { Text(item.titulo) },
                selected = item.titulo == destinoSeleccionado,
                onClick = { onDestinoSeleccionado(item.titulo) },
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}