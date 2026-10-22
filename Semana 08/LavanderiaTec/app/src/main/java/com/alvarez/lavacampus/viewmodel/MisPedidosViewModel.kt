
package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Gestiona los pedidos activos y el historial del estudiante.
class MisPedidosViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    private val _activos = MutableStateFlow<List<Pedido>>(emptyList())
    val activos: StateFlow<List<Pedido>> = _activos.asStateFlow()

    private val _historial = MutableStateFlow<List<Pedido>>(emptyList())
    val historial: StateFlow<List<Pedido>> = _historial.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    private var trabajoObservacion: Job? = null

    // Consulta los pedidos del estudiante en tiempo real.
    fun cargarPedidos(usuarioId: Int) {
        trabajoObservacion?.cancel()

        trabajoObservacion = viewModelScope.launch {
            _cargando.value = true
            _mensajeError.value = null

            try {
                repository.observarPedidosPorUsuario(usuarioId)
                    .collectLatest { pedidos ->

                        val ordenados = pedidos.sortedByDescending {
                            it.fechaCreacion
                        }

                        _activos.value = ordenados.filter {
                            it.estado in listOf(
                                "RECIBIDO",
                                "EN_PROCESO",
                                "LISTO"
                            )
                        }

                        _historial.value = ordenados.filter {
                            it.estado in listOf(
                                "ENTREGADO",
                                "CANCELADO"
                            )
                        }

                        _cargando.value = false
                    }
            } catch (e: Exception) {
                _mensajeError.value =
                    "No se pudieron cargar los pedidos: ${e.message}"
                _cargando.value = false
            }
        }
    }

    // Cancela únicamente los pedidos que siguen en RECIBIDO.
    fun cancelar(pedido: Pedido) {
        viewModelScope.launch {
            _mensajeError.value = null

            try {
                val pedidoActual =
                    repository.buscarPedidoPorId(pedido.id)

                if (pedidoActual == null) {
                    _mensajeError.value = "El pedido no existe."
                    return@launch
                }

                if (pedidoActual.estado != "RECIBIDO") {
                    _mensajeError.value =
                        "Solo se cancela antes de empezar el lavado"
                    return@launch
                }

                repository.cambiarEstadoPedido(
                    pedidoId = pedidoActual.id,
                    estado = "CANCELADO"
                )

                // Room actualizará automáticamente Activos e Historial.
            } catch (e: Exception) {
                _mensajeError.value =
                    "No se pudo cancelar el pedido: ${e.message}"
            }
        }
    }

    fun limpiarError() {
        _mensajeError.value = null
    }
}
