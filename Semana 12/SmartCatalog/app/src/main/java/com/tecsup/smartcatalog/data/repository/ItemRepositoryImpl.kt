package com.tecsup.smartcatalog.data.repository

import com.tecsup.smartcatalog.data.local.ItemDao
import com.tecsup.smartcatalog.data.mapper.toDomain
import com.tecsup.smartcatalog.data.mapper.toEntity
import com.tecsup.smartcatalog.data.remote.ApiService
import com.tecsup.smartcatalog.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ItemRepositoryImpl(
    private val api: ApiService,
    private val dao: ItemDao
) : ItemRepository {

    override fun observarProductos(): Flow<List<Item>> =
        dao.obtenerTodos().map { lista ->
            lista.map { it.toDomain() }
        }

    override fun observarFavoritos(): Flow<List<Item>> =
        dao.obtenerFavoritos().map { lista ->
            lista.map { it.toDomain() }
        }

    override fun contarProductos(): Flow<Int> =
        dao.contarProductos()

    override fun contarFavoritos(): Flow<Int> =
        dao.contarFavoritos()

    override suspend fun sincronizarProductos() {
        val productosApi = api.obtenerProductos().products

        val productosLocales = dao.obtenerTodosUnaVez()
            .associateBy { it.id }

        val entidades = productosApi.map { dto ->
            dto.toEntity(
                favorito = productosLocales[dto.id]?.favorito ?: false
            )
        }

        dao.insertarTodos(entidades)
    }

    override suspend fun obtenerDetalle(id: Int): Item {
        val dto = api.obtenerDetalle(id)
        val local = dao.obtenerPorId(id)

        return dto.toDomain().copy(
            favorito = local?.favorito ?: false
        )
    }

    override suspend fun cambiarFavorito(id: Int, favorito: Boolean) {
        dao.actualizarFavorito(id, favorito)
    }

    override suspend fun limpiarCache() {
        dao.limpiarCache()
    }
}
