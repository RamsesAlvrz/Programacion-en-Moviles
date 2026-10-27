package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

// COMMIT 14: Gestiona las estadísticas del perfil del estudiante.
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
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