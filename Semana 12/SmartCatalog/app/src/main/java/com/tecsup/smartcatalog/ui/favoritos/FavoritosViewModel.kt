package com.tecsup.smartcatalog.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.smartcatalog.data.repository.ItemRepository
import com.tecsup.smartcatalog.domain.model.Item
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritosViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    val favoritos: StateFlow<List<Item>> =
        repository.observarFavoritos().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun quitarFavorito(item: Item) {
        viewModelScope.launch {
            repository.cambiarFavorito(
                id = item.id,
                favorito = false
            )
        }
    }
}

