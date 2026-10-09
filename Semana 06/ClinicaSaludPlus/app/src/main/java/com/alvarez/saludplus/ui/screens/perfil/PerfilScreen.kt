package com.alvarez.saludplus.ui.screens.perfil

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.*

@Composable
fun PerfilScreen(
    onCerrarSesion: () -> Unit
) {
    val usuario = Repositorio.usuarioActual

    val cantidad = usuario?.let {
        Repositorio.citasDelUsuario(it.id).size
    } ?: 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoAgendamiento)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Mi perfil",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextoAgendamiento
        )

        if (usuario != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CelesteAgendamiento
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val iniciales = listOf(
                        usuario.nombre,
                        usuario.apellido
                    ).mapNotNull {
                        it.trim().firstOrNull()
                    }.joinToString("") {
                        it.uppercaseChar().toString()
                    }

                    Surface(
                        modifier = Modifier.size(86.dp),
                        shape = CircleShape,
                        color = AzulAgendamiento
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = iniciales,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Text(
                        text = "${usuario.nombre} ${usuario.apellido}",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoAgendamiento,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = "Paciente · SaludPlus",
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 7.dp
                            ),
                            color = AzulAgendamiento,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Text(
                text = "Información personal",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextoAgendamiento
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    DatoPerfil(
                        titulo = "Nombre completo",
                        valor = "${usuario.nombre} ${usuario.apellido}",
                        tipoIcono = "persona"
                    )

                    HorizontalDivider(
                        color = Color(0xFFE5EDF1)
                    )

                    DatoPerfil(
                        titulo = "Correo electrónico",
                        valor = usuario.correo,
                        tipoIcono = "documento"
                    )

                    HorizontalDivider(
                        color = Color(0xFFE5EDF1)
                    )

                    DatoPerfil(
                        titulo = "Teléfono",
                        valor = usuario.telefono,
                        tipoIcono = "persona"
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD9F3E7)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    IconoSaludPlus(
                        tipo = "calendario",
                        color = Color(0xFF359367),
                        modifier = Modifier.size(38.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Mis citas",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoAgendamiento
                        )

                        Text(
                            text = if (cantidad == 1) {
                                "1 cita registrada"
                            } else {
                                "$cantidad citas registradas"
                            },
                            color = Color(0xFF536672),
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = cantidad.toString(),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF359367)
                    )
                }
            }
        } else {
            Text(
                text = "No hay una sesión activa.",
                color = TextoAgendamiento
            )
        }

        OutlinedButton(
            onClick = {
                Repositorio.cerrarSesion()
                onCerrarSesion()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AzulAgendamiento
            )
        ) {
            Text(
                text = "Cerrar sesión",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DatoPerfil(
    titulo: String,
    valor: String,
    tipoIcono: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = CelesteAgendamiento
        ) {
            Box(
                modifier = Modifier.size(44.dp),
                contentAlignment = Alignment.Center
            ) {
                IconoSaludPlus(
                    tipo = tipoIcono,
                    color = AzulAgendamiento,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = titulo,
                fontSize = 14.sp,
                color = Color(0xFF657885)
            )

            Text(
                text = valor,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextoAgendamiento
            )
        }
    }
}