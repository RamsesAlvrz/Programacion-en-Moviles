package com.tecsup.smartcatalog.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.smartcatalog.data.repository.ItemRepository
import com.tecsup.smartcatalog.domain.model.Item
import com.tecsup.smartcatalog.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<Item>>>(UiState.Loading)

    val uiState: StateFlow<UiState<List<Item>>> =
        _uiState.asStateFlow()

    init {
        cargarProductos()
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                repository.sincronizarProductos()
                repository.observarProductos().collect { productos ->
                    _uiState.value = UiState.Success(productos)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    e.message ?: "Error al cargar productos"
                )
            }
        }
    }

    fun cambiarFavorito(item: Item) {
        viewModelScope.launch {
            repository.cambiarFavorito(
                item.id,
                !item.favorito
            )
        }
    }
}
