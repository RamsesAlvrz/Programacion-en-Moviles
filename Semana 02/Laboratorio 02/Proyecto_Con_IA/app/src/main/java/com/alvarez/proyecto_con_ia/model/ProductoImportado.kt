package com.alvarez.proyecto_con_ia.model

// HERENCIA: ProductoImportado hereda de la clase base Producto
class ProductoImportado(
    nombre: String,
    precioBase: Double,
    cantidad: Int,
    tasaArancelInicial: Double = 0.15 // 15% de impuesto por defecto
) : Producto(nombre, precioBase, cantidad) {

    // ENCAPSULAMIENTO: Atributo privado para el impuesto de importación
    var tasaArancel: Double = if (tasaArancelInicial >= 0) tasaArancelInicial else 0.15
        private set

    // POLIMORFISMO: Sobrescribe el cálculo sumando la tasa de arancel
    override fun calcularImporte(): Double {
        val subtotal = calcularSubtotalBase()
        return subtotal + (subtotal * tasaArancel)
    }

    // POLIMORFISMO: Sobrescribe el detalle para indicar que incluye arancel
    override fun obtenerDetalle(): String {
        val porcentaje = (tasaArancel * 100).toInt()
        return String.format("%-20s x%-2d S/ %8.2f (Imp. %d%%)", nombre, cantidad, calcularImporte(), porcentaje)
    }
}