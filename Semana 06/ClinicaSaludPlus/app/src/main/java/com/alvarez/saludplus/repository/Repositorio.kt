package com.alvarez.saludplus.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.alvarez.saludplus.model.Cita
import com.alvarez.saludplus.model.Especialidad
import com.alvarez.saludplus.model.Medico
import com.alvarez.saludplus.model.Usuario

object Repositorio {

    // Colecciones compartidas en memoria.
    private val usuarios = mutableStateListOf<Usuario>()
    private val citas = mutableStateListOf<Cita>()

    private var siguienteUsuarioId = 1
    private var siguienteCitaId = 1

    var usuarioActual by mutableStateOf<Usuario?>(null)
        private set

    private val especialidades = listOf<Especialidad>()

    private val medicos = listOf<Medico>()

    private val horariosBase = listOf(
        "08:00", "08:30",
        "09:00", "09:30",
        "10:00", "10:30",
        "11:00", "11:30",
        "14:00", "14:30",
        "15:00", "15:30"
    )

    fun registrarUsuario(
        nombre: String,
        apellido: String,
        correo: String,
        contrasena: String,
        telefono: String
    ): Boolean {

        val correoLimpio = correo.trim().lowercase()
        val telefonoLimpio = telefono.trim()

        // Comprobar datos obligatorios.
        if (nombre.isBlank() || apellido.isBlank()) {
            return false
        }

        // Comprobar el formato del correo.
        val correoValido = android.util.Patterns.EMAIL_ADDRESS
            .matcher(correoLimpio)
            .matches()

        if (!correoValido) {
            return false
        }

        // Comprobar que el teléfono tenga nueve dígitos.
        if (
            telefonoLimpio.length != 9 ||
            !telefonoLimpio.all { it.isDigit() }
        ) {
            return false
        }

        // Comprobar la longitud mínima de la contraseña.
        if (contrasena.length < 6) {
            return false
        }

        // Evitar registrar dos pacientes con el mismo correo.
        val correoRegistrado = usuarios.any {
            it.correo.equals(
                correoLimpio,
                ignoreCase = true
            )
        }

        if (correoRegistrado) {
            return false
        }

        // Crear al paciente.
        val nuevoUsuario = Usuario(
            id = siguienteUsuarioId,
            nombre = nombre.trim(),
            apellido = apellido.trim(),
            correo = correoLimpio,
            contrasena = contrasena,
            telefono = telefonoLimpio
        )

        // Guardarlo en la colección.
        usuarios.add(nuevoUsuario)

        siguienteUsuarioId++

        return true
    }

    fun iniciarSesion(
        correo: String,
        contrasena: String
    ): Boolean {

        val correoLimpio = correo.trim()

        val usuarioEncontrado = usuarios.find {
            it.correo.equals(
                correoLimpio,
                ignoreCase = true
            ) && it.contrasena == contrasena
        }

        usuarioActual = usuarioEncontrado

        return usuarioEncontrado != null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    fun buscarEspecialidades(
        texto: String
    ): List<Especialidad> {
        return emptyList()
    }

    fun especialidadesDestacadas(): List<Especialidad> {
        return emptyList()
    }

    fun obtenerEspecialidad(
        id: Int
    ): Especialidad? {
        return null
    }

    fun obtenerMedico(
        id: Int
    ): Medico? {

        return null
    }

    fun medicosPorEspecialidad(
        especialidadId: Int
    ): List<Medico> {
        return emptyList()
    }

    fun buscarMedicos(
        especialidadId: Int,
        texto: String
    ): List<Medico> {
        return emptyList()
    }

    fun horariosDisponibles(
        medicoId: Int,
        fecha: String
    ): List<String> {
        return emptyList()
    }

    fun agendarCita(
        medicoId: Int,
        fecha: String,
        hora: String
    ): Cita? {
        return null
    }

    fun obtenerCita(
        id: Int
    ): Cita? {
        return null
    }

    fun citasDelUsuario(
        usuarioId: Int
    ): List<Cita> {
        return emptyList()
    }

    fun cancelarCita(
        id: Int
    ): Boolean {
        return false
    }
}