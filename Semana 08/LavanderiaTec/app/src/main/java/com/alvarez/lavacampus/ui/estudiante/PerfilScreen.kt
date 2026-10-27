package com.alvarez.lavacampus.ui.estudiante

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.viewmodel.PerfilViewModel
import com.alvarez.lavacampus.viewmodel.PreferenciasViewModel
import java.util.Locale

private val Turquesa = Color(0xFF0895B5)
private val FondoPerfil = Color(0xFFF0FBFD)
private val CelesteTarjeta = Color(0xFFD5F8FC)
private val TextoPrincipal = Color(0xFF263341)
private val TextoSecundario = Color(0xFF88949F)

@Composable
fun PerfilScreen(
    usuario: Usuario,
    perfilViewModel: PerfilViewModel,
    preferenciasViewModel: PreferenciasViewModel,
    onVolver: () -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val pedidosActivos by perfilViewModel.pedidosActivos
        .collectAsStateWithLifecycle()

    val totalPedidos by perfilViewModel.totalPedidos
        .collectAsStateWithLifecycle()

    val kilosLavados by perfilViewModel.kilosLavados
        .collectAsStateWithLifecycle()

    val modoOscuro by preferenciasViewModel.modoOscuro
        .collectAsStateWithLifecycle()

    val notificaciones by preferenciasViewModel.notificaciones
        .collectAsStateWithLifecycle()

    LaunchedEffect(usuario.id) {
        perfilViewModel.cargarPerfil(usuario.id)
    }

    val oscuro = modoOscuro

    val fondo = if (oscuro) {
        MaterialTheme.colorScheme.background
    } else {
        FondoPerfil
    }

    val superficie = MaterialTheme.colorScheme.surface
    val texto = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(fondo)
    ) {

        // =====================================
        // ENCABEZADO
        // =====================================

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            IconButton(
                onClick = onVolver,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = texto
                )
            }

            Text(
                text = "Perfil",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = texto
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            // =====================================
            // TARJETA DEL ESTUDIANTE
            // =====================================

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = superficie,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                color = CelesteTarjeta,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PersonOutline,
                            contentDescription = null,
                            tint = Turquesa,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = usuario.nombre,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = texto,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = usuario.correo,
                            fontSize = 11.sp,
                            color = TextoSecundario,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = "Estudiante · ${usuario.codigo}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Turquesa
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // =====================================
            // TRES ESTADÍSTICAS
            // =====================================

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {

                EstadisticaPerfil(
                    valor = pedidosActivos.toString(),
                    etiqueta = "Pedidos activos",
                    modifier = Modifier.weight(1f)
                )

                EstadisticaPerfil(
                    valor = totalPedidos.toString(),
                    etiqueta = "Pedidos totales",
                    modifier = Modifier.weight(1f)
                )

                EstadisticaPerfil(
                    valor = String.format(
                        Locale.US,
                        "%.1f kg",
                        kilosLavados
                    ),
                    etiqueta = "Lavados",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(13.dp))

            // =====================================
            // CAMBIAR DE PERFIL
            // =====================================

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onCambiarPerfil),
                shape = RoundedCornerShape(11.dp),
                color = Turquesa
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Filled.SwapHoriz,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Cambiar de perfil",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "Modo público, ir a vista Administrador",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 10.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Preferencias",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = texto
            )

            Spacer(modifier = Modifier.height(9.dp))

            // =====================================
            // PREFERENCIAS
            // =====================================

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = superficie,
                shadowElevation = 2.dp
            ) {
                Column {

                    FilaPreferenciaPerfil(
                        titulo = "Modo oscuro",
                        descripcion = "Cambia el tema de la app",
                        activado = modoOscuro,
                        onCambiar = preferenciasViewModel::cambiarModoOscuro
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = Color(0xFFE9EDF1)
                    )

                    FilaPreferenciaPerfil(
                        titulo = "Avisos de pedido listo",
                        descripcion = "Notificar cuando tu pedido esté listo",
                        activado = notificaciones,
                        onCambiar = preferenciasViewModel::cambiarNotificaciones
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // =====================================
            // INFORMACIÓN DE DATASTORE
            // =====================================

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = if (oscuro) {
                    MaterialTheme.colorScheme.surfaceVariant
                } else {
                    Color(0xFFEAFBFD)
                }
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Se guardan en el dispositivo",
                        fontSize = 11.sp,
                        color = if (oscuro) texto else TextoSecundario
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "con DataStore",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Turquesa
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // =====================================
            // CERRAR SESIÓN
            // =====================================

            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFFEF4444)
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = superficie
                )
            ) {
                Text(
                    text = "Cerrar sesión",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFEF4444)
                )
            }

            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}

@Composable
private fun EstadisticaPerfil(
    valor: String,
    etiqueta: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(72.dp),
        shape = RoundedCornerShape(11.dp),
        color = CelesteTarjeta
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = valor,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Turquesa,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = etiqueta,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                color = Turquesa,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FilaPreferenciaPerfil(
    titulo: String,
    descripcion: String,
    activado: Boolean,
    onCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .padding(horizontal = 12.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = titulo,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = descripcion,
                fontSize = 10.sp,
                color = TextoSecundario
            )
        }

        Switch(
            checked = activado,
            onCheckedChange = onCambiar,
            modifier = Modifier.height(30.dp),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Turquesa,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFD4D8DE),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}