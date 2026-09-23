package com.alvarez.navlabia.navigation

// Clase sellada que actúa como contrato central de navegación.
sealed class Screen(val route: String) {

    // Pantalla de Login - startDestination
    object Login : Screen(route = "login")

    // Pantalla de inicio (Bienvenida)
    object Home : Screen(route = "home")

    // Pantalla del Directorio de Alumnos
    object List : Screen(route = "directorio")

    // Pantalla de Configuración de Perfil
    object Profile : Screen(route = "perfil")

    // Pantalla de Expediente Académico con argumento alumnoId
    object Detail : Screen(route = "expediente/{alumnoId}") {
        fun createRoute(alumnoId: Int): String = "expediente/$alumnoId"
    }
}
