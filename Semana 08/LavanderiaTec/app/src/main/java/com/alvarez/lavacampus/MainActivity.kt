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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.alvarez.lavacampus.ui.estudiante.LoginScreen
import com.alvarez.lavacampus.ui.theme.LavaCampusTheme
import com.alvarez.lavacampus.viewmodel.LavaViewModelFactory
import com.alvarez.lavacampus.viewmodel.SesionViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as LavaApp

        val factory = LavaViewModelFactory(
            repository = app.repository,
            preferenciasStore = app.preferenciasStore
        )

        setContent {

            val sesionViewModel: SesionViewModel = viewModel(
                factory = factory
            )

            var rolActual by remember {
                mutableStateOf<String?>(null)
            }

            LavaCampusTheme {

                if (rolActual == null) {

                    LoginScreen(
                        viewModel = sesionViewModel,
                        onLoginExitoso = { rol ->
                            rolActual = rol
                        }
                    )

                } else {

                    // Pantalla temporal para verificar el login
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
                                text = "¡Bienvenido a LavaCampus!",
                                style = MaterialTheme.typography.headlineSmall
                            )

                            Spacer(
                                modifier = Modifier.height(16.dp)
                            )

                            Text(
                                text = if (rolActual == "ADMIN") {
                                    "Sesión de administrador"
                                } else {
                                    "Sesión de estudiante"
                                }
                            )

                            Spacer(
                                modifier = Modifier.height(24.dp)
                            )

                            Button(
                                onClick = {
                                    sesionViewModel.cerrarSesion {
                                        rolActual = null
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
