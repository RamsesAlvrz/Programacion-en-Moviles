package com.alvarez.lavacampus.data.repository

import androidx.room.withTransaction
import com.alvarez.lavacampus.data.local.LavaDatabase
import com.alvarez.lavacampus.data.local.entity.DetallePedido
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.local.relation.PedidoConDetalle
import kotlinx.coroutines.flow.Flow

// Implementación del repositorio utilizando Room
class LavaRepositoryRoom(
    private val database: LavaDatabase
) : LavaRepository {

    // Acceso a los DAO de la base de datos
    private val usuarioDao = database.usuarioDao()
    private val servicioDao = database.servicioDao()
    private val pedidoDao = database.pedidoDao()
    private val detallePedidoDao = database.detallePedidoDao()

    // OPERACIONES DE USUARIOS

    override suspend fun registrarUsuario(usuario: Usuario): Long {
        return usuarioDao.insertar(usuario)
    }

    override suspend fun buscarUsuarioPorCorreo(
        correo: String
    ): Usuario? {
        return usuarioDao.obtenerPorCorreo(correo)
    }

    override suspend fun buscarUsuarioPorId(
        id: Int
    ): Usuario? {
        return usuarioDao.obtenerPorId(id)
    }

    override suspend fun actualizarUsuario(usuario: Usuario) {
        usuarioDao.actualizar(usuario)
    }

    override fun observarClientes(): Flow<List<Usuario>> {
        return usuarioDao.obtenerClientes()
    }

    // OPERACIONES DE SERVICIOS

    override fun observarServicios(): Flow<List<Servicio>> {
        return servicioDao.obtenerTodos()
    }

    override fun observarServiciosActivos(): Flow<List<Servicio>> {
        return servicioDao.obtenerActivos()
    }

    override suspend fun buscarServicioPorId(
        id: Int
    ): Servicio? {
        return servicioDao.obtenerPorId(id)
    }

    override suspend fun registrarServicio(
        servicio: Servicio
    ): Long {
        return servicioDao.insertar(servicio)
    }

    override suspend fun actualizarServicio(servicio: Servicio) {
        servicioDao.actualizar(servicio)
    }

    // OPERACIONES DE PEDIDOS

    override suspend fun registrarPedido(pedido: Pedido): Long {
        return pedidoDao.insertar(pedido)
    }

    override suspend fun actualizarPedido(pedido: Pedido) {
        pedidoDao.actualizar(pedido)
    }

    override suspend fun buscarPedidoPorId(id: Int): Pedido? {
        return pedidoDao.obtenerPorId(id)
    }

    override suspend fun obtenerPedidoConDetalle(
        id: Int
    ): PedidoConDetalle? {
        return pedidoDao.obtenerConDetalle(id)
    }

    override fun observarPedidos(): Flow<List<Pedido>> {
        return pedidoDao.obtenerTodos()
    }

    override fun observarPedidosPorUsuario(
        usuarioId: Int
    ): Flow<List<Pedido>> {
        return pedidoDao.obtenerPorUsuario(usuarioId)
    }

    override fun observarPedidosPorEstado(
        estado: String
    ): Flow<List<Pedido>> {
        return pedidoDao.obtenerPorEstado(estado)
    }

    // OPERACIONES ADMINISTRATIVAS

    override suspend fun cambiarEstadoPedido(
        pedidoId: Int,
        estado: String
    ) {
        pedidoDao.actualizarEstado(pedidoId, estado)
    }

    override suspend fun asignarCasillero(
        pedidoId: Int,
        casillero: String
    ) {
        pedidoDao.asignarCasillero(pedidoId, casillero)
    }

    override suspend fun actualizarPagoPedido(
        pedidoId: Int,
        pagado: Boolean
    ) {
        pedidoDao.actualizarPago(pedidoId, pagado)
    }

    override fun contarPedidosPorEstado(
        estado: String
    ): Flow<Int> {
        return pedidoDao.contarPorEstado(estado)
    }

    // OPERACIONES DE DETALLES

    override suspend fun registrarDetalles(
        detalles: List<DetallePedido>
    ) {
        detallePedidoDao.insertarTodos(detalles)
    }

    override suspend fun obtenerDetallesPorPedido(
        pedidoId: Int
    ): List<DetallePedido> {
        return detallePedidoDao.listarPorPedido(pedidoId)
    }

    // OPERACIÓN TRANSACCIONAL ADICIONAL

    // Registra un pedido y todos sus detalles como una sola operación.
    // Si ocurre un error, Room revierte la transacción.
    suspend fun registrarPedidoCompleto(
        pedido: Pedido,
        detalles: List<DetallePedido>
    ): Long {
        return database.withTransaction {

            val pedidoId = pedidoDao.insertar(pedido)

            val detallesConPedido = detalles.map { detalle ->
                detalle.copy(pedidoId = pedidoId.toInt())
            }

            detallePedidoDao.insertarTodos(detallesConPedido)

            pedidoId
        }
    }
}
