package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import java.util.Locale

// Datos necesarios para cada tarjeta de la bandeja.
data class PedidoBandeja(
    val pedido: Pedido,
    val estudiante: Usuario?,
    val detalleResumen: String = ""
)

class BandejaViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _filtroEstado = MutableStateFlow("TODOS")
    val filtroEstado: StateFlow<String> = _filtroEstado.asStateFlow()

    private val pedidosConDatos = combine(
        repository.observarPedidos(),
        repository.observarClientes(),
        repository.observarServicios()
    ) { listaPedidos, listaClientes, listaServicios ->
        Triple(listaPedidos, listaClientes, listaServicios)
    }.mapLatest { (listaPedidos, listaClientes, listaServicios) ->
        val clientesPorId = listaClientes.associateBy { it.id }
        val serviciosPorId = listaServicios.associateBy { it.id }

        listaPedidos.map { pedido ->
            val detalles = repository.obtenerDetallesPorPedido(pedido.id)
            val unidades = detalles.mapNotNull { detalle ->
                val servicio = serviciosPorId[detalle.servicioId]
                    ?: return@mapNotNull null
                servicio.unidad.trim().uppercase(Locale.ROOT) to detalle.cantidad
            }

            // Solo sumamos cantidades si todas las unidades son iguales.
            val resumen = if (unidades.isEmpty()) {
                ""
            } else {
                val unidadesDistintas = unidades.map { it.first }.distinct()
                if (unidadesDistintas.size == 1) {
                    val cantidadTotal = unidades.sumOf { it.second }
                    val numero = if (cantidadTotal % 1.0 == 0.0) {
                        cantidadTotal.toInt().toString()
                    } else {
                        String.format(Locale.US, "%.1f", cantidadTotal)
                    }
                    val unidad = when (unidadesDistintas.first()) {
                        "KG", "KILO", "KILOS", "KILOGRAMO", "KILOGRAMOS" -> "kg"
                        "PRENDA", "PRENDAS" -> if (cantidadTotal == 1.0) "prenda" else "prendas"
                        "UNIDAD", "UNIDADES" -> if (cantidadTotal == 1.0) "unidad" else "unidades"
                        else -> unidadesDistintas.first().lowercase(Locale.ROOT)
                    }
                    "$numero $unidad"
                } else {
                    // No mezclar kilogramos con prendas o unidades.
                    "Varias unidades"
                }
            }

            PedidoBandeja(
                pedido = pedido,
                estudiante = clientesPorId[pedido.usuarioId],
                detalleResumen = resumen
            )
        }
    }

    val pedidos: StateFlow<List<PedidoBandeja>> = combine(
        pedidosConDatos,
        _busqueda,
        _filtroEstado
    ) { listaPedidos, texto, estado ->
        val textoBuscado = texto.trim()
        listaPedidos.filter { item ->
            val coincideEstado = estado == "TODOS" || item.pedido.estado == estado
            val nombre = item.estudiante?.nombre.orEmpty()
            val numero = item.pedido.id.toString()
            val numeroConCeros = numero.padStart(3, '0')
            val coincideBusqueda = textoBuscado.isBlank() ||
                    nombre.contains(textoBuscado, ignoreCase = true) ||
                    numero.contains(textoBuscado) ||
                    numeroConCeros.contains(textoBuscado) ||
                    "#$numeroConCeros".contains(textoBuscado)
            coincideEstado && coincideBusqueda
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun actualizarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    fun seleccionarFiltro(estado: String) {
        if (estado in setOf("TODOS", "RECIBIDO", "EN_PROCESO", "LISTO")) {
            _filtroEstado.value = estado
        }
    }

    fun limpiarFiltros() {
        _busqueda.value = ""
        _filtroEstado.value = "TODOS"
    }
}
