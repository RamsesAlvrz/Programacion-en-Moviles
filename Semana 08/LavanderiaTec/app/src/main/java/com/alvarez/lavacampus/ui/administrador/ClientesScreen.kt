package com.alvarez.lavacampus.ui.administrador

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.lavacampus.data.model.ClienteResumen
import java.util.Locale

private val MoradoClientes = Color(0xFF7629DB)

@Composable
fun ClientesScreen(
    viewModel: ClientesViewModel,
    onVolver: () -> Unit = {}
) {
    val clientes by viewModel.clientes.collectAsStateWithLifecycle()
    val busqueda by viewModel.busqueda.collectAsStateWithLifecycle()
    val colores = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
        ) {
            IconButton(
                onClick = onVolver,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = colores.onBackground,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = "Clientes",
                modifier = Modifier.align(Alignment.Center),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colores.onBackground
            )
        }

        Spacer(Modifier.height(8.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(43.dp),
            shape = RoundedCornerShape(11.dp),
            color = colores.surface,
            border = BorderStroke(1.dp, colores.outlineVariant),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Buscar",
                    tint = colores.onSurfaceVariant,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = busqueda,
                    onValueChange = viewModel::actualizarBusqueda,
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 12.sp, color = colores.onSurface),
                    cursorBrush = SolidColor(MoradoClientes),
                    decorationBox = { campo ->
                        Box {
                            if (busqueda.isEmpty()) {
                                Text(
                                    text = "Buscar estudiante",
                                    fontSize = 12.sp,
                                    color = colores.onSurfaceVariant
                                )
                            }
                            campo()
                        }
                    }
                )
                if (busqueda.isNotEmpty()) {
                    IconButton(
                        onClick = viewModel::limpiarBusqueda,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Limpiar búsqueda",
                            tint = colores.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(13.dp))

        if (clientes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.People,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = colores.onSurfaceVariant
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = if (busqueda.isBlank())
                            "Todavía no hay clientes registrados"
                        else
                            "No se encontraron clientes",
                        fontSize = 13.sp,
                        color = colores.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 18.dp
                ),
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                items(items = clientes, key = { it.id }) { cliente ->
                    TarjetaCliente(cliente)
                }
            }
        }
    }
}

@Composable
private fun TarjetaCliente(cliente: ClienteResumen) {
    val colores = MaterialTheme.colorScheme
    val oscuro = colores.background.luminance() < 0.5f
    val moradoLegible = if (oscuro) Color(0xFFCAA7FF) else MoradoClientes

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = colores.surface,
        border = BorderStroke(1.dp, colores.outlineVariant),
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 79.dp)
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(43.dp)
                    .background(
                        if (oscuro) Color(0xFF382651) else Color(0xFFF0EAFF),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = obtenerIniciales(cliente.nombre),
                    color = moradoLegible,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = cliente.nombre,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = cliente.codigo,
                    fontSize = 10.sp,
                    color = colores.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${cliente.cantidadPedidos} pedidos",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = moradoLegible
                )
            }

            Spacer(Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = colores.onSurfaceVariant,
                    modifier = Modifier.size(17.dp)
                )
                Text(
                    text = String.format(Locale.US, "S/ %.2f", cliente.totalGastado),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = colores.onSurface
                )
            }
        }
    }
}

private fun obtenerIniciales(nombre: String): String {
    return nombre.trim()
        .split(Regex("\\s+"))
        .filter { it.isNotBlank() }
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "?" }
}
