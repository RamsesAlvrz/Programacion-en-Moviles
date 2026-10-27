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

// DAO encargado de gestionar los pedidos de lavandería.
@Dao
interface PedidoDao {

    // Registrar un nuevo pedido.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(pedido: Pedido): Long

    // Actualizar los datos de un pedido.
    @Update
    suspend fun actualizar(pedido: Pedido)

    // Obtener un pedido mediante su identificador.
    @Query("SELECT * FROM pedidos WHERE id = :id LIMIT 1")
    suspend fun obtenerPorId(id: Int): Pedido?

    // Consultar los pedidos de un estudiante.
    @Query(
        "SELECT * FROM pedidos " +
                "WHERE usuarioId = :usuarioId " +
                "ORDER BY fechaCreacion DESC"
    )
    fun obtenerPorUsuario(usuarioId: Int): Flow<List<Pedido>>

    // Consultar todos los pedidos para el administrador.
    @Query("SELECT * FROM pedidos ORDER BY fechaCreacion DESC")
    fun obtenerTodos(): Flow<List<Pedido>>

    // Consultar los pedidos según su estado.
    @Query(
        "SELECT * FROM pedidos " +
                "WHERE estado = :estado " +
                "ORDER BY fechaCreacion DESC"
    )
    fun obtenerPorEstado(estado: String): Flow<List<Pedido>>

    // Cambiar el estado y registrar la fecha real de la transición.
    @Query(
        """
        UPDATE pedidos
        SET estado = :estado,
            fechaInicioProceso = CASE
                WHEN :estado = 'EN_PROCESO'
                     AND fechaInicioProceso IS NULL
                THEN :fechaCambio
                ELSE fechaInicioProceso
            END,
            fechaListo = CASE
                WHEN :estado = 'LISTO'
                     AND fechaListo IS NULL
                THEN :fechaCambio
                ELSE fechaListo
            END,
            fechaEntregado = CASE
                WHEN :estado = 'ENTREGADO'
                     AND fechaEntregado IS NULL
                THEN :fechaCambio
                ELSE fechaEntregado
            END
        WHERE id = :pedidoId
        """
    )
    suspend fun actualizarEstado(
        pedidoId: Int,
        estado: String,
        fechaCambio: Long = System.currentTimeMillis()
    )

    // Asignar un casillero al pedido.
    @Query("UPDATE pedidos SET casillero = :casillero WHERE id = :pedidoId")
    suspend fun asignarCasillero(
        pedidoId: Int,
        casillero: String
    )

    // Registrar el pago del pedido.
    @Query("UPDATE pedidos SET pagado = :pagado WHERE id = :pedidoId")
    suspend fun actualizarPago(
        pedidoId: Int,
        pagado: Boolean
    )

    // Contar los pedidos que tienen un estado determinado.
    @Query("SELECT COUNT(*) FROM pedidos WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    // Obtener un pedido junto con sus detalles.
    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :pedidoId LIMIT 1")
    suspend fun obtenerConDetalle(
        pedidoId: Int
    ): PedidoConDetalle?

    // Observar un pedido y sus detalles en tiempo real.
    @Transaction
    @Query("SELECT * FROM pedidos WHERE id = :pedidoId LIMIT 1")
    fun observarConDetalle(
        pedidoId: Int
    ): Flow<PedidoConDetalle?>

    // COMMIT 13: Observar los casilleros ocupados.
    // Un casillero permanece ocupado mientras el pedido esté LISTO.
    @Query(
        """
        SELECT DISTINCT casillero
        FROM pedidos
        WHERE casillero IS NOT NULL
          AND casillero != ''
          AND estado = 'LISTO'
        """
    )
    fun observarCasillerosOcupados(): Flow<List<String>>

    // Marcar como LISTO y asignar un casillero disponible.
    // Registrar la fecha únicamente si la operación se realiza.
    @Query(
        """
        UPDATE pedidos
        SET casillero = :casillero,
            estado = 'LISTO',
            fechaListo = COALESCE(fechaListo, :fechaCambio)
        WHERE id = :pedidoId
          AND estado = 'EN_PROCESO'
          AND NOT EXISTS (
              SELECT 1
              FROM pedidos AS otro
              WHERE otro.casillero = :casillero
                AND otro.estado = 'LISTO'
                AND otro.id != :pedidoId
          )
        """
    )
    suspend fun marcarListoConCasillero(
        pedidoId: Int,
        casillero: String,
        fechaCambio: Long = System.currentTimeMillis()
    ): Int

    @Query(
        """
        SELECT COUNT(*)
        FROM pedidos
        WHERE usuarioId = :usuarioId
        """
    )
    fun contarPedidosUsuario(
        usuarioId: Int
    ): Flow<Int>

    // Contar los pedidos que todavía están activos.
    @Query(
        """
        SELECT COUNT(*)
        FROM pedidos
        WHERE usuarioId = :usuarioId
          AND estado NOT IN ('ENTREGADO', 'CANCELADO')
        """
    )
    fun contarPedidosActivosUsuario(
        usuarioId: Int
    ): Flow<Int>

    // Calcular únicamente los kilos de servicios por kilo.
    @Query(
        """
        SELECT COALESCE(SUM(d.cantidad), 0.0)
        FROM detalles_pedido AS d
        INNER JOIN pedidos AS p
            ON p.id = d.pedidoId
        INNER JOIN servicios AS s
            ON s.id = d.servicioId
        WHERE p.usuarioId = :usuarioId
          AND LOWER(TRIM(s.unidad)) = 'kilo'
          AND p.estado != 'CANCELADO'
        """
    )
    fun obtenerKilosLavadosUsuario(
        usuarioId: Int
    ): Flow<Double>

}