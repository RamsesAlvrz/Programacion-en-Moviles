package com.tecsup.smartcatalog.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {

    @Query("SELECT * FROM productos ORDER BY id")
    fun obtenerTodos(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM productos")
    suspend fun obtenerTodosUnaVez(): List<ItemEntity>

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): ItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(items: List<ItemEntity>)

    @Query("UPDATE productos SET favorito = :favorito WHERE id = :id")
    suspend fun actualizarFavorito(id: Int, favorito: Boolean)

    @Query("SELECT COUNT(*) FROM productos")
    fun contarProductos(): Flow<Int>

    @Query("SELECT COUNT(*) FROM productos WHERE favorito = 1")
    fun contarFavoritos(): Flow<Int>

    @Query("DELETE FROM productos")
    suspend fun limpiarCache()

    // FAVORITOS POR USUARIO

    @Query(
        "SELECT productoId FROM favoritos_usuario " +
                "WHERE usuarioId = :usuarioId"
    )
    fun observarIdsFavoritos(usuarioId: String): Flow<List<Int>>

    @Query(
        "SELECT COUNT(*) FROM favoritos_usuario " +
                "WHERE usuarioId = :usuarioId"
    )
    fun contarFavoritosUsuario(usuarioId: String): Flow<Int>

    @Query(
        "SELECT EXISTS(SELECT 1 FROM favoritos_usuario " +
                "WHERE usuarioId = :usuarioId AND productoId = :productoId)"
    )
    suspend fun esFavorito(
        usuarioId: String,
        productoId: Int
    ): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun agregarFavorito(favorito: FavoritoEntity)

    @Query(
        "DELETE FROM favoritos_usuario " +
                "WHERE usuarioId = :usuarioId AND productoId = :productoId"
    )
    suspend fun quitarFavorito(
        usuarioId: String,
        productoId: Int
    )
}
