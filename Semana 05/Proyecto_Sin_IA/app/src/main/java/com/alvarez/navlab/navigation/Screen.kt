package com.alvarez.navlab.navigation // Asegúrate de mantener tu paquete original arriba

// Clase sellada que actúa como contrato central de navegación.
// Recibe "route" como parámetro: es el identificador único de cada pantalla.
sealed class Screen(val route: String) {

    // Pantalla de inicio - punto de entrada de la app
    object Home : Screen(route = "home")

    // Pantalla que muestra la lista de elementos
    object List : Screen(route = "list")

    // Pantalla del perfil del usuario
    object Profile : Screen(route = "profile")

    // RUTA CON ARGUMENTO: {itemId} es el placeholder que Navigation reemplaza
    object Detail : Screen(route = "detail/{itemId}") {
        // Construye la ruta final sustituyendo el placeholder por el valor real (Ej: "detail/5")
        fun createRoute(itemId: Int): String = "detail/$itemId"
    }
}
