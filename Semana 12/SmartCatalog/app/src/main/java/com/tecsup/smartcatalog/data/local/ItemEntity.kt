package com.tecsup.smartcatalog.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "productos")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen: String,
    val marca: String,
    val favorito: Boolean = false
)
