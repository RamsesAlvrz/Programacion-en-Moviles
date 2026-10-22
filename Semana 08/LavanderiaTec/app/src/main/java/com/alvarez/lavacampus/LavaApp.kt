
package com.alvarez.lavacampus

import android.app.Application
import androidx.room.Room
import com.alvarez.lavacampus.data.local.LavaDatabase
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.prefs.PreferenciasStore
import com.alvarez.lavacampus.data.repository.LavaRepository
import com.alvarez.lavacampus.data.repository.LavaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// Clase principal de la aplicación LavaCampus
class LavaApp : Application() {

    // Base de datos Room
    lateinit var database: LavaDatabase
        private set

    // Repositorio para acceder a los datos
    lateinit var repository: LavaRepository
        private set

    // Preferencias para gestionar la sesión
    lateinit var preferenciasStore: PreferenciasStore
        private set

    // Alcance para ejecutar operaciones en segundo plano
    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    override fun onCreate() {
        super.onCreate()

        // Inicializar la base de datos Room
        database = Room.databaseBuilder(
            applicationContext,
            LavaDatabase::class.java,
            "lavacampus_database"
        ).build()

        // Inicializar el repositorio
        repository = LavaRepositoryRoom(database)

        // Inicializar las preferencias del usuario
        preferenciasStore = PreferenciasStore(applicationContext)

        // Registrar los datos iniciales
        applicationScope.launch {
            insertarDatosIniciales()
        }
    }

    // Insertar los registros iniciales de LavaCampus
    private suspend fun insertarDatosIniciales() {

        // Comprobar si existe el administrador
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

        // Comprobar si existen los servicios iniciales
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
