package com.alvarez.proyecto_con_ia.model

// HERENCIA: ProductoEstandar hereda de la clase base Producto
class ProductoEstandar(
    nombre: String,
    precioBase: Double,
    cantidad: Int
) : Producto(nombre, precioBase, cantidad) {

    // POLIMORFISMO: Implementación directa del cálculo sin recargos adicionales
    override fun calcularImporte(): Double {
        return calcularSubtotalBase()
    }
}