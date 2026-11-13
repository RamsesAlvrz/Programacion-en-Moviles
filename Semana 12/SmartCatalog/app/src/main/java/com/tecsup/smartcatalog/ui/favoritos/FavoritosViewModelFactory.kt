package com.tecsup.smartcatalog.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.smartcatalog.data.repository.ItemRepository

class FavoritosViewModelFactory(
    private val repository: ItemRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(FavoritosViewModel::class.java)) {
            return FavoritosViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
