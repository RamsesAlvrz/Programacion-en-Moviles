
package com.alvarez.lavacampus.data.repository

import com.alvarez.lavacampus.data.local.entity.DetallePedido
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

interface LavaRepository {

    // =========================
    // USUARIOS
    // =========================

    suspend fun registrarUsuario(usuario: Usuario): Long

    suspend fun buscarUsuarioPorCorreo(correo: String): Usuario?

    suspend fun buscarUsuarioPorId(id: Int): Usuario?

    suspend fun actualizarUsuario(usuario: Usuario)

    fun observarClientes(): Flow<List<Usuario>>

    // =========================
    // SERVICIOS
    // =========================

    fun observarServicios(): Flow<List<Servicio>>

    fun observarServiciosActivos(): Flow<List<Servicio>>

    suspend fun buscarServicioPorId(id: Int): Servicio?

    suspend fun registrarServicio(servicio: Servicio): Long

    suspend fun actualizarServicio(servicio: Servicio)

    // =========================
    // PEDIDOS
    // =========================

    suspend fun registrarPedido(pedido: Pedido): Long

    suspend fun actualizarPedido(pedido: Pedido)

    suspend fun buscarPedidoPorId(id: Int): Pedido?

    suspend fun obtenerPedidoConDetalle(id: Int): PedidoConDetalle?

    fun observarPedido(id: Int): Flow<PedidoConDetalle?>

    fun observarPedidos(): Flow<List<Pedido>>

    fun observarPedidosPorUsuario(usuarioId: Int): Flow<List<Pedido>>

    fun observarPedidosPorEstado(estado: String): Flow<List<Pedido>>

    suspend fun cambiarEstadoPedido(
        pedidoId: Int,
        estado: String
    )

    suspend fun asignarCasillero(
        pedidoId: Int,
        casillero: String
    )

    suspend fun actualizarPagoPedido(
        pedidoId: Int,
        pagado: Boolean
    )

    fun contarPedidosPorEstado(estado: String): Flow<Int>

    // =========================
    // CASILLEROS - COMMIT 13
    // =========================

    // Permite observar en tiempo real los casilleros
    // asignados a pedidos que están LISTOS.
    fun observarCasillerosOcupados(): Flow<List<String>>

    suspend fun marcarListoConCasillero(
        pedidoId: Int,
        casillero: String
    ): Int

    // =========================
    // DETALLES DEL PEDIDO
    // =========================

    suspend fun registrarDetalles(detalles: List<DetallePedido>)

    suspend fun obtenerDetallesPorPedido(
        pedidoId: Int
    ): List<DetallePedido>

    suspend fun registrarPedidoCompleto(
        pedido: Pedido,
        detalles: List<DetallePedido>
    ): Long
}
