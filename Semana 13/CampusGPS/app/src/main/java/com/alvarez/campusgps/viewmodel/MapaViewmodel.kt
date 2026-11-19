package com.alvarez.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.campusgps.data.LugaresRepository
import com.alvarez.campusgps.data.PreferenciasRepository
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository,
    private val preferenciasRepo: PreferenciasRepository
) : ViewModel() {

    companion object {
        val CATEGORIAS = listOf(
            "Todos",
            "Estudio",
            "Comida",
            "Eventos",
            "Servicios"
        )

        val RADIOS = listOf(100, 200, 500)
    }

    private val _estado =
        MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)

    val estado: StateFlow<UbicacionUi> =
        _estado.asStateFlow()

    // Lista original de lugares.
    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map {
            LugarConDistancia(it, null)
        }
    )

    val lugares: StateFlow<List<LugarConDistancia>> =
        _lugares.asStateFlow()

    // Filtro por categoria.
    private val _categoriaSeleccionada =
        MutableStateFlow("Todos")

    val categoriaSeleccionada: StateFlow<String> =
        _categoriaSeleccionada.asStateFlow()

    // Radio de proximidad.
    private val _radioSeleccionado =
        MutableStateFlow<Int?>(null)

    val radioSeleccionado: StateFlow<Int?> =
        _radioSeleccionado.asStateFlow()

    // Filtrado combinado de categoria y radio.
    val lugaresFiltrados: StateFlow<List<LugarConDistancia>> =
        combine(
            lugares,
            categoriaSeleccionada,
            radioSeleccionado
        ) { lista, categoria, radio ->

            lista.filter { item ->
                val coincideCategoria =
                    categoria == "Todos" ||
                            item.lugar.categoria == categoria

                val coincideRadio =
                    radio == null ||
                            (item.metros != null &&
                                    item.metros <= radio.toFloat())

                coincideCategoria && coincideRadio
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

    // Evita que la lectura inicial sobrescriba
    // una seleccion realizada durante el arranque.
    private var seleccionManualRealizada = false

    // Lugar mas cercano global.
    val masCercano: StateFlow<LugarConDistancia?> =
        lugares.map { lista ->
            lista.firstOrNull { it.metros != null }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Lugar mas cercano visible.
    val masCercanoVisible: StateFlow<LugarConDistancia?> =
        lugaresFiltrados.map { lista ->
            lista.firstOrNull { it.metros != null }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    init {
        recuperarUltimoLugar()
    }

    // COMMIT 14: recuperar seleccion guardada.
    private fun recuperarUltimoLugar() {
        viewModelScope.launch {
            try {
                val idGuardado =
                    preferenciasRepo.ultimoLugarId.first()

                if (!seleccionManualRealizada &&
                    idGuardado != null
                ) {
                    val existe = _lugares.value.any {
                        it.lugar.id == idGuardado
                    }

                    if (existe) {
                        _seleccionado.value = idGuardado
                        validarSeleccion()

                        Log.d(
                            "CampusGPS",
                            "Lugar recuperado: $idGuardado"
                        )
                    } else {
                        preferenciasRepo.limpiarUltimoLugar()
                    }
                }
            } catch (error: Exception) {
                Log.e(
                    "CampusGPS",
                    "No se pudo recuperar el lugar",
                    error
                )
            }
        }
    }

    // Guardar la seleccion en DataStore.
    private fun guardarSeleccion(id: Int) {
        viewModelScope.launch {
            try {
                preferenciasRepo.guardarUltimoLugar(id)

                Log.d(
                    "CampusGPS",
                    "Lugar guardado: $id"
                )
            } catch (error: Exception) {
                Log.e(
                    "CampusGPS",
                    "No se pudo guardar el lugar",
                    error
                )
            }
        }
    }

    fun seleccionarCategoria(categoria: String) {
        if (categoria !in CATEGORIAS) return

        _categoriaSeleccionada.value = categoria
        validarSeleccion()
    }

    fun seleccionarRadio(radio: Int?) {
        if (radio != null && radio !in RADIOS) return

        _radioSeleccionado.value = radio
        validarSeleccion()
    }

    // Elimina solo la seleccion visible cuando
    // el filtro deja de incluir ese lugar.
    // No borra la preferencia guardada.
    private fun validarSeleccion() {
        val idActual = _seleccionado.value ?: return

        val categoria = _categoriaSeleccionada.value
        val radio = _radioSeleccionado.value

        val sigueVisible = _lugares.value.any { item ->
            val coincideCategoria =
                categoria == "Todos" ||
                        item.lugar.categoria == categoria

            val coincideRadio =
                radio == null ||
                        (item.metros != null &&
                                item.metros <= radio.toFloat())

            item.lugar.id == idActual &&
                    coincideCategoria &&
                    coincideRadio
        }

        if (!sigueVisible) {
            _seleccionado.value = null
        }
    }

    // Calcular y ordenar distancias.
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
        validarSeleccion()

        listaOrdenada.forEach { item ->
            Log.d(
                "CampusGPS",
                "${item.lugar.nombre}: " +
                        formatearDistancia(item.metros!!)
            )
        }
    }

    private fun limpiarDistancias() {
        _lugares.value = lugaresRepo.obtenerLugares().map {
            LugarConDistancia(it, null)
        }

        _seleccionado.value = null
    }

    // Buscar ubicacion.
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

    // Seleccionar y persistir un lugar visible.
    fun seleccionar(id: Int?) {
        seleccionManualRealizada = true

        if (id == null) {
            _seleccionado.value = null
            return
        }

        val categoria = _categoriaSeleccionada.value
        val radio = _radioSeleccionado.value

        val esVisible = _lugares.value.any { item ->
            val coincideCategoria =
                categoria == "Todos" ||
                        item.lugar.categoria == categoria

            val coincideRadio =
                radio == null ||
                        (item.metros != null &&
                                item.metros <= radio.toFloat())

            item.lugar.id == id &&
                    coincideCategoria &&
                    coincideRadio
        }

        if (esVisible) {
            _seleccionado.value = id
            guardarSeleccion(id)
        }
    }
}
