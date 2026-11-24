package com.alvarez.murocampus.data

import java.io.IOException
import retrofit2.HttpException
import kotlinx.serialization.SerializationException

fun mensajeDe(e: Throwable): String {
    return when (e) {
        is IOException ->
            "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."

        is HttpException ->
            "Error del servidor (${e.code()})"

        is SerializationException ->
            "Respuesta inesperada"

        else ->
            "Error desconocido"
    }
}