package com.alvarez.alvareztecstore.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alvarez.alvareztecstore.Producto
import com.alvarez.alvareztecstore.ui.components.AppDrawer
import com.alvarez.alvareztecstore.ui.components.TarjetaProducto
import kotlinx.coroutines.launch

@Composable
fun AppNavegacion() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destinoSeleccionado by remember { mutableStateOf("Mis pedidos") }

    // 1. VARIABLE DE ESTADO PARA EL CONTADOR DE FAVORITOS
    var contadorFavoritos by remember { mutableIntStateOf(0) }

    val colorMoradoBarra = Color(0xFF522175)

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
                contadorFavoritos = contadorFavoritos, // 2. PASAR ESTADO AL DRAWER
                onDestinoSeleccionado = { nuevoDestino ->
                    destinoSeleccionado = nuevoDestino
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                // Encabezado morado personalizado
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colorMoradoBarra)
                        .padding(start = 8.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú principal",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "RAMSES Alvarez",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Mas vendidos",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.85f)
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
                    .background(Color.White)
            ) {
                when (destinoSeleccionado) {
                    "Inicio", "Mis pedidos" -> {
                        LazyColumn(
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            items(listaProductos) { producto ->
                                TarjetaProducto(
                                    producto = producto,
                                    onAgregarFavorito = { contadorFavoritos++ } // 3. INCREMENTAR CONTADOR
                                )
                            }
                        }
                    }
                    else -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Pantalla: $destinoSeleccionado",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = colorMoradoBarra
                            )
                        }
                    }
                }
            }
        }
    }
}