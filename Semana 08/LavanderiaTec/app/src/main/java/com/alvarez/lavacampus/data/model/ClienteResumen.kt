package com.alvarez.lavacampus.data.model

// COMMIT 14: Resumen de clientes para el administrador.
// Combina los datos del estudiante con sus estadísticas de pedidos.
data class ClienteResumen(
    val id: Int,
    val nombre: String,
    val codigo: String,
    val cantidadPedidos: Int,
    val totalGastado: Double
)