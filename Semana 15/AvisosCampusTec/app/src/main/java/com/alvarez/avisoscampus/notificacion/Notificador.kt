
package com.alvarez.avisoscampus.notificacion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.alvarez.avisoscampus.model.Aviso

object Notificador {

    const val CANAL = "avisos"

    fun crearCanal(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val canal = NotificationChannel(
                CANAL,
                "Avisos",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones de la app"
            }

            val manager = context.getSystemService(
                NotificationManager::class.java
            )

            manager.createNotificationChannel(canal)
        }
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
