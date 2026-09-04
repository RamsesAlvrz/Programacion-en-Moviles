package com.alvarez.consola

import java.util.Locale

// Constante fija de la institución
const val NOMBRE_INSTITUCION = "UNIVERSIDAD TECNOLÓGICA"

// Constantes de matrícula
const val MAX_CREDITOS_SIN_PERMISO = 18
const val LIMITE_CARGA_PARCIAL = 12
const val LIMITE_PAGO_TRES_CUOTAS = 2500.0

// Constantes de recargos e impuestos
const val RECARGO_MANANA = 0.10
const val RECARGO_TARDE = 0.15
const val RECARGO_NOCHE = 0.20
const val IGV = 0.18

fun main() {

    println("===== SISTEMA DE MATRICULA - $NOMBRE_INSTITUCION =====")

    // Entrada del aforo máximo por consola
    val aforoMaximo = pedirEntero(
        mensaje = "Ingrese el aforo maximo de la institucion: ",
        min = 1
    )

    var contadorEstudiantes = 0

    while (true) {

        // Control de aforo
        if (contadorEstudiantes >= aforoMaximo) {
            println("-------------------------------------------------------")
            println("¡ALERTA DE AFORO! Se ha alcanzado el limite maximo de $aforoMaximo estudiantes.")
            println("El sistema prohibe registrar mas matriculas.")
            println("=======================================================")
            break
        }

        println("\n>>> REGISTRO DE ESTUDIANTE #${contadorEstudiantes + 1} (Disponibles: ${aforoMaximo - contadorEstudiantes}) <<<")

        // Datos del estudiante
        print("Nombre del estudiante: ")
        val nombreEstudiante = readln().trim().ifEmpty { "Estudiante" }

        val turno = pedirTurno("Turno (M: Manana [+10%], T: Tarde [+15%], N: Noche [+20%]): ")
        val categoria = pedirCategoria("Categoria (O: Ordinario, B: Becado): ")

        // Solicitud condicional de matrícula (Manual para Ordinario, Automático 0 para Becado)
        val costoMatricula = if (categoria == "Becado") {
            println("Colocar el precio de matricula: 0")
            0.0
        } else {
            pedirDecimal("Colocar el precio de matricula: ", min = 0.0)
        }

        val cantidadCursos = pedirEntero(
            mensaje = "Cantidad de cursos a matricular: ",
            min = 1
        )

        val valorCredito = pedirDecimal(
            mensaje = "Valor de cada credito (S/): ",
            min = 0.01
        )

        var totalCreditos = 0
        var subtotalCursos = 0.0

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
            subtotalCursos += costoCurso

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

                    val reintentar = pedirRespuestaSiNo("\n¿Desea intentar matricular a otro estudiante? (S/N): ")
                    if (!reintentar) break else continue
                }
            }
        }

        // CÁLCULOS (Matrícula + Cursos + Recargo Turno + IGV)
        val porcentajeTurno = when (turno) {
            "Manana" -> RECARGO_MANANA
            "Tarde" -> RECARGO_TARDE
            "Noche" -> RECARGO_NOCHE
            else -> 0.0
        }

        val subtotalBase = costoMatricula + subtotalCursos
        val montoRecargoTurno = subtotalBase * porcentajeTurno
        val baseConRecargo = subtotalBase + montoRecargoTurno
        val montoIGV = baseConRecargo * IGV
        val totalPagar = baseConRecargo + montoIGV

        // Determinar número de cuotas y monto
        val cuotas = if (totalPagar > LIMITE_PAGO_TRES_CUOTAS) 3 else 2
        val montoCuota = totalPagar / cuotas

        // Incrementar contador de aforo
        contadorEstudiantes++

        // Mostrar resultado
        mostrarResultado(
            nombreEstudiante = nombreEstudiante,
            turno = turno,
            categoria = categoria,
            costoMatricula = costoMatricula,
            cantidadCursos = cantidadCursos,
            totalCreditos = totalCreditos,
            subtotalCursos = subtotalCursos,
            montoRecargoTurno = montoRecargoTurno,
            montoIGV = montoIGV,
            totalPagar = totalPagar,
            cargaAcademica = cargaAcademica,
            cuotas = cuotas,
            montoCuota = montoCuota,
            detalleCursos = detalleCursos
        )

        // Consultar si desea seguir matriculando dentro del aforo
        if (contadorEstudiantes < aforoMaximo) {
            val continuar = pedirRespuestaSiNo("\n¿Desea registrar a otro estudiante? (S/N): ")
            if (!continuar) {
                println("\nSaliendo del sistema de matricula. ¡Hasta luego!")
                break
            }
        }
    }
}


/**
 * Solicita el turno del estudiante.
 */
fun pedirTurno(mensaje: String): String {

    while (true) {

        print(mensaje)

        when (readln().trim().uppercase()) {

            "M", "MANANA", "MAÑANA" -> return "Manana"

            "T", "TARDE" -> return "Tarde"

            "N", "NOCHE" -> return "Noche"

            else -> {
                println("Turno invalido. Ingrese M (Manana), T (Tarde) o N (Noche).")
            }
        }
    }
}


/**
 * Solicita la categoría del estudiante.
 */
fun pedirCategoria(mensaje: String): String {

    while (true) {

        print(mensaje)

        when (readln().trim().uppercase()) {

            "O", "ORDINARIO" -> return "Ordinario"

            "B", "BECADO" -> return "Becado"

            else -> {
                println("Categoria invalida. Ingrese O (Ordinario) o B (Becado).")
            }
        }
    }
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


/**
 * Muestra el resumen final de la matrícula.
 */
fun mostrarResultado(
    nombreEstudiante: String,
    turno: String,
    categoria: String,
    costoMatricula: Double,
    cantidadCursos: Int,
    totalCreditos: Int,
    subtotalCursos: Double,
    montoRecargoTurno: Double,
    montoIGV: Double,
    totalPagar: Double,
    cargaAcademica: String,
    cuotas: Int,
    montoCuota: Double,
    detalleCursos: StringBuilder
) {

    println("\n================ RESULTADO FINAL ================")

    println("Estudiante          : $nombreEstudiante")
    println("Turno               : $turno")
    println("Categoria           : $categoria\n")

    println(
        String.format(
            "%-25s %-10s %-10s",
            "Curso",
            "Creditos",
            "Costo"
        )
    )

    println("-------------------------------------------------------")

    print(detalleCursos)

    println("-------------------------------------------------------")

    println("Cursos matriculados : $cantidadCursos")
    println("Total de creditos   : $totalCreditos")

    println(
        "Costo matricula     : S/ ${
            String.format(Locale.US, "%.2f", costoMatricula)
        }"
    )

    println(
        "Subtotal cursos     : S/ ${
            String.format(Locale.US, "%.2f", subtotalCursos)
        }"
    )

    println(
        "Recargo por turno   : S/ ${
            String.format(Locale.US, "%.2f", montoRecargoTurno)
        }"
    )

    println(
        "IGV (18%)           : S/ ${
            String.format(Locale.US, "%.2f", montoIGV)
        }"
    )

    println(
        "Total a pagar       : S/ ${
            String.format(Locale.US, "%.2f", totalPagar)
        }"
    )

    println("Carga academica     : $cargaAcademica")

    println(
        "Forma de pago       : $cuotas cuotas de S/ ${
            String.format(Locale.US, "%.2f", montoCuota)
        }"
    )

    println("=======================================================")
}