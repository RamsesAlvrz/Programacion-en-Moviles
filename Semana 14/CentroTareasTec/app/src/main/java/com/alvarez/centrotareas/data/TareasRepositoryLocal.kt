package com.alvarez.centrotareas.data

import kotlinx.coroutines.delay
import java.io.IOException

class TareasRepositoryLocal : TareasRepository {

    override suspend fun contarPrimos(hasta: Int): Int {
        // TODO: implementar en el paso de numeros primos
        return 0
    }

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(
        simularError: Boolean
    ): List<String> {
        delay(3000)

        if (simularError) {
            throw IOException("Sin conexion")
        }

        return listOf(
            "Pedido 101",
            "Pedido 102",
            "Pedido 103"
        )
    }
}
