        package com.tecsup.smartcatalog.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.tecsup.smartcatalog.data.local.FavoritoEntity
import com.tecsup.smartcatalog.data.local.ItemDao
import com.tecsup.smartcatalog.data.mapper.toDomain
import com.tecsup.smartcatalog.data.mapper.toEntity
import com.tecsup.smartcatalog.data.remote.ApiService
import com.tecsup.smartcatalog.domain.model.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import android.content.Context
import com.tecsup.smartcatalog.notification.NotificationHelper
        class ItemRepositoryImpl(
            private val api: ApiService,
            private val dao: ItemDao,
            private val context: Context
        ) : ItemRepository {

    private fun usuarioId(): String {
        return FirebaseAuth.getInstance().currentUser?.uid
            ?: throw IllegalStateException(
                "Debes iniciar sesión para gestionar favoritos"
            )
    }

    override fun observarProductos(): Flow<List<Item>> {

        val uid = usuarioId()

        return combine(
            dao.obtenerTodos(),
            dao.observarIdsFavoritos(uid)
        ) { productos, ids ->

            val favoritos = ids.toSet()

            productos.map { entidad ->
                entidad.toDomain().copy(
                    favorito = entidad.id in favoritos
                )
            }
        }
    }

    override fun observarFavoritos(): Flow<List<Item>> {

        val uid = usuarioId()

        return combine(
            dao.obtenerTodos(),
            dao.observarIdsFavoritos(uid)
        ) { productos, ids ->

            val favoritos = ids.toSet()

            productos
                .filter { it.id in favoritos }
                .map {
                    it.toDomain().copy(favorito = true)
                }
        }
    }

    override fun contarProductos(): Flow<Int> {
        return dao.contarProductos()
    }

    override fun contarFavoritos(): Flow<Int> {
        return dao.contarFavoritosUsuario(usuarioId())
    }

    override suspend fun sincronizarProductos() {

        val productosApi = api.obtenerProductos().products

        val entidades = productosApi.map { dto ->
            dto.toEntity(favorito = false)
        }

        dao.insertarTodos(entidades)
    }

    override suspend fun obtenerDetalle(id: Int): Item {

        val uid = usuarioId()
        val local = dao.obtenerPorId(id)
        val favorito = dao.esFavorito(uid, id)

        return try {
            api.obtenerDetalle(id)
                .toDomain()
                .copy(favorito = favorito)

        } catch (e: Exception) {
            local?.toDomain()?.copy(favorito = favorito)
                ?: throw e
        }
    }


            override suspend fun cambiarFavorito(
                id: Int,
                favorito: Boolean
            ) {
                val uid = usuarioId()

                if (favorito) {

                    val yaEraFavorito = dao.esFavorito(
                        usuarioId = uid,
                        productoId = id
                    )

                    dao.agregarFavorito(
                        FavoritoEntity(
                            usuarioId = uid,
                            productoId = id
                        )
                    )

                    // Notificar solo cuando se agrega un favorito nuevo.
                    if (!yaEraFavorito) {
                        val producto = dao.obtenerPorId(id)

                        if (producto != null) {
                            NotificationHelper.notificarFavorito(
                                context = context,
                                nombreProducto = producto.nombre
                            )
                        }
                    }

                } else {
                    dao.quitarFavorito(
                        usuarioId = uid,
                        productoId = id
                    )
                }
            }


    override suspend fun limpiarCache() {
        dao.limpiarCache()
    }
}
