package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.*
import kotlinx.coroutines.delay
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale

@Composable
fun MedicosScreen(
    especialidadId: Int,
    onVolver: () -> Unit,
    onMedico: (Int) -> Unit
) {
    var busqueda by rememberSaveable(especialidadId) {
        mutableStateOf("")
    }

    val zonaLima = remember {
        ZoneId.of("America/Lima")
    }

    var ahora by remember {
        mutableStateOf(ZonedDateTime.now(zonaLima))
    }

    // Actualiza el estado mientras la pantalla está abierta.
    LaunchedEffect(zonaLima) {
        while (true) {
            ahora = ZonedDateTime.now(zonaLima)
            delay(15_000L)
        }
    }

    val especialidad = Repositorio.obtenerEspecialidad(
        especialidadId
    )

    val medicos = Repositorio.buscarMedicos(
        especialidadId,
        busqueda
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoAgendamiento)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(10.dp))

        EncabezadoAgendamiento(
            titulo = especialidad?.let {
                "Médicos de ${it.nombre}"
            } ?: "Médicos",
            onVolver = onVolver
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = {
                busqueda = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Buscar médico...",
                    fontSize = 15.sp
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE6EFF3),
                unfocusedContainerColor = Color(0xFFE6EFF3),
                focusedBorderColor = AzulAgendamiento,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = AzulAgendamiento
            )
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            if (medicos.isEmpty()) {
                item {
                    Text(
                        text = "No se encontraron médicos.",
                        modifier = Modifier.padding(vertical = 20.dp),
                        fontSize = 16.sp,
                        color = TextoAgendamiento
                    )
                }
            }

            items(
                items = medicos,
                key = { it.id }
            ) { medico ->
                val citaEnCurso = Repositorio.medicoTieneCitaEnCurso(
                    medicoId = medico.id,
                    ahora = ahora
                )

                val colorEstado = if (citaEnCurso) {
                    Color(0xFF85534D)
                } else {
                    Color(0xFF327B58)
                }

                val fondoEstado = if (citaEnCurso) {
                    Color(0xFFF5DEDA)
                } else {
                    Color(0xFFD2F1E1)
                }

                Card(
                    onClick = {
                        onMedico(medico.id)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                    fontSize = 17.sp,
                                    lineHeight = 21.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextoAgendamiento
                                )

                                Text(
                                    text = especialidad?.nombre.orEmpty(),
                                    fontSize = 14.sp,
                                    color = TextoAgendamiento
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(
                                        text = "★",
                                        fontSize = 20.sp,
                                        color = Color(0xFFE8AD35)
                                    )

                                    Text(
                                        text = String.format(
                                            Locale.US,
                                            "%.1f (%d)",
                                            medico.calificacion,
                                            medico.cantidadOpiniones
                                        ),
                                        fontSize = 14.sp,
                                        color = TextoAgendamiento
                                    )
                                }
                            }
                        }

                        Surface(
                            modifier = Modifier.align(Alignment.End),
                            color = fondoEstado,
                            shape = RoundedCornerShape(7.dp)
                        ) {
                            Text(
                                text = if (citaEnCurso) {
                                    "Cita en curso"
                                } else {
                                    "Disponible ahora"
                                },
                                modifier = Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                ),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = colorEstado
                            )
                        }
                    }
                }
            }
        }
    }
}