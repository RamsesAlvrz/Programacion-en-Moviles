package com.alvarez.campusgps.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.campusgps.data.LugaresRepository
import com.alvarez.campusgps.data.UbicacionRepository
import com.alvarez.campusgps.model.LugarConDistancia
import com.alvarez.campusgps.model.Posicion
import com.alvarez.campusgps.model.UbicacionUi
import com.alvarez.campusgps.util.distanciaMetros
import com.alvarez.campusgps.util.formatearDistancia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MapaViewModel(
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

        // Mostrar resultados en Logcat
        listaOrdenada.forEach { item ->
            Log.d(
                "CampusGPS",
                "${item.lugar.nombre}: " +
                        formatearDistancia(item.metros!!)
            )
        }
    }

    fun buscar() {
        _estado.value = UbicacionUi.Buscando

        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion()
                .onSuccess { posicion ->
                    actualizarDistancias(posicion)
                    _estado.value = UbicacionUi.Lista(posicion)
                }
                .onFailure {
                    _estado.value = UbicacionUi.Error(
                        it.message
                            ?: "No se pudo obtener la ubicación"
                    )
                }
        }
    }

    fun permisoDenegado() {
        _estado.value = UbicacionUi.Error(
            "Sin permiso de ubicación no puedo mostrar dónde estás"
        )
    }

    fun seleccionar(id: Int?) {
        _seleccionado.value = id
    }
}
