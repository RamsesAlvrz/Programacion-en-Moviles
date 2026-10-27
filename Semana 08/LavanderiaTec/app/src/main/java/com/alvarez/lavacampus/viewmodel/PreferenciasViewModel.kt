package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.prefs.PreferenciasStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// COMMIT 14: Gestiona las preferencias de LavaCampus.
class PreferenciasViewModel(
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    // =====================================
    // MODO OSCURO
    // =====================================

    val modoOscuro: StateFlow<Boolean> =
        preferenciasStore.modoOscuro.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = false
        )

    fun cambiarModoOscuro(activado: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarModoOscuro(activado)
        }
    }

    // =====================================
    // NOTIFICACIONES
    // =====================================

    val notificaciones: StateFlow<Boolean> =
        preferenciasStore.notificaciones.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = true
        )

    fun cambiarNotificaciones(activadas: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarNotificaciones(activadas)
        }
    }

    // =====================================
    // RECEPCIÓN DE PEDIDOS
    // =====================================

    val recibirPedidos: StateFlow<Boolean> =
        preferenciasStore.recibirPedidos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = true
        )

    fun cambiarRecepcionPedidos(activado: Boolean) {
        viewModelScope.launch {
            preferenciasStore.guardarRecibirPedidos(activado)
        }
    }
}