
package com.alvarez.lavacampus.data.model

// Estados disponibles para los pedidos de LavaCampus.
enum class EstadoPedido(
    val codigo: String,
    val etiqueta: String
) {

    RECIBIDO(
        codigo = "RECIBIDO",
        etiqueta = "Recibido"
    ),

    EN_PROCESO(
        codigo = "EN_PROCESO",
        etiqueta = "En proceso"
    ),

    LISTO(
        codigo = "LISTO",
        etiqueta = "Listo"
    ),

    ENTREGADO(
        codigo = "ENTREGADO",
        etiqueta = "Entregado"
    ),

    CANCELADO(
        codigo = "CANCELADO",
        etiqueta = "Cancelado"
    );

    // Devuelve el siguiente estado permitido.
    fun siguiente(): EstadoPedido? {
        return when (this) {
            RECIBIDO -> EN_PROCESO
            EN_PROCESO -> LISTO
            LISTO -> ENTREGADO
            ENTREGADO -> null
            CANCELADO -> null
        }
    }

    // Solo se permite cancelar pedidos recibidos o en proceso.
    fun puedeCancelar(): Boolean {
        return this == RECIBIDO || this == EN_PROCESO
    }

    companion object {

        // Convierte el estado almacenado en Room a EstadoPedido.
        fun desdeCodigo(codigo: String): EstadoPedido? {
            return entries.firstOrNull {
                it.codigo == codigo
            }
        }
    }
}
