package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// COMMIT 14: ViewModel para administrar los servicios de lavandería.
class ServiciosViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    // Lista de todos los servicios registrados.
    // Incluye tanto los activos como los inactivos.
    val servicios = repository.observarServicios()

    // Mensaje que se mostrará después de una operación.
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // Indica si se está realizando una operación.
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // COMMIT 14: Indica si el servicio se guardó correctamente.
    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso.asStateFlow()

    // =====================================
    // GUARDAR SERVICIO
    // =====================================

    // Registra un servicio nuevo o actualiza uno existente.
    fun guardar(servicio: Servicio) {

        if (_cargando.value) return

        _guardadoExitoso.value = false

        viewModelScope.launch {
            _cargando.value = true
            _mensaje.value = null

            try {
                // Validar el nombre.
                require(servicio.nombre.isNotBlank()) {
                    "El nombre del servicio es obligatorio."
                }

                // Validar el precio.
                require(servicio.precio > 0) {
                    "El precio debe ser mayor que cero."
                }

                // Validar la unidad de cobro.
                require(servicio.unidad in listOf("Kilo", "Prenda", "Unidad")) {
                    "Selecciona una unidad válida."
                }

                // Validar el tiempo de entrega.
                require(servicio.horasEntrega in listOf(24, 48, 72)) {
                    "Selecciona un tiempo de entrega válido."
                }

                if (servicio.id == 0) {

                    // Registrar un servicio nuevo.
                    repository.registrarServicio(servicio)

                    _mensaje.value = "Servicio registrado correctamente."

                } else {

                    // Comprobar que el servicio exista.
                    val existente = repository.buscarServicioPorId(servicio.id)

                    require(existente != null) {
                        "El servicio que intentas actualizar no existe."
                    }

                    // Actualizar el servicio existente.
                    repository.actualizarServicio(servicio)

                    _mensaje.value = "Servicio actualizado correctamente."
                }

            } catch (e: Exception) {
                _mensaje.value = e.message
                    ?: "No se pudo guardar el servicio."
            } finally {
                _cargando.value = false
            }
        }
    }

    // =====================================
    // CAMBIAR DISPONIBILIDAD - COMMIT 14
    // =====================================

    // Activa o desactiva un servicio desde su interruptor.
    fun cambiarDisponibilidad(
        servicio: Servicio,
        activo: Boolean
    ) {

        if (_cargando.value) return

        viewModelScope.launch {
            _cargando.value = true
            _mensaje.value = null

            try {
                // Obtener la versión actual del servicio.
                val servicioActual = repository.buscarServicioPorId(servicio.id)

                require(servicioActual != null) {
                    "El servicio seleccionado no existe."
                }

                // Actualizar únicamente su disponibilidad.
                repository.actualizarServicio(
                    servicioActual.copy(activo = activo)
                )

                _mensaje.value = if (activo) {
                    "Servicio activado correctamente."
                } else {
                    "Servicio desactivado correctamente."
                }

            } catch (e: Exception) {
                _mensaje.value = e.message
                    ?: "No se pudo cambiar la disponibilidad."
            } finally {
                _cargando.value = false
            }
        }
    }

    // =====================================
    // ELIMINAR SERVICIO
    // =====================================

    // Elimina un servicio o lo desactiva si tiene pedidos asociados.
    fun eliminar(servicio: Servicio) {

        if (_cargando.value) return

        viewModelScope.launch {
            _cargando.value = true
            _mensaje.value = null

            try {
                val eliminado = repository.eliminarServicio(servicio.id)

                _mensaje.value = if (eliminado) {
                    "Servicio eliminado correctamente."
                } else {
                    "El servicio tiene pedidos asociados. " +
                            "Se desactivó para conservar el historial."
                }

            } catch (e: Exception) {
                _mensaje.value = e.message
                    ?: "No se pudo eliminar el servicio."
            } finally {
                _cargando.value = false
            }
        }
    }

    // =====================================
    // LIMPIAR MENSAJE
    // =====================================

    // Evita mostrar repetidamente mensajes anteriores.
    fun limpiarMensaje() {
        _mensaje.value = null
    }

    // Reinicia el resultado para evitar navegaciones repetidas.
    fun limpiarGuardadoExitoso() {
        _guardadoExitoso.value = false
    }
}