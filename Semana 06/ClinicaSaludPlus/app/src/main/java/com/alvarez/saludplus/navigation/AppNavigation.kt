package com.alvarez.saludplus.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alvarez.saludplus.ui.components.BarraInferior
import com.alvarez.saludplus.ui.screens.agendamiento.CitaExitosaScreen
import com.alvarez.saludplus.ui.screens.agendamiento.ConfirmarCitaScreen
import com.alvarez.saludplus.ui.screens.agendamiento.EspecialidadesScreen
import com.alvarez.saludplus.ui.screens.agendamiento.FechaHoraScreen
import com.alvarez.saludplus.ui.screens.agendamiento.MedicosScreen
import com.alvarez.saludplus.ui.screens.auth.LoginScreen
import com.alvarez.saludplus.ui.screens.auth.RegistroScreen
import com.alvarez.saludplus.ui.screens.auth.SplashScreen
import com.alvarez.saludplus.ui.screens.auth.TerminosScreen
import com.alvarez.saludplus.ui.screens.citas.DetalleCitaScreen
import com.alvarez.saludplus.ui.screens.citas.MisCitasScreen
import com.alvarez.saludplus.ui.screens.home.HomeScreen
import com.alvarez.saludplus.ui.screens.notificaciones.NotificacionesScreen
import com.alvarez.saludplus.ui.screens.perfil.PerfilScreen
import com.alvarez.saludplus.ui.screens.resultados.ResultadosScreen

