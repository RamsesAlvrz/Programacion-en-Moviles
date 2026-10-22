package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.local.entity.Pedido
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.local.LavaDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// ViewModel encargado de gestionar el inicio del estudiante
class InicioViewModel(
    private val database: LavaDatabase
) : ViewModel() {

    // Datos del estudiante
    private val _estudiante = MutableStateFlow<Usuario?>(null)
    val estudiante: StateFlow<Usuario?> = _estudiante.asStateFlow()

    // Lista de pedidos del estudiante
    private val _pedidos = MutableStateFlow<List<Pedido>>(emptyList())
    val pedidos: StateFlow<List<Pedido>> = _pedidos.asStateFlow()

    // Indicador de carga
    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    // Mensaje de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Consultar los datos del estudiante
    fun cargarDatos(usuarioId: Int) {
        viewModelScope.launch {
            _cargando.value = true
            _error.value = null

            try {
                // Buscar el estudiante en Room
                _estudiante.value = database.usuarioDao()
                    .obtenerPorId(usuarioId)

                // Obtener los pedidos del estudiante
                database.pedidoDao()
                    .obtenerPorUsuario(usuarioId)
                    .collect { listaPedidos ->
                        _pedidos.value = listaPedidos
                        _cargando.value = false
                    }

            } catch (e: Exception) {
                _error.value = "Error al cargar los pedidos: ${e.message}"
                _cargando.value = false
            }
        }
    }

    // Obtener el pedido más reciente
    fun pedidoReciente(): Pedido? {
        return _pedidos.value.maxByOrNull {
            it.fechaCreacion
        }
    }

    // Contar los pedidos que todavía no fueron entregados
    fun cantidadPedidosActivos(): Int {
        return _pedidos.value.count {
            it.estado != "ENTREGADO" &&
                    it.estado != "CANCELADO"
        }
    }
}
