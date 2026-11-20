package com.alvarez.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alvarez.centrotareas.model.CargaUi
import com.alvarez.centrotareas.viewmodel.ComparacionTiempos
import com.alvarez.centrotareas.viewmodel.TareasViewModel
import java.util.Locale

private val Azul = Color(0xFF176B9A)
private val Fondo = Color(0xFFF3F6FA)
private val Verde = Color(0xFF167448)
private val Rojo = Color(0xFFB42332)
private val Naranja = Color(0xFFB76B12)

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel) {

    val carga by viewModel.carga.collectAsState()
    val simularError by viewModel.simularError.collectAsState()
    val limiteActivo by viewModel.limiteActivo.collectAsState()
    val reintentoAutomatico by viewModel.reintentoAutomatico.collectAsState()
    val intentoActual by viewModel.intentoActual.collectAsState()
    val mensajeReintento by viewModel.mensajeReintento.collectAsState()
    val comparando by viewModel.comparando.collectAsState()
    val comparacion by viewModel.comparacion.collectAsState()
    val mensajeComparacion by viewModel.mensajeComparacion.collectAsState()

    val ocupado = carga is CargaUi.Cargando || comparando

    Scaffold(
        containerColor = Fondo,
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "CentroTareas Tec",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Panel de operaciones",
                            color = Color.White.copy(alpha = 0.8f),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Azul
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Azul)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        "CENTRO DE CONTROL",
                        color = Color.White.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        "Gestión inteligente de tareas",
                        color = Color.White,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Controla cargas, recupera errores y compara rendimiento.",
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Configuración",
                        color = Azul,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    FilaConfiguracion(
                        titulo = "Límite de tiempo",
                        descripcion = "Cancelar después de 2 segundos",
                        activo = limiteActivo,
                        habilitado = !ocupado,
                        cambiar = viewModel::cambiarLimiteActivo
                    )

                    HorizontalDivider()

                    FilaConfiguracion(
                        titulo = "Simular error",
                        descripcion = "Probar fallos de conexión",
                        activo = simularError,
                        habilitado = !ocupado,
                        cambiar = viewModel::cambiarSimularError
                    )

                    HorizontalDivider()

                    FilaConfiguracion(
                        titulo = "Reintento automático",
                        descripcion = "Hasta 3 intentos con espera progresiva",
                        activo = reintentoAutomatico,
                        habilitado = !ocupado,
                        cambiar = viewModel::cambiarReintentoAutomatico
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Ejecución de cargas",
                        color = Azul,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Selecciona cómo procesar el perfil y los pedidos.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = viewModel::cargarSecuencial,
                            enabled = !ocupado,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Una por una")
                        }

                        Button(
                            onClick = viewModel::cargarParalelo,
                            enabled = !ocupado,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Azul
                            )
                        ) {
                            Text("A la vez")
                        }
                    }

                    if (mensajeReintento.isNotEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFFFFF5E7),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "ESTADO DE INTENTOS",
                                color = Naranja,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.labelMedium
                            )

                            Text(
                                mensajeReintento,
                                color = Naranja
                            )

                            if (reintentoAutomatico && intentoActual > 0) {
                                Text(
                                    "Intento $intentoActual de 3",
                                    color = Naranja,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }

                    when (val estado = carga) {
                        CargaUi.Inicial -> {
                            Text(
                                "Listo para iniciar una operación.",
                                color = Color.Gray
                            )
                        }

                        CargaUi.Cargando -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CircularProgressIndicator(color = Azul)
                                Text(
                                    "Procesando información...",
                                    color = Azul
                                )
                            }
                        }

                        is CargaUi.Exito -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color(0xFFE7F6EE),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    "OPERACIÓN COMPLETADA",
                                    color = Verde,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Modo: ${estado.modo}")
                                Text("Perfil: ${estado.perfil}")
                                Text("Pedidos: ${estado.pedidos}")
                                Text("Duración: ${estado.milisegundos} ms")
                            }
                        }

                        is CargaUi.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Color(0xFFFFECEE),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    "OPERACIÓN INTERRUMPIDA",
                                    color = Rojo,
                                    fontWeight = FontWeight.Bold
                                )

                                Text(
                                    estado.mensaje.replace(
                                        "Sin conexion",
                                        "Sin conexión"
                                    ),
                                    color = Rojo
                                )

                                OutlinedButton(
                                    onClick = viewModel::reintentarCarga,
                                    enabled = !ocupado
                                ) {
                                    Text("Reintentar operación")
                                }
                            }
                        }
                    }
                }
            }

            // NUEVA TARJETA: MEJORA 3
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        "Comparación de rendimiento",
                        color = Azul,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        "Mide cuánto tarda cada modalidad y descubre cuál es más rápida.",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Button(
                        onClick = viewModel::compararTiempos,
                        enabled = !ocupado,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Azul
                        )
                    ) {
                        Text("Comparar tiempos")
                    }

                    if (comparando) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.height(26.dp),
                                color = Azul,
                                strokeWidth = 3.dp
                            )
                            Text(
                                mensajeComparacion,
                                color = Azul
                            )
                        }
                    } else {
                        Text(
                            mensajeComparacion,
                            color = if (comparacion == null &&
                                mensajeComparacion.startsWith("No se pudo")
                            ) Rojo else Color.Gray,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    comparacion?.let { resultado ->
                        HorizontalDivider()

                        TablaComparacion(resultado)

                        HorizontalDivider()

                        BarrasComparacion(resultado)

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color(0xFFE7F6EE),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                "RESULTADO DEL ANÁLISIS",
                                color = Verde,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Más rápido: ${resultado.masRapido}",
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                "Diferencia: ${resultado.diferenciaMs} ms"
                            )

                            Text(
                                "Ahorro: ${
                                    String.format(
                                        Locale.US,
                                        "%.1f",
                                        resultado.ahorroPorcentaje
                                    )
                                }%"
                            )
                        }
                    }
                }
            }

            Text(
                "CentroTareasTec · Fase 2 · Mejora con IA",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
private fun FilaConfiguracion(
    titulo: String,
    descripcion: String,
    activo: Boolean,
    habilitado: Boolean,
    cambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                titulo,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                descripcion,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Switch(
            checked = activo,
            onCheckedChange = cambiar,
            enabled = habilitado
        )
    }
}