@Composable
fun AppNavigation() {

    val nav = rememberNavController()

    val entrada by nav.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route

    val destinosPrincipales = listOf(
        Rutas.HOME,
        Rutas.CITAS,
        Rutas.RESULTADOS,
        Rutas.PERFIL
    )

    val volver: () -> Unit = {
        nav.popBackStack()
        Unit
    }

    val entrar: () -> Unit = {
        nav.navigate(Rutas.HOME) {
            popUpTo(Rutas.SPLASH) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Scaffold(
        bottomBar = {
            if (rutaActual in destinosPrincipales) {
                BarraInferior(
                    rutaActual = rutaActual,
                    onDestino = { destino ->
                        nav.navigate(destino) {
                            popUpTo(Rutas.HOME) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    ) { padding ->

        NavHost(
            navController = nav,
            startDestination = Rutas.SPLASH,
            modifier = Modifier.padding(padding)
        ) {

            composable(Rutas.SPLASH) {
                SplashScreen(
                    onRegistro = {
                        nav.navigate(Rutas.REGISTRO)
                    },
                    onLogin = {
                        nav.navigate(Rutas.LOGIN)
                    }
                )
            }

            composable(Rutas.REGISTRO) {
                RegistroScreen(
                    onVolver = {
                        nav.popBackStack()
                    },
                    onRegistrado = {
                        nav.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.REGISTRO) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    onTerminos = {
                        nav.navigate(Rutas.TERMINOS)
                    },
                    onLogin = {
                        nav.navigate(Rutas.LOGIN) {
                            popUpTo(Rutas.REGISTRO) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Rutas.LOGIN) {
                LoginScreen(
                    onVolver = volver,
                    onAcceso = entrar,
                    onRegistro = {
                        nav.navigate(Rutas.REGISTRO)
                    }
                )
            }

            composable(Rutas.HOME) {
                HomeScreen(
                    onEspecialidades = {
                        nav.navigate(Rutas.ESPECIALIDADES)
                    },
                    onEspecialidad = { especialidadId ->
                        nav.navigate(
                            Rutas.medicos(especialidadId)
                        )
                    },
                    onCitas = {
                        nav.navigate(Rutas.CITAS)
                    },
                    onResultados = {
                        nav.navigate(Rutas.RESULTADOS)
                    },
                    onNotificaciones = {
                        nav.navigate(Rutas.NOTIFICACIONES)
                    }
                )
            }

            composable(Rutas.ESPECIALIDADES) {
                EspecialidadesScreen(
                    onVolver = volver,
                    onEspecialidad = { especialidadId ->
                        nav.navigate(
                            Rutas.medicos(especialidadId)
                        )
                    }
                )
            }

            composable(
                route = Rutas.MEDICOS,
                arguments = listOf(
                    navArgument("especialidadId") {
                        type = NavType.IntType
                    }
                )
            ) { entradaMedicos ->

                val especialidadId =
                    entradaMedicos.arguments
                        ?.getInt("especialidadId") ?: 0

                MedicosScreen(
                    especialidadId = especialidadId,
                    onVolver = volver,
                    onMedico = { medicoId ->
                        nav.navigate(
                            Rutas.fechaHora(medicoId)
                        )
                    }
                )
            }

            composable(
                route = Rutas.FECHA_HORA,
                arguments = listOf(
                    navArgument("medicoId") {
                        type = NavType.IntType
                    }
                )
            ) { entradaFecha ->

                val medicoId =
                    entradaFecha.arguments
                        ?.getInt("medicoId") ?: 0

                FechaHoraScreen(
                    medicoId = medicoId,
                    onVolver = volver,
                    onContinuar = { fecha, hora ->
                        nav.navigate(
                            Rutas.confirmar(
                                medicoId,
                                fecha,
                                hora
                            )
                        )
                    }
                )
            }

            composable(
                route = Rutas.CONFIRMAR,
                arguments = listOf(
                    navArgument("medicoId") {
                        type = NavType.IntType
                    },
                    navArgument("fecha") {
                        type = NavType.StringType
                    },
                    navArgument("hora") {
                        type = NavType.StringType
                    }
                )
            ) { entradaConfirmar ->

                val medicoId =
                    entradaConfirmar.arguments
                        ?.getInt("medicoId") ?: 0

                val fecha =
                    entradaConfirmar.arguments
                        ?.getString("fecha").orEmpty()

                val hora =
                    entradaConfirmar.arguments
                        ?.getString("hora").orEmpty()

                ConfirmarCitaScreen(
                    medicoId = medicoId,
                    fecha = fecha,
                    hora = hora,
                    onVolver = volver,
                    onConfirmada = { citaId ->
                        nav.navigate(
                            Rutas.exitosa(citaId)
                        ) {
                            popUpTo(Rutas.HOME) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route = Rutas.EXITOSA,
                arguments = listOf(
                    navArgument("citaId") {
                        type = NavType.IntType
                    }
                )
            ) { entradaExitosa ->

                val citaId =
                    entradaExitosa.arguments
                        ?.getInt("citaId") ?: 0

                CitaExitosaScreen(
                    citaId = citaId,
                    onInicio = {
                        nav.popBackStack(
                            Rutas.HOME,
                            false
                        )
                    },
                    onMisCitas = {
                        nav.navigate(Rutas.CITAS) {
                            popUpTo(Rutas.HOME) {
                                inclusive = false
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Rutas.CITAS) {
                MisCitasScreen(
                    onDetalle = { citaId ->
                        nav.navigate(
                            Rutas.detalleCita(citaId)
                        )
                    },
                    onAgendar = {
                        nav.navigate(Rutas.ESPECIALIDADES)
                    }
                )
            }

            composable(
                route = Rutas.DETALLE_CITA,
                arguments = listOf(
                    navArgument("citaId") {
                        type = NavType.IntType
                    }
                )
            ) { entradaDetalle ->

                val citaId =
                    entradaDetalle.arguments
                        ?.getInt("citaId") ?: 0

                DetalleCitaScreen(
                    citaId = citaId,
                    onVolver = volver
                )
            }

            composable(Rutas.RESULTADOS) {
                ResultadosScreen()
            }

            composable(Rutas.PERFIL) {
                PerfilScreen(
                    onCerrarSesion = {
                        nav.navigate(Rutas.SPLASH) {
                            popUpTo(nav.graph.id) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(Rutas.NOTIFICACIONES) {
                NotificacionesScreen(
                    onVolver = volver
                )
            }

            composable(Rutas.TERMINOS) {
                TerminosScreen(
                    onVolver = volver
                )
            }
        }
    }
}