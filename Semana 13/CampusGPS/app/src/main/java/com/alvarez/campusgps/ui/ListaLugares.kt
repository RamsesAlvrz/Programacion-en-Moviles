package com.alvarez.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val colores = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(
            horizontal = 14.dp,
            vertical = 6.dp
        ),
        verticalArrangement = Arrangement.spacedBy(9.dp)
    ) {
        items(
            items = lugares,
            key = { it.lugar.id }
        ) { item ->
            val activo = seleccionado == item.lugar.id
            val esMasCercano = item.lugar.id == masCercanoId

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(item.lugar.id) },
                shape = RoundedCornerShape(16.dp),
                border = if (activo) {
                    BorderStroke(1.5.dp, colores.primary)
                } else {
                    BorderStroke(1.dp, colores.outline)
                },
                colors = CardDefaults.cardColors(
                    containerColor = if (activo) {
                        colores.primaryContainer
                    } else {
                        colores.surface
                    }
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (activo) 3.dp else 1.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = if (esMasCercano) {
                            colores.primary
                        } else {
                            colores.secondaryContainer
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Canvas(Modifier.size(10.dp)) {
                                drawCircle(
                                    color = if (esMasCercano) {
                                        colores.onPrimary
                                    } else {
                                        colores.onSecondaryContainer
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = item.lugar.nombre,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = colores.onSurface
                        )

                        Text(
                            text = item.lugar.categoria,
                            fontSize = 11.sp,
                            color = colores.onSurfaceVariant
                        )

                        if (esMasCercano) {
                            Text(
                                text = "MÁS CERCANO",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = colores.primary
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    Text(
                        text = item.metros?.let {
                            formatearDistancia(it)
                        } ?: "--",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = colores.primary
                    )
                }
            }
        }
    }
}
