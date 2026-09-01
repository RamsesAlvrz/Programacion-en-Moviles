package com.alvarez.consola

fun main() {
    println("===== SISTEMA DE MATRICULA UNIVERSITARIA =====")

    print("Nombre del estudiante: ")
    val nombreEstudiante = readln().trim().ifEmpty { "Estudiante" }

    val cantidadCursos = pedirEntero("Cantidad de cursos a matricular: ", min = 1)
    val valorCredito = pedirDecimal("Valor de cada credito (S/): ", min = 0.01)

    var totalCreditos = 0
    var totalPagar = 0.0
    val detalleCursos = StringBuilder()

    for (i in 1..cantidadCursos) {
        println("--- Curso $i ---")
        print("Nombre del curso: ")
        val nombreCurso = readln().trim().ifEmpty { "Curso $i" }

        val creditos = pedirEntero("Creditos del curso: ", min = 1)
        val costoCurso = creditos * valorCredito

        totalCreditos += creditos
        totalPagar += costoCurso

        detalleCursos.append(String.format("%-20s %-10d S/ %-8.2f\n", nombreCurso, creditos, costoCurso))
    }

    val cargaAcademica = when {
        totalCreditos <= 12 -> "Carga Parcial"
        totalCreditos in 13..18 -> "Carga Completa"
        else -> {
            println("\nSu carga ($totalCreditos creditos) supera los 18 creditos.")
            print("Requiere Permiso Autorizado. Cuenta con dicha autorizacion? (S/N): ")
            if (readln().trim().uppercase() == "S") {
                "Permiso Autorizado"
            } else {
                println("\nNo cuenta con autorizacion. No es posible matricular esta carga academica.")
                return
            }
        }
    }

    val cuotas = if (totalPagar > 2500) 3 else 2
    val montoCuota = totalPagar / cuotas

    println("\n================ RESULTADO FINAL ================")
    println("Estudiante: $nombreEstudiante\n")
    println(String.format("%-20s %-10s %-10s", "Curso", "Creditos", "Costo"))
    println("---------------------------------------------")
    print(detalleCursos)
    println("---------------------------------------------")
    println("Cursos matriculados : $cantidadCursos")
    println("Total de creditos   : $totalCreditos")
    println("Total a pagar       : S/ ${String.format("%.2f", totalPagar)}")
    println("Carga academica     : $cargaAcademica")
    println("Forma de pago       : $cuotas cuotas de S/ ${String.format("%.2f", montoCuota)}")
    println("===================================================")
}

fun pedirEntero(mensaje: String, min: Int): Int {
    while (true) {
        print(mensaje)
        val valor = readln().trim().toIntOrNull()
        if (valor != null && valor >= min) return valor
        print("Valor invalido. ")
    }
}

fun pedirDecimal(mensaje: String, min: Double): Double {
    while (true) {
        print(mensaje)
        val valor = readln().trim().toDoubleOrNull()
        if (valor != null && valor >= min) return valor
        print("Valor invalido. ")
    }
}