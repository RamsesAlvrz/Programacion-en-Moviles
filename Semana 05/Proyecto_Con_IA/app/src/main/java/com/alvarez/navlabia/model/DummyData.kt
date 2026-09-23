package com.alvarez.navlabia.model

object DummyData {
    val alumnos = listOf(
        Alumno(
            id = 1,
            nombre = "Juan León",
            carrera = "Ingeniería de Sistemas",
            correo = "juan.leon@example.com",
            facultad = "Ingeniería y Tecnología",
            biografia = "Estudiante destacado con interés en desarrollo Android.",
            fotoUrl = "https://i.pravatar.cc/300?img=12"
        ),
        Alumno(
            id = 2,
            nombre = "María García",
            carrera = "Arquitectura",
            correo = "maria.garcia@example.com",
            facultad = "Arquitectura y Diseño",
            biografia = "Apasionada por el diseño sostenible y el urbanismo moderno.",
            fotoUrl = "https://i.pravatar.cc/300?img=47"
        ),
        Alumno(
            id = 3,
            nombre = "Carlos Perez",
            carrera = "Medicina",
            correo = "carlos.perez@example.com",
            facultad = "Ciencias de la Salud",
            biografia = "Interesado en la investigación médica y salud comunitaria.",
            fotoUrl = "https://i.pravatar.cc/300?img=33"
        ),
        Alumno(
            id = 4,
            nombre = "Ana Lopez",
            carrera = "Derecho",
            correo = "ana.lopez@example.com",
            facultad = "Derecho y Ciencias Políticas",
            biografia = "Especializándose en derecho corporativo y propiedad intelectual.",
            fotoUrl = "https://i.pravatar.cc/300?img=26"
        ),
        Alumno(
            id = 5,
            nombre = "Luis Ramírez",
            carrera = "Administración",
            correo = "luis.ramirez@example.com",
            facultad = "Ciencias Empresariales",
            biografia = "Enfocado en la gestión de proyectos y emprendimiento tecnológico.",
            fotoUrl = "https://i.pravatar.cc/300?img=60"
        )
    )

    fun getAlumnoById(id: Int): Alumno {
        return alumnos.find { it.id == id } ?: alumnos.first()
    }

    // Configuración de Perfil (Usuario logueado)
    const val userNombreCompleto = "Juan León Suiyon"
    const val userCorreo = "juan.leon@tecsup.edu.pe"
    const val userTelefono = "+51 987 654 321"
    const val userCarrera = "Ingeniería de Software"
    const val userCiclo = "VI Ciclo"
    const val userFotoUrl = "https://i.pravatar.cc/300?img=12"
}
