
package com.alvarez.avisoscampus

import android.app.Application
import com.alvarez.avisoscampus.notificacion.Notificador

class AvisosApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Notificador.crearCanal(this)
    }
}
