
package com.alvarez.lavacampus.model

import com.alvarez.lavacampus.data.local.entity.Servicio

// Representa un servicio seleccionado dentro de un pedido
data class LineaPedido(
    val servicio: Servicio,
    val cantidad: Double = 0.0
) {

    // Calcular el subtotal según la cantidad seleccionada
    val subtotal: Double
        get() = servicio.precio * cantidad

    // Verificar si el estudiante seleccionó este servicio
    val seleccionado: Boolean
        get() = cantidad > 0.0

    // Actualizar la cantidad sin permitir valores negativos
    fun actualizarCantidad(nuevaCantidad: Double): LineaPedido {

        // Establecer el límite según el tipo de servicio
        val cantidadMaxima = if (servicio.unidad == "KG") {
            10.0
        } else {
            30.0
        }

        return copy(
            cantidad = nuevaCantidad.coerceIn(
                0.0,
                cantidadMaxima
            )
        )
    }
}
