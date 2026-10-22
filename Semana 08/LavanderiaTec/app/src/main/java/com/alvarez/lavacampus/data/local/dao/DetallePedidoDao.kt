package com.alvarez.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.alvarez.lavacampus.data.local.entity.DetallePedido
import kotlinx.coroutines.flow.Flow

// DAO encargado de gestionar los detalles de cada pedido
@Dao
interface DetallePedidoDao {

    // Registrar un detalle de pedido
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertar(detalle: DetallePedido): Long

    // Registrar varios detalles de un mismo pedido
    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertarTodos(detalles: List<DetallePedido>)

    // Consultar los detalles de un pedido
    @Query("SELECT * FROM detalles_pedido WHERE pedidoId = :pedidoId")
    fun obtenerPorPedido(pedidoId: Int): Flow<List<DetallePedido>>

    // Obtener los detalles para operaciones internas
    @Query("SELECT * FROM detalles_pedido WHERE pedidoId = :pedidoId")
    suspend fun listarPorPedido(pedidoId: Int): List<DetallePedido>

    // Eliminar los detalles asociados a un pedido
    @Query("DELETE FROM detalles_pedido WHERE pedidoId = :pedidoId")
    suspend fun eliminarPorPedido(pedidoId: Int)
}
