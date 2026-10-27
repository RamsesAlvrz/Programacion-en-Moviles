package com.alvarez.lavacampus.ui.administrador

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.model.ClienteResumen
import com.alvarez.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// COMMIT 14: ViewModel para la pantalla de clientes.
class ClientesViewModel(
    private val repository: LavaRepository
) : ViewModel() {

    // Texto ingresado en el buscador.
    private val _busqueda = MutableStateFlow("")

    val busqueda: StateFlow<String> = _busqueda

    // Lista de clientes obtenida desde Room.
    private val clientesRoom = repository.observarResumenClientes()

    // Combina los datos de Room con el texto de búsqueda.
    val clientes: StateFlow<List<ClienteResumen>> = combine(
        clientesRoom,
        _busqueda
    ) { listaClientes, textoBusqueda ->

        val texto = textoBusqueda.trim()

        if (texto.isEmpty()) {

            // Sin búsqueda: mostrar todos los clientes.
            listaClientes

        } else {

            // Buscar por nombre o código institucional.
            listaClientes.filter { cliente ->

                cliente.nombre.contains(
                    texto,
                    ignoreCase = true
                ) || cliente.codigo.contains(
                    texto,
                    ignoreCase = true
                )
            }
        }

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    // Actualizar el texto del buscador.
    fun actualizarBusqueda(texto: String) {
        _busqueda.value = texto
    }

    // Limpiar el buscador.
    fun limpiarBusqueda() {
        _busqueda.value = ""
    }
}