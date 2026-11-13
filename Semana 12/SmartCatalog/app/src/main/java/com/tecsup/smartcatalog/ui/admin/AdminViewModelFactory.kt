package com.tecsup.smartcatalog.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tecsup.smartcatalog.data.repository.ItemRepository

class AdminViewModelFactory(
    private val repository: ItemRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
            return AdminViewModel(repository) as T
        }

        throw IllegalArgumentException("ViewModel desconocido")
    }
}
