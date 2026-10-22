package com.alvarez.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alvarez.lavacampus.data.local.entity.Servicio
import kotlinx.coroutines.flow.Flow

// DAO encargado de gestionar los servicios de lavandería
@Dao
interface ServicioDao {

    // Registrar un nuevo servicio
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(servicio: Servicio): Long

    // Registrar varios servicios
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(servicios: List<Servicio>)

    // Actualizar los datos de un servicio
    @Update
    suspend fun actualizar(servicio: Servicio)

    // Buscar un servicio por su identificador
    @Query("SELECT * FROM servicios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Servicio?

    // Consultar todos los servicios
    @Query("SELECT * FROM servicios ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<Servicio>>

    // Consultar únicamente los servicios disponibles
    @Query("SELECT * FROM servicios WHERE activo = 1 ORDER BY nombre ASC")
    fun obtenerActivos(): Flow<List<Servicio>>

    // Cambiar la disponibilidad de un servicio
    @Query("UPDATE servicios SET activo = :activo WHERE id = :servicioId")
    suspend fun cambiarDisponibilidad(
        servicioId: Int,
        activo: Boolean
    )

    // Modificar el precio de un servicio
    @Query("UPDATE servicios SET precio = :precio WHERE id = :servicioId")
    suspend fun actualizarPrecio(
        servicioId: Int,
        precio: Double
    )
}
