package com.alvarez.proyecto_registronotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Registro de Notas",
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(0xFF5E4B8B),
                                titleContentColor = Color.White
                            )
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PantallaRegistroNotas(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun PantallaRegistroNotas(modifier: Modifier = Modifier) {
    var nota1 by remember { mutableFloatStateOf(0f) }
    var nota2 by remember { mutableFloatStateOf(0f) }
    var nota3 by remember { mutableFloatStateOf(0f) }
    var nota4 by remember { mutableFloatStateOf(0f) }

    var redondear by remember { mutableStateOf(false) }
    var confirmado by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }

    var promedioPonderado by remember { mutableStateOf(0.0) }
    var promedioFinalStr by remember { mutableStateOf("") }
    var observacion by remember { mutableStateOf("") }
    var chipBgColor by remember { mutableStateOf(Color.Unspecified) }
    var chipTextColor by remember { mutableStateOf(Color.Unspecified) }

    val scrollState = rememberScrollState()

    val gradientBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFEDE7F6),
            Color(0xFFFFFFFF)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState)
        ) {
            Text(
                text = "Notas del ciclo",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Desliza para asignar cada nota (0 a 20)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            ItemCurso(
                nombre = "Fundamentos de Programación",
                peso = "(20%)",
                nota = nota1,
                onNotaChange = {
                    nota1 = it
                    mostrarResultado = false
                }
            )

            ItemCurso(
                nombre = "Programación Orientada a Objetos",
                peso = "(25%)",
                nota = nota2,
                onNotaChange = {
                    nota2 = it
                    mostrarResultado = false
                }
            )

            ItemCurso(
                nombre = "Programación en Móviles",
                peso = "(30%)",
                nota = nota3,
                onNotaChange = {
                    nota3 = it
                    mostrarResultado = false
                }
            )

            ItemCurso(
                nombre = "Base de Datos",
                peso = "(25%)",
                nota = nota4,
                onNotaChange = {
                    nota4 = it
                    mostrarResultado = false
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Redondear promedio final",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = redondear,
                    onCheckedChange = {
                        redondear = it
                        mostrarResultado = false
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF5E4B8B)
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = confirmado,
                    onCheckedChange = {
                        confirmado = it
                        if (!it) mostrarResultado = false
                    },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF5E4B8B)
                    )
                )
                Text(
                    text = "Confirmo que las notas son correctas",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val n1 = nota1.toInt()
                    val n2 = nota2.toInt()
                    val n3 = nota3.toInt()
                    val n4 = nota4.toInt()

                    val pond = (n1 * 0.20) + (n2 * 0.25) + (n3 * 0.30) + (n4 * 0.25)
                    promedioPonderado = pond

                    val valFinalNum: Double
                    if (redondear) {
                        val finalInt = pond.roundToInt()
                        valFinalNum = finalInt.toDouble()
                        promedioFinalStr = "$finalInt"
                    } else {
                        valFinalNum = pond
                        promedioFinalStr = String.format(Locale.US, "%.2f", pond)
                    }

                    when {
                        valFinalNum >= 17.0 -> {
                            observacion = "EXCELENTE"
                            chipBgColor = Color(0xFFDCEDC8)
                            chipTextColor = Color(0xFF1B5E20)
                        }
                        valFinalNum >= 13.0 -> {
                            observacion = "APROBADO"
                            chipBgColor = Color(0xFFE8F5E9)
                            chipTextColor = Color(0xFF2E7D32)
                        }
                        valFinalNum >= 10.0 -> {
                            observacion = "EN RECUPERACIÓN"
                            chipBgColor = Color(0xFFFFF3E0)
                            chipTextColor = Color(0xFFE65100)
                        }
                        else -> {
                            observacion = "DESAPROBADO"
                            chipBgColor = Color(0xFFFFEBEE)
                            chipTextColor = Color(0xFFC62828)
                        }
                    }

                    mostrarResultado = true
                },
                enabled = confirmado,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5E4B8B),
                    disabledContainerColor = Color(0xFFC5CAE9)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(
                    text = "CALCULAR PROMEDIO",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (!mostrarResultado) {
                Text(
                    text = "Asigna las notas y confirma para calcular",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 4.dp)
                )
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E0))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row {
                            Text(
                                text = "Promedio ponderado:  ",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = String.format(Locale.US, "%.2f", promedioPonderado),
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "Promedio final:  ",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5E4B8B)
                            )
                            Text(
                                text = promedioFinalStr,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF5E4B8B)
                            )
                        }

                        if (redondear) {
                            Text(
                                text = "(redondeado)",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = chipBgColor
                        ) {
                            Text(
                                text = observacion,
                                color = chipTextColor,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCurso(
    nombre: String,
    peso: String,
    nota: Float,
    onNotaChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = peso,
                    color = Color(0xFF7E57C2),
                    fontSize = 12.sp
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFEDE7F6)
            ) {
                Text(
                    text = "${nota.toInt()}",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5E4B8B),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontSize = 14.sp
                )
            }
        }
        Slider(
            value = nota,
            onValueChange = onNotaChange,
            valueRange = 0f..20f,
            steps = 19,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF5E4B8B),
                activeTrackColor = Color(0xFF5E4B8B),
                inactiveTrackColor = Color(0xFFE0E0E0)
            )
        )
    }
}