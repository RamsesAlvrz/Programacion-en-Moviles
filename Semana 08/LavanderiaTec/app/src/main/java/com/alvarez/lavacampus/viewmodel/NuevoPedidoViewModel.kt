
package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.local.entity.DetallePedido
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.repository.LavaRepository
import com.alvarez.lavacampus.model.LineaPedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// ViewModel encargado de registrar nuevos pedidos de lavandería
class NuevoPedidoViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    private val _lineas = MutableStateFlow<List<LineaPedido>>(emptyList())
    val lineas: StateFlow<List<LineaPedido>> = _lineas.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val formatoFecha = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy HH:mm"
    )

    // Cargar únicamente los servicios activos
    fun cargarServicios() {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                val servicios = repository
                    .observarServiciosActivos()
                    .first()

                _lineas.value = servicios.map { servicio ->
                    LineaPedido(
                        servicio = servicio,
                        cantidad = 0.0
                    )
                }

            } catch (e: Exception) {
                _error.value =
                    "No se pudieron cargar los servicios: ${e.message}"

            } finally {
                _cargando.value = false
            }
        }
    }

    // Aumentar la cantidad según la unidad del servicio
    fun aumentarCantidad(servicioId: Int) {
        _lineas.value = _lineas.value.map { linea ->

            if (linea.servicio.id != servicioId) {
                linea
            } else {

                val nuevaCantidad = when {

                    // Al seleccionar KG por primera vez, iniciar en 1 kg
                    linea.servicio.unidad == "KG" &&
                            linea.cantidad == 0.0 -> 1.0

                    // Los siguientes incrementos son de 0.5 kg
                    linea.servicio.unidad == "KG" ->
                        linea.cantidad + 0.5

                    // PRENDA y UNIDAD aumentan de uno en uno
                    else -> linea.cantidad + 1.0
                }

                linea.actualizarCantidad(nuevaCantidad)
            }
        }
    }

    // Disminuir la cantidad según la unidad del servicio
    fun disminuirCantidad(servicioId: Int) {
        _lineas.value = _lineas.value.map { linea ->

            if (linea.servicio.id != servicioId) {
                linea
            } else {

                val nuevaCantidad = when {

                    linea.servicio.unidad == "KG" &&
                            linea.cantidad <= 1.0 -> 0.0

                    linea.servicio.unidad == "KG" ->
                        linea.cantidad - 0.5

                    else -> linea.cantidad - 1.0
                }

                linea.actualizarCantidad(nuevaCantidad)
            }
        }
    }

    // Calcular el importe total del pedido
    fun calcularTotal(): Double {
        return _lineas.value.sumOf { it.subtotal }
    }

    // Comprobar que exista algún servicio seleccionado
    fun tieneServiciosSeleccionados(): Boolean {
        return _lineas.value.any { it.seleccionado }
    }

    // Obtener las líneas que tienen una cantidad mayor que cero
    fun obtenerLineasSeleccionadas(): List<LineaPedido> {
        return _lineas.value.filter { it.seleccionado }
    }

    // Calcular la fecha de entrega omitiendo los domingos
    private fun obtenerFechaEntrega(
        fechaInicio: LocalDateTime,
        horasEntrega: Int
    ): LocalDateTime {

        // Convertir las horas de la guía a días de entrega
        val diasEntrega = (horasEntrega + 23) / 24

        var fecha = fechaInicio
        var diasAgregados = 0

        while (diasAgregados < diasEntrega) {
            fecha = fecha.plusDays(1)

            // Los domingos no cuentan como día de entrega
            if (fecha.dayOfWeek != DayOfWeek.SUNDAY) {
                diasAgregados++
            }
        }

        return fecha
    }

    // Mostrar la fecha estimada de entrega
    fun calcularFechaEntrega(): String {

        val seleccionadas = obtenerLineasSeleccionadas()

        if (seleccionadas.isEmpty()) {
            return "Selecciona un servicio"
        }

        val horasMaximas = seleccionadas.maxOf {
            it.servicio.horasEntrega
        }

        return obtenerFechaEntrega(
            fechaInicio = LocalDateTime.now(),
            horasEntrega = horasMaximas
        ).format(formatoFecha)
    }

    // Registrar un pedido junto con sus detalles
    fun registrarPedido(
        usuarioId: Int,
        nota: String,
        onResultado: (Long?) -> Unit
    ) {

        if (_guardando.value) return

        val seleccionadas = obtenerLineasSeleccionadas()

        if (seleccionadas.isEmpty()) {
            _error.value = "Selecciona al menos un servicio"
            onResultado(null)
            return
        }

        if (usuarioId <= 0) {
            _error.value = "No se identificó al estudiante"
            onResultado(null)
            return
        }

        viewModelScope.launch {

            _guardando.value = true
            _error.value = null

            try {

                // Consultar los pedidos actuales del estudiante
                val pedidosUsuario = repository
                    .observarPedidosPorUsuario(usuarioId)
                    .first()

                // Contar únicamente los pedidos activos
                val pedidosActivos = pedidosUsuario.count {
                    it.estado == "RECIBIDO" ||
                            it.estado == "EN_PROCESO" ||
                            it.estado == "LISTO"
                }

                // La guía permite como máximo tres pedidos activos
                if (pedidosActivos >= 3) {
                    _error.value = "Ya tienes 3 pedidos activos"
                    onResultado(null)
                    return@launch
                }

                val ahora = LocalDateTime.now()

                val horasMaximas = seleccionadas.maxOf {
                    it.servicio.horasEntrega
                }

                val fechaRegistro = ahora.format(formatoFecha)

                val fechaEntrega = obtenerFechaEntrega(
                    fechaInicio = ahora,
                    horasEntrega = horasMaximas
                ).format(formatoFecha)

                val total = seleccionadas.sumOf {
                    it.subtotal
                }

                // Crear el pedido principal
                val pedido = Pedido(
                    usuarioId = usuarioId,
                    fechaRegistro = fechaRegistro,
                    fechaEntrega = fechaEntrega,
                    estado = "RECIBIDO",
                    total = total,
                    casillero = null,
                    pagado = false,
                    nota = nota.trim()
                )

                // Crear los detalles de cada servicio seleccionado
                val detalles = seleccionadas.map { linea ->
                    DetallePedido(
                        pedidoId = 0,
                        servicioId = linea.servicio.id,
                        cantidad = linea.cantidad,
                        precioUnitario = linea.servicio.precio,
                        subtotal = linea.subtotal
                    )
                }

                // Guardar pedido y detalles en una transacción
                val pedidoId = repository.registrarPedidoCompleto(
                    pedido = pedido,
                    detalles = detalles
                )

                limpiarSeleccion()
                onResultado(pedidoId)

            } catch (e: Exception) {

                _error.value =
                    "No se pudo registrar el pedido: ${e.message}"

                onResultado(null)

            } finally {
                _guardando.value = false
            }
        }
    }

    // Restablecer todas las cantidades a cero
    fun limpiarSeleccion() {
        _lineas.value = _lineas.value.map { linea ->
            linea.actualizarCantidad(0.0)
        }

        _error.value = null
    }
}
