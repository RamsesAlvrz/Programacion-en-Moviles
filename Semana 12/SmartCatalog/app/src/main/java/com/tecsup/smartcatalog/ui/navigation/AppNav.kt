
package com.tecsup.smartcatalog.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.smartcatalog.data.auth.AuthRepository
import com.tecsup.smartcatalog.data.repository.ItemRepository
import com.tecsup.smartcatalog.ui.detalle.DetalleScreen
import com.tecsup.smartcatalog.ui.detalle.DetalleViewModel
import com.tecsup.smartcatalog.ui.detalle.DetalleViewModelFactory
import com.tecsup.smartcatalog.ui.favoritos.FavoritosScreen
import com.tecsup.smartcatalog.ui.favoritos.FavoritosViewModel
import com.tecsup.smartcatalog.ui.favoritos.FavoritosViewModelFactory
import com.tecsup.smartcatalog.ui.lista.ListaScreen
import com.tecsup.smartcatalog.ui.lista.ListaViewModel
import com.tecsup.smartcatalog.ui.lista.ListaViewModelFactory
import com.tecsup.smartcatalog.ui.login.LoginScreen
import com.tecsup.smartcatalog.ui.login.LoginViewModel
import com.tecsup.smartcatalog.ui.login.LoginViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AppNav(repository: ItemRepository) {

    val navController = rememberNavController()
    val authRepository = remember { AuthRepository() }
    val scope = rememberCoroutineScope()

    // Recuperar sesión y verificar rol antes de mostrar pantallas.
    var cargandoSesion by remember { mutableStateOf(true) }
    var rolActual by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        rolActual = try {
            if (authRepository.usuarioActual() != null) {
                val rol = authRepository.obtenerRol()

                if (rol == "admin" || rol == "usuario") {
                    rol
                } else {
                    authRepository.cerrarSesion()
                    null
                }
            } else {
                null
            }
        } catch (e: Exception) {
            authRepository.cerrarSesion()
            null
        }

        cargandoSesion = false
    }

    if (cargandoSesion) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.padding(24.dp)
            )
        }
        return
    }

    val destinoInicial = when (rolActual) {
        "admin" -> "admin"
        "usuario" -> "catalogo"
        else -> "login"
    }

    fun cerrarSesion() {
        authRepository.cerrarSesion()
        rolActual = null

        navController.navigate("login") {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = destinoInicial
    ) {

        composable("login") {

            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(authRepository)
            )

            LoginScreen(
                viewModel = loginViewModel,
                onLoginExitoso = { rol ->

                    if (rol == "usuario" || rol == "admin") {
                        rolActual = rol

                        val destino = if (rol == "admin") {
                            "admin"
                        } else {
                            "catalogo"
                        }

                        navController.navigate(destino) {
                            popUpTo("login") {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable("catalogo") {

            val listaViewModel: ListaViewModel = viewModel(
                factory = ListaViewModelFactory(repository)
            )

            Column(modifier = Modifier.fillMaxSize()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { cerrarSesion() }) {
                        Text("Cerrar sesión")
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    ListaScreen(
                        viewModel = listaViewModel,
                        onVerDetalle = { id ->
                            navController.navigate("detalle/$id")
                        },
                        onVerFavoritos = {
                            navController.navigate("favoritos")
                        }
                    )
                }
            }
        }

        composable(
            route = "detalle/{productoId}",
            arguments = listOf(
                navArgument("productoId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            val productoId = backStackEntry.arguments
                ?.getInt("productoId") ?: return@composable

            val detalleViewModel: DetalleViewModel = viewModel(
                factory = DetalleViewModelFactory(repository)
            )

            DetalleScreen(
                productoId = productoId,
                viewModel = detalleViewModel,
                onVolver = {
                    navController.popBackStack()
                }
            )
        }

        composable("favoritos") {

            val favoritosViewModel: FavoritosViewModel = viewModel(
                factory = FavoritosViewModelFactory(repository)
            )

            FavoritosScreen(
                viewModel = favoritosViewModel,
                onVolver = {
                    navController.popBackStack()
                },
                onVerDetalle = { id ->
                    navController.navigate("detalle/$id")
                }
            )
        }

        composable("admin") {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {

                Text(
                    text = "Panel de administrador",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Bienvenido, administrador de SmartCatalog")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { cerrarSesion() }) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}
