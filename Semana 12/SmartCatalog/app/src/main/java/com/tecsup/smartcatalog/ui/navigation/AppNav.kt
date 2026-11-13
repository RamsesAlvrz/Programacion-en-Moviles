package com.tecsup.smartcatalog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tecsup.smartcatalog.data.repository.ItemRepository
import com.tecsup.smartcatalog.ui.detalle.DetalleScreen
import com.tecsup.smartcatalog.ui.detalle.DetalleViewModel
import com.tecsup.smartcatalog.ui.detalle.DetalleViewModelFactory
import com.tecsup.smartcatalog.ui.lista.ListaScreen
import com.tecsup.smartcatalog.ui.lista.ListaViewModel
import com.tecsup.smartcatalog.ui.lista.ListaViewModelFactory

@Composable
fun AppNav(repository: ItemRepository) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "catalogo"
    ) {

        composable("catalogo") {

            val listaViewModel: ListaViewModel = viewModel(
                factory = ListaViewModelFactory(repository)
            )

            ListaScreen(
                viewModel = listaViewModel,
                onVerDetalle = { id ->
                    navController.navigate("detalle/$id")
                }
            )
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
    }
}
