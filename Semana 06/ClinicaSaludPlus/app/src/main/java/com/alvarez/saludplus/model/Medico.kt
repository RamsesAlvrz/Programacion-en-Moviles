package com.alvarez.saludplus.model

data class Medico(
    val id: Int,
    val especialidadId: Int,
    val nombre: String,
    val experiencia: Int,
    val precio: Double,
    val calificacion: Double = 0.0,
    val cantidadOpiniones: Int = 0
)