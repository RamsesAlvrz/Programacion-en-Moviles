package com.alvarez.alvareztecstore.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val colorMorado = Color(0xFF522175)
    val colorFondoSeleccionado = Color(0xFFEDE7F6)

    ModalDrawerSheet(
        drawerContainerColor = Color.White
    ) {
        // 1. Encabezado de usuario (Avatar, Nombre y Correo)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(colorFondoSeleccionado),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "RA",
                        fontWeight = FontWeight.Bold,
                        color = colorMorado,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Ramses Alvarez",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "ramses@tecsup.edu.pe",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color(0xFFEEEEEE))
        Spacer(modifier = Modifier.height(12.dp))

        // 2. Lista de opciones con resaltado activo
        opcionesDrawer.forEach { item ->
            val isSelected = item.titulo == destinoSeleccionado

            NavigationDrawerItem(
                label = {
                    Text(
                        text = item.titulo,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) colorMorado else Color(0xFF333333)
                    )
                },
                selected = isSelected,
                onClick = { onDestinoSeleccionado(item.titulo) },
                colors = NavigationDrawerItemDefaults.colors(
                    selectedContainerColor = colorFondoSeleccionado,
                    unselectedContainerColor = Color.Transparent
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )
        }
    }
}