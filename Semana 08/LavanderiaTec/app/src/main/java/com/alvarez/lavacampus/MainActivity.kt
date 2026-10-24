package com.alvarez.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.PeopleOutline
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material.icons.outlined.Sell
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alvarez.lavacampus.ui.administrador.BandejaScreen
import com.alvarez.lavacampus.ui.administrador.PanelScreen
import com.alvarez.lavacampus.ui.administrador.GestionPedidoScreen
import com.alvarez.lavacampus.ui.estudiante.DetallePedidoScreen
import com.alvarez.lavacampus.ui.estudiante.InicioScreen
import com.alvarez.lavacampus.ui.estudiante.LoginScreen
import com.alvarez.lavacampus.ui.estudiante.MisPedidosScreen
import com.alvarez.lavacampus.ui.estudiante.NuevoPedidoScreen
import com.alvarez.lavacampus.ui.estudiante.TarifasScreen
import com.alvarez.lavacampus.ui.theme.LavaCampusTheme
import com.alvarez.lavacampus.viewmodel.BandejaViewModel
import com.alvarez.lavacampus.viewmodel.DetallePedidoViewModel
import com.alvarez.lavacampus.viewmodel.InicioViewModel
import com.alvarez.lavacampus.viewmodel.LavaViewModelFactory
import com.alvarez.lavacampus.viewmodel.MisPedidosViewModel
import com.alvarez.lavacampus.viewmodel.NuevoPedidoViewModel
import com.alvarez.lavacampus.viewmodel.PanelViewModel
import com.alvarez.lavacampus.viewmodel.SesionViewModel
import com.alvarez.lavacampus.viewmodel.GestionPedidoViewModel

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

            // COMMIT 10: Detalle y cancelación del pedido.
            val detallePedidoViewModel: DetallePedidoViewModel = viewModel(
                factory = factory
            )

            // COMMIT 11: Pedidos activos e historial.
            val misPedidosViewModel: MisPedidosViewModel = viewModel(
                factory = factory
            )

            // COMMIT 12: Indicadores y gráficos del administrador.
            val panelViewModel: PanelViewModel = viewModel(
                factory = factory
            )

            // COMMIT 13: Bandeja de pedidos del administrador.
            val bandejaViewModel: BandejaViewModel = viewModel(
                factory = factory
            )

            // COMMIT 13: Gestión de estados, casilleros y cobros.
            val gestionPedidoViewModel: GestionPedidoViewModel = viewModel(
                factory = factory
            )

            val usuarioActual by sesionViewModel.usuarioActual
                .collectAsStateWithLifecycle()

            var pantallaEstudiante by rememberSaveable {
                mutableStateOf("INICIO")
            }

            var pedidoSeleccionadoId by rememberSaveable {
                mutableIntStateOf(0)
            }

            // Permite volver a Inicio o Mis pedidos desde el detalle.
            var pantallaOrigenDetalle by rememberSaveable {
                mutableStateOf("INICIO")
            }

            // Permite volver a la pantalla anterior tras crear un pedido.
            var pantallaOrigenNuevoPedido by rememberSaveable {
                mutableStateOf("INICIO")
            }

            // COMMIT 12: navegación del administrador.
            var pantallaAdministrador by rememberSaveable {
                mutableStateOf("PANEL")
            }

            // Pedido que el administrador selecciona desde la bandeja.
            var pedidoAdministradorId by rememberSaveable {
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
                pantallaOrigenDetalle = "INICIO"
                pantallaOrigenNuevoPedido = "INICIO"
                pantallaAdministrador = "PANEL"
                pedidoAdministradorId = 0
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
                                        pantallaEstudiante =
                                            pantallaOrigenNuevoPedido
                                    },
                                    onPedidoRegistrado = {
                                        pantallaEstudiante = "PEDIDOS"
                                    }
                                )
                            }

                            "DETALLE_PEDIDO" -> {
                                if (pedidoSeleccionadoId > 0) {

                                    DetallePedidoScreen(
                                        pedidoId = pedidoSeleccionadoId,
                                        viewModel = detallePedidoViewModel,
                                        onVolver = {
                                            pantallaEstudiante =
                                                pantallaOrigenDetalle
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
                                                    selected =
                                                        pantallaEstudiante == ruta,

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

                                                    colors =
                                                        NavigationBarItemDefaults.colors(
                                                            selectedIconColor =
                                                                ColorNavegacion,
                                                            selectedTextColor =
                                                                ColorNavegacion,
                                                            indicatorColor =
                                                                Color(0xFFD8F7FB),
                                                            unselectedIconColor =
                                                                Color.Gray,
                                                            unselectedTextColor =
                                                                Color.Gray
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
                                                        pantallaOrigenNuevoPedido =
                                                            "INICIO"
                                                        pantallaEstudiante =
                                                            "NUEVO_PEDIDO"
                                                    },

                                                    onVerDetalle = { pedidoId ->
                                                        pedidoSeleccionadoId =
                                                            pedidoId
                                                        pantallaOrigenDetalle =
                                                            "INICIO"
                                                        pantallaEstudiante =
                                                            "DETALLE_PEDIDO"
                                                    },

                                                    onMisPedidos = {
                                                        pantallaEstudiante =
                                                            "PEDIDOS"
                                                    }
                                                )
                                            }

                                            // COMMIT 11: Activos e Historial.
                                            "PEDIDOS" -> {

                                                MisPedidosScreen(
                                                    viewModel =
                                                        misPedidosViewModel,
                                                    usuarioId = estudianteId,

                                                    onVolver = {
                                                        pantallaEstudiante =
                                                            "INICIO"
                                                    },

                                                    onNuevoPedido = {
                                                        pantallaOrigenNuevoPedido =
                                                            "PEDIDOS"
                                                        pantallaEstudiante =
                                                            "NUEVO_PEDIDO"
                                                    },

                                                    onVerDetalle = { pedidoId ->
                                                        pedidoSeleccionadoId =
                                                            pedidoId
                                                        pantallaOrigenDetalle =
                                                            "PEDIDOS"
                                                        pantallaEstudiante =
                                                            "DETALLE_PEDIDO"
                                                    }
                                                )
                                            }

                                            // COMMIT 11: Tarifas vigentes.
                                            "TARIFAS" -> {

                                                TarifasScreen(
                                                    repository =
                                                        app.repository,

                                                    onVolver = {
                                                        pantallaEstudiante =
                                                            "INICIO"
                                                    }
                                                )
                                            }

                                            "PERFIL" -> {

                                                PantallaPendiente(
                                                    titulo = "Mi perfil",
                                                    descripcion =
                                                        "Aquí podrás consultar tus datos personales."
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    usuarioActual?.rol == "ADMIN" -> {

                        Scaffold(
                            containerColor = Color(0xFFF5F8FC),
                            topBar = {
                                if (pantallaAdministrador != "GESTION_PEDIDO") {
                                    Surface(
                                        color = Color.White,
                                        shadowElevation = 1.dp
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(48.dp)
                                                .padding(horizontal = 14.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "LavaCampus · Admin",
                                                modifier = Modifier.weight(1f),
                                                style = MaterialTheme.typography.labelLarge
                                            )
                                            TextButton(
                                                onClick = {
                                                    sesionViewModel.cerrarSesion {
                                                        pantallaAdministrador = "PANEL"
                                                        pantallaEstudiante = "INICIO"
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Logout,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(Modifier.width(4.dp))
                                                Text("Salir")
                                            }
                                        }
                                    }
                                }
                            },
                            bottomBar = {
                                if (pantallaAdministrador != "GESTION_PEDIDO") {
                                    NavigationBar(
                                        containerColor = Color.White,
                                        tonalElevation = 2.dp
                                    ) {
                                        val opcionesAdmin = listOf(
                                            Triple("PANEL", "Panel", Icons.Filled.BarChart),
                                            Triple("PEDIDOS", "Pedidos", Icons.Outlined.ReceiptLong),
                                            Triple("SERVICIOS", "Servicios", Icons.Outlined.Sell),
                                            Triple("CLIENTES", "Clientes", Icons.Outlined.PeopleOutline),
                                            Triple("AJUSTES", "Ajustes", Icons.Filled.Settings)
                                        )

                                        opcionesAdmin.forEach { (ruta, nombre, icono) ->
                                            NavigationBarItem(
                                                selected = pantallaAdministrador == ruta ||
                                                        (ruta == "PEDIDOS" && pantallaAdministrador == "GESTION_PEDIDO"),
                                                onClick = { pantallaAdministrador = ruta },
                                                icon = {
                                                    Icon(
                                                        imageVector = icono,
                                                        contentDescription = nombre,
                                                        modifier = Modifier.size(21.dp)
                                                    )
                                                },
                                                label = {
                                                    Text(text = nombre, fontSize = 10.sp)
                                                },
                                                alwaysShowLabel = true,
                                                colors = NavigationBarItemDefaults.colors(
                                                    selectedIconColor = Color(0xFF6D28D9),
                                                    selectedTextColor = Color(0xFF6D28D9),
                                                    indicatorColor = Color.Transparent,
                                                    unselectedIconColor = Color(0xFFADB5C2),
                                                    unselectedTextColor = Color(0xFFADB5C2)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        ) { paddingValues ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(paddingValues)
                            ) {
                                when (pantallaAdministrador) {
                                    "PANEL" -> PanelScreen(
                                        viewModel = panelViewModel,
                                        onVerPedidosListos = {
                                            pantallaAdministrador = "PEDIDOS"
                                        }
                                    )
                                    "PEDIDOS" -> BandejaScreen(
                                        viewModel = bandejaViewModel,
                                        onVolver = {
                                            pantallaAdministrador = "PANEL"
                                        },
                                        onGestionarPedido = { pedidoId ->
                                            pedidoAdministradorId = pedidoId
                                            pantallaAdministrador = "GESTION_PEDIDO"
                                        }
                                    )

                                    "GESTION_PEDIDO" -> {
                                        if (pedidoAdministradorId > 0) {
                                            GestionPedidoScreen(
                                                pedidoId = pedidoAdministradorId,
                                                viewModel = gestionPedidoViewModel,
                                                onVolver = {
                                                    pantallaAdministrador = "PEDIDOS"
                                                }
                                            )
                                        } else {
                                            LaunchedEffect(Unit) {
                                                pantallaAdministrador = "PEDIDOS"
                                            }
                                        }
                                    }

                                    "SERVICIOS" -> PantallaPendiente(
                                        titulo = "Servicios",
                                        descripcion = "Gestión de servicios y tarifas."
                                    )
                                    "CLIENTES" -> PantallaPendiente(
                                        titulo = "Clientes",
                                        descripcion = "Listado de clientes registrados."
                                    )
                                    "AJUSTES" -> PantallaPendiente(
                                        titulo = "Ajustes",
                                        descripcion = "Configuración del administrador."
                                    )
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
