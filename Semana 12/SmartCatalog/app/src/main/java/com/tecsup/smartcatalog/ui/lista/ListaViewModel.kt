
package com.tecsup.smartcatalog.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.smartcatalog.data.repository.ItemRepository
import com.tecsup.smartcatalog.domain.model.Item
import com.tecsup.smartcatalog.ui.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
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

                val productos = repository.observarProductos().first()

                _uiState.value = UiState.Success(
                    data = productos,
                    offline = false
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {

                val productosLocales = repository.observarProductos().first()

                if (productosLocales.isNotEmpty()) {
                    _uiState.value = UiState.Success(
                        data = productosLocales,
                        offline = true
                    )
                } else {
                    _uiState.value = UiState.Error(
                        "No hay conexión y todavía no existen productos guardados."
                    )
                }
            }
        }
    }

    fun cambiarFavorito(item: Item) {
        viewModelScope.launch {
            repository.cambiarFavorito(
                item.id,
                !item.favorito
            )

            val productos = repository.observarProductos().first()

            val estadoAnterior = _uiState.value
            val esOffline = (estadoAnterior as? UiState.Success)?.offline ?: false

            _uiState.value = UiState.Success(
                data = productos,
                offline = esOffline
            )
        }
    }
}
