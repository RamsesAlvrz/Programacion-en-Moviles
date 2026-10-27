
package com.alvarez.lavacampus.data.repository

import androidx.room.withTransaction
import com.alvarez.lavacampus.data.local.LavaDatabase
import com.alvarez.lavacampus.data.local.entity.DetallePedido
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.local.relation.PedidoConDetalle
import com.alvarez.lavacampus.data.model.ClienteResumen
import kotlinx.coroutines.flow.Flow

// Implementación del repositorio utilizando Room.
class LavaRepositoryRoom(
    private val database: LavaDatabase
) : LavaRepository {

    // Acceso a los DAO de la base de datos.
    private val usuarioDao = database.usuarioDao()
    private val servicioDao = database.servicioDao()
    private val pedidoDao = database.pedidoDao()
    private val detallePedidoDao = database.detallePedidoDao()

    // =====================================
    // OPERACIONES DE USUARIOS
    // =====================================

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

    // COMMIT 14: Obtener estudiantes con sus estadísticas de pedidos.
    override fun observarResumenClientes(): Flow<List<ClienteResumen>> {
        return usuarioDao.obtenerResumenClientes()
    }

    // =====================================
    // OPERACIONES DE SERVICIOS
    // =====================================

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

    override suspend fun eliminarServicio(servicioId: Int): Boolean {

        return database.withTransaction {

            // Verificar que el servicio exista.
            val servicio = servicioDao.obtenerPorId(servicioId)
                ?: throw IllegalArgumentException(
                    "El servicio seleccionado no existe."
                )

            // Comprobar si el servicio está asociado a pedidos.
            val cantidadDetalles =
                detallePedidoDao.contarPorServicio(servicioId)

            if (cantidadDetalles > 0) {

                // Si tiene pedidos, no se elimina.
                // Solamente se desactiva.
                servicioDao.actualizar(
                    servicio.copy(activo = false)
                )

                false

            } else {

                // Si no tiene pedidos, se elimina definitivamente.
                val filasEliminadas =
                    servicioDao.eliminarPorId(servicioId)

                if (filasEliminadas == 0) {
                    throw IllegalStateException(
                        "No se pudo eliminar el servicio."
                    )
                }

                true
            }
        }
    }

    // =====================================
    // OPERACIONES DE PEDIDOS
    // =====================================

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

    // Observar los cambios de un pedido en tiempo real.
    override fun observarPedido(
        id: Int
    ): Flow<PedidoConDetalle?> {
        return pedidoDao.observarConDetalle(id)
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

    // =====================================
    // OPERACIONES ADMINISTRATIVAS
    // =====================================

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

    // Obtener el total de pedidos del estudiante.
    override fun contarPedidosUsuario(
        usuarioId: Int
    ): Flow<Int> {
        return pedidoDao.contarPedidosUsuario(usuarioId)
    }

    // Obtener la cantidad de pedidos activos.
    override fun contarPedidosActivosUsuario(
        usuarioId: Int
    ): Flow<Int> {
        return pedidoDao.contarPedidosActivosUsuario(usuarioId)
    }

    // Obtener los kilos registrados en pedidos no cancelados.
    override fun obtenerKilosLavadosUsuario(
        usuarioId: Int
    ): Flow<Double> {
        return pedidoDao.obtenerKilosLavadosUsuario(usuarioId)
    }

    // =====================================
    // CASILLEROS - COMMIT 13
    // =====================================

    // Consulta los casilleros utilizados por pedidos LISTOS.
    // Room actualizará la lista cuando cambien los pedidos.
    override fun observarCasillerosOcupados(): Flow<List<String>> {
        return pedidoDao.observarCasillerosOcupados()
    }
    override suspend fun marcarListoConCasillero(
        pedidoId: Int,
        casillero: String
    ): Int {
        return pedidoDao.marcarListoConCasillero(
            pedidoId = pedidoId,
            casillero = casillero
        )
    }
    // =====================================
    // OPERACIONES DE DETALLES
    // =====================================

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

    // =====================================
    // OPERACIÓN TRANSACCIONAL
    // =====================================

    // Registra el pedido y sus detalles en una sola transacción.
    // Si ocurre un error, Room revierte los cambios.
    override suspend fun registrarPedidoCompleto(
        pedido: Pedido,
        detalles: List<DetallePedido>
    ): Long {

        return database.withTransaction {

            // Registrar el pedido y obtener su ID.
            val pedidoId = pedidoDao.insertar(pedido)

            // Asociar los detalles con el pedido registrado.
            val detallesConPedido = detalles.map { detalle ->
                detalle.copy(
                    pedidoId = pedidoId.toInt()
                )
            }

            // Registrar los detalles del pedido.
            detallePedidoDao.insertarTodos(detallesConPedido)

            // Devolver el identificador del pedido.
            pedidoId
        }
    }
}
