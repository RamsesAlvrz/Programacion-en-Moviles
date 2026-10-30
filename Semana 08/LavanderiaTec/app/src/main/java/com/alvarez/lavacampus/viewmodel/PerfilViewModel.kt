package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

// Representa el gasto de un estudiante durante un mes.
data class GastoMensual(
    val mes: String,
    val anio: Int,
    val total: Double
)

// Gestiona las estadísticas del perfil del estudiante.
// MEJORA D: Incorpora el cálculo de gastos mensuales.
@OptIn(ExperimentalCoroutinesApi::class)
class PerfilViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    // Identificador del estudiante cuyo perfil se está consultando.
    private val _usuarioId = MutableStateFlow<Int?>(null)

    // =====================================
    // PEDIDOS ACTIVOS
    // =====================================

    val pedidosActivos: StateFlow<Int> =
        _usuarioId.flatMapLatest { usuarioId ->

            if (usuarioId != null) {
                repository.contarPedidosActivosUsuario(usuarioId)
            } else {
                flowOf(0)
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    // =====================================
    // TOTAL DE PEDIDOS
    // =====================================

    val totalPedidos: StateFlow<Int> =
        _usuarioId.flatMapLatest { usuarioId ->

            if (usuarioId != null) {
                repository.contarPedidosUsuario(usuarioId)
            } else {
                flowOf(0)
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0
        )

    // =====================================
    // KILOS REGISTRADOS
    // =====================================

    val kilosLavados: StateFlow<Double> =
        _usuarioId.flatMapLatest { usuarioId ->

            if (usuarioId != null) {
                repository.obtenerKilosLavadosUsuario(usuarioId)
            } else {
                flowOf(0.0)
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = 0.0
        )

    // =====================================
    // MEJORA D: GASTOS MENSUALES
    // =====================================

    // Obtiene los pedidos del estudiante y calcula
    // cuánto ha gastado durante los últimos seis meses.
    val gastosMensuales: StateFlow<List<GastoMensual>> =
        _usuarioId.flatMapLatest { usuarioId ->

            if (usuarioId != null) {

                repository.observarPedidosPorUsuario(usuarioId)
                    .map { pedidos ->

                        // Mes actual según la zona horaria del dispositivo.
                        val mesActual = YearMonth.now()

                        // Generar los últimos seis meses.
                        val meses = (5 downTo 0).map { diferencia ->
                            mesActual.minusMonths(diferencia.toLong())
                        }

                        // Considerar únicamente pedidos pagados
                        // que no estén cancelados.
                        val pedidosPagados = pedidos.filter { pedido ->
                            pedido.pagado &&
                                    pedido.estado != "CANCELADO"
                        }

                        // Agrupar los pedidos por mes de creación.
                        val gastosPorMes = pedidosPagados
                            .groupBy { pedido ->

                                val fecha = Instant
                                    .ofEpochMilli(pedido.fechaCreacion)
                                    .atZone(ZoneId.systemDefault())

                                YearMonth.from(fecha)
                            }
                            .mapValues { (_, pedidosDelMes) ->

                                pedidosDelMes.sumOf { pedido ->
                                    pedido.total
                                }
                            }

                        // Preparar los datos del gráfico.
                        meses.map { mes ->

                            val nombreMes = mes.month
                                .getDisplayName(
                                    TextStyle.SHORT,
                                    Locale("es", "PE")
                                )
                                .replaceFirstChar { it.uppercase() }

                            GastoMensual(
                                mes = nombreMes,
                                anio = mes.year,
                                total = gastosPorMes[mes] ?: 0.0
                            )
                        }
                    }

            } else {
                flowOf(emptyList())
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // =====================================
    // SELECCIONAR ESTUDIANTE
    // =====================================

    // Establece el estudiante del que se mostrarán las estadísticas.
    fun cargarPerfil(usuarioId: Int) {
        _usuarioId.value = usuarioId
    }

    // Limpia los datos cuando dejamos de consultar un perfil.
    fun limpiarPerfil() {
        _usuarioId.value = null
    }
}