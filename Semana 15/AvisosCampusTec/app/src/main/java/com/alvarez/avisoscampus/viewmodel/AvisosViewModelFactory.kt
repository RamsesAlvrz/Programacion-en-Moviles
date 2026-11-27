
package com.alvarez.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alvarez.avisoscampus.data.AvisosRepository

class AvisosViewModelFactory(
    private val repository: AvisosRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(
                AvisosViewModel::class.java
            )
        ) {
            return AvisosViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "ViewModel desconocido"
        )
    }
}
