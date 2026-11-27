
package com.alvarez.avisoscampus.notificacion

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.alvarez.avisoscampus.MainActivity
import com.alvarez.avisoscampus.R
import com.alvarez.avisoscampus.model.Aviso
import com.alvarez.avisoscampus.model.Origen

object Notificador {

    const val CANAL = "avisos"
    const val CANAL_URGENTE = "urgentes"

    fun crearCanal(context: Context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val canal = NotificationChannel(
                CANAL,
                "Avisos",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones generales"
            }

            val canalUrgente = NotificationChannel(
                CANAL_URGENTE,
                "Urgentes",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Recordatorios importantes"
                enableVibration(true)
            }

            val manager = context.getSystemService(
                NotificationManager::class.java
            )

            manager.createNotificationChannels(
                listOf(canal, canalUrgente)
            )
        }
    }

    fun tienePermiso(context: Context): Boolean {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return true
        }

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun mostrar(
        context: Context,
        aviso: Aviso
    ): Boolean {

        if (!tienePermiso(context)) {
            return false
        }

        val esUrgente = aviso.origen == Origen.RECORDATORIO

        val canal = if (esUrgente) {
            CANAL_URGENTE
        } else {
            CANAL
        }

        val prioridad = if (esUrgente) {
            NotificationCompat.PRIORITY_HIGH
        } else {
            NotificationCompat.PRIORITY_DEFAULT
        }

        val intent = Intent(
            context,
            MainActivity::class.java
        )

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val textoCompleto = when (aviso.origen) {

            Origen.BOTON ->
                "${aviso.texto}. Recuerda revisar tus actividades " +
                        "académicas y verificar que tus entregas estén completas."

            Origen.RECORDATORIO ->
                "${aviso.texto}. Recuerda completar tus actividades " +
                        "y revisar las fechas de entrega de tus laboratorios."

            Origen.MODO_AVION ->
                "Modo avión: ${aviso.texto}. " +
                        "Verifica el estado de conectividad de tu dispositivo."
        }

        val notificacion = NotificationCompat.Builder(
            context,
            canal
        )
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(textoCompleto)
                    .setBigContentTitle(aviso.titulo)
                    .setSummaryText("AvisosCampus")
            )
            .setPriority(prioridad)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(aviso.id, notificacion)

        return true
    }
}
