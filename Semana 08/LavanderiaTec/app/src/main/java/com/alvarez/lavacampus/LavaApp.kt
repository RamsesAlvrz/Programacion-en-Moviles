
package com.alvarez.lavacampus

import android.app.Application
import androidx.room.Room
import com.alvarez.lavacampus.data.local.LavaDatabase
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.repository.LavaRepository
import com.alvarez.lavacampus.data.repository.LavaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// Clase principal de la aplicación LavaCampus
class LavaApp : Application() {

    // Base de datos Room disponible para toda la aplicación
    lateinit var database: LavaDatabase
        private set

    // Repositorio que utilizarán los ViewModel
    lateinit var repository: LavaRepository
        private set

    // Alcance para ejecutar operaciones en segundo plano
    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    override fun onCreate() {
        super.onCreate()

        // Crear la instancia de la base de datos
        database = Room.databaseBuilder(
            applicationContext,
            LavaDatabase::class.java,
            "lavacampus_database"
        ).build()

        // Conectar el repositorio con la base de datos Room
        repository = LavaRepositoryRoom(database)

        // Registrar los datos iniciales de la aplicación
        applicationScope.launch {
            insertarDatosIniciales()
        }
    }

    // Insertar los registros necesarios para las pruebas
    private suspend fun insertarDatosIniciales() {

        // Verificar si ya existe el administrador
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
