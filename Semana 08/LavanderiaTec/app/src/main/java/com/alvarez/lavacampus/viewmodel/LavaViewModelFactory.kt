
package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvarez.lavacampus.data.local.LavaDatabase
import com.alvarez.lavacampus.data.prefs.PreferenciasStore
import com.alvarez.lavacampus.data.repository.LavaRepository

// Fábrica encargada de crear los ViewModels de LavaCampus
class LavaViewModelFactory(
    private val repository: LavaRepository,
    private val preferenciasStore: PreferenciasStore,
    private val database: LavaDatabase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        // ViewModel para gestionar el inicio y cierre de sesión
        if (modelClass.isAssignableFrom(SesionViewModel::class.java)) {
            return SesionViewModel(
                repository = repository,
                preferenciasStore = preferenciasStore
            ) as T
        }

        // ViewModel para consultar los pedidos del estudiante
        if (modelClass.isAssignableFrom(InicioViewModel::class.java)) {
            return InicioViewModel(
                database = database
            ) as T
        }

        throw IllegalArgumentException(
            "ViewModel no reconocido: ${modelClass.name}"
        )
    }
}
