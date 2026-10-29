package com.alvarez.lavacampus.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.entity.Servicio
import com.alvarez.lavacampus.data.repository.LavaRepository
import com.alvarez.lavacampus.ui.components.graficos.DatoGrafico
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.TemporalAdjusters
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class PanelViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    private val formatoFechaEntrega = DateTimeFormatter.ofPattern(
        "dd/MM/yyyy HH:mm"
    )

    // COMMIT A: Reloj para actualizar los indicadores cada minuto.
    private val reloj = flow {
        while (true) {
            emit(LocalDateTime.now())
            delay(60_000L)
        }
    }

    private val _pedidosHoy = MutableStateFlow(0)
    val pedidosHoy: StateFlow<Int> = _pedidosHoy.asStateFlow()

    private val _enProceso = MutableStateFlow(0)
    val enProceso: StateFlow<Int> = _enProceso.asStateFlow()

    private val _porRecoger = MutableStateFlow(0)
    val porRecoger: StateFlow<Int> = _porRecoger.asStateFlow()

    // COMMIT A: Nuevo contador de pedidos atrasados.
    private val _pedidosAtrasados = MutableStateFlow(0)
    val pedidosAtrasados: StateFlow<Int> =
        _pedidosAtrasados.asStateFlow()

    private val _ingresosSemana = MutableStateFlow(0.0)
    val ingresosSemana: StateFlow<Double> =
        _ingresosSemana.asStateFlow()

    private val _pedidosPagadosSemana = MutableStateFlow(0)
    val pedidosPagadosSemana: StateFlow<Int> =
        _pedidosPagadosSemana.asStateFlow()

    private val _porEstado =
        MutableStateFlow<List<DatoGrafico>>(emptyList())

    val porEstado: StateFlow<List<DatoGrafico>> =
        _porEstado.asStateFlow()

    private val _porServicio =
        MutableStateFlow<List<DatoGrafico>>(emptyList())

    val porServicio: StateFlow<List<DatoGrafico>> =
        _porServicio.asStateFlow()

    private val _cargando = MutableStateFlow(true)
    val cargando: StateFlow<Boolean> =
        _cargando.asStateFlow()

    private val _mensajeError =
        MutableStateFlow<String?>(null)

    val mensajeError: StateFlow<String?> =
        _mensajeError.asStateFlow()

    init {
        observarDatosPanel()
    }

    // COMMIT A: Identificar si un pedido está atrasado.
