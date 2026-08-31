package com.alvarez.lab02carritokotlin

data class Producto(
    val nombre: String,
    val precio: Double,
    var cantidad: Int
)

// Para los colores
const val RESET = "\u001B[0m"
const val CYAN = "\u001B[36m"
const val GREEN = "\u001B[32m"
const val YELLOW = "\u001B[33m"
const val BLUE = "\u001B[34m"

// Funciones de cálculo
fun calcularSubtotal(productos: List<Producto>): Double {
    var subtotal = 0.0
    for (p in productos) {
        subtotal += p.precio * p.cantidad
    }
    return subtotal
}

fun calcularIGV(subtotal: Double): Double {
    return subtotal * 0.18
}

fun calcularTotal(subtotal: Double, igv: Double): Double {
    return subtotal + igv
}

// Descuento con when
fun calcularDescuento(total: Double): Double {
    return when {
        total > 5000 -> total * 0.10
        total > 3000 -> total * 0.05
        else -> 0.0
    }
}

// Reporte con formato
fun mostrarDetalle(productos: List<Producto>) {
    println("${YELLOW}-------- DETALLE DEL CARRITO --------${RESET}")
    var i = 1
    for (p in productos) {
        val importe = p.precio * p.cantidad
        println(String.format("%d. %-20s x%d  S/ %8.2f",
            i, p.nombre, p.cantidad, importe))
        i++
    }
    println("${YELLOW}-------------------------------------${RESET}")
}

fun buscarProducto(productos: List<Producto>, nombre: String): Producto? {
    return productos.find { it.nombre.equals(nombre, ignoreCase = true) }
}

fun main() {
    println("${CYAN}=========================================${RESET}")
    println("${CYAN}   CARRITO DE COMPRAS - TIENDA TECSUP    ${RESET}")
    println("${CYAN}=========================================${RESET}")

    val nombreCliente = "Ramses Alvarez"
    val carrito = mutableListOf<Producto>()

    println("Cliente: $nombreCliente\n")

    val productosAñadir = listOf(
        Producto("Laptop HP", 2500.0, 1),
        Producto("Mouse Logitech", 45.5, 2),
        Producto("Audifonos Sony", 120.0, 1),
        Producto("USB Kingston 64GB", 25.0, 3)
    )

    for (p in productosAñadir) {
        carrito.add(p)
        println("${GREEN}Producto agregado: ${p.nombre}${RESET}")
    }
    println()

    // Detalle del carrito
    mostrarDetalle(carrito)

    // Cantidad de productos
    println(String.format("%-20s : %d", "Cantidad de productos", carrito.size))

    // Cálculos de totales
    val subtotal = calcularSubtotal(carrito)
    val igv = calcularIGV(subtotal)
    val total = calcularTotal(subtotal, igv)

    println(String.format("%-20s : S/ %8.2f", "Subtotal", subtotal))
    println(String.format("%-20s : S/ %8.2f", "IGV (18%)", igv))

    // TOTAL A PAGAR
    println("${GREEN}" + String.format("%-20s : S/ %8.2f", "TOTAL A PAGAR", total) + "${RESET}")
    println("${YELLOW}-------------------------------------${RESET}")

    // Producto más caro
    val masCaro = carrito.maxByOrNull { it.precio }
    if (masCaro != null) {
        println("${BLUE}Producto mas caro: ${masCaro.nombre} " +
                String.format("(S/ %.2f)", masCaro.precio) + "${RESET}")
    }

    // Descuento
    val descuento = calcularDescuento(total)
    val totalConDescuento = total - descuento

    val textoDescuento = when {
        total > 5000 -> "10% por compra mayor a S/ 5000"
        total > 3000 -> "5% por compra mayor a S/ 3000"
        else -> "Sin descuento"
    }

    println("${YELLOW}Descuento aplicado: $textoDescuento${RESET}")

    // TOTAL CON DESCUENTO
    println("${GREEN}" + String.format("%-20s : S/ %8.2f", "TOTAL CON DESCUENTO", totalConDescuento) + "${RESET}")
    println()

    println("${BLUE}Gracias por su compra, $nombreCliente!${RESET}")

    // ===============
    // RETO ADICIONAL
    // ===============
    println("\n>>> RETO ADICIONAL <<<")

    val terminoBusqueda = "Mouse Logitech"
    val encontrado = buscarProducto(carrito, terminoBusqueda)

    if (encontrado != null) {
        println("Producto encontrado: ${encontrado.nombre} | Precio: S/ ${encontrado.precio}")
    } else {
        println("El producto '$terminoBusqueda' no existe en el carrito.")
    }

    println("\nEliminando '$terminoBusqueda' del carrito...")
    carrito.removeIf { it.nombre.equals(terminoBusqueda, ignoreCase = true) }

    println("\n--- DETALLE POST-ELIMINACION ---")
    mostrarDetalle(carrito)

    val subtotalAct = calcularSubtotal(carrito)
    val igvAct = calcularIGV(subtotalAct)
    val totalAct = calcularTotal(subtotalAct, igvAct)
    val descuentoAct = calcularDescuento(totalAct)
    val totalConDescuentoAct = totalAct - descuentoAct

    println(String.format("%-20s : S/ %8.2f", "Subtotal", subtotalAct))
    println(String.format("%-22s: S/ %8.2f", "IGV (18%)", igvAct))
    println(String.format("%-20s : S/ %8.2f", "TOTAL CON DESCUENTO", totalConDescuentoAct))
    println("=========================================")
}