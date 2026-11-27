
package com.alvarez.avisoscampus.notificacion

import android.content.Context
import com.alvarez.avisoscampus.model.Aviso

object Notificador {

    const val CANAL = "avisos"

    fun crearCanal(context: Context) {
        // TODO Paso 1: crear canal de notificaciones
    }

    fun tienePermiso(context: Context): Boolean {
        // TODO Paso 2: comprobar permiso
        return false
    }

    fun mostrar(
        context: Context,
        aviso: Aviso
    ): Boolean {
        // TODO Paso 3: mostrar notificación
        // TODO Paso 4: agregar PendingIntent
        return false
    }
}
