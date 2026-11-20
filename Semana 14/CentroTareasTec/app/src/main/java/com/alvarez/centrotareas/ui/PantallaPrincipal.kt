package com.alvarez.centrotareas.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.alvarez.centrotareas.viewmodel.TareasViewModel

private val Azul = Color(0xFF176B9A)
private val Fondo = Color(0xFFF3F6FA)

@Composable
fun PantallaPrincipal(
    viewModel: TareasViewModel
) {
    var seccionSeleccionada by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        containerColor = Fondo,
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = androidx.compose.ui.unit.Dp(8f)
            ) {
                NavigationBarItem(
                    selected = seccionSeleccionada == 0,
                    onClick = { seccionSeleccionada = 0 },
                    icon = {
                        Text(
                            text = "▦",
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    label = {
                        Text(
                            text = "Tareas",
                            fontWeight = if (seccionSeleccionada == 0) {
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
                    onClick = { seccionSeleccionada = 1 },
                    icon = {
                        Text(
                            text = "◷",
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    label = {
                        Text(
                            text = "Historial",
                            fontWeight = if (seccionSeleccionada == 1) {
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

        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (seccionSeleccionada) {
                0 -> PantallaTareas(viewModel = viewModel)
                1 -> PantallaHistorial()
            }
        }
    }
}
