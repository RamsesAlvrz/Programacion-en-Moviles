package com.alvarez.saludplus.ui.screens.citas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.*

@Composable
fun MisCitasScreen(
    onDetalle: (Int) -> Unit,
    onAgendar: () -> Unit
) {
    val usuario = Repositorio.usuarioActual

    val citas = usuario?.let {
        Repositorio.citasDelUsuario(it.id)
    }.orEmpty().sortedWith(
        compareBy(
            { it.fecha },
            { it.hora }
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoAgendamiento),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    Text(
                        text = "Mis citas",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoAgendamiento
                    )

                    Text(
                        text = if (citas.size == 1) {
                            "Tienes 1 cita registrada"
                        } else {
                            "Tienes ${citas.size} citas registradas"
                        },
                        fontSize = 15.sp,
                        color = Color(0xFF536672)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = CelesteAgendamiento
                ) {
                    Box(
                        modifier = Modifier.size(52.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconoSaludPlus(
                            tipo = "calendario",
                            color = AzulAgendamiento,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
        }

        if (citas.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        IconoSaludPlus(
                            tipo = "calendario",
                            color = AzulAgendamiento,
                            modifier = Modifier.size(64.dp)
                        )

                        Text(
                            text = "Todavía no tienes citas",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoAgendamiento,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Text(
                            text = "Agenda tu primera consulta médica.",
                            fontSize = 15.sp,
                            color = Color(0xFF536672),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        items(
            items = citas,
            key = { it.id }
        ) { cita ->
            val medico = Repositorio.obtenerMedico(cita.medicoId)

            val especialidad = medico?.let {
                Repositorio.obtenerEspecialidad(it.especialidadId)
            }

            Card(
                onClick = {
                    onDetalle(cita.id)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (medico != null) {
                            FotoMedico(medico)
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = medico?.nombre ?: "Médico",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoAgendamiento
                            )

                            Text(
                                text = especialidad?.nombre.orEmpty(),
                                fontSize = 14.sp,
                                color = Color(0xFF536672)
                            )
                        }

                        Text(
                            text = "›",
                            fontSize = 30.sp,
                            color = AzulAgendamiento
                        )
                    }

                    HorizontalDivider(
                        color = Color(0xFFE5EDF1)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconoSaludPlus(
                            tipo = "calendario",
                            color = AzulAgendamiento,
                            modifier = Modifier.size(28.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            Text(
                                text = DatosConsulta.fechaEnEspanol(cita.fecha),
                                fontSize = 15.sp,
                                lineHeight = 21.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = TextoAgendamiento
                            )

                            Text(
                                text = "Hora: ${cita.hora}",
                                fontSize = 15.sp,
                                color = AzulAgendamiento
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Surface(
                            color = CelesteAgendamiento,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "Reserva #${cita.id}",
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                ),
                                fontSize = 13.sp,
                                color = AzulAgendamiento
                            )
                        }

                        Text(
                            text = "Ver detalle",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AzulAgendamiento
                        )
                    }
                }
            }
        }

        item {
            Button(
                onClick = onAgendar,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulAgendamiento
                )
            ) {
                Text(
                    text = if (citas.isEmpty()) {
                        "Agendar cita"
                    } else {
                        "Agendar otra cita"
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}