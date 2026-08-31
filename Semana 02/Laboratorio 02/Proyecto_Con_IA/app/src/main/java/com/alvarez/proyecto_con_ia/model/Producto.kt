package com.alvarez.proyecto_con_ia.model

// ABSTRACCIÓN: Clase abstracta base que representa cualquier producto del sistema
abstract class Producto(
    protected val nombre: String,
    precioBaseInicial: Double,
    cantidadInicial: Int
) {
    // ENCAPSULAMIENTO: Propiedades privadas con setter validado
    var precioBase: Double = if (precioBaseInicial > 0) precioBaseInicial else 1.0
        private set(value) {
            if (value > 0) field = value
        }

    var cantidad: Int = if (cantidadInicial > 0) cantidadInicial else 1
        private set(value) {
            if (value > 0) field = value
        }

    // Encapsulamiento: Getters explícitos
    fun obtenerNombre(): String = nombre
    fun obtenerPrecioBase(): Double = precioBase
    fun obtenerCantidad(): Int = cantidad

    // Subtotal base (precio * cantidad sin recargos)
    fun calcularSubtotalBase(): Double {
        return precioBase * cantidad
    }

    // POLIMORFISMO Y ABSTRACCIÓN: Método abstracto que cada tipo de producto implementará de forma distinta
    abstract fun calcularImporte(): Double

    // Información básica del producto
    open fun obtenerDetalle(): String {
        return String.format("%-20s x%-2d S/ %8.2f", nombre, cantidad, calcularImporte())
    }
}