package com.alvarez.lavacampus

import android.app.Application
import androidx.room.Room
import androidx.room.withTransaction
import com.alvarez.lavacampus.data.local.DatosIniciales
import com.alvarez.lavacampus.data.local.LavaDatabase
import com.alvarez.lavacampus.data.prefs.PreferenciasStore
import com.alvarez.lavacampus.data.repository.LavaRepository
import com.alvarez.lavacampus.data.repository.LavaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
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

    // Alcance para operaciones en segundo plano
    private val applicationScope = CoroutineScope(
        SupervisorJob() + Dispatchers.IO
    )

    override fun onCreate() {
        super.onCreate()

        // Inicializar Room
        database = Room.databaseBuilder(
            applicationContext,
            LavaDatabase::class.java,
            "lavacampus_database"
        )
            .addMigrations(LavaDatabase.MIGRATION_1_2)
            .build()

        // Inicializar el repositorio
        repository = LavaRepositoryRoom(database)

        // Inicializar las preferencias
        preferenciasStore = PreferenciasStore(applicationContext)

        // Registrar datos iniciales y actualizar nombres de prueba
        applicationScope.launch {
            insertarDatosIniciales()
        }
    }

    // Cargar los datos de prueba sin duplicar registros
    private suspend fun insertarDatosIniciales() {

        val usuarioDao = database.usuarioDao()
        val servicioDao = database.servicioDao()
        val pedidoDao = database.pedidoDao()
        val detallePedidoDao = database.detallePedidoDao()

        // PASO 1: Insertar usuarios iniciales y completar sus apellidos
        for (usuario in DatosIniciales.usuarios) {

            val existente = usuarioDao.obtenerPorCorreo(usuario.correo)

            if (existente == null) {

                // Registrar el usuario cuando todavía no existe
                usuarioDao.insertar(usuario)

            } else {

                // Identificar los nombres originales de los estudiantes de prueba
                val nombreAnterior = when (usuario.correo) {
                    "ana@correo.test" -> "Ana"
                    "luis@correo.test" -> "Luis"
                    "marta@correo.test" -> "Marta"
                    else -> null
                }

                // Actualizar solo si el estudiante conserva su nombre original.
                // Así evitamos sobrescribir nombres personalizados.
                if (
                    nombreAnterior != null &&
                    existente.nombre == nombreAnterior &&
                    existente.nombre != usuario.nombre
                ) {
                    usuarioDao.actualizar(
                        existente.copy(
                            nombre = usuario.nombre
                        )
                    )
                }
            }
        }

        // PASO 2: Sincronizar los servicios con el catálogo de la guía
        val serviciosGuardados = servicioDao.obtenerTodos().first()

        for (servicioInicial in DatosIniciales.servicios) {

            // Buscar si el servicio ya existe por su nombre
            val servicioExistente = serviciosGuardados.find {
                it.nombre.equals(
                    servicioInicial.nombre,
                    ignoreCase = true
                )
            }

            if (servicioExistente == null) {

                // Insertar el servicio cuando todavía no existe
                servicioDao.insertar(servicioInicial)

            } else {

                // Actualizar los datos conservando el ID original
                servicioDao.actualizar(
                    servicioInicial.copy(
                        id = servicioExistente.id
                    )
                )
            }
        }

        // Desactivar el servicio antiguo que no pertenece al catálogo
        val servicioAnterior = serviciosGuardados.find {
            it.nombre.equals(
                "Lavado de prendas",
                ignoreCase = true
            )
        }

        if (servicioAnterior != null && servicioAnterior.activo) {
            servicioDao.cambiarDisponibilidad(
                servicioId = servicioAnterior.id,
                activo = false
            )
        }

        // PASO 3: Recuperar los identificadores de estudiantes
        val ana = usuarioDao.obtenerPorCorreo("ana@correo.test")
        val luis = usuarioDao.obtenerPorCorreo("luis@correo.test")
        val marta = usuarioDao.obtenerPorCorreo("marta@correo.test")

        if (ana == null || luis == null || marta == null) {
            return
        }

        // PASO 4: Recuperar los identificadores de servicios
        val servicios = servicioDao.obtenerTodos().first()

        val lavado = servicios.find {
            it.nombre == "Lavado y secado"
        }

        val planchado = servicios.find {
            it.nombre == "Planchado"
        }

        val lavadoSeco = servicios.find {
            it.nombre == "Lavado en seco"
        }

        val edredon = servicios.find {
            it.nombre == "Edredón / frazada"
        }

        if (
            lavado == null ||
            planchado == null ||
            lavadoSeco == null ||
            edredon == null
        ) {
            return
        }

        // PASO 5: Evitar insertar los pedidos iniciales más de una vez
        val pedidosExistentes = pedidoDao.obtenerTodos().first()

        if (pedidosExistentes.isNotEmpty()) {
            return
        }

        // PASO 6: Preparar los 10 pedidos de ejemplo
        val pedidosIniciales = DatosIniciales.obtenerPedidosIniciales(
            anaId = ana.id,
            luisId = luis.id,
            martaId = marta.id
        )

        // PASO 7: Guardar pedidos y detalles en una transacción
        database.withTransaction {

            pedidosIniciales.forEachIndexed { indice, pedido ->

                // Insertar el pedido y obtener su ID
                val pedidoId = pedidoDao.insertar(pedido).toInt()

                // Preparar los detalles asociados al pedido
                val detalles = DatosIniciales.obtenerDetallesIniciales(
                    numeroPedido = indice + 1,
                    pedidoId = pedidoId,
                    lavadoId = lavado.id,
                    planchadoId = planchado.id,
                    lavadoSecoId = lavadoSeco.id,
                    edredonId = edredon.id
                )

                // Insertar los detalles
                detallePedidoDao.insertarTodos(detalles)
            }
        }
    }
}