package com.alvarez.saludplus.model

data class Usuario(
    val id: Int,
    val nombre: String,
    val apellido: String,
    val correo: String,
    val contrasena: String,
    val telefono: String
)