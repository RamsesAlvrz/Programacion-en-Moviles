
package com.tecsup.smartcatalog.ui.admin

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.smartcatalog.ui.theme.*

@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

    var busqueda by remember { mutableStateOf("") }
    var filtroRol by remember { mutableStateOf("TODOS") }
    var usuarioSeleccionado by remember {
        mutableStateOf<UsuarioAdmin?>(null)
    }

    val usuariosFiltrados = when (val actual = estado) {
        is AdminUiState.Success -> {
            actual.usuarios.filter { usuario ->
                val coincideBusqueda =
                    usuario.nombre.contains(
                        busqueda.trim(),
                        ignoreCase = true
                    ) ||
                            usuario.correo.contains(
                                busqueda.trim(),
                                ignoreCase = true
                            )

                val coincideRol =
                    filtroRol == "TODOS" ||
                            usuario.rol.equals(
                                filtroRol,
                                ignoreCase = true
                            )

                coincideBusqueda && coincideRol
            }
        }

        else -> emptyList()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPremium),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 18.dp,
            bottom = 28.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        // ENCABEZADO
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = AzulElectrico
                ) {
                    Icon(
                        imageVector = Icons.Default.Dashboard,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .padding(12.dp)
                            .size(25.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Panel de control",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextoPremium
                    )

                    Text(
                        text = "Administración de SmartCatalog",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoPremiumSecundario
                    )
                }

                IconButton(
                    onClick = onCerrarSesion
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Cerrar sesión",
                        tint = AzulBrillante
                    )
                }
            }
        }

        // BIENVENIDA
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFF172D59)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "SMARTCATALOG 2.0",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AzulBrillante
                    )

                    Text(
                        text = "¡Bienvenido, administrador!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextoPremium
                    )

                    Text(
                        text = "Supervisa los productos, usuarios y favoritos de tu aplicación desde un solo lugar.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoPremiumSecundario
                    )
                }
            }
        }

        // RESUMEN Y BOTÓN ACTUALIZAR
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Resumen general",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextoPremium
                )

                IconButton(
                    onClick = { viewModel.cargarDatos() },
                    enabled = estado !is AdminUiState.Loading
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar datos",
                        tint = AzulBrillante
                    )
                }
            }
        }

        when (val actual = estado) {

            is AdminUiState.Loading -> {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = AzulBrillante
                        )
                    }
                }
            }

            is AdminUiState.Error -> {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        color = SuperficiePremium
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement =
                                Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = actual.mensaje,
                                color = Color(0xFFFCA5A5)
                            )

                            Button(
                                onClick = {
                                    viewModel.cargarDatos()
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = AzulElectrico
                                )
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }

            is AdminUiState.Success -> {

                // TARJETAS ESTADÍSTICAS
                item {
                    Column(
                        verticalArrangement =
                            Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {
                            AdminStatCard(
                                titulo = "Productos",
                                valor = actual.stats
                                    .totalProductos.toString(),
                                icono = Icons.Default.Smartphone,
                                modifier = Modifier.weight(1f)
                            )

                            AdminStatCard(
                                titulo = "Usuarios",
                                valor = actual.stats
                                    .totalUsuarios.toString(),
                                icono = Icons.Default.People,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            horizontalArrangement =
                                Arrangement.spacedBy(12.dp)
                        ) {
                            AdminStatCard(
                                titulo = "Mis favoritos",
                                valor = actual.stats
                                    .totalFavoritos.toString(),
                                icono = Icons.Default.Favorite,
                                modifier = Modifier.weight(1f)
                            )

                            AdminStatCard(
                                titulo = "Panel",
                                valor = "Activo",
                                icono = Icons.Default.Dashboard,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // ENCABEZADO DE GESTIÓN DE USUARIOS
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Gestión de usuarios",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextoPremium
                        )

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF172D59)
                        ) {
                            Text(
                                text = "${actual.usuarios.size}",
                                color = AzulBrillante,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                            )
                        }
                    }
                }

                // BUSCADOR
                item {
                    OutlinedTextField(
                        value = busqueda,
                        onValueChange = { busqueda = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Buscar por nombre o correo")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null
                            )
                        },
                        trailingIcon = {
                            if (busqueda.isNotEmpty()) {
                                IconButton(
                                    onClick = { busqueda = "" }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription =
                                            "Limpiar búsqueda"
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextoPremium,
                            unfocusedTextColor = TextoPremium,
                            focusedContainerColor = SuperficiePremium,
                            unfocusedContainerColor = SuperficiePremium,
                            focusedBorderColor = AzulBrillante,
                            unfocusedBorderColor = BordePremium,
                            focusedLeadingIconColor = AzulBrillante,
                            unfocusedLeadingIconColor = AzulBrillante,
                            focusedTrailingIconColor = AzulBrillante,
                            unfocusedTrailingIconColor = AzulBrillante,
                            focusedPlaceholderColor =
                                TextoPremiumSecundario,
                            unfocusedPlaceholderColor =
                                TextoPremiumSecundario,
                            cursorColor = AzulBrillante
                        )
                    )
                }

                // FILTROS POR ROL
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "TODOS" to "Todos",
                            "ADMIN" to "Admin",
                            "USUARIO" to "Usuario"
                        ).forEach { (valor, etiqueta) ->

                            val seleccionado = filtroRol == valor

                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        filtroRol = valor
                                    },
                                shape = RoundedCornerShape(12.dp),
                                color = if (seleccionado) {
                                    AzulElectrico
                                } else {
                                    SuperficiePremium
                                },
                                border = BorderStroke(
                                    1.dp,
                                    if (seleccionado) {
                                        AzulBrillante
                                    } else {
                                        BordePremium
                                    }
                                )
                            ) {
                                Text(
                                    text = etiqueta,
                                    modifier = Modifier.padding(
                                        vertical = 11.dp,
                                        horizontal = 4.dp
                                    ),
                                    textAlign =
                                        androidx.compose.ui.text.style.TextAlign.Center,
                                    color = if (seleccionado) {
                                        Color.White
                                    } else {
                                        TextoPremiumSecundario
                                    },
                                    style =
                                        MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }

                // RESULTADOS DE BÚSQUEDA
                item {
                    Text(
                        text = "${usuariosFiltrados.size} usuario(s) encontrado(s)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextoPremiumSecundario
                    )
                }

                if (usuariosFiltrados.isEmpty()) {
                    item {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            color = SuperficiePremium,
                            border = BorderStroke(
                                1.dp,
                                BordePremium
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment =
                                    Alignment.CenterHorizontally,
                                verticalArrangement =
                                    Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = AzulBrillante,
                                    modifier = Modifier.size(30.dp)
                                )

                                Text(
                                    text = "No se encontraron usuarios",
                                    color = TextoPremium,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "Prueba con otro nombre, correo o filtro.",
                                    color = TextoPremiumSecundario,
                                    style =
                                        MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                } else {
                    items(
                        items = usuariosFiltrados,
                        key = { it.uid }
                    ) { usuario ->
                        UsuarioCard(
                            usuario = usuario,
                            onClick = {
                                usuarioSeleccionado = usuario
                            }
                        )
                    }
                }
            }
        }

        // CERRAR SESIÓN
        item {
            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(
                    1.dp,
                    BordePremium
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = AzulBrillante
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = null
                )

                Spacer(Modifier.width(10.dp))

                Text(
                    text = "Cerrar sesión",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    // VENTANA DE DETALLES DEL USUARIO
    usuarioSeleccionado?.let { usuario ->
        AlertDialog(
            onDismissRequest = {
                usuarioSeleccionado = null
            },
            containerColor = SuperficiePremium,
            shape = RoundedCornerShape(22.dp),
            title = {
                Text(
                    text = "Detalles del usuario",
                    color = TextoPremium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement =
                        Arrangement.spacedBy(14.dp)
                ) {
                    DetalleUsuarioCampo(
                        etiqueta = "Nombre",
                        valor = usuario.nombre.ifBlank {
                            "Sin nombre"
                        }
                    )

                    DetalleUsuarioCampo(
                        etiqueta = "Correo electrónico",
                        valor = usuario.correo.ifBlank {
                            "Sin correo"
                        }
                    )

                    DetalleUsuarioCampo(
                        etiqueta = "Rol",
                        valor = usuario.rol.uppercase()
                    )

                    DetalleUsuarioCampo(
                        etiqueta = "UID de Firebase",
                        valor = usuario.uid
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        usuarioSeleccionado = null
                    }
                ) {
                    Text(
                        text = "Cerrar",
                        color = AzulBrillante
                    )
                }
            }
        )
    }
}

@Composable
private fun AdminStatCard(
    titulo: String,
    valor: String,
    icono: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(142.dp)
            .border(
                width = 1.dp,
                color = BordePremium,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        color = SuperficiePremium
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement =
                Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = AzulBrillante,
                modifier = Modifier.size(26.dp)
            )

            Text(
                text = valor,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextoPremium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = titulo,
                style = MaterialTheme.typography.bodySmall,
                color = TextoPremiumSecundario,
                maxLines = 1
            )
        }
    }
}

@Composable
fun UsuarioCard(
    usuario: UsuarioAdmin,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = SuperficiePremium,
        border = BorderStroke(
            1.dp,
            BordePremium
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement =
                Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = Color(0xFF243653)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = AzulBrillante
                    )
                }
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                    Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = usuario.nombre.ifBlank {
                        "Sin nombre"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoPremium
                )

                Text(
                    text = usuario.correo,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextoPremiumSecundario,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF172D59)
                ) {
                    Text(
                        text = usuario.rol.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = AzulBrillante,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 5.dp
                        )
                    )
                }
            }

            Text(
                text = "Ver",
                color = AzulBrillante,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DetalleUsuarioCampo(
    etiqueta: String,
    valor: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = etiqueta,
            color = AzulBrillante,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = valor,
            color = TextoPremium,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
