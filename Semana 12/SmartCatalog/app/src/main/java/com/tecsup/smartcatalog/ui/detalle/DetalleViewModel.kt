package com.tecsup.smartcatalog.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.smartcatalog.data.repository.ItemRepository
import com.tecsup.smartcatalog.domain.model.Item
import com.tecsup.smartcatalog.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<Item>>(UiState.Loading)

    val uiState: StateFlow<UiState<Item>> =
        _uiState.asStateFlow()

    fun cargarDetalle(id: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                val producto = repository.obtenerDetalle(id)
                _uiState.value = UiState.Success(producto)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(
                    e.message ?: "No se pudo cargar el detalle"
                )
            }
        }
    }

    fun cambiarFavorito(item: Item) {
        viewModelScope.launch {
            repository.cambiarFavorito(item.id, !item.favorito)
            _uiState.value = UiState.Success(
                item.copy(favorito = !item.favorito)
            )
        }
    }
}
