package com.alvarez.saludplus.model

data class Cita(
    val id: Int,
    val usuarioId: Int,
    val medicoId: Int,
    val fecha: String,
    val hora: String
)