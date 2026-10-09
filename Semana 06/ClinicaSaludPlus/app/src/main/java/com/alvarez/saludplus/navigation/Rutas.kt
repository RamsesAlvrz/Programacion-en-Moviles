package com.alvarez.saludplus.navigation

import android.net.Uri

object Rutas {

    const val SPLASH = "splash"
    const val REGISTRO = "registro"
    const val LOGIN = "login"

    const val HOME = "home"

    const val ESPECIALIDADES = "especialidades"
    const val MEDICOS = "medicos/{especialidadId}"
    const val FECHA_HORA = "fecha_hora/{medicoId}"
    const val CONFIRMAR = "confirmar/{medicoId}/{fecha}/{hora}"
    const val EXITOSA = "exitosa/{citaId}"

    const val CITAS = "citas"
    const val DETALLE_CITA = "detalle_cita/{citaId}"

    const val RESULTADOS = "resultados"
    const val PERFIL = "perfil"
    const val NOTIFICACIONES = "notificaciones"
    const val TERMINOS = "terminos"

    fun medicos(
        especialidadId: Int
    ): String {
        return "medicos/$especialidadId"
    }

    fun fechaHora(
        medicoId: Int
    ): String {
        return "fecha_hora/$medicoId"
    }

    fun confirmar(
        medicoId: Int,
        fecha: String,
        hora: String
    ): String {
        return "confirmar/$medicoId/${Uri.encode(fecha)}/${Uri.encode(hora)}"
    }

    fun exitosa(
        citaId: Int
    ): String {
        return "exitosa/$citaId"
    }

    fun detalleCita(
        citaId: Int
    ): String {
        return "detalle_cita/$citaId"
    }
}