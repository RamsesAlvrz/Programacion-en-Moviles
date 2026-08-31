package com.alvarez.proyecto_con_ia.service

import com.alvarez.proyecto_con_ia.model.Producto

// RESPONSABILIDAD: Administrar la lista de productos y la lógica de cálculo global
class CarritoCompras {

    // ENCAPSULAMIENTO: Lista privada para controlar el acceso a los datos del carrito
    private val productos: MutableList<Producto> = mutableListOf()

    // Método para agregar un producto al carrito
    fun agregarProducto(producto: Producto) {
        productos.add(producto)
    }

    // Retorna una copia inmutable de la lista para proteger la encapsulación
    fun obtenerProductos(): List<Producto> = productos.toList()

    // POLIMORFISMO: Recorre la lista invocando calcularImporte() de cada objeto concreto
    fun calcularSubtotal(): Double {
        var subtotal = 0.0
        for (producto in productos) {
            subtotal += producto.calcularImporte()
        }
        return subtotal
    }

    // Calcula el IGV (18%) sobre el subtotal acumulado
    fun calcularIGV(subtotal: Double): Double {
        return subtotal * 0.18
    }

    // Obtener el producto con el importe final más alto usando colecciones de Kotlin
    fun obtenerProductoMasCaro(): Producto? {
        return productos.maxByOrNull { it.calcularImporte() }
    }

    // Búsqueda de producto por nombre exacto
    fun buscarProductoPorNombre(nombre: String): Producto? {
        return productos.find { it.obtenerNombre().equals(nombre, ignoreCase = true) }
    }

    // Eliminación de producto por nombre
    fun eliminarProductoPorNombre(nombre: String): Boolean {
        return productos.removeIf { it.obtenerNombre().equals(nombre, ignoreCase = true) }
    }
}