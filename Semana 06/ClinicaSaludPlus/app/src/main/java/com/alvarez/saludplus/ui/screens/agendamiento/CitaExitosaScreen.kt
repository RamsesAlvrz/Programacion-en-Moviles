package com.alvarez.saludplus.ui.screens.agendamiento

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
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.*

@Composable
fun CitaExitosaScreen(
    citaId: Int,
    onInicio: () -> Unit,
    onMisCitas: () -> Unit
) {
    val cita = Repositorio.obtenerCita(citaId)
    val medico = cita?.let {
        Repositorio.obtenerMedico(it.medicoId)
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoAgendamiento)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = maxHeight)
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 24.dp,
                    vertical = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                space = 22.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            Surface(
                modifier = Modifier.size(112.dp),
                shape = CircleShape,
                color = if (cita != null) {
                    Color(0xFFD9F3E7)
                } else {
                    CelesteAgendamiento
                }
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (cita != null) "✓" else "!",
                        fontSize = 66.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (cita != null) {
                            Color(0xFF359367)
                        } else {
                            AzulAgendamiento
                        }
                    )
                }
            }

            Text(
                text = if (cita != null) {
                    "¡Cita agendada!"
                } else {
                    "Cita no encontrada"
                },
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextoAgendamiento,
                textAlign = TextAlign.Center
            )

            Text(
                text = if (cita != null) {
                    "Tu reserva se registró correctamente."
                } else {
                    "No pudimos encontrar esta reserva en tu sesión."
                },
                fontSize = 16.sp,
                lineHeight = 22.sp,
                color = Color(0xFF536672),
                textAlign = TextAlign.Center
            )

            if (cita != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Reserva #${cita.id}",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoAgendamiento
                        )

                        if (medico != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                FotoMedico(medico)

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = medico.nombre,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextoAgendamiento
                                    )

                                    Text(
                                        text = Repositorio.obtenerEspecialidad(
                                            medico.especialidadId
                                        )?.nombre.orEmpty(),
                                        color = Color(0xFF536672)
                                    )
                                }
                            }
                        }

                        HorizontalDivider(
                            color = Color(0xFFDCE7EC)
                        )

                        Text(
                            text = DatosConsulta.fechaEnEspanol(cita.fecha),
                            fontSize = 16.sp,
                            color = TextoAgendamiento
                        )

                        Text(
                            text = "Hora: ${cita.hora}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AzulAgendamiento
                        )

                        Text(
                            text = DatosConsulta.DIRECCION,
                            fontSize = 14.sp,
                            color = Color(0xFF536672)
                        )
                    }
                }
            }

            Button(
                onClick = onMisCitas,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulAgendamiento
                )
            ) {
                Text(
                    text = "Ver mis citas",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = onInicio,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Volver al inicio",
                    fontSize = 16.sp,
                    color = AzulAgendamiento
                )
            }
        }
    }
}