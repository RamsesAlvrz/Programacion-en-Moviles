package com.alvarez.saludplus.ui.screens.agendamiento

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.saludplus.repository.Repositorio
import com.alvarez.saludplus.ui.components.IconoEspecialidad

private val FondoEspecialidades = Color(0xFFF5FAFC)
private val TextoEspecialidades = Color(0xFF17324F)
private val AzulEspecialidades = Color(0xFF2378C9)

@Composable
fun EspecialidadesScreen(
    onVolver: () -> Unit,
    onEspecialidad: (Int) -> Unit
) {
    var busqueda by rememberSaveable {
        mutableStateOf("")
    }

    val especialidades = Repositorio.buscarEspecialidades(busqueda)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoEspecialidades)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Especialidades",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextoEspecialidades
            )

            TextButton(
                onClick = onVolver,
                modifier = Modifier.align(Alignment.CenterStart),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "←",
                    fontSize = 28.sp,
                    color = TextoEspecialidades
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = busqueda,
            onValueChange = {
                busqueda = it
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Buscar especialidad...",
                    fontSize = 16.sp
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFE6EFF3),
                unfocusedContainerColor = Color(0xFFE6EFF3),
                focusedBorderColor = AzulEspecialidades,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = AzulEspecialidades
            )
        )

        Spacer(Modifier.height(12.dp))

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            // Mantiene el tamaño de las filas incluso al buscar.
            // En pantallas pequeñas permite desplazamiento.
            val altoFila = ((maxHeight - 16.dp) / 6f)
                .coerceAtLeast(92.dp)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                if (especialidades.isEmpty()) {
                    item {
                        Text(
                            text = "No se encontraron especialidades.",
                            modifier = Modifier.padding(vertical = 24.dp),
                            fontSize = 16.sp,
                            color = TextoEspecialidades
                        )
                    }
                }

                items(
                    items = especialidades,
                    key = { it.id }
                ) { especialidad ->
                    Surface(
                        onClick = {
                            onEspecialidad(especialidad.id)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        color = FondoEspecialidades
                    ) {
                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = altoFila - 1.dp)
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                IconoEspecialidad(
                                    especialidadId = especialidad.id,
                                    tamano = 60.dp
                                )

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(
                                        text = especialidad.nombre,
                                        fontSize = 18.sp,
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextoEspecialidades
                                    )

                                    Text(
                                        text = especialidad.descripcion,
                                        fontSize = 14.sp,
                                        lineHeight = 18.sp,
                                        color = Color(0xFF536672)
                                    )
                                }

                                Text(
                                    text = "›",
                                    fontSize = 32.sp,
                                    color = Color(0xFF657885)
                                )
                            }

                            HorizontalDivider(
                                color = Color(0xFFDCE7EC),
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}