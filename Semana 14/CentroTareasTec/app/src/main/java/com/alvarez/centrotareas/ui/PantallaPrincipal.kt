package com.alvarez.centrotareas.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import com.alvarez.centrotareas.viewmodel.HistorialViewModel
import com.alvarez.centrotareas.viewmodel.TareasViewModel

private val Azul = Color(0xFF176B9A)
private val Fondo = Color(0xFFF3F6FA)

@Composable
fun PantallaPrincipal(
    viewModel: TareasViewModel,
    historialViewModel: HistorialViewModel
) {
    var seccionSeleccionada by rememberSaveable {
        mutableIntStateOf(0)
    }

    val statusBarHeight = WindowInsets.statusBars
        .asPaddingValues()
        .calculateTopPadding()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Fondo)
    ) {

        // Fondo azul que se extiende detrás de la barra de estado.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(statusBarHeight)
                .background(Azul)
                .align(Alignment.TopCenter)
        )

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            containerColor = Fondo,
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    NavigationBarItem(
                        selected = seccionSeleccionada == 0,
                        onClick = {
                            seccionSeleccionada = 0
                        },
                        icon = {
                            Icon(
                                imageVector =
                                    if (seccionSeleccionada == 0) {
                                        Icons.Filled.Assignment
                                    } else {
                                        Icons.Outlined.Assignment
                                    },
                                contentDescription = "Tareas"
                            )
                        },
                        label = {
                            Text(
                                text = "Tareas",
                                fontWeight =
                                    if (seccionSeleccionada == 0) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Normal
                                    }
                            )
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Azul,
                            selectedTextColor = Azul,
                            indicatorColor = Color(0xFFE2F0F8),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )

                    NavigationBarItem(
                        selected = seccionSeleccionada == 1,
                        onClick = {
                            seccionSeleccionada = 1
                        },
                        icon = {
                            Icon(
                                imageVector =
                                    if (seccionSeleccionada == 1) {
                                        Icons.Filled.History
                                    } else {
                                        Icons.Outlined.History
                                    },
                                contentDescription = "Historial"
                            )
                        },
                        label = {
                            Text(
                                text = "Historial",
                                fontWeight =
                                    if (seccionSeleccionada == 1) {
                                        FontWeight.Bold
                                    } else {
                                        FontWeight.Normal
                                    }
                            )
                        },
                        alwaysShowLabel = true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Azul,
                            selectedTextColor = Azul,
                            indicatorColor = Color(0xFFE2F0F8),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        )
                    )
                }
            }
        ) { innerPadding ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (seccionSeleccionada) {
                    0 -> PantallaTareas(
                        viewModel = viewModel
                    )

                    1 -> PantallaHistorial(
                        viewModel = historialViewModel
                    )
                }
            }
        }
    }
}
