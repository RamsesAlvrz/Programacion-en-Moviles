package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.local.entity.DetallePedido
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.prefs.PreferenciasStore
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

// ViewModel encargado de registrar nuevos pedidos de lavandería.
class NuevoPedidoViewModel(
    private val repository: LavaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    // ==========================================
    // ESTADOS DEL FORMULARIO
    // ==========================================

    private val _lineas = MutableStateFlow<List<LineaPedido>>(emptyList())
    val lineas: StateFlow<List<LineaPedido>> = _lineas.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // COMMIT 14: Estado de recepción de pedidos.
    val recibirPedidos = preferenciasStore.recibirPedidos

    private val formatoFecha = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy HH:mm"
    )

    // ==========================================
    // CARGAR SERVICIOS ACTIVOS
    // ==========================================

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

    // ==========================================
// MEJORA C: REPETIR UN PEDIDO ANTERIOR
// ==========================================

    // Recupera los servicios y cantidades de un pedido anterior.
// Utiliza los precios actuales y conserva las validaciones
// del formulario de nuevo pedido.
    fun cargarPedidoAnterior(
        pedidoId: Int,
        usuarioId: Int
    ) {
        viewModelScope.launch {

            _cargando.value = true
            _error.value = null

            try {
                // Verificar que el pedido exista y pertenezca
                // al estudiante que inició sesión.
                val pedidoAnterior = repository.buscarPedidoPorId(pedidoId)

                if (pedidoAnterior == null ||
                    pedidoAnterior.usuarioId != usuarioId
                ) {
                    _error.value = "No se encontró el pedido anterior."
                    return@launch
                }

                // Recuperar los servicios y cantidades originales.
                val detallesAnteriores =
                    repository.obtenerDetallesPorPedido(pedidoId)

                // Consultar únicamente los servicios activos,
                // utilizando sus precios actuales.
                val serviciosActuales = repository
                    .observarServiciosActivos()
                    .first()

                var serviciosNoDisponibles = 0

                _lineas.value = serviciosActuales.map { servicio ->

                    val detalleAnterior = detallesAnteriores.firstOrNull {
                        it.servicioId == servicio.id
                    }

                    val cantidadAnterior = detalleAnterior?.cantidad ?: 0.0

                    val linea = LineaPedido(
                        servicio = servicio,
                        cantidad = 0.0
                    )

                    // Respetar los límites de cantidad definidos
                    // en LineaPedido.
                    linea.actualizarCantidad(cantidadAnterior)
                }

                // Detectar servicios que ya no están disponibles.
                serviciosNoDisponibles = detallesAnteriores.count { detalle ->
                    serviciosActuales.none { servicio ->
                        servicio.id == detalle.servicioId
                    }
                }

                if (serviciosNoDisponibles > 0) {
                    _error.value =
                        "Algunos servicios del pedido anterior ya no están disponibles."
                }

            } catch (e: Exception) {

                _error.value =
                    "No se pudo recuperar el pedido anterior: ${e.message}"

            } finally {

                _cargando.value = false
            }
        }
    }

    // ==========================================
    // AUMENTAR CANTIDAD
    // ==========================================

    fun aumentarCantidad(servicioId: Int) {

        _lineas.value = _lineas.value.map { linea ->

            if (linea.servicio.id != servicioId) {

                linea

            } else {

                val nuevaCantidad = when {

                    // Al seleccionar KG por primera vez, iniciar en 1 kg.
                    linea.servicio.unidad == "KG" &&
                            linea.cantidad == 0.0 -> 1.0

                    // Los siguientes incrementos son de 0.5 kg.
                    linea.servicio.unidad == "KG" ->
                        linea.cantidad + 0.5

                    // PRENDA y UNIDAD aumentan de uno en uno.
                    else -> linea.cantidad + 1.0
                }

                linea.actualizarCantidad(nuevaCantidad)
            }
        }
    }

    // ==========================================
    // DISMINUIR CANTIDAD
    // ==========================================

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

