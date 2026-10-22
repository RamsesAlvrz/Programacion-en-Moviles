package com.alvarez.lavacampus

import android.app.Application
import androidx.room.Room
import com.alvarez.lavacampus.data.local.LavaDatabase
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.local.entity.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// Clase principal encargada de inicializar la base de datos
class LavaApp : Application() {

    // Base de datos disponible para toda la aplicación
    lateinit var database: LavaDatabase
        private set

    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    override fun onCreate() {
        super.onCreate()

        // Crear la instancia de Room
        database = Room.databaseBuilder(
            applicationContext,
            LavaDatabase::class.java,
            "lavacampus_database"
        ).build()

        // Registrar los datos iniciales
        applicationScope.launch {
            insertarDatosIniciales()
        }
    }

    private suspend fun insertarDatosIniciales() {

        // Registrar administrador inicial si todavía no existe
        val administrador = database.usuarioDao()
            .obtenerPorCorreo("admin@lavacampus.com")

        if (administrador == null) {
            database.usuarioDao().insertar(
                Usuario(
                    nombre = "Administrador",
                    codigo = "",
                    correo = "admin@lavacampus.com",
                    clave = "admin123",
                    rol = "ADMIN"
                )
            )
        }

        // Registrar servicios iniciales solo si no existen
        val servicioInicial = database.servicioDao().obtenerPorId(1)

        if (servicioInicial == null) {
            database.servicioDao().insertarTodos(
                listOf(
                    Servicio(
                        nombre = "Lavado y secado",
                        descripcion = "Lavado y secado de ropa por kilogramo",
                        unidad = "KG",
                        precio = 8.0,
                        horasEntrega = 24,
                        icono = "wash"
                    ),
                    Servicio(
                        nombre = "Planchado",
                        descripcion = "Planchado de prendas de vestir",
                        unidad = "PRENDA",
                        precio = 4.0,
                        horasEntrega = 12,
                        icono = "iron"
                    ),
                    Servicio(
                        nombre = "Lavado de prendas",
                        descripcion = "Lavado individual de prendas",
                        unidad = "PRENDA",
                        precio = 6.0,
                        horasEntrega = 24,
                        icono = "shirt"
                    )
                )
            )
        }
    }
}
