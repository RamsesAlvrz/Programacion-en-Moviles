
package com.alvarez.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.campusgps.data.LugaresRepository
import com.alvarez.campusgps.data.UbicacionRepository
import com.alvarez.campusgps.model.LugarConDistancia
import com.alvarez.campusgps.model.Posicion
import com.alvarez.campusgps.model.TipoError
import com.alvarez.campusgps.model.UbicacionUi
import com.alvarez.campusgps.util.distanciaMetros
import com.alvarez.campusgps.util.formatearDistancia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    companion object {
        val CATEGORIAS = listOf(
            "Todos",
            "Estudio",
            "Comida",
            "Eventos",
            "Servicios"
        )
    }

    private val _estado =
        MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)

    val estado: StateFlow<UbicacionUi> =
        _estado.asStateFlow()

    // Lista completa: no se modifica al filtrar.
    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map {
            LugarConDistancia(it, null)
        }
    )

    val lugares: StateFlow<List<LugarConDistancia>> =
        _lugares.asStateFlow()

    // Categoria seleccionada.
    private val _categoriaSeleccionada =
        MutableStateFlow("Todos")

    val categoriaSeleccionada: StateFlow<String> =
        _categoriaSeleccionada.asStateFlow()

    // Filtrado reactivo dentro del ViewModel.
    val lugaresFiltrados: StateFlow<List<LugarConDistancia>> =
        combine(
            lugares,
            categoriaSeleccionada
        ) { lista, categoria ->
            if (categoria == "Todos") {
                lista
            } else {
                lista.filter {
                    it.lugar.categoria == categoria
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = _lugares.value
        )

    private val _seleccionado =
        MutableStateFlow<Int?>(null)

    val seleccionado: StateFlow<Int?> =
        _seleccionado.asStateFlow()

    // Lugar mas cercano de todos los lugares.
    val masCercano: StateFlow<LugarConDistancia?> =
        lugares.map { lista ->
            lista.firstOrNull { it.metros != null }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Lugar mas cercano dentro del filtro activo.
    val masCercanoVisible: StateFlow<LugarConDistancia?> =
        lugaresFiltrados.map { lista ->
            lista.firstOrNull { it.metros != null }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    // Cambiar la categoria activa.
    fun seleccionarCategoria(categoria: String) {
        if (categoria !in CATEGORIAS) return

        _categoriaSeleccionada.value = categoria

        // Si el lugar seleccionado queda oculto,
        // se elimina la seleccion anterior.
        val seleccionadoActual = _seleccionado.value

        if (seleccionadoActual != null) {
            val sigueVisible = _lugares.value.any {
                it.lugar.id == seleccionadoActual &&
                        (categoria == "Todos" ||
                                it.lugar.categoria == categoria)
            }

            if (!sigueVisible) {
                _seleccionado.value = null
            }
        }
    }

    // Calcular y ordenar las distancias.
    private fun actualizarDistancias(pos: Posicion) {
        val listaOrdenada = lugaresRepo.obtenerLugares()
            .map { lugar ->
                LugarConDistancia(
                    lugar = lugar,
                    metros = distanciaMetros(pos, lugar)
                )
            }
            .sortedBy { it.metros }

        _lugares.value = listaOrdenada

        listaOrdenada.forEach { item ->
            Log.d(
                "CampusGPS",
                "${item.lugar.nombre}: " +
                        formatearDistancia(item.metros!!)
            )
        }
    }

    // Reiniciar distancias al iniciar una busqueda.
    private fun limpiarDistancias() {
        _lugares.value = lugaresRepo.obtenerLugares().map {
            LugarConDistancia(it, null)
        }

        _seleccionado.value = null
    }

    // Buscar ubicacion con manejo de errores.
    fun buscar() {
        if (_estado.value == UbicacionUi.Buscando) return

        _estado.value = UbicacionUi.Buscando
        limpiarDistancias()

        viewModelScope.launch {

            if (!ubicacionRepo.playServicesDisponible()) {
                _estado.value = UbicacionUi.Error(
                    mensaje = "Este dispositivo no tiene Google Play Services",
                    tipo = TipoError.SIN_PLAY_SERVICES
                )
                return@launch
            }

            if (!ubicacionRepo.gpsDisponible()) {
                _estado.value = UbicacionUi.Error(
                    mensaje = "No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo",
                    tipo = TipoError.GPS_APAGADO
                )
                return@launch
            }

            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->
                    actualizarDistancias(posicion)
                    _estado.value = UbicacionUi.Lista(posicion)
                }
                .onFailure { error ->
                    _estado.value = when (error) {

                        is SecurityException -> {
                            UbicacionUi.Error(
                                mensaje = "Sin permiso de ubicación no puedo mostrar dónde estás",
                                tipo = TipoError.SIN_PERMISO
                            )
                        }

                        is IllegalStateException -> {
                            UbicacionUi.Error(
                                mensaje = "No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo",
                                tipo = TipoError.GPS_APAGADO
                            )
                        }

                        else -> {
                            UbicacionUi.Error(
                                mensaje = error.message
                                    ?: "Ocurrió un error al obtener la ubicación",
                                tipo = TipoError.OTRO
                            )
                        }
                    }
                }
        }
    }

    fun permisoDenegado() {
        limpiarDistancias()

        _estado.value = UbicacionUi.Error(
            mensaje = "Sin permiso de ubicación no puedo mostrar dónde estás",
            tipo = TipoError.SIN_PERMISO
        )
    }

    // Seleccionar un lugar visible.
    fun seleccionar(id: Int?) {
        if (id == null) {
            _seleccionado.value = null
            return
        }

        val categoria = _categoriaSeleccionada.value

        val esVisible = _lugares.value.any {
            it.lugar.id == id &&
                    (categoria == "Todos" ||
                            it.lugar.categoria == categoria)
        }

        if (esVisible) {
            _seleccionado.value = id
        }
    }
}
