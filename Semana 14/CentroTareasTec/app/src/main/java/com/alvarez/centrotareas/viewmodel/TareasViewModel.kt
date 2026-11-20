package com.alvarez.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.centrotareas.data.TareasRepository
import com.alvarez.centrotareas.model.CargaUi
import com.alvarez.centrotareas.model.PrimosUi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout

class TareasViewModel(
    private val repository: TareasRepository
) : ViewModel() {

    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> = _simularError.asStateFlow()

    private val _limiteActivo = MutableStateFlow(false)
    val limiteActivo: StateFlow<Boolean> = _limiteActivo.asStateFlow()

    // FASE 2 - MEJORA 2
    private val _reintentoAutomatico = MutableStateFlow(false)
    val reintentoAutomatico: StateFlow<Boolean> =
        _reintentoAutomatico.asStateFlow()

    private val _intentoActual = MutableStateFlow(0)
    val intentoActual: StateFlow<Int> =
        _intentoActual.asStateFlow()

    private val _mensajeReintento = MutableStateFlow("")
    val mensajeReintento: StateFlow<String> =
        _mensajeReintento.asStateFlow()

    private var tareaJob: Job? = null
    private var primosJob: Job? = null
    private var cargaJob: Job? = null
    private var ultimoModoParalelo = false

    fun cambiarLimiteActivo(activo: Boolean) {
        if (_carga.value !is CargaUi.Cargando) {
            _limiteActivo.value = activo
        }
    }

    fun cambiarSimularError(activar: Boolean) {
        if (_carga.value !is CargaUi.Cargando) {
            _simularError.value = activar
        }
    }

    fun cambiarReintentoAutomatico(activo: Boolean) {
        if (_carga.value !is CargaUi.Cargando) {
            _reintentoAutomatico.value = activo
        }
    }

    // FUNCIONALIDAD DE LA FASE 1
    fun tareaLarga() {
        if (_corriendo.value) return

        _corriendo.value = true
        _progreso.value = 0f
        _mensaje.value = "Trabajando..."

        tareaJob = viewModelScope.launch {
            Log.d("HILO", "Inicio: ${Thread.currentThread().name}")

            try {
                withContext(Dispatchers.Default) {
                    Log.d("HILO", "Trabajo: ${Thread.currentThread().name}")

                    for (i in 1..100) {
                        delay(40)
                        _progreso.value = i / 100f
                    }
                }

                Log.d("HILO", "Final: ${Thread.currentThread().name}")
                _mensaje.value = "Terminado"

            } catch (e: CancellationException) {
                _mensaje.value = "Cancelado"
                throw e

            } finally {
                _corriendo.value = false
                tareaJob = null
            }
        }
    }

    fun cancelarTarea() {
        tareaJob?.cancel()
    }

    fun contarPrimos(hasta: Int) {
        if (hasta < 2 || _primos.value is PrimosUi.Calculando) {
            return
        }

        _primos.value = PrimosUi.Calculando

        primosJob = viewModelScope.launch {
            val inicio = System.currentTimeMillis()

            try {
                val cantidad = repository.contarPrimos(hasta)
                val tiempo = System.currentTimeMillis() - inicio

                _primos.value = PrimosUi.Listo(
                    cantidad = cantidad,
                    milisegundos = tiempo
                )

            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e

            } finally {
                primosJob = null
            }
        }
    }

    fun cancelarPrimos() {
        primosJob?.cancel()
    }

    // CARGAS
    fun cargarSecuencial() {
        ejecutarCarga(paralelo = false)
    }

    fun cargarParalelo() {
        ejecutarCarga(paralelo = true)
    }

    fun reintentarCarga() {
        ejecutarCarga(paralelo = ultimoModoParalelo)
    }

    private fun ejecutarCarga(paralelo: Boolean) {
        if (cargaJob?.isActive == true) return

        ultimoModoParalelo = paralelo
        _carga.value = CargaUi.Cargando
        _intentoActual.value = 0
        _mensajeReintento.value = ""

        val usarLimite = _limiteActivo.value
        val provocarError = _simularError.value
        val reintentar = _reintentoAutomatico.value

        cargaJob = viewModelScope.launch {
            val inicio = System.currentTimeMillis()
            val maxIntentos = if (reintentar) 3 else 1

            try {
                for (intento in 1..maxIntentos) {
                    _intentoActual.value = intento
                    _mensajeReintento.value =
                        "Ejecutando intento $intento de $maxIntentos"

                    try {
                        val resultado = if (usarLimite) {
                            withTimeout(2000L) {
                                obtenerDatos(paralelo, provocarError)
                            }
                        } else {
                            obtenerDatos(paralelo, provocarError)
                        }

                        val tiempo =
                            System.currentTimeMillis() - inicio

                        _mensajeReintento.value =
                            "Operación completada en el intento $intento"

                        _carga.value = CargaUi.Exito(
                            modo = if (paralelo) {
                                "A la vez"
                            } else {
                                "Una por una"
                            },
                            perfil = resultado.first,
                            pedidos = resultado.second.size,
                            milisegundos = tiempo
                        )

                        return@launch

                    } catch (e: TimeoutCancellationException) {
                        _mensajeReintento.value =
                            "Límite de tiempo alcanzado"

                        _carga.value = CargaUi.Error(
                            mensaje = "Tiempo máximo superado. " +
                                    "La carga tardó más de 2 segundos."
                        )

                        return@launch

                    } catch (e: CancellationException) {
                        throw e

                    } catch (e: Exception) {
                        if (intento == maxIntentos) {
                            _mensajeReintento.value =
                                "Se agotaron los $maxIntentos intentos"

                            _carga.value = CargaUi.Error(
                                mensaje = "No se pudo completar la carga " +
                                        "después de $maxIntentos intento(s): " +
                                        (e.message ?: "Error desconocido")
                            )
                        } else {
                            val espera = intento * 1000L

                            _mensajeReintento.value =
                                "Intento $intento fallido. " +
                                        "Nuevo intento en ${espera / 1000} s"

                            delay(espera)
                        }
                    }
                }
            } finally {
                // El estado deja de estar en Cargando al
                // finalizar correctamente o agotar intentos.
                // La cancelación del ViewModel se propaga.
            }
        }
    }

    private suspend fun obtenerDatos(
        paralelo: Boolean,
        simularError: Boolean
    ): Pair<String, List<String>> {

        return if (paralelo) {
            coroutineScope {
                val perfil = async {
                    repository.cargarPerfil()
                }

                val pedidos = async {
                    repository.cargarPedidos(simularError)
                }

                Pair(perfil.await(), pedidos.await())
            }
        } else {
            val perfil = repository.cargarPerfil()
            val pedidos = repository.cargarPedidos(simularError)

            Pair(perfil, pedidos)
        }
    }
}
