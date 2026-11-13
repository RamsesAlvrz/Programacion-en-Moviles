package com.tecsup.smartcatalog.data.local

import androidx.room.Entity

@Entity(
    tableName = "favoritos_usuario",
    primaryKeys = ["usuarioId", "productoId"]
)
data class FavoritoEntity(
    val usuarioId: String,
    val productoId: Int
)
