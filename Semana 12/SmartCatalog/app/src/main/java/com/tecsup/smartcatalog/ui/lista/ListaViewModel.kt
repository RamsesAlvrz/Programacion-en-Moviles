
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

    private val _busqueda = MutableStateFlow("")
    val busqueda: StateFlow<String> = _busqueda.asStateFlow()

    private val _marcaSeleccionada = MutableStateFlow("Todas")
    val marcaSeleccionada: StateFlow<String> =
        _marcaSeleccionada.asStateFlow()

    private val _ordenPrecio = MutableStateFlow("Normal")
    val ordenPrecio: StateFlow<String> = _ordenPrecio.asStateFlow()

    init {
        cargarProductos()
    }

    fun actualizarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    fun seleccionarMarca(marca: String) {
        _marcaSeleccionada.value = marca
    }

    fun seleccionarOrden(orden: String) {
        _ordenPrecio.value = orden
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            try {
                repository.sincronizarProductos()

                val productos =
                    repository.observarProductos().first()

                _uiState.value = UiState.Success(
                    data = productos,
                    offline = false
                )

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {

                val productosLocales =
                    repository.observarProductos().first()

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

            val productos =
                repository.observarProductos().first()

            val estadoAnterior = _uiState.value

            val esOffline =
                (estadoAnterior as? UiState.Success)?.offline ?: false

            _uiState.value = UiState.Success(
                data = productos,
                offline = esOffline
            )
        }
    }
}
