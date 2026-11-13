package com.tecsup.smartcatalog.data.repository

import com.tecsup.smartcatalog.domain.model.Item
import kotlinx.coroutines.flow.Flow

interface ItemRepository {
    fun observarProductos(): Flow<List<Item>>
    fun observarFavoritos(): Flow<List<Item>>
    fun contarProductos(): Flow<Int>
    fun contarFavoritos(): Flow<Int>

    suspend fun sincronizarProductos()
    suspend fun obtenerDetalle(id: Int): Item
    suspend fun cambiarFavorito(id: Int, favorito: Boolean)
    suspend fun limpiarCache()
}
