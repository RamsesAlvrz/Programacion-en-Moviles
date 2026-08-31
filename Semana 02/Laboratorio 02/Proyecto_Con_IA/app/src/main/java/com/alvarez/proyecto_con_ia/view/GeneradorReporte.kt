package com.alvarez.proyecto_con_ia.view

import com.alvarez.proyecto_con_ia.service.CarritoCompras
import com.alvarez.proyecto_con_ia.service.EstrategiaDescuento

// RESPONSABILIDAD: Formatear y mostrar la salida del comprobante en la consola
class GeneradorReporte {

    fun imprimirComprobante(carrito: CarritoCompras, estrategiaDescuento: EstrategiaDescuento) {
        val subtotal = carrito.calcularSubtotal()
        val porcentajeDescuento = estrategiaDescuento.calcularPorcentajeDescuento(subtotal)
        val montoDescuento = estrategiaDescuento.calcularMontoDescuento(subtotal)
        val subtotalConDescuento = subtotal - montoDescuento
        val igv = carrito.calcularIGV(subtotalConDescuento)
        val total = subtotalConDescuento + igv
        val productoMasCaro = carrito.obtenerProductoMasCaro()

        println("=====================================================")
        println("             REPORTE DE COMPRA (POO)                 ")
        println("=====================================================")

        // Polimorfismo: Cada producto imprime su detalle con su propio formato
        for (producto in carrito.obtenerProductos()) {
            println(producto.obtenerDetalle())
        }

        println("-----------------------------------------------------")
        println(String.format("%-30s: S/ %8.2f", "Subtotal Base", subtotal))
        println(String.format("%-30s: S/ %8.2f (%d%%)", "Descuento Aplicado", montoDescuento, (porcentajeDescuento * 100).toInt()))
        println(String.format("%-30s: S/ %8.2f", "Subtotal Neto", subtotalConDescuento))
        println(String.format("%-30s: S/ %8.2f", "IGV (18%)", igv))
        println("-----------------------------------------------------")
        println(String.format("%-30s: S/ %8.2f", "TOTAL A PAGAR", total))
        println("=====================================================")

        if (productoMasCaro != null) {
            println("\n--> PRODUCTO MÁS CARO:")
            println("    ${productoMasCaro.obtenerNombre()} (Importe: S/ ${String.format("%.2f", productoMasCaro.calcularImporte())})")
        }
        println("=====================================================")
    }
}