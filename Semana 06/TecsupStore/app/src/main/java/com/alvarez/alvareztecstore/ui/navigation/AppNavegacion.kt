package com.alvarez.alvareztecstore.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alvarez.alvareztecstore.Producto
import com.alvarez.alvareztecstore.ui.components.AppDrawer
import com.alvarez.alvareztecstore.ui.components.TarjetaProducto
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destinoSeleccionado by remember { mutableStateOf("Inicio") }

    val listaProductos = remember {
        listOf(
            Producto(1, "Audífonos", "S/ 89.00"),
            Producto(2, "Smartwatch", "S/ 199.00"),
            Producto(3, "Funda celular", "S/ 25.00")
        )
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                destinoSeleccionado = destinoSeleccionado,
                onDestinoSeleccionado = { nuevoDestino ->
                    destinoSeleccionado = nuevoDestino
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("TECSUP Store - $destinoSeleccionado") },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (destinoSeleccionado) {
                    "Inicio", "Mis pedidos" -> {
                        LazyColumn {
                            items(listaProductos) { producto ->
                                TarjetaProducto(producto = producto)
                            }
                        }
                    }
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Pantalla de $destinoSeleccionado")
                        }
                    }
                }
            }
        }
    }
}