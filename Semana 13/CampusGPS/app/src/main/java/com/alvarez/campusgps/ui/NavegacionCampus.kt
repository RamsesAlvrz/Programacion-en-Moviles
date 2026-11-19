package com.alvarez.campusgps.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alvarez.campusgps.viewmodel.MapaViewModel

private enum class SeccionCampus {
    INICIO,
    MAPA,
    ULTIMO_LUGAR
}

@Composable
fun NavegacionCampus(vm: MapaViewModel) {

    val colores = MaterialTheme.colorScheme

    var seccion by rememberSaveable {
        mutableStateOf(SeccionCampus.INICIO)
    }

    var mostrarAyudaRuta by rememberSaveable {
        mutableStateOf(false)
    }

    val lugares by vm.lugares.collectAsStateWithLifecycle()
    val seleccionado by vm.seleccionado.collectAsStateWithLifecycle()

    val ultimoLugar = lugares.firstOrNull {
        it.lugar.id == seleccionado
    }

    fun abrirMapa() {
        vm.seleccionarCategoria("Todos")
        vm.seleccionarRadio(null)
        mostrarAyudaRuta = false
        seccion = SeccionCampus.MAPA
    }

    fun abrirRutas() {
        vm.seleccionarCategoria("Todos")
        vm.seleccionarRadio(null)
        mostrarAyudaRuta = true
        seccion = SeccionCampus.MAPA
    }

    fun abrirCercanos() {
        vm.seleccionarCategoria("Todos")
        vm.seleccionarRadio(200)
        mostrarAyudaRuta = false
        seccion = SeccionCampus.MAPA
    }

    BackHandler(
        enabled = seccion != SeccionCampus.INICIO
    ) {
        seccion = SeccionCampus.INICIO
        mostrarAyudaRuta = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colores.primary)
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.statusBars
                        .asPaddingValues()
                        .calculateTopPadding()
                ),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = colores.background,
            bottomBar = {
                NavigationBar(
                    containerColor = colores.surface,
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        selected = seccion == SeccionCampus.INICIO,
                        onClick = {
                            seccion = SeccionCampus.INICIO
                            mostrarAyudaRuta = false
                        },
                        icon = {
                            Text(
                                "⌂",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        label = {
                            Text(
                                "Inicio",
                                fontWeight =
                                    if (seccion == SeccionCampus.INICIO)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colores.onPrimaryContainer,
                            selectedTextColor = colores.primary,
                            indicatorColor = colores.primaryContainer,
                            unselectedIconColor = colores.onSurfaceVariant,
                            unselectedTextColor = colores.onSurfaceVariant
                        )
                    )

                    NavigationBarItem(
                        selected = seccion == SeccionCampus.MAPA,
                        onClick = { abrirMapa() },
                        icon = {
                            Text(
                                "⌖",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        label = { Text("Mapa") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colores.onPrimaryContainer,
                            selectedTextColor = colores.primary,
                            indicatorColor = colores.primaryContainer,
                            unselectedIconColor = colores.onSurfaceVariant,
                            unselectedTextColor = colores.onSurfaceVariant
                        )
                    )

                    NavigationBarItem(
                        selected = seccion == SeccionCampus.ULTIMO_LUGAR,
                        onClick = {
                            seccion = SeccionCampus.ULTIMO_LUGAR
                            mostrarAyudaRuta = false
                        },
                        icon = {
                            Text(
                                "◉",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        label = { Text("Último lugar") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = colores.onPrimaryContainer,
                            selectedTextColor = colores.primary,
                            indicatorColor = colores.primaryContainer,
                            unselectedIconColor = colores.onSurfaceVariant,
                            unselectedTextColor = colores.onSurfaceVariant
                        )
                    )
                }
            }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                when (seccion) {

                    SeccionCampus.INICIO -> {
                        PantallaInicio(
                            ultimoLugar = ultimoLugar,
                            mostrarUltimoLugar = false,
                            onExplorarMapa = { abrirMapa() },
                            onAbrirRutas = { abrirRutas() },
                            onAbrirCercanos = { abrirCercanos() },
                            onVerUltimoLugar = {
                                seccion = SeccionCampus.ULTIMO_LUGAR
                            }
                        )
                    }

                    SeccionCampus.MAPA -> {
                        // La ayuda ahora se muestra dentro
                        // de PantallaMapa, no encima del encabezado.
                        PantallaMapa(
                            vm = vm,
                            mostrarAyudaRuta = mostrarAyudaRuta,
                            onCerrarAyudaRuta = {
                                mostrarAyudaRuta = false
                            }
                        )
                    }

                    SeccionCampus.ULTIMO_LUGAR -> {
                        PantallaInicio(
                            ultimoLugar = ultimoLugar,
                            mostrarUltimoLugar = true,
                            onExplorarMapa = { abrirMapa() },
                            onAbrirRutas = { abrirRutas() },
                            onAbrirCercanos = { abrirCercanos() },
                            onVerUltimoLugar = {}
                        )
                    }
                }
            }
        }
    }
}
