package com.alvarez.consola

fun main() {

    println("===== SISTEMA DE MATRICULA UNIVERSITARIA =====")

    // Datos del estudiante
    print("Nombre del estudiante: ")
    val nombreEstudiante = readln().trim().ifEmpty {
        "Estudiante"
    }

    val cantidadCursos = pedirEntero(
        mensaje = "Cantidad de cursos a matricular: ",
        min = 1
    )

    val valorCredito = pedirDecimal(
        mensaje = "Valor de cada credito (S/): ",
        min = 0.01
    )

    for (i in 1..cantidadCursos) {

        println("\n--- Curso $i ---")

        print("Nombre del curso: ")
        val nombreCurso = readln().trim().ifEmpty {
            "Curso $i"
        }

        val creditos = pedirEntero(
            mensaje = "Creditos del curso: ",
            min = 1
        )

        println("Curso registrado: $nombreCurso")
        println("Creditos registrados: $creditos")
    }
}


/**
 * Solicita un número entero válido.
 */
fun pedirEntero(mensaje: String, min: Int): Int {

    while (true) {

        print(mensaje)

        val valor = readln()
            .trim()
            .toIntOrNull()

        if (valor != null && valor >= min) {
            return valor
        }

        println("Valor invalido.")
    }
}


/**
 * Solicita un número decimal válido.
 */
fun pedirDecimal(mensaje: String, min: Double): Double {

    while (true) {

        print(mensaje)

        val valor = readln()
            .trim()
            .replace(",", ".")
            .toDoubleOrNull()

        if (valor != null && valor >= min) {
            return valor
        }

        println("Valor invalido.")
    }
}