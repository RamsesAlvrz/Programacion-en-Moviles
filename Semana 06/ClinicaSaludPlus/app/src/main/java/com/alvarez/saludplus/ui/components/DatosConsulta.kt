package com.alvarez.saludplus.ui.components

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DatosConsulta {
    const val DIRECCION = "Av. Los Olivos 123 · Lince"
    const val TIPO_ATENCION = "Consulta presencial"

    fun fechaEnEspanol(fecha: String): String {
        val locale = Locale("es", "PE")

        return runCatching {
            LocalDate.parse(fecha)
                .format(
                    DateTimeFormatter.ofPattern(
                        "EEEE d 'de' MMMM 'de' yyyy",
                        locale
                    )
                )
                .replaceFirstChar { it.titlecase(locale) }
        }.getOrElse {
            fecha
        }
    }
}