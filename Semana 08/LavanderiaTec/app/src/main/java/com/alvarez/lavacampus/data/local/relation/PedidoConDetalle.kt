package com.alvarez.lavacampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.alvarez.lavacampus.data.local.entity.DetallePedido
import com.alvarez.lavacampus.data.local.entity.Pedido

// Representa un pedido junto con todos sus detalles
data class PedidoConDetalle(

    // Información principal del pedido
    @Embedded
    val pedido: Pedido,

    // Lista de servicios asociados al pedido
    @Relation(
        parentColumn = "id",
        entityColumn = "pedidoId"
    )
    val detalles: List<DetallePedido>
)
