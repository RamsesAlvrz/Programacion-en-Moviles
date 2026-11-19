package com.alvarez.campusgps.viewmodel

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.campusgps.data.LugaresRepository
import com.alvarez.campusgps.data.PreferenciasRepository
import com.alvarez.campusgps.data.RutaRepository
import com.alvarez.campusgps.data.RutaRepositoryOSRM
import com.alvarez.campusgps.data.UbicacionRepository
import com.alvarez.campusgps.model.EstadoRuta
import com.alvarez.campusgps.model.LugarConDistancia
import com.alvarez.campusgps.model.Posicion
import com.alvarez.campusgps.model.TipoError
import com.alvarez.campusgps.model.UbicacionUi
import com.alvarez.campusgps.util.distanciaMetros
import com.alvarez.campusgps.util.formatearDistancia
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
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
    private val preferenciasRepo: PreferenciasRepository,
    private val rutaRepo: RutaRepository = RutaRepositoryOSRM()
) : ViewModel() {

    companion object {
        val CATEGORIAS = listOf(
            "Todos", "Estudio", "Comida",
            "Eventos", "Servicios"
        )

        val RADIOS = listOf(100, 200, 500)
    }

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

    private val _categoriaSeleccionada =
        MutableStateFlow("Todos")

    val categoriaSeleccionada: StateFlow<String> =
        _categoriaSeleccionada.asStateFlow()

    private val _radioSeleccionado =
        MutableStateFlow<Int?>(null)

    val radioSeleccionado: StateFlow<Int?> =
        _radioSeleccionado.asStateFlow()

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

    private var seleccionManualRealizada = false

    val masCercano: StateFlow<LugarConDistancia?> =
        lugares.map { lista ->
            lista.firstOrNull { it.metros != null }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    val masCercanoVisible: StateFlow<LugarConDistancia?> =
        lugaresFiltrados.map { lista ->
            lista.firstOrNull { it.metros != null }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    // COMMIT 15: estado del recorrido peatonal.
    private val _estadoRuta =
        MutableStateFlow<EstadoRuta>(EstadoRuta.SinRuta)

    val estadoRuta: StateFlow<EstadoRuta> =
        _estadoRuta.asStateFlow()

    private var trabajoRuta: Job? = null

    // El servidor demo permite como maximo
    // una solicitud por segundo.
    private var ultimaSolicitudRuta = 0L

    init {
        recuperarUltimoLugar()
    }

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
                    } else {
                        preferenciasRepo.limpiarUltimoLugar()
                    }
                }
            } catch (error: Exception) {
                Log.e(
                    "CampusGPS",
                    "Error al recuperar seleccion",
                    error
                )
            }
        }
    }

    private fun guardarSeleccion(id: Int) {
        viewModelScope.launch {
            try {
                preferenciasRepo.guardarUltimoLugar(id)
            } catch (error: Exception) {
                Log.e(
                    "CampusGPS",
                    "Error al guardar seleccion",
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
                    coincideCategoria && coincideRadio
        }

        if (!sigueVisible) {
            _seleccionado.value = null
            limpiarRuta()
        }
    }

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
        limpiarRuta()
    }

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
                        is SecurityException ->
                            UbicacionUi.Error(
                                mensaje = "Sin permiso de ubicación no puedo mostrar dónde estás",
                                tipo = TipoError.SIN_PERMISO
                            )

                        is IllegalStateException ->
                            UbicacionUi.Error(
                                mensaje = "No se pudo obtener la ubicación. Activa el GPS e intenta de nuevo",
                                tipo = TipoError.GPS_APAGADO
                            )

                        else ->
                            UbicacionUi.Error(
                                mensaje = error.message
                                    ?: "Ocurrió un error al obtener la ubicación",
                                tipo = TipoError.OTRO
                            )
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

    fun seleccionar(id: Int?) {
        seleccionManualRealizada = true

        if (id == null) {
            _seleccionado.value = null
            limpiarRuta()
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
                    coincideCategoria && coincideRadio
        }

        if (esVisible) {
            if (_seleccionado.value != id) {
                limpiarRuta()
            }

            _seleccionado.value = id
            guardarSeleccion(id)
        }
    }

    // Solicitar ruta desde la ubicacion actual.
    fun calcularRuta() {
        val posicion =
            (_estado.value as? UbicacionUi.Lista)
                ?.posicion ?: run {
                _estadoRuta.value = EstadoRuta.Error(
                    "Primero obtén tu ubicación."
                )
                return
            }

        val id = _seleccionado.value ?: run {
            _estadoRuta.value = EstadoRuta.Error(
                "Selecciona un lugar del campus."
            )
            return
        }

        val destino = lugaresFiltrados.value
            .firstOrNull { it.lugar.id == id }
            ?.lugar ?: run {
            _estadoRuta.value = EstadoRuta.Error(
                "El lugar seleccionado no está visible."
            )
            return
        }

        if (_estadoRuta.value is EstadoRuta.Cargando) return

        val ahora = SystemClock.elapsedRealtime()

        if (ahora - ultimaSolicitudRuta < 1200L) {
            _estadoRuta.value = EstadoRuta.Error(
                "Espera un momento antes de solicitar otra ruta."
            )
            return
        }

        ultimaSolicitudRuta = ahora
        trabajoRuta?.cancel()
        _estadoRuta.value = EstadoRuta.Cargando

        trabajoRuta = viewModelScope.launch {
            try {
                val ruta = rutaRepo.obtenerRuta(
                    origen = posicion,
                    destinoLatitud = destino.latitud,
                    destinoLongitud = destino.longitud
                )

                _estadoRuta.value = EstadoRuta.Lista(
                    ruta = ruta,
                    destinoId = destino.id,
                    destinoNombre = destino.nombre
                )
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                Log.e(
                    "CampusGPS",
                    "Error calculando ruta",
                    error
                )

                _estadoRuta.value = EstadoRuta.Error(
                    error.message
                        ?: "No se pudo calcular la ruta."
                )
            }
        }
    }

    fun limpiarRuta() {
        trabajoRuta?.cancel()
        trabajoRuta = null
        _estadoRuta.value = EstadoRuta.SinRuta
    }
}
