package com.alvarez.centrotareas.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ejecuciones")
data class EjecucionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val tipo: String,
    val estado: String,
    val descripcion: String,
    val duracionMs: Long? = null,
    val fecha: Long = System.currentTimeMillis()
)
