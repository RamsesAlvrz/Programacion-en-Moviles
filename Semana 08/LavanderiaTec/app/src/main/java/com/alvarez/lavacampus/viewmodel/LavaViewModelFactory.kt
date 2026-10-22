package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvarez.lavacampus.data.prefs.PreferenciasStore
import com.alvarez.lavacampus.data.repository.LavaRepository

// Fábrica encargada de crear los ViewModels
class LavaViewModelFactory(
    private val repository: LavaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
            return SesionViewModel(
                repository = repository,
                preferenciasStore = preferenciasStore
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel no reconocido: ${modelClass.name}"
        )
    }
}
