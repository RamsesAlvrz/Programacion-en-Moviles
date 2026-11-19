
package com.alvarez.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.campusgps.model.LugarConDistancia
import com.alvarez.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    masCercanoId: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = 10.dp,
            vertical = 4.dp
        ),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        items(
            items = lugares,
            key = { it.lugar.id }
        ) { item ->

            val activo = seleccionado == item.lugar.id

            val esMasCercano =
                item.lugar.id == masCercanoId

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onClick(item.lugar.id)
                    },
                shape = RoundedCornerShape(9.dp),
                border = if (activo) {
                    BorderStroke(
                        1.dp,
                        Color(0xFF2584C1)
                    )
                } else {
                    null
                },
                colors = CardDefaults.cardColors(
                    containerColor = if (activo) {
                        Color(0xFFDDEFFA)
                    } else {
                        Color(0xFFF0F3F7)
                    }
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // Punto verde para el más cercano
                    // Azul para los demás
                    Canvas(
                        modifier = Modifier.size(7.dp)
                    ) {
                        drawCircle(
                            color = if (esMasCercano) {
                                Color(0xFF2EA050)
                            } else {
                                Color(0xFF2584C1)
                            }
                        )
                    }

                    Spacer(Modifier.width(9.dp))

                    // Nombre y categoría
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = item.lugar.nombre,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF172A3A),
                            lineHeight = 14.sp
                        )

                        Text(
                            text = item.lugar.categoria,
                            fontSize = 10.sp,
                            color = Color.Gray,
                            lineHeight = 12.sp
                        )
                    }

                    Spacer(Modifier.width(6.dp))

                    // Distancia a la derecha
                    Text(
                        text = item.metros?.let {
                            formatearDistancia(it)
                        } ?: "Sin calcular",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF176B9A)
                    )
                }
            }
        }
    }
}
