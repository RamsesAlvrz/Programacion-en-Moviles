package com.tecsup.smartcatalog.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.smartcatalog.ui.theme.*

@Composable
fun AdminScreen(
    viewModel: AdminViewModel,
    onCerrarSesion: () -> Unit
) {
    val estado by viewModel.uiState.collectAsStateWithLifecycle()

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

        item {
            Text(
                text = "Resumen general",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextoPremium
            )
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
                            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AdminStatCard(
                                titulo = "Productos",
                                valor = actual.stats.totalProductos.toString(),
                                icono = Icons.Default.Smartphone,
                                modifier = Modifier.weight(1f)
                            )

                            AdminStatCard(
                                titulo = "Usuarios",
                                valor = actual.stats.totalUsuarios.toString(),
                                icono = Icons.Default.People,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AdminStatCard(
                                titulo = "Mis favoritos",
                                valor = actual.stats.totalFavoritos.toString(),
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

                // SECCIÓN USUARIOS
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Usuarios registrados",
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

                if (actual.usuarios.isEmpty()) {
                    item {
                        Text(
                            text = "Todavía no hay usuarios registrados.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoPremiumSecundario
                        )
                    }
                } else {
                    items(
                        items = actual.usuarios,
                        key = { it.uid }
                    ) { usuario ->
                        UsuarioCard(usuario)
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
                border = androidx.compose.foundation.BorderStroke(
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
            verticalArrangement = Arrangement.SpaceBetween
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
fun UsuarioCard(usuario: UsuarioAdmin) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BordePremium,
                shape = RoundedCornerShape(18.dp)
            ),
        shape = RoundedCornerShape(18.dp),
        color = SuperficiePremium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
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
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = usuario.nombre,
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
        }
    }
}
