package com.alvarez.centrotareas.data

import com.alvarez.centrotareas.data.local.EjecucionDao
import com.alvarez.centrotareas.data.local.EjecucionEntity
import kotlinx.coroutines.flow.Flow

class HistorialRepository(
    private val dao: EjecucionDao
) {
    val ejecuciones: Flow<List<EjecucionEntity>> =
        dao.observarEjecuciones()

    suspend fun registrar(
        tipo: String,
        estado: String,
        descripcion: String,
        duracionMs: Long? = null
    ) {
        dao.insertar(
            EjecucionEntity(
                tipo = tipo,
                estado = estado,
                descripcion = descripcion,
                duracionMs = duracionMs
            )
        )
    }

    suspend fun limpiarHistorial() {
        dao.eliminarTodo()
    }
}
