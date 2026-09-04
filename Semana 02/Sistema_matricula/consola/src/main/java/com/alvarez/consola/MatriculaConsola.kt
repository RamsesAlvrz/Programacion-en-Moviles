package com.alvarez.consola

import java.util.Locale

const val MAX_CREDITOS_SIN_PERMISO = 18
const val LIMITE_CARGA_PARCIAL = 12
const val LIMITE_PAGO_TRES_CUOTAS = 2500.0

fun main() {

    println("===== SISTEMA DE MATRICULA UNIVERSITARIA =====")

    // Datos del estudiante
    print("Nombre del estudiante: ")
    val nombreEstudiante = readln().trim().ifEmpty { "Estudiante" }

    val cantidadCursos = pedirEntero(
        mensaje = "Cantidad de cursos a matricular: ",
        min = 1
    )

    val valorCredito = pedirDecimal(
        mensaje = "Valor de cada credito (S/): ",
        min = 0.01
    )

    var totalCreditos = 0
    var totalPagar = 0.0

    val detalleCursos = StringBuilder()

    // Registro de cursos
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

        val costoCurso = creditos * valorCredito

        totalCreditos += creditos
        totalPagar += costoCurso

        detalleCursos.append(
            String.format(
                Locale.US,
                "%-25s %-10d S/ %-10.2f%n",
                nombreCurso,
                creditos,
                costoCurso
            )
        )
    }

    // Determinar carga académica
    val cargaAcademica = when {

        totalCreditos <= LIMITE_CARGA_PARCIAL -> {
            "Carga Parcial"
        }

        totalCreditos <= MAX_CREDITOS_SIN_PERMISO -> {
            "Carga Completa"
        }

        else -> {

            println(
                "\nSu carga ($totalCreditos creditos) supera los " +
                        "$MAX_CREDITOS_SIN_PERMISO creditos."
            )

            val tienePermiso = pedirRespuestaSiNo(
                "Requiere Permiso Autorizado. " +
                        "¿Cuenta con dicha autorizacion? (S/N): "
            )

            if (tienePermiso) {
                "Permiso Autorizado"
            } else {
                println(
                    "\nNo cuenta con autorizacion. " +
                            "No es posible matricular esta carga academica."
                )
                return
            }
        }
    }

    // Determinar número de cuotas
    val cuotas = if (totalPagar > LIMITE_PAGO_TRES_CUOTAS) {
        3
    } else {
        2
    }

    val montoCuota = totalPagar / cuotas
}


/**
 * Solicita un número entero mayor o igual al mínimo indicado.
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

        println("Valor invalido. Debe ingresar un numero entero mayor o igual a $min.")
    }
}


/**
 * Solicita un número decimal mayor o igual al mínimo indicado.
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

        println("Valor invalido. Debe ingresar un numero valido mayor o igual a $min.")
    }
}


/**
 * Solicita una respuesta de tipo Sí o No.
 */
fun pedirRespuestaSiNo(mensaje: String): Boolean {

    while (true) {

        print(mensaje)

        when (readln().trim().uppercase()) {

            "S", "SI", "SÍ" -> return true

            "N", "NO" -> return false

            else -> {
                println("Respuesta invalida. Ingrese S para Si o N para No.")
            }
        }
    }
}