
package com.alvarez.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.LocalOffer
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alvarez.lavacampus.ui.estudiante.DetallePedidoScreen
import com.alvarez.lavacampus.ui.estudiante.InicioScreen
import com.alvarez.lavacampus.ui.estudiante.LoginScreen
import com.alvarez.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.alvarez.lavacampus.ui.theme.LavaCampusTheme
import com.alvarez.lavacampus.viewmodel.DetallePedidoViewModel
import com.alvarez.lavacampus.viewmodel.InicioViewModel
import com.alvarez.lavacampus.viewmodel.LavaViewModelFactory
import com.alvarez.lavacampus.viewmodel.NuevoPedidoViewModel
import com.alvarez.lavacampus.viewmodel.SesionViewModel

private val ColorNavegacion = Color(0xFF0895B5)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as LavaApp

        val factory = LavaViewModelFactory(
            repository = app.repository,
            preferenciasStore = app.preferenciasStore,
            database = app.database
        )

        setContent {
            val sesionViewModel: SesionViewModel = viewModel(
                factory = factory
            )

            val inicioViewModel: InicioViewModel = viewModel(
                factory = factory
            )

            val nuevoPedidoViewModel: NuevoPedidoViewModel = viewModel(
                factory = factory
            )

            // COMMIT 10: ViewModel del detalle del pedido
            val detallePedidoViewModel: DetallePedidoViewModel = viewModel(
                factory = factory
            )

            val usuarioActual by sesionViewModel.usuarioActual
                .collectAsStateWithLifecycle()

            var pantallaEstudiante by rememberSaveable {
                mutableStateOf("INICIO")
            }

            // Guarda el identificador del pedido seleccionado.
            var pedidoSeleccionadoId by rememberSaveable {
                mutableIntStateOf(0)
            }

            var sesionVerificada by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(500)
                sesionVerificada = true
            }

            LaunchedEffect(usuarioActual?.id) {
                pantallaEstudiante = "INICIO"
                pedidoSeleccionadoId = 0
            }

            LavaCampusTheme {
                when {
                    !sesionVerificada -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    usuarioActual == null -> {
                        LoginScreen(
                            viewModel = sesionViewModel,
                            onLoginExitoso = {}
                        )
                    }

                    usuarioActual?.rol == "CLIENTE" -> {
                        val estudianteId = usuarioActual!!.id

                        when (pantallaEstudiante) {

                            "NUEVO_PEDIDO" -> {
                                NuevoPedidoScreen(
                                    viewModel = nuevoPedidoViewModel,
                                    usuarioId = estudianteId,
                                    onVolver = {
                                        pantallaEstudiante = "INICIO"
                                    },
                                    onPedidoRegistrado = {
                                        pantallaEstudiante = "INICIO"
                                    }
                                )
                            }

                            // COMMIT 10: Detalle del pedido seleccionado.
                            "DETALLE_PEDIDO" -> {
                                if (pedidoSeleccionadoId > 0) {
                                    DetallePedidoScreen(
                                        pedidoId = pedidoSeleccionadoId,
                                        viewModel = detallePedidoViewModel,
                                        onVolver = {
                                            pantallaEstudiante = "INICIO"
                                        }
                                    )
                                } else {
                                    LaunchedEffect(Unit) {
                                        pantallaEstudiante = "INICIO"
                                    }
                                }
                            }

                            else -> {
                                Scaffold(
                                    containerColor = Color(0xFFF7FAFC),
                                    bottomBar = {
                                        NavigationBar(
                                            containerColor = Color.White,
                                            tonalElevation = 3.dp
                                        ) {
                                            val opciones = listOf(
                                                Triple(
                                                    "INICIO",
                                                    "Inicio",
                                                    Icons.Filled.Home
                                                ),
                                                Triple(
                                                    "PEDIDOS",
                                                    "Pedidos",
                                                    Icons.Outlined.List
                                                ),
                                                Triple(
                                                    "TARIFAS",
                                                    "Tarifas",
                                                    Icons.Outlined.LocalOffer
                                                ),
                                                Triple(
                                                    "PERFIL",
                                                    "Perfil",
                                                    Icons.Outlined.PersonOutline
                                                )
                                            )

                                            opciones.forEach { (ruta, nombre, icono) ->
                                                NavigationBarItem(
                                                    selected = pantallaEstudiante == ruta,
                                                    onClick = {
                                                        pantallaEstudiante = ruta
                                                    },
                                                    icon = {
                                                        Icon(
                                                            imageVector = icono,
                                                            contentDescription = nombre
                                                        )
                                                    },
                                                    label = {
                                                        Text(nombre)
                                                    },
                                                    alwaysShowLabel = true,
                                                    colors = NavigationBarItemDefaults.colors(
                                                        selectedIconColor = ColorNavegacion,
                                                        selectedTextColor = ColorNavegacion,
                                                        indicatorColor = Color(0xFFD8F7FB),
                                                        unselectedIconColor = Color.Gray,
                                                        unselectedTextColor = Color.Gray
                                                    )
                                                )
                                            }
                                        }
                                    }
                                ) { paddingValues ->
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(paddingValues)
                                    ) {
                                        when (pantallaEstudiante) {

                                            "INICIO" -> {
                                                InicioScreen(
                                                    viewModel = inicioViewModel,
                                                    usuarioId = estudianteId,
                                                    onNuevoPedido = {
                                                        pantallaEstudiante = "NUEVO_PEDIDO"
                                                    },

                                                    // Abre el detalle del pedido pulsado.
                                                    onVerDetalle = { pedidoId ->
                                                        pedidoSeleccionadoId = pedidoId
                                                        pantallaEstudiante = "DETALLE_PEDIDO"
                                                    },

                                                    onMisPedidos = {
                                                        pantallaEstudiante = "PEDIDOS"
                                                    }
                                                )
                                            }

                                            "PEDIDOS" -> {
                                                PantallaPendiente(
                                                    titulo = "Mis pedidos",
                                                    descripcion = "Aquí podrás consultar todos tus pedidos."
                                                )
                                            }

                                            "TARIFAS" -> {
                                                PantallaPendiente(
                                                    titulo = "Tarifas",
                                                    descripcion = "Aquí podrás consultar los servicios y sus precios."
                                                )
                                            }

                                            "PERFIL" -> {
                                                PantallaPendiente(
                                                    titulo = "Mi perfil",
                                                    descripcion = "Aquí podrás consultar tus datos personales."
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    usuarioActual?.rol == "ADMIN" -> {
                        Scaffold { paddingValues ->
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Panel de administrador",
                                    style = MaterialTheme.typography.headlineSmall
                                )

                                Spacer(Modifier.height(12.dp))

                                Text(
                                    text = "Bienvenido, ${usuarioActual?.nombre}"
                                )

                                Spacer(Modifier.height(24.dp))

                                Button(
                                    onClick = {
                                        sesionViewModel.cerrarSesion {
                                            pantallaEstudiante = "INICIO"
                                        }
                                    }
                                ) {
                                    Text("Cerrar sesión")
                                }
                            }
                        }
                    }

                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Rol de usuario no reconocido")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PantallaPendiente(
    titulo: String,
    descripcion: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = titulo,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
