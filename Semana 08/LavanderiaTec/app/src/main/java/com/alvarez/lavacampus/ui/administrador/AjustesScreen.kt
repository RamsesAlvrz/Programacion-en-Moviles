package com.alvarez.lavacampus.ui.administrador

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
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.viewmodel.PreferenciasViewModel

private val MoradoGuia = Color(0xFF6D28D9)
private val FondoGuia = Color(0xFFF9F8FD)
private val LilaGuia = Color(0xFFF5F2FF)
private val TextoGuia = Color(0xFF262638)
private val GrisGuia = Color(0xFF8B8D9B)
private val RojoGuia = Color(0xFFE4484D)

@Composable
fun AjustesScreen(
    usuario: Usuario,
    preferenciasViewModel: PreferenciasViewModel,
    onVolver: () -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val modoOscuro by preferenciasViewModel.modoOscuro.collectAsStateWithLifecycle()
    val notificaciones by preferenciasViewModel.notificaciones.collectAsStateWithLifecycle()
    val recibirPedidos by preferenciasViewModel.recibirPedidos.collectAsStateWithLifecycle()

    val fondo = if (modoOscuro) MaterialTheme.colorScheme.background else FondoGuia
    val superficie = if (modoOscuro) MaterialTheme.colorScheme.surface else Color.White
    val texto = if (modoOscuro) MaterialTheme.colorScheme.onSurface else TextoGuia
    val secundario = if (modoOscuro) MaterialTheme.colorScheme.onSurfaceVariant else GrisGuia

    Column(
        modifier = Modifier.fillMaxSize().background(fondo)
    ) {
        Box(modifier = Modifier.fillMaxWidth().height(48.dp)) {
            IconButton(onClick = onVolver, modifier = Modifier.align(Alignment.CenterStart)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = texto,
                    modifier = Modifier.size(19.dp)
                )
            }
            Text(
                text = "Ajustes",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = texto
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier.height(7.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(11.dp),
                color = superficie,
                shadowElevation = 1.dp,
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 11.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(42.dp).background(LilaGuia, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PersonOutline,
                            contentDescription = null,
                            tint = MoradoGuia,
                            modifier = Modifier.size(23.dp)
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = usuario.nombre,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = texto,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = usuario.correo,
                            fontSize = 9.sp,
                            color = secundario,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(Modifier.height(3.dp))
                        Text(
                            text = "Perfil activo",
                            fontSize = 9.sp,
                            color = MoradoGuia,
                            modifier = Modifier.background(LilaGuia, RoundedCornerShape(4.dp))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onCambiarPerfil),
                shape = RoundedCornerShape(9.dp),
                color = MoradoGuia
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 13.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.SwapHoriz,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Cambiar de perfil", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(Modifier.height(2.dp))
                        Text("Modo público, ir a vista Estudiante", fontSize = 9.sp, color = Color.White.copy(alpha = 0.85f))
                    }
                    Text("›", fontSize = 23.sp, color = Color.White)
                }
            }

            Spacer(Modifier.height(15.dp))
            Text("Preferencias", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = texto)
            Spacer(Modifier.height(7.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(11.dp),
                color = superficie,
                shadowElevation = 1.dp,
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Column {
                    PreferenciaCompacta(
                        titulo = "Modo oscuro",
                        descripcion = "Cambia el tema de la app",
                        activado = modoOscuro,
                        texto = texto,
                        secundario = secundario,
                        onCambiar = preferenciasViewModel::cambiarModoOscuro
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), thickness = 0.5.dp)
                    PreferenciaCompacta(
                        titulo = "Notificaciones",
                        descripcion = "Recibir avisos de nuevos pedidos",
                        activado = notificaciones,
                        texto = texto,
                        secundario = secundario,
                        onCambiar = preferenciasViewModel::cambiarNotificaciones
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 12.dp), thickness = 0.5.dp)
                    PreferenciaCompacta(
                        titulo = "Recibir pedidos",
                        descripcion = "Abrir o cerrar la recepción",
                        activado = recibirPedidos,
                        texto = texto,
                        secundario = secundario,
                        onCambiar = preferenciasViewModel::cambiarRecepcionPedidos
                    )
                }
            }

            Spacer(Modifier.height(13.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(9.dp),
                color = if (modoOscuro) MaterialTheme.colorScheme.surfaceVariant else LilaGuia
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)) {
                    Text("Se guardan en el dispositivo", fontSize = 9.sp, color = secundario)
                    Spacer(Modifier.height(3.dp))
                    Text("con DataStore", fontSize = 10.sp, fontWeight = FontWeight.SemiBold, color = MoradoGuia)
                }
            }

            Spacer(Modifier.height(17.dp))

            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, RojoGuia),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = superficie)
            ) {
                Text("Cerrar sesión", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = RojoGuia)
            }
            Spacer(Modifier.height(18.dp))
        }
    }
}

@Composable
private fun PreferenciaCompacta(
    titulo: String,
    descripcion: String,
    activado: Boolean,
    texto: Color,
    secundario: Color,
    onCambiar: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(59.dp).padding(horizontal = 11.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(titulo, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = texto)
            Spacer(Modifier.height(3.dp))
            Text(descripcion, fontSize = 9.sp, color = secundario, maxLines = 1)
        }
        Spacer(Modifier.width(5.dp))
        Switch(
            checked = activado,
            onCheckedChange = onCambiar,
            modifier = Modifier.scale(0.76f),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MoradoGuia,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFFD5D6DC),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}