// ==========================================
// MEJORA B: DESCUENTO POR VOLUMEN
// ==========================================

    // Calcula el subtotal original de todos los servicios.
    fun calcularSubtotal(): Double {
        return _lineas.value.sumOf { it.subtotal }
    }

    // Calcula el 10 % de descuento cuando el lavado supera los 5 kg.
    fun calcularDescuento(): Double {

        val lavado = _lineas.value.firstOrNull { linea ->
            linea.servicio.nombre.equals(
                "Lavado y secado",
                ignoreCase = true
            ) && linea.servicio.unidad == "KG"
        }

        return if (lavado != null && lavado.cantidad > 5.0) {
            lavado.subtotal * 0.10
        } else {
            0.0
        }
    }

    // Calcula el importe final después del descuento.
    fun calcularTotal(): Double {
        return calcularSubtotal() - calcularDescuento()
    }

    // ==========================================
    // VALIDAR SERVICIOS SELECCIONADOS
    // ==========================================

    fun tieneServiciosSeleccionados(): Boolean {

        return _lineas.value.any { it.seleccionado }
    }

    // ==========================================
    // OBTENER SERVICIOS SELECCIONADOS
    // ==========================================

    fun obtenerLineasSeleccionadas(): List<LineaPedido> {

        return _lineas.value.filter { it.seleccionado }
    }

    // ==========================================
    // CALCULAR FECHA DE ENTREGA
    // ==========================================

    private fun obtenerFechaEntrega(
        fechaInicio: LocalDateTime,
        horasEntrega: Int
    ): LocalDateTime {

        // Convertir las horas de la guía a días de entrega.
        val diasEntrega = (horasEntrega + 23) / 24

        var fecha = fechaInicio
        var diasAgregados = 0

        while (diasAgregados < diasEntrega) {

            fecha = fecha.plusDays(1)

            // Los domingos no cuentan como día de entrega.
            if (fecha.dayOfWeek != DayOfWeek.SUNDAY) {

                diasAgregados++
            }
        }

        return fecha
    }

    // ==========================================
    // MOSTRAR FECHA ESTIMADA DE ENTREGA
    // ==========================================

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

    // ==========================================
    // REGISTRAR NUEVO PEDIDO
    // ==========================================

    fun registrarPedido(
        usuarioId: Int,
        nota: String,
        onResultado: (Long?) -> Unit
    ) {

        // Evitar registrar dos veces el mismo pedido.
        if (_guardando.value) return

        val seleccionadas = obtenerLineasSeleccionadas()

        // Comprobar que exista algún servicio seleccionado.
        if (seleccionadas.isEmpty()) {

            _error.value = "Selecciona al menos un servicio"
            onResultado(null)
            return
        }

        // Comprobar que el estudiante sea válido.
        if (usuarioId <= 0) {

            _error.value = "No se identificó al estudiante"
            onResultado(null)
            return
        }

        viewModelScope.launch {

            _guardando.value = true
            _error.value = null

            try {

                // ==========================================
                // COMMIT 14: VALIDAR RECEPCIÓN DE PEDIDOS
                // ==========================================

                // Consultar la preferencia guardada en DataStore.
                val aceptaPedidos = preferenciasStore
                    .recibirPedidos
                    .first()

                // Si el administrador desactivó la recepción,
                // no se permite registrar nuevos pedidos.
                if (!aceptaPedidos) {

                    _error.value =
                        "La lavandería no está recibiendo nuevos pedidos en este momento."

                    onResultado(null)
                    return@launch
                }

                // ==========================================
                // VALIDAR LÍMITE DE PEDIDOS ACTIVOS
                // ==========================================

                // Consultar los pedidos actuales del estudiante.
                val pedidosUsuario = repository
                    .observarPedidosPorUsuario(usuarioId)
                    .first()

                // Contar únicamente los pedidos activos.
                val pedidosActivos = pedidosUsuario.count {

                    it.estado == "RECIBIDO" ||
                            it.estado == "EN_PROCESO" ||
                            it.estado == "LISTO"
                }

                // La guía permite como máximo tres pedidos activos.
                if (pedidosActivos >= 3) {

                    _error.value = "Ya tienes 3 pedidos activos"

                    onResultado(null)
                    return@launch
                }

                // ==========================================
                // CALCULAR FECHAS Y TOTAL
                // ==========================================

                val ahora = LocalDateTime.now()

                val horasMaximas = seleccionadas.maxOf {
                    it.servicio.horasEntrega
                }

                val fechaRegistro = ahora.format(formatoFecha)

                val fechaEntrega = obtenerFechaEntrega(
                    fechaInicio = ahora,
                    horasEntrega = horasMaximas
                ).format(formatoFecha)

                // Guardar el total final con el descuento aplicado.
                val total = calcularTotal()

                // ==========================================
                // CREAR PEDIDO PRINCIPAL
                // ==========================================

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

                // ==========================================
                // CREAR DETALLES DEL PEDIDO
                // ==========================================

                val detalles = seleccionadas.map { linea ->

                    val esLavadoConDescuento =
                        linea.servicio.nombre.equals(
                            "Lavado y secado",
                            ignoreCase = true
                        ) &&
                                linea.servicio.unidad == "KG" &&
                                linea.cantidad > 5.0

                    val subtotalFinal = if (esLavadoConDescuento) {
                        linea.subtotal * 0.90
                    } else {
                        linea.subtotal
                    }

                    DetallePedido(
                        pedidoId = 0,
                        servicioId = linea.servicio.id,
                        cantidad = linea.cantidad,
                        precioUnitario = linea.servicio.precio,
                        subtotal = subtotalFinal
                    )
                }

                // ==========================================
                // GUARDAR PEDIDO Y DETALLES EN ROOM
                // ==========================================

                // Registrar todo dentro de una transacción.
                val pedidoId = repository.registrarPedidoCompleto(
                    pedido = pedido,
                    detalles = detalles
                )

                // Limpiar el formulario después del registro.
                limpiarSeleccion()

                // Comunicar el ID del pedido registrado.
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

    // ==========================================
    // LIMPIAR SELECCIÓN
    // ==========================================

    fun limpiarSeleccion() {

        _lineas.value = _lineas.value.map { linea ->

            linea.actualizarCantidad(0.0)
        }

        _error.value = null
    }
}