// COMMIT A: Detectar pedidos atrasados con ambos formatos de fecha.
    private fun esPedidoAtrasado(
        pedido: Pedido,
        ahora: LocalDateTime
    ): Boolean {

        // Los pedidos finalizados no se consideran atrasados.
        if (
            pedido.estado == "ENTREGADO" ||
            pedido.estado == "CANCELADO"
        ) {
            return false
        }

        val fechaTexto = pedido.fechaEntrega.trim()

        return try {

            // Formato actual: dd/MM/yyyy HH:mm
            val fechaEntrega = LocalDateTime.parse(
                fechaTexto,
                formatoFechaEntrega
            )

            fechaEntrega.isBefore(ahora)

        } catch (e: DateTimeParseException) {

            try {

                // Formato antiguo: yyyy-MM-dd
                val fechaEntregaAntigua = LocalDate.parse(
                    fechaTexto,
                    DateTimeFormatter.ISO_LOCAL_DATE
                )

                fechaEntregaAntigua.isBefore(ahora.toLocalDate())

            } catch (e: DateTimeParseException) {

                false
            }
        }
    }

    private fun observarDatosPanel() {

        viewModelScope.launch {

            try {

                combine(
                    repository.observarPedidos(),
                    repository.observarServicios(),
                    reloj
                ) { pedidos, servicios, ahora ->

                    Triple(pedidos, servicios, ahora)

                }.collect { (pedidos, servicios, ahora) ->

                    actualizarIndicadores(
                        pedidos = pedidos,
                        ahora = ahora
                    )

                    actualizarGraficoEstados(pedidos)

                    actualizarGraficoServicios(
                        pedidos,
                        servicios
                    )

                    _cargando.value = false
                    _mensajeError.value = null
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                _cargando.value = false

                _mensajeError.value =
                    "No se pudieron cargar las estadísticas: ${e.message}"
            }
        }
    }

    private fun actualizarIndicadores(
        pedidos: List<Pedido>,
        ahora: LocalDateTime
    ) {

        val hoy = ahora.toLocalDate()
        val lunes = hoy.with(
            TemporalAdjusters.previousOrSame(
                DayOfWeek.MONDAY
            )
        )

        val zona = ZoneId.systemDefault()

        // Pedidos registrados hoy.
        _pedidosHoy.value = pedidos.count { pedido ->

            val fecha = Instant
                .ofEpochMilli(pedido.fechaCreacion)
                .atZone(zona)
                .toLocalDate()

            fecha == hoy
        }

        // Pedidos en proceso.
        _enProceso.value = pedidos.count {
            it.estado == "EN_PROCESO"
        }

        // Pedidos listos para recoger.
        _porRecoger.value = pedidos.count {
            it.estado == "LISTO"
        }

        // COMMIT A: Contar pedidos atrasados.
        _pedidosAtrasados.value = pedidos.count { pedido ->

            esPedidoAtrasado(
                pedido = pedido,
                ahora = ahora
            )
        }

        // Obtener los pedidos pagados de la semana actual.
        val pedidosPagados = pedidos.filter { pedido ->

            val fecha = Instant
                .ofEpochMilli(pedido.fechaCreacion)
                .atZone(zona)
                .toLocalDate()

            pedido.pagado &&
                    !fecha.isBefore(lunes) &&
                    !fecha.isAfter(hoy)
        }

        // Calcular ingresos semanales.
        _ingresosSemana.value = pedidosPagados.sumOf {
            it.total
        }

        // Contar pedidos pagados de la semana.
        _pedidosPagadosSemana.value = pedidosPagados.size
    }

    private fun actualizarGraficoEstados(
        pedidos: List<Pedido>
    ) {

        val estados = listOf(
            Triple(
                "RECIBIDO",
                "Recibido",
                Color(0xFFF59E0B)
            ),
            Triple(
                "EN_PROCESO",
                "En proceso",
                Color(0xFF3B82F6)
            ),
            Triple(
                "LISTO",
                "Listo",
                Color(0xFF22C55E)
            ),
            Triple(
                "ENTREGADO",
                "Entregado",
                Color(0xFF8B5CF6)
            ),
            Triple(
                "CANCELADO",
                "Cancelado",
                Color(0xFFEF4444)
            )
        )

        _porEstado.value = estados.map {
                (codigo, etiqueta, color) ->

            DatoGrafico(
                etiqueta = etiqueta,
                valor = pedidos.count {
                    it.estado == codigo
                }.toDouble(),
                color = color
            )
        }
    }

    private suspend fun actualizarGraficoServicios(
        pedidos: List<Pedido>,
        servicios: List<Servicio>
    ) {

        val ingresos = mutableMapOf<Int, Double>()

        // Se consideran únicamente los pedidos pagados.
        for (pedido in pedidos.filter { it.pagado }) {

            val detalles =
                repository.obtenerDetallesPorPedido(pedido.id)

            for (detalle in detalles) {

                ingresos[detalle.servicioId] =
                    (ingresos[detalle.servicioId] ?: 0.0) +
                            detalle.subtotal
            }
        }

        val colores = listOf(
            Color(0xFF0895B5),
            Color(0xFF8B5CF6),
            Color(0xFF22C55E),
            Color(0xFFF59E0B),
            Color(0xFFEF4444)
        )

        _porServicio.value = servicios.mapIndexed {
                indice, servicio ->

            DatoGrafico(
                etiqueta = servicio.nombre,
                valor = ingresos[servicio.id] ?: 0.0,
                color = colores[indice % colores.size]
            )
        }
    }
}