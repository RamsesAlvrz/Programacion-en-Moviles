package com.alvarez.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alvarez.lavacampus.data.local.entity.Usuario
import kotlinx.coroutines.flow.Flow

// DAO encargado de gestionar los usuarios registrados
@Dao
interface UsuarioDao {

    // Registrar un nuevo usuario
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario): Long

    // Actualizar los datos de un usuario
    @Update
    suspend fun actualizar(usuario: Usuario)

    // Buscar un usuario por su identificador
    @Query("SELECT * FROM usuarios WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Usuario?

    // Buscar un usuario mediante su correo electrónico
    @Query("SELECT * FROM usuarios WHERE correo = :correo LIMIT 1")
    suspend fun obtenerPorCorreo(correo: String): Usuario?

    // Consultar los usuarios registrados
    @Query("SELECT * FROM usuarios ORDER BY nombre ASC")
    fun obtenerTodos(): Flow<List<Usuario>>

    // Consultar únicamente los estudiantes
    @Query("SELECT * FROM usuarios WHERE rol = 'CLIENTE' ORDER BY nombre ASC")
    fun obtenerClientes(): Flow<List<Usuario>>
}
