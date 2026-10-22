package com.alvarez.lavacampus.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.alvarez.lavacampus.data.local.entity.DetallePedido
import com.alvarez.lavacampus.data.local.entity.Servicio

// Representa un detalle de pedido junto con su servicio
data class DetalleConServicio(

    // Información del detalle del pedido
    @Embedded
    val detalle: DetallePedido,

    // Servicio asociado al detalle
    @Relation(
        parentColumn = "servicioId",
        entityColumn = "id"
    )
    val servicio: Servicio
)
