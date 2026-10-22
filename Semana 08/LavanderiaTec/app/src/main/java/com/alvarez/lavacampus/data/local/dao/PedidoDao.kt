package com.alvarez.lavacampus.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

// DAO encargado de gestionar los pedidos de lavandería
@Dao
interface PedidoDao {

    // Registrar un nuevo pedido
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(pedido: Pedido): Long

    // Actualizar los datos de un pedido
    @Update
    suspend fun actualizar(pedido: Pedido)

    // Obtener un pedido mediante su identificador
    @Query("SELECT * FROM pedidos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Pedido?

    // Consultar los pedidos de un estudiante
    @Query(
        "SELECT * FROM pedidos " +
                "WHERE usuarioId = :usuarioId " +
                "ORDER BY fechaCreacion DESC"
    )
    fun obtenerPorUsuario(usuarioId: Int): Flow<List<Pedido>>

    // Consultar todos los pedidos para el administrador
    @Query("SELECT * FROM pedidos ORDER BY fechaCreacion DESC")
    fun obtenerTodos(): Flow<List<Pedido>>

    // Consultar los pedidos según su estado
    @Query(
        "SELECT * FROM pedidos " +
                "WHERE estado = :estado " +
                "ORDER BY fechaCreacion DESC"
    )
    fun obtenerPorEstado(estado: String): Flow<List<Pedido>>

    // Cambiar el estado de un pedido
    @Query("UPDATE pedidos SET estado = :estado WHERE id = :pedidoId")
    suspend fun actualizarEstado(pedidoId: Int, estado: String)

    // Asignar un casillero al pedido
    @Query("UPDATE pedidos SET casillero = :casillero WHERE id = :pedidoId")
    suspend fun asignarCasillero(pedidoId: Int, casillero: String)

    // Registrar el pago del pedido
    @Query("UPDATE pedidos SET pagado = :pagado WHERE id = :pedidoId")
    suspend fun actualizarPago(pedidoId: Int, pagado: Boolean)

    // Contar los pedidos que tienen un estado determinado
    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    // Obtener un pedido junto con sus detalles
    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :pedidoId LIMIT 1")
    suspend fun obtenerConDetalle(pedidoId: Int): PedidoConDetalle?

    // Observar un pedido y sus detalles en tiempo real
// Room emitirá nuevos datos cuando cambie el pedido o sus detalles
    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :pedidoId LIMIT 1")
    fun observarConDetalle(pedidoId: Int): Flow<PedidoConDetalle?>

}