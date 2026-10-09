package com.alvarez.saludplus.ui.screens.resultados

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.saludplus.ui.components.*

@Composable
fun ResultadosScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoAgendamiento)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Mis resultados",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextoAgendamiento
        )

        Text(
            text = "Consulta tus resultados médicos.",
            fontSize = 15.sp,
            color = Color(0xFF536672)
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = maxHeight)
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 22.dp,
                    alignment = Alignment.CenterVertically
                )
            ) {
                Surface(
                    modifier = Modifier.size(120.dp),
                    shape = CircleShape,
                    color = CelesteAgendamiento
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        IconoSaludPlus(
                            tipo = "documento",
                            color = AzulAgendamiento,
                            modifier = Modifier.size(58.dp)
                        )
                    }
                }

                Text(
                    text = "Aún no tienes resultados",
                    fontSize = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoAgendamiento,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Cuando tengas resultados disponibles,\n" +
                            "aparecerán en esta sección.",
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    color = Color(0xFF536672),
                    textAlign = TextAlign.Center
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        IconoSaludPlus(
                            tipo = "salud",
                            color = AzulAgendamiento,
                            modifier = Modifier.size(32.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = "Tu información médica",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoAgendamiento
                            )

                            Text(
                                text = "Aquí podrás consultar los resultados " +
                                        "que se incorporen a tu atención.",
                                fontSize = 14.sp,
                                lineHeight = 20.sp,
                                color = Color(0xFF536672)
                            )
                        }
                    }
                }
            }
        }
    }
}