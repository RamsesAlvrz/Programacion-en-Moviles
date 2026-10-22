
package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.local.relation.PedidoConDetalle
import com.alvarez.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Gestiona la información y cancelación de un pedido del estudiante.
class DetallePedidoViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    // Pedido observado desde Room en tiempo real.
    private val _pedido = MutableStateFlow<PedidoConDetalle?>(null)
    val pedido: StateFlow<PedidoConDetalle?> = _pedido.asStateFlow()

    // Servicios registrados en la base de datos.
    // Permiten mostrar el nombre y la unidad de cada servicio del pedido.
    private val _servicios = MutableStateFlow<List<Servicio>>(emptyList())
    val servicios: StateFlow<List<Servicio>> = _servicios.asStateFlow()

    // Estado de carga de la pantalla.
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // Mensajes de error durante la consulta o cancelación.
    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    // Solo se permite cancelar pedidos en estado RECIBIDO.
    val puedeCancelar: Boolean
        get() = _pedido.value?.pedido?.estado == "RECIBIDO"

    init {
        // Observa los servicios para identificar sus nombres y unidades.
        viewModelScope.launch {
            repository.observarServicios().collectLatest { lista ->
                _servicios.value = lista
            }
        }
    }

    // Observa un pedido y actualiza automáticamente su información.
    fun observarPedido(pedidoId: Int) {
        viewModelScope.launch {
            _cargando.value = true
            _mensajeError.value = null

            try {
                repository.observarPedido(pedidoId).collectLatest { resultado ->
                    _pedido.value = resultado
                    _cargando.value = false
                }
            } catch (e: Exception) {
                _mensajeError.value =
                    "No se pudo cargar el pedido: ${e.message}"
                _cargando.value = false
            }
        }
    }

    // Cancela un pedido únicamente cuando su estado es RECIBIDO.
    fun cancelar() {
        val pedidoActual = _pedido.value?.pedido ?: return

        viewModelScope.launch {
            _mensajeError.value = null

            try {
                // Volver a consultar el estado actual desde Room.
                val pedidoActualizado =
                    repository.buscarPedidoPorId(pedidoActual.id)

                if (pedidoActualizado == null) {
                    _mensajeError.value = "El pedido no existe."
                    return@launch
                }

                // Impedir cancelaciones si el lavado ya comenzó.
                if (pedidoActualizado.estado != "RECIBIDO") {
                    _mensajeError.value =
                        "Solo se cancela antes de empezar el lavado"
                    return@launch
                }

                // Actualizar el estado del pedido.
                repository.cambiarEstadoPedido(
                    pedidoId = pedidoActualizado.id,
                    estado = "CANCELADO"
                )

                // Room notificará automáticamente el cambio
                // mediante el Flow de observarPedido().

            } catch (e: Exception) {
                _mensajeError.value =
                    "No se pudo cancelar el pedido: ${e.message}"
            }
        }
    }

    // Limpia los mensajes de error mostrados en la pantalla.
    fun limpiarError() {
        _mensajeError.value = null
    }
}
