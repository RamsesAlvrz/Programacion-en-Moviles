package com.alvarez.proyecto_con_ia.service

// RESPONSABILIDAD: Encapsular la regla de negocio para el cálculo de descuentos
class EstrategiaDescuento {

    // Evalúa el porcentaje de descuento según el monto acumulado del subtotal
    fun calcularPorcentajeDescuento(subtotal: Double): Double {
        return when {
            subtotal >= 500.0 -> 0.15 // 15% de descuento para compras de S/ 500 a más
            subtotal >= 200.0 -> 0.10 // 10% de descuento para compras de S/ 200 a S/ 499.99
            subtotal >= 100.0 -> 0.05 // 5% de descuento para compras de S/ 100 a S/ 199.99
            else -> 0.00             // Sin descuento para montos menores a S/ 100
        }
    }

    // Calcula el monto exacto en soles a descontar
    fun calcularMontoDescuento(subtotal: Double): Double {
        val porcentaje = calcularPorcentajeDescuento(subtotal)
        return subtotal * porcentaje
    }
}