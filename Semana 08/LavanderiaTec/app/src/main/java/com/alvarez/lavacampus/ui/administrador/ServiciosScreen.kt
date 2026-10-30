package com.alvarez.lavacampus.ui.administrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.DryCleaning
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.Wash
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.viewmodel.ServiciosViewModel
import java.util.Locale

private val MoradoServicios = Color(0xFF6D28D9)

@Composable
fun ServiciosScreen(
    viewModel: ServiciosViewModel,
    onVolver: () -> Unit,
    onNuevoServicio: () -> Unit,
    onEditarServicio: (Int) -> Unit
) {
    val servicios by viewModel.servicios.collectAsStateWithLifecycle(
        initialValue = emptyList()
    )

    val mensaje by viewModel.mensaje.collectAsStateWithLifecycle()
    val cargando by viewModel.cargando.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensaje) {
        mensaje?.let { texto ->
            snackbarHostState.showSnackbar(texto)
            viewModel.limpiarMensaje()
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,

        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.background
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVolver) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Regresar",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Servicios y tarifas",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = onNuevoServicio,
                modifier = Modifier.size(58.dp),
                shape = RoundedCornerShape(16.dp),
                containerColor = MoradoServicios,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Agregar servicio",
                    modifier = Modifier.size(28.dp)
                )
            }
        },

        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }

    ) { paddingValues ->

        if (servicios.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Todavía no hay servicios registrados.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(
                    start = 18.dp,
                    end = 18.dp,
                    top = 12.dp,
                    bottom = 88.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = servicios,
                    key = { it.id }
                ) { servicio ->

                    TarjetaServicioAdmin(
                        servicio = servicio,
                        habilitado = !cargando,
                        onCambiarDisponibilidad = { activo ->
                            viewModel.cambiarDisponibilidad(
                                servicio = servicio,
                                activo = activo
                            )
                        },
                        onEditar = {
                            onEditarServicio(servicio.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaServicioAdmin(
    servicio: Servicio,
    habilitado: Boolean,
    onCambiarDisponibilidad: (Boolean) -> Unit,
    onEditar: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 13.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Ícono del servicio.
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = obtenerIconoServicio(servicio.icono),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(25.dp)
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Nombre, precio y tiempo de entrega.
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = servicio.nombre,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = construirDetalleServicio(servicio),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Activar o desactivar el servicio.
            Switch(
                checked = servicio.activo,
                onCheckedChange = onCambiarDisponibilidad,
                enabled = habilitado,
                modifier = Modifier
                    .width(45.dp)
                    .height(30.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White,
                    checkedTrackColor = MoradoServicios,
                    uncheckedThumbColor = Color.White,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    uncheckedBorderColor = MaterialTheme.colorScheme.outline
                )
            )

            // Editar servicio.
            IconButton(
                onClick = onEditar,
                enabled = habilitado,
                modifier = Modifier.size(35.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = "Editar ${servicio.nombre}",
                    tint = MoradoServicios,
                    modifier = Modifier.size(19.dp)
                )
            }
        }
    }
}

// Construye el texto que aparece debajo del nombre.
private fun construirDetalleServicio(servicio: Servicio): String {
    val precio = String.format(
        Locale.forLanguageTag("es-PE"),
        "%.2f",
        servicio.precio
    )

    val unidad = when (servicio.unidad.lowercase()) {
        "kilo" -> "kg"
        "prenda" -> "prenda"
        "unidad" -> "unidad"
        else -> servicio.unidad
    }

    return "S/ $precio por $unidad · ${servicio.horasEntrega} h"
}

// Selecciona el ícono según el valor almacenado en Room.
private fun obtenerIconoServicio(icono: String): ImageVector {
    return when (icono.lowercase()) {
        "lavadora", "lavado", "wash" ->
            Icons.Filled.LocalLaundryService

        "plancha", "planchado" ->
            Icons.Filled.DryCleaning

        "ropa", "camiseta", "prenda" ->
            Icons.Filled.Checkroom

        "edredon", "edredón", "frazada" ->
            Icons.Filled.Wash

        else ->
            Icons.Filled.LocalLaundryService
    }
}
