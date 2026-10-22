
package com.alvarez.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alvarez.lavacampus.ui.estudiante.InicioScreen
import com.alvarez.lavacampus.ui.estudiante.LoginScreen
import com.alvarez.lavacampus.ui.theme.LavaCampusTheme
import com.alvarez.lavacampus.viewmodel.InicioViewModel
import com.alvarez.lavacampus.viewmodel.LavaViewModelFactory
import com.alvarez.lavacampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtener las dependencias de la aplicación
        val app = application as LavaApp

        val factory = LavaViewModelFactory(
            repository = app.repository,
            preferenciasStore = app.preferenciasStore,
            database = app.database
        )

        setContent {

            // ViewModel encargado de gestionar la sesión
            val sesionViewModel: SesionViewModel = viewModel(
                factory = factory
            )

            // ViewModel encargado del inicio del estudiante
            val inicioViewModel: InicioViewModel = viewModel(
                factory = factory
            )

            // Observar el usuario que inició sesión
            val usuarioActual by sesionViewModel.usuarioActual
                .collectAsStateWithLifecycle()

            // Indicar si ya terminó de recuperarse la sesión
            var sesionVerificada by remember {
                mutableStateOf(false)
            }

            LaunchedEffect(Unit) {
                // Dar tiempo a recuperar la sesión almacenada
                kotlinx.coroutines.delay(500)
                sesionVerificada = true
            }

            LavaCampusTheme {

                when {

                    // Pantalla temporal de carga
                    !sesionVerificada -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    // Usuario sin sesión: mostrar login
                    usuarioActual == null -> {
                        LoginScreen(
                            viewModel = sesionViewModel,
                            onLoginExitoso = {
                                // El StateFlow del ViewModel
                                // actualizará automáticamente la pantalla
                            }
                        )
                    }

                    // Estudiante: mostrar su pantalla de inicio
                    usuarioActual?.rol == "CLIENTE" -> {

                        InicioScreen(
                            viewModel = inicioViewModel,
                            usuarioId = usuarioActual!!.id,
                            onNuevoPedido = {
                                // Se conectará en el Commit 9
                            },
                            onVerDetalle = {
                                // Se conectará en el Commit 10
                            },
                            onMisPedidos = {
                                // Se conectará en el Commit 11
                            }
                        )
                    }

                    // Administrador: pantalla temporal
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
                                            // La pantalla volverá al login
                                        }
                                    }
                                ) {
                                    Text("Cerrar sesión")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
