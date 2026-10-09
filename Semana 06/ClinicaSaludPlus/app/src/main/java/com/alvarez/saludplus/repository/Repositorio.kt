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

    private val especialidades = listOf(
        Especialidad(
            id = 1,
            nombre = "Medicina general",
            descripcion = "Atención y prevención para toda la familia"
        ),
        Especialidad(
            id = 2,
            nombre = "Cardiología",
            descripcion = "Cuidado y prevención de enfermedades del corazón"
        ),
        Especialidad(
            id = 3,
            nombre = "Dermatología",
            descripcion = "Atención de la piel, cabello y uñas"
        ),
        Especialidad(
            id = 4,
            nombre = "Pediatría",
            descripcion = "Atención para niños y adolescentes"
        ),
        Especialidad(
            id = 5,
            nombre = "Traumatología",
            descripcion = "Cuidado de huesos y articulaciones"
        ),
        Especialidad(
            id = 6,
            nombre = "Oftalmología",
            descripcion = "Cuidado de la visión y salud de los ojos"
        )
    )

    private val medicos = listOf(
        Medico(
            id = 1,
            especialidadId = 1,
            nombre = "Dra. Ana Torres",
            experiencia = 12,
            precio = 80.00
        ),
        Medico(
            id = 2,
            especialidadId = 1,
            nombre = "Dr. Luis Ramos",
            experiencia = 8,
            precio = 75.00
        ),
        Medico(
            id = 3,
            especialidadId = 2,
            nombre = "Dr. Carlos Medina",
            experiencia = 15,
            precio = 120.00
        ),
        Medico(
            id = 4,
            especialidadId = 2,
            nombre = "Dra. Elena Rojas",
            experiencia = 10,
            precio = 110.00
        ),
        Medico(
            id = 5,
            especialidadId = 3,
            nombre = "Dra. María Vega",
            experiencia = 9,
            precio = 100.00
        ),
        Medico(
            id = 6,
            especialidadId = 4,
            nombre = "Dr. José Salas",
            experiencia = 11,
            precio = 90.00
        ),
        Medico(
            id = 7,
            especialidadId = 5,
            nombre = "Dr. Pedro Castillo",
            experiencia = 14,
            precio = 115.00
        ),
        Medico(
            id = 8,
            especialidadId = 6,
            nombre = "Dra. Lucía Flores",
            experiencia = 7,
            precio = 95.00
        )
    )

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

        val textoLimpio = texto.trim()

        return especialidades.filter {
            it.nombre.contains(
                textoLimpio,
                ignoreCase = true
            )
        }
    }

    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.take(4)
    }

    fun obtenerEspecialidad(
        id: Int
    ): Especialidad? {

        return especialidades.find {
            it.id == id
        }
    }

    fun obtenerMedico(
        id: Int
    ): Medico? {

        return medicos.find {
            it.id == id
        }
    }

    fun medicosPorEspecialidad(
        especialidadId: Int
    ): List<Medico> {

        return medicos
            .filter {
                it.especialidadId == especialidadId
            }
            .sortedByDescending {
                it.experiencia
            }
    }

    fun buscarMedicos(
        especialidadId: Int,
        texto: String
    ): List<Medico> {

        val textoLimpio = texto.trim()

        return medicosPorEspecialidad(especialidadId)
            .filter {
                it.nombre.contains(
                    textoLimpio,
                    ignoreCase = true
                )
            }
    }

    fun horariosDisponibles(
        medicoId: Int,
        fecha: String
    ): List<String> {

        if (
            obtenerMedico(medicoId) == null ||
            fecha.isBlank()
        ) {
            return emptyList()
        }

        val horariosOcupados = citas
            .filter {
                it.medicoId == medicoId &&
                        it.fecha == fecha
            }
            .map {
                it.hora
            }

        return horariosBase.filter {
            it !in horariosOcupados
        }
    }

    fun agendarCita(
        medicoId: Int,
        fecha: String,
        hora: String,
        motivoConsulta: String = ""
    ): Cita? {
        val usuario = usuarioActual ?: return null

        if (
            obtenerMedico(medicoId) == null ||
            fecha.isBlank() ||
            hora !in horariosBase
        ) {
            return null
        }

        val horarioOcupado = citas.any {
            it.medicoId == medicoId &&
                    it.fecha == fecha &&
                    it.hora == hora
        }

        if (horarioOcupado) {
            return null
        }

        val nuevaCita = Cita(
            id = siguienteCitaId,
            usuarioId = usuario.id,
            medicoId = medicoId,
            fecha = fecha,
            hora = hora,
            motivoConsulta = motivoConsulta.trim()
        )

        citas.add(nuevaCita)
        siguienteCitaId++

        return nuevaCita
    }

    fun obtenerCita(
        id: Int
    ): Cita? {

        val usuario = usuarioActual ?: return null

        return citas.find {
            it.id == id &&
                    it.usuarioId == usuario.id
        }
    }

    fun citasDelUsuario(
        usuarioId: Int
    ): List<Cita> {

        return citas
            .filter {
                it.usuarioId == usuarioId
            }
            .sortedWith(
                compareBy<Cita> {
                    it.fecha
                }.thenBy {
                    it.hora
                }
            )
    }

    fun cancelarCita(
        id: Int
    ): Boolean {

        val cita = obtenerCita(id) ?: return false

        return citas.remove(cita)
    }
}