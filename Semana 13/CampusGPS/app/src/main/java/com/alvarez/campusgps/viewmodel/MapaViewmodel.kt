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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class
MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModel() {

    private val _estado =
        MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)

    val estado: StateFlow<UbicacionUi> =
        _estado.asStateFlow()

    private val _lugares = MutableStateFlow(
        lugaresRepo.obtenerLugares().map {
            LugarConDistancia(it, null)
        }
    )

    val lugares: StateFlow<List<LugarConDistancia>> =
        _lugares.asStateFlow()

    private val _seleccionado = MutableStateFlow<Int?>(null)

    val seleccionado: StateFlow<Int?> =
        _seleccionado.asStateFlow()

    // Lugar más cercano del COMMIT 8
    val masCercano: StateFlow<LugarConDistancia?> =
        lugares.map { lista ->
            lista.firstOrNull { it.metros != null }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    // Calcular y ordenar lugares por distancia
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

    // Reiniciar distancias al iniciar otra búsqueda
    private fun limpiarDistancias() {
        _lugares.value = lugaresRepo.obtenerLugares().map {
            LugarConDistancia(it, null)
        }
        _seleccionado.value = null
    }

    // Buscar ubicación con manejo de errores
    fun buscar() {
        if (_estado.value == UbicacionUi.Buscando) return

        _estado.value = UbicacionUi.Buscando

        limpiarDistancias()

        viewModelScope.launch {

            // Comprobar Google Play Services
            if (!ubicacionRepo.playServicesDisponible()) {
                _estado.value = UbicacionUi.Error(
                    mensaje = "Este dispositivo no tiene Google Play Services",
                    tipo = TipoError.SIN_PLAY_SERVICES
                )
                return@launch
            }

            // Comprobar si el GPS está apagado
            if (!ubicacionRepo.gpsDisponible()) {
                _estado.value = UbicacionUi.Error(
                    mensaje = "No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo",
                    tipo = TipoError.GPS_APAGADO
                )
                return@launch
            }

            // Obtener la ubicación
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

    // Permiso denegado
    fun permisoDenegado() {
        limpiarDistancias()

        _estado.value = UbicacionUi.Error(
            mensaje = "Sin permiso de ubicación no puedo mostrar dónde estás",
            tipo = TipoError.SIN_PERMISO
        )
    }

    // Seleccionar un lugar
    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }
}
