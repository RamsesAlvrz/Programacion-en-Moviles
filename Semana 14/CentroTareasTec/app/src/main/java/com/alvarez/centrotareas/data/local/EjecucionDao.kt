package com.alvarez.centrotareas.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EjecucionDao {

    @Query("SELECT * FROM ejecuciones ORDER BY fecha DESC, id DESC")
    fun observarEjecuciones(): Flow<List<EjecucionEntity>>

    @Insert
    suspend fun insertar(ejecucion: EjecucionEntity)

    @Query("DELETE FROM ejecuciones")
    suspend fun eliminarTodo()
}
