package com.tecsup.smartcatalog.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.smartcatalog.data.repository.ItemRepository

class ListaViewModelFactory(
    private val repository: ItemRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(ListaViewModel::class.java)) {
            return ListaViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
