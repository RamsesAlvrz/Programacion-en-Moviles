package com.alvarez.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.centrotareas.data.HistorialRepository
import com.alvarez.centrotareas.data.local.EjecucionEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistorialViewModel(
    private val repository: HistorialRepository
) : ViewModel() {

    val ejecuciones: StateFlow<List<EjecucionEntity>> =
        repository.ejecuciones.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun limpiarHistorial() {
        viewModelScope.launch {
            repository.limpiarHistorial()
        }
    }
}
