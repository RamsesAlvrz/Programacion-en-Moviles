package com.alvarez.campusgps.model

import com.alvarez.campusgps.data.RutaPeatonal

sealed interface EstadoRuta {

    data object SinRuta : EstadoRuta

    data object Cargando : EstadoRuta

    data class Lista(
        val ruta: RutaPeatonal,
        val destinoId: Int,
        val destinoNombre: String
    ) : EstadoRuta

    data class Error(
        val mensaje: String
    ) : EstadoRuta
}