@Composable
private fun TablaComparacion(resultado: ComparacionTiempos) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            "TABLA DE TIEMPOS",
            color = Azul,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Modalidad", fontWeight = FontWeight.Bold)
            Text("Tiempo", fontWeight = FontWeight.Bold)
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Una por una")
            Text("${resultado.secuencialMs} ms")
        }

        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("A la vez")
            Text("${resultado.paraleloMs} ms")
        }
    }
}

@Composable
private fun BarrasComparacion(resultado: ComparacionTiempos) {
    val maximo = maxOf(
        resultado.secuencialMs,
        resultado.paraleloMs,
        1L
    ).toFloat()

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            "COMPARACIÓN VISUAL",
            color = Azul,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.labelMedium
        )

        BarraTiempo(
            titulo = "Una por una",
            tiempo = resultado.secuencialMs,
            proporcion = resultado.secuencialMs / maximo,
            color = Naranja
        )

        BarraTiempo(
            titulo = "A la vez",
            tiempo = resultado.paraleloMs,
            proporcion = resultado.paraleloMs / maximo,
            color = Azul
        )
    }
}

@Composable
private fun BarraTiempo(
    titulo: String,
    tiempo: Long,
    proporcion: Float,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(titulo)
            Text(
                "$tiempo ms",
                fontWeight = FontWeight.SemiBold
            )
        }

        androidx.compose.material3.LinearProgressIndicator(
            progress = { proporcion.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp),
            color = color,
            trackColor = Color(0xFFE8EDF3)
        )
    }
}
