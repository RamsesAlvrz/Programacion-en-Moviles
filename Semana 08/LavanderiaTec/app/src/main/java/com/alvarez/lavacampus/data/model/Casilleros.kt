
package com.alvarez.lavacampus.data.model

// Catálogo de casilleros disponibles en LavaCampus.
object Casilleros {

    // Genera los casilleros C-01, C-02, ... C-30.
    val todos: List<String> = (1..30).map { numero ->
        "C-${numero.toString().padStart(2, '0')}"
    }

    // Comprueba que el casillero pertenezca al catálogo.
    fun esValido(casillero: String): Boolean {
        return casillero in todos
    }
}
