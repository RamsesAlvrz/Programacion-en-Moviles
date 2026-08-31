package com.alvarez.proyecto_con_ia

import com.alvarez.proyecto_con_ia.model.ProductoEstandar
import com.alvarez.proyecto_con_ia.model.ProductoImportado
import com.alvarez.proyecto_con_ia.service.CarritoCompras
import com.alvarez.proyecto_con_ia.service.EstrategiaDescuento
import com.alvarez.proyecto_con_ia.view.GeneradorReporte

fun main() {
    // 1. Instanciación de componentes principales
    val carrito = CarritoCompras()
    val estrategiaDescuento = EstrategiaDescuento()
    val reporte = GeneradorReporte()

    println("--> Registrando productos en el Carrito de Compras (POO)...\n")

    // 2. HERENCIA Y POLIMORFISMO: Agregando distintos tipos de productos a la lista polimórfica
    carrito.agregarProducto(ProductoEstandar("Teclado Mecánico", 120.0, 2))
    carrito.agregarProducto(ProductoImportado("Monitor Gamer 27\"", 850.0, 1, 0.15))
    carrito.agregarProducto(ProductoEstandar("Mouse Inalámbrico", 45.0, 3))
    carrito.agregarProducto(ProductoImportado("Audífonos Bluetooth", 180.0, 1, 0.10))

    // 3. Imprimir comprobante de compra inicial
    reporte.imprimirComprobante(carrito, estrategiaDescuento)

    // 4. Demostración de Retos Adicionales: Búsqueda y Eliminación
    println("\n--> DEMOSTRACIÓN DE MÉTODOS ADICIONALES (find y removeIf):")

    // Búsqueda con .find
    val buscado = carrito.buscarProductoPorNombre("Mouse Inalámbrico")
    if (buscado != null) {
        println("✔ Producto encontrado con .find: ${buscado.obtenerNombre()} (Precio Base: S/ ${buscado.obtenerPrecioBase()})")
    }

    // Eliminación con .removeIf
    val eliminado = carrito.eliminarProductoPorNombre("Mouse Inalámbrico")
    if (eliminado) {
        println("✔ Producto 'Mouse Inalámbrico' eliminado correctamente con .removeIf.")
    }

    // 5. Imprimir comprobante actualizado tras la eliminación
    println("\n--> REPORTE ACTUALIZADO TRAS LA ELIMINACIÓN:")
    reporte.imprimirComprobante(carrito, estrategiaDescuento)
}