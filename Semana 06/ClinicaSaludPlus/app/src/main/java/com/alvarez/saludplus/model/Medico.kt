package com.alvarez.saludplus.model

data class Medico(
    val id: Int,
    val especialidadId: Int,
    val nombre: String,
    val experiencia: Int,
    val precio: Double
)