
package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.model.Casilleros
import com.alvarez.lavacampus.data.model.EstadoPedido
import com.alvarez.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class GestionPedidoViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    // Pedido seleccionado por el administrador.
    private val _pedido = MutableStateFlow<Pedido?>(null)
    val pedido: StateFlow<Pedido?> = _pedido.asStateFlow()

    // Estudiante propietario del pedido.
    private val _estudiante = MutableStateFlow<Usuario?>(null)
    val estudiante: StateFlow<Usuario?> = _estudiante.asStateFlow()

    // Casilleros ocupados por otros pedidos.
    private val _casillerosOcupados =
        MutableStateFlow<List<String>>(emptyList())

    val casillerosOcupados: StateFlow<List<String>> =
        _casillerosOcupados.asStateFlow()

    // Casillero seleccionado en la interfaz.
    private val _casilleroElegido = MutableStateFlow<String?>(null)
    val casilleroElegido: StateFlow<String?> =
        _casilleroElegido.asStateFlow()

    // Mensajes de confirmación o error.
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // Evita operaciones repetidas mientras se guarda.
    private val _procesando = MutableStateFlow(false)
    val procesando: StateFlow<Boolean> = _procesando.asStateFlow()

    // Trabajo que observa el pedido actual.
    private var observacionPedido: Job? = null

    init {
        observarCasilleros()
    }

    // =========================================
    // OBSERVACIÓN DE CASILLEROS
    // =========================================

    private fun observarCasilleros() {
        viewModelScope.launch {

            combine(
                repository.observarCasillerosOcupados(),
                _pedido
            ) { ocupados, pedidoActual ->

                // Excluir el casillero del propio pedido.
                ocupados.filterNot { casillero ->
                    casillero == pedidoActual?.casillero &&
                            pedidoActual.estado == EstadoPedido.LISTO.codigo
                }

            }.collect { ocupados ->
                _casillerosOcupados.value = ocupados
            }
        }
    }

    // =========================================
    // CARGAR PEDIDO
    // =========================================

    fun cargarPedido(pedidoId: Int) {

        // Detener la observación del pedido anterior.
        observacionPedido?.cancel()

        _pedido.value = null
        _estudiante.value = null
        _casilleroElegido.value = null
        _mensaje.value = null

        observacionPedido = viewModelScope.launch {

            repository.observarPedido(pedidoId).collect { resultado ->

                val pedidoActual = resultado?.pedido

                _pedido.value = pedidoActual

                if (pedidoActual != null) {

                    _estudiante.value =
                        repository.buscarUsuarioPorId(
                            pedidoActual.usuarioId
                        )

                    // Recuperar el casillero asignado, si existe.
                    if (_casilleroElegido.value == null) {
                        _casilleroElegido.value =
                            pedidoActual.casillero
                    }

                } else {
                    _estudiante.value = null
                }
            }
        }
    }

    // =========================================
    // SELECCIONAR CASILLERO
    // =========================================

    fun elegirCasillero(casillero: String) {

        if (_procesando.value) return

        if (!Casilleros.esValido(casillero)) {
            _mensaje.value = "El casillero seleccionado no es válido."
            return
        }

        if (casillero in _casillerosOcupados.value) {
            _mensaje.value = "El casillero $casillero está ocupado."
            return
        }

        _casilleroElegido.value = casillero
        _mensaje.value = null
    }

    // =========================================
    // REGISTRAR PAGO
    // =========================================

    fun marcarPagado(pagado: Boolean) {

        val pedidoActual = _pedido.value ?: return

        if (_procesando.value) return

        viewModelScope.launch {

            _procesando.value = true
            _mensaje.value = null

            try {
                repository.actualizarPagoPedido(
                    pedidoId = pedidoActual.id,
                    pagado = pagado
                )

                _mensaje.value = if (pagado) {
                    "Pago registrado correctamente."
                } else {
                    "El pedido quedó pendiente de pago."
                }

            } catch (e: Exception) {
                _mensaje.value =
                    "No se pudo actualizar el pago: ${e.message}"
            } finally {
                _procesando.value = false
            }
        }
    }

    // =========================================
    // AVANZAR ESTADO
    // =========================================

    fun avanzar() {

        val pedidoActual = _pedido.value ?: return

        if (_procesando.value) return

        viewModelScope.launch {

            _procesando.value = true
            _mensaje.value = null

            try {
                // Consultar los datos más recientes.
                val actualizado =
                    repository.buscarPedidoPorId(pedidoActual.id)

                if (actualizado == null) {
                    _mensaje.value = "No se encontró el pedido."
                    return@launch
                }

                val estadoActual =
                    EstadoPedido.desdeCodigo(actualizado.estado)

                if (estadoActual == null) {
                    _mensaje.value = "El estado del pedido no es válido."
                    return@launch
                }

                val siguiente = estadoActual.siguiente()

                if (siguiente == null) {
                    _mensaje.value =
                        "Este pedido ya no puede avanzar de estado."
                    return@launch
                }

                // Para pasar a LISTO, utilizar la operación
                // que valida y reserva el casillero en Room.
                if (siguiente == EstadoPedido.LISTO) {

                    val casillero = _casilleroElegido.value

                    if (casillero == null) {
                        _mensaje.value =
                            "Selecciona un casillero antes de marcar el pedido como listo."
                        return@launch
                    }

                    if (!Casilleros.esValido(casillero)) {
                        _mensaje.value = "El casillero seleccionado no es válido."
                        return@launch
                    }

                    val resultado =
                        repository.marcarListoConCasillero(
                            pedidoId = actualizado.id,
                            casillero = casillero
                        )

                    if (resultado == 0) {
                        _mensaje.value =
                            "No se pudo marcar como listo. El casillero podría estar ocupado o el pedido cambió de estado."
                        return@launch
                    }

                    _mensaje.value =
                        "Pedido marcado como listo. Casillero: $casillero"

                    return@launch
                }

                // Para entregar, comprobar el pago.
                if (siguiente == EstadoPedido.ENTREGADO) {

                    if (!actualizado.pagado) {
                        _mensaje.value =
                            "Debes marcar el pedido como cobrado antes de entregarlo."
                        return@launch
                    }
                }

                // Avanzar a EN_PROCESO o ENTREGADO.
                repository.cambiarEstadoPedido(
                    pedidoId = actualizado.id,
                    estado = siguiente.codigo
                )

                _mensaje.value =
                    "Pedido actualizado a ${siguiente.etiqueta}."

            } catch (e: Exception) {
                _mensaje.value =
                    "No se pudo actualizar el pedido: ${e.message}"
            } finally {
                _procesando.value = false
            }
        }
    }

    // =========================================
    // CANCELAR PEDIDO
    // =========================================

    fun cancelar() {

        val pedidoActual = _pedido.value ?: return

        if (_procesando.value) return

        viewModelScope.launch {

            _procesando.value = true
            _mensaje.value = null

            try {
                val actualizado =
                    repository.buscarPedidoPorId(pedidoActual.id)

                if (actualizado == null) {
                    _mensaje.value = "No se encontró el pedido."
                    return@launch
                }

                val estadoActual =
                    EstadoPedido.desdeCodigo(actualizado.estado)

                if (estadoActual?.puedeCancelar() != true) {
                    _mensaje.value =
                        "Solo puedes cancelar pedidos recibidos o en proceso."
                    return@launch
                }

                repository.cambiarEstadoPedido(
                    pedidoId = actualizado.id,
                    estado = EstadoPedido.CANCELADO.codigo
                )

                _mensaje.value = "Pedido cancelado correctamente."

            } catch (e: Exception) {
                _mensaje.value =
                    "No se pudo cancelar el pedido: ${e.message}"
            } finally {
                _procesando.value = false
            }
        }
    }

    // =========================================
    // LIMPIAR MENSAJE
    // =========================================

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
