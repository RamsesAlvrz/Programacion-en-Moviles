
package com.alvarez.lavacampus.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Tabla que almacena los pedidos realizados por los estudiantes
@Entity(
    tableName = "pedidos",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("usuarioId"),
        Index("estado")
    ]
)
data class Pedido(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioId: Int,
    val fechaRegistro: String,
    val fechaEntrega: String,
    val estado: String = "RECIBIDO",
    val total: Double,
    val casillero: String? = null,
    val pagado: Boolean = false,
    val nota: String = "",
    val fechaCreacion: Long = System.currentTimeMillis()
)
