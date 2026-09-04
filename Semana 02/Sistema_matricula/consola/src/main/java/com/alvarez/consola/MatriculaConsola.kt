package com.alvarez.consola

import java.util.Locale

const val NOMBRE_INSTITUCION = "UNIVERSIDAD TECNOLÓGICA"

const val MAX_CREDITOS_SIN_PERMISO = 18
const val LIMITE_CARGA_PARCIAL = 12
const val LIMITE_PAGO_TRES_CUOTAS = 2500.0

fun main() {

    println("===== SISTEMA DE MATRICULA - $NOMBRE_INSTITUCION =====")

    val aforoMaximo = pedirEntero(
        mensaje = "Ingrese el aforo maximo de la institucion: ",
        min = 1
    )

    var contadorEstudiantes = 0

    while (true) {

        if (contadorEstudiantes >= aforoMaximo) {
            println("-------------------------------------------------------")
            println("¡ALERTA DE AFORO! Se ha alcanzado el limite maximo de $aforoMaximo estudiantes.")
            println("El sistema prohibe registrar mas matriculas.")
            println("=======================================================")
            break
        }

        println("\n>>> REGISTRO DE ESTUDIANTE #${contadorEstudiantes + 1} (Disponibles: ${aforoMaximo - contadorEstudiantes}) <<<")

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

                    val reintentar = pedirRespuestaSiNo("\n¿Desea intentar matricular a otro estudiante? (S/N): ")
                    if (!reintentar) break else continue
                }
            }
        }

        val cuotas = if (totalPagar > LIMITE_PAGO_TRES_CUOTAS) 3 else 2
        val montoCuota = totalPagar / cuotas

        contadorEstudiantes++

        mostrarResultado(
            nombreEstudiante = nombreEstudiante,
            cantidadCursos = cantidadCursos,
            totalCreditos = totalCreditos,
            totalPagar = totalPagar,
            cargaAcademica = cargaAcademica,
            cuotas = cuotas,
            montoCuota = montoCuota,
            detalleCursos = detalleCursos
        )

        if (contadorEstudiantes < aforoMaximo) {
            val continuar = pedirRespuestaSiNo("\n¿Desea registrar a otro estudiante? (S/N): ")
            if (!continuar) {
                println("\nSaliendo del sistema de matricula. ¡Hasta luego!")
                break
            }
        }
    }
}

fun pedirEntero(mensaje: String, min: Int): Int {
    while (true) {
        print(mensaje)
        val valor = readln().trim().toIntOrNull()
        if (valor != null && valor >= min) return valor
        println("Valor invalido. Debe ingresar un numero entero mayor o igual a $min.")
    }
}

fun pedirDecimal(mensaje: String, min: Double): Double {
    while (true) {
        print(mensaje)
        val valor = readln().trim().replace(",", ".").toDoubleOrNull()
        if (valor != null && valor >= min) return valor
        println("Valor invalido. Debe ingresar un numero valido mayor o igual a $min.")
    }
}

fun pedirRespuestaSiNo(mensaje: String): Boolean {
    while (true) {
        print(mensaje)
        when (readln().trim().uppercase()) {
            "S", "SI", "SÍ" -> return true
            "N", "NO" -> return false
            else -> println("Respuesta invalida. Ingrese S para Si o N para No.")
        }
    }
}

fun mostrarResultado(
    nombreEstudiante: String,
    cantidadCursos: Int,
    totalCreditos: Int,
    totalPagar: Double,
    cargaAcademica: String,
    cuotas: Int,
    montoCuota: Double,
    detalleCursos: StringBuilder
) {
    println("\n================ RESULTADO FINAL ================")
    println("Estudiante: $nombreEstudiante\n")
    println(String.format("%-25s %-10s %-10s", "Curso", "Creditos", "Costo"))
    println("-------------------------------------------------------")
    print(detalleCursos)
    println("-------------------------------------------------------")
    println("Cursos matriculados : $cantidadCursos")
    println("Total de creditos   : $totalCreditos")
    println("Total a pagar       : S/ ${String.format(Locale.US, "%.2f", totalPagar)}")
    println("Carga academica     : $cargaAcademica")
    println("Forma de pago       : $cuotas cuotas de S/ ${String.format(Locale.US, "%.2f", montoCuota)}")
    println("=======================================================")
}