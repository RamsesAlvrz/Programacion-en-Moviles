package com.alvarez.saludplus.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.saludplus.R
import com.alvarez.saludplus.model.Medico
import com.alvarez.saludplus.repository.Repositorio

val FondoAgendamiento = Color(0xFFF5FAFC)
val AzulAgendamiento = Color(0xFF2378C9)
val TextoAgendamiento = Color(0xFF17324F)
val CelesteAgendamiento = Color(0xFFE3F1F7)

@Composable
fun EncabezadoAgendamiento(
    titulo: String,
    onVolver: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 58.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onVolver,
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier.width(40.dp)
        ) {
            Text(
                text = "←",
                fontSize = 28.sp,
                color = TextoAgendamiento
            )
        }

        Text(
            text = titulo,
            modifier = Modifier.weight(1f),
            fontSize = 21.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight.Bold,
            color = TextoAgendamiento
        )
    }
}

@Composable
fun FotoMedico(
    medico: Medico,
    modifier: Modifier = Modifier
) {
    val recurso = when (medico.id) {
        1 -> R.drawable.medico_ana
        2 -> R.drawable.medico_luis
        3 -> R.drawable.medico_carlos
        4 -> R.drawable.medico_elena
        5 -> R.drawable.medico_maria
        6 -> R.drawable.medico_jose
        7 -> R.drawable.medico_pedro
        8 -> R.drawable.medico_lucia
        else -> null
    }

    if (recurso != null) {
        Image(
            painter = painterResource(recurso),
            contentDescription = "Fotografía de ${medico.nombre}",
            modifier = modifier
                .size(64.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    } else {
        val iniciales = medico.nombre
            .split(" ")
            .filter {
                it.isNotBlank() &&
                        !it.startsWith("Dr", ignoreCase = true)
            }
            .take(2)
            .joinToString("") {
                it.first().uppercaseChar().toString()
            }

        Box(
            modifier = modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFD0E8F5)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = iniciales,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = AzulAgendamiento
            )
        }
    }
}

@Composable
fun ResumenMedico(
    medico: Medico,
    modifier: Modifier = Modifier
) {
    val especialidad = Repositorio.obtenerEspecialidad(
        medico.especialidadId
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = CelesteAgendamiento
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            FotoMedico(medico)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = medico.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextoAgendamiento
                )

                Text(
                    text = especialidad?.nombre.orEmpty(),
                    fontSize = 15.sp,
                    color = TextoAgendamiento
                )

                Text(
                    text = "${medico.experiencia} años de experiencia",
                    fontSize = 13.sp,
                    color = Color(0xFF536672)
                )
            }
        }
    }
}