package com.alvarez.navlabia.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alvarez.navlabia.screens.DetailScreen
import com.alvarez.navlabia.screens.HomeScreen
import com.alvarez.navlabia.screens.ListScreen
import com.alvarez.navlabia.screens.LoginScreen
import com.alvarez.navlabia.screens.ProfileScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.List.route) {
            ListScreen(navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument(name = "alumnoId") {
                    type = NavType.IntType
                    defaultValue = 1
                }
            )
        ) { backStackEntry ->
            val alumnoId = backStackEntry.arguments?.getInt("alumnoId") ?: 1
            DetailScreen(navController, alumnoId)
        }
    }
}
