package com.alvarez.centrotareas.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.IOException
import kotlin.coroutines.coroutineContext

class TareasRepositoryLocal : TareasRepository {

    override suspend fun contarPrimos(hasta: Int): Int =
        withContext(Dispatchers.Default) {

            var cantidad = 0

            for (numero in 2..hasta) {
                coroutineContext.ensureActive()

                if (esPrimo(numero)) {
                    cantidad++
                }
            }

            cantidad
        }

    private fun esPrimo(n: Int): Boolean {
        if (n < 2) return false

        var divisor = 2

        while (divisor <= n / divisor) {
            if (n % divisor == 0) {
                return false
            }

            divisor++
        }

        return true
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
