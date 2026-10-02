package com.alvarez.alvareztecstore.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.alvareztecstore.Producto

@Composable
fun TarjetaProducto(
    producto: Producto,
    onAgregarFavorito: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val colorMorado = Color(0xFF522175)
    val colorFondoRosa = Color(0xFFF6F2FA)
    val colorIconoFondo = Color(0xFFEDE7F6)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        // 1. Tarjeta Principal del producto
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colorFondoRosa),
            border = BorderStroke(2.dp, colorMorado)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Recuadro con icono de bolsa morada
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .background(colorIconoFondo, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Producto",
                            tint = colorMorado,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Nombre y precio
                    Column {
                        Text(
                            text = producto.nombre,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = producto.precio,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = colorMorado
                        )
                    }
                }

                // Boton de 3 puntos (activa o desactiva la visibilidad del menú desplegable)
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Opciones",
                        tint = Color.Black
                    )
                }
            }
        }

        // 2. Menú desplegable alineado a la derecha debajo de la tarjeta
        // Al estar dentro de la Column, empuja a las demás tarjetas hacia abajo
        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                    shadowElevation = 2.dp,
                    modifier = Modifier.width(170.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        // Opción Favoritos
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onAgregarFavorito()
                                    expanded = false
                                }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favoritos",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Favoritos",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = Color(0xFFEEEEEE)
                        )

                        // Opción Compartir
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = false }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Compartir",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Compartir",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            color = Color(0xFFEEEEEE)
                        )

                        // Opción Reportar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expanded = false }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Reportar",
                                tint = Color.Black,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Reportar",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}