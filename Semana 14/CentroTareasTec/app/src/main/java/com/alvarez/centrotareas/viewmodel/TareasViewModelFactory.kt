package com.alvarez.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvarez.centrotareas.data.HistorialRepository
import com.alvarez.centrotareas.data.TareasRepository

class TareasViewModelFactory(
    private val repository: TareasRepository,
    private val historialRepository: HistorialRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(TareasViewModel::class.java)) {
            return TareasViewModel(
                repository,
                historialRepository
            ) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
