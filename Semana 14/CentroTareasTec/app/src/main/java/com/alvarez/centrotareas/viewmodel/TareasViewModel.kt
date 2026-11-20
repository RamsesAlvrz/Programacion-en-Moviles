package com.alvarez.centrotareas.viewmodel

import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.centrotareas.data.HistorialRepository
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
import java.util.Locale
import kotlin.math.abs

data class ComparacionTiempos(
    val secuencialMs: Long,
    val paraleloMs: Long
) {
    val diferenciaMs: Long
        get() = abs(secuencialMs - paraleloMs)

    val masRapido: String
        get() = when {
            paraleloMs < secuencialMs -> "A la vez"
            secuencialMs < paraleloMs -> "Una por una"
            else -> "Empate"
        }

    val ahorroPorcentaje: Double
        get() {
            val mayor = maxOf(secuencialMs, paraleloMs)
            return if (mayor > 0L) {
                diferenciaMs * 100.0 / mayor
            } else {
                0.0
            }
        }
}

class TareasViewModel(
    private val repository: TareasRepository,
    private val historialRepository: HistorialRepository
) : ViewModel() {

    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    private val _primos =
        MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private val _carga =
        MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> =
        _simularError.asStateFlow()

    private val _limiteActivo = MutableStateFlow(false)
    val limiteActivo: StateFlow<Boolean> =
        _limiteActivo.asStateFlow()

    private val _reintentoAutomatico = MutableStateFlow(false)
    val reintentoAutomatico: StateFlow<Boolean> =
        _reintentoAutomatico.asStateFlow()

    private val _intentoActual = MutableStateFlow(0)
    val intentoActual: StateFlow<Int> =
        _intentoActual.asStateFlow()

    private val _mensajeReintento = MutableStateFlow("")
    val mensajeReintento: StateFlow<String> =
        _mensajeReintento.asStateFlow()

    private val _comparando = MutableStateFlow(false)
    val comparando: StateFlow<Boolean> =
        _comparando.asStateFlow()

    private val _comparacion =
        MutableStateFlow<ComparacionTiempos?>(null)
    val comparacion: StateFlow<ComparacionTiempos?> =
        _comparacion.asStateFlow()

    private val _mensajeComparacion = MutableStateFlow(
        "Ejecuta una comparación para visualizar los tiempos."
    )
    val mensajeComparacion: StateFlow<String> =
        _mensajeComparacion.asStateFlow()

    private var tareaJob: Job? = null
    private var primosJob: Job? = null
    private var cargaJob: Job? = null
    private var ultimoModoParalelo = false

    private fun ocupado(): Boolean =
        _carga.value is CargaUi.Cargando ||
                _comparando.value ||
                cargaJob?.isActive == true

    fun cambiarLimiteActivo(activo: Boolean) {
        if (!ocupado()) _limiteActivo.value = activo
    }

    fun cambiarSimularError(activar: Boolean) {
        if (!ocupado()) _simularError.value = activar
    }

    fun cambiarReintentoAutomatico(activo: Boolean) {
        if (!ocupado()) _reintentoAutomatico.value = activo
    }

    // FUNCIONES ORIGINALES

    fun tareaLarga() {
        if (_corriendo.value) return

        _corriendo.value = true
        _progreso.value = 0f
        _mensaje.value = "Trabajando..."

        tareaJob = viewModelScope.launch {
            Log.d("HILO", "Inicio: ${Thread.currentThread().name}")

            try {
                withContext(Dispatchers.Default) {
                    for (i in 1..100) {
                        delay(40)
                        _progreso.value = i / 100f
                    }
                }

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
            val inicio = SystemClock.elapsedRealtime()

            try {
                val cantidad = repository.contarPrimos(hasta)
                val tiempo = SystemClock.elapsedRealtime() - inicio

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
        if (ocupado()) return

        ultimoModoParalelo = paralelo
        _carga.value = CargaUi.Cargando
        _intentoActual.value = 0
        _mensajeReintento.value = ""

        val usarLimite = _limiteActivo.value
        val provocarError = _simularError.value
        val reintentar = _reintentoAutomatico.value

        val tipo = if (paralelo) {
            "Carga paralela"
        } else {
            "Carga secuencial"
        }

        cargaJob = viewModelScope.launch {
            val inicio = SystemClock.elapsedRealtime()
            val maxIntentos = if (reintentar) 3 else 1

            for (intento in 1..maxIntentos) {
                _intentoActual.value = intento
                _mensajeReintento.value =
                    "Ejecutando intento $intento de $maxIntentos"

                try {
                    val resultado = cargarConLimite(
                        paralelo = paralelo,
                        provocarError = provocarError,
                        usarLimite = usarLimite
                    )

                    val tiempo =
                        SystemClock.elapsedRealtime() - inicio

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

                    registrarHistorial(
                        tipo = tipo,
                        estado = "Exitosa",
                        descripcion =
                            "Perfil: ${resultado.first}. " +
                                    "Pedidos: ${resultado.second.size}. " +
                                    "Intentos: $intento.",
                        duracionMs = tiempo
                    )
                    return@launch

                } catch (e: TimeoutCancellationException) {
                    val tiempo =
                        SystemClock.elapsedRealtime() - inicio

                    val error =
                        "Tiempo máximo superado. " +
                                "La carga tardó más de 2 segundos."

                    _mensajeReintento.value =
                        "Límite de tiempo alcanzado"

                    _carga.value = CargaUi.Error(error)

                    registrarHistorial(
                        tipo = tipo,
                        estado = "Fallida",
                        descripcion = error,
                        duracionMs = tiempo
                    )
                    return@launch

                } catch (e: CancellationException) {
                    throw e

                } catch (e: Exception) {
                    if (intento == maxIntentos) {
                        val tiempo =
                            SystemClock.elapsedRealtime() - inicio

                        val error =
                            "No se pudo completar la carga después " +
                                    "de $maxIntentos intento(s): " +
                                    (e.message ?: "Error desconocido")

                        _mensajeReintento.value =
                            "Se agotaron los $maxIntentos intentos"

                        _carga.value = CargaUi.Error(error)

                        registrarHistorial(
                            tipo = tipo,
                            estado = "Fallida",
                            descripcion = error,
                            duracionMs = tiempo
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
        }
    }

    // COMPARACIÓN DE TIEMPOS

    fun compararTiempos() {
        if (ocupado()) return

        _comparando.value = true
        _comparacion.value = null
        _mensajeComparacion.value = "Preparando comparación..."
        _mensajeReintento.value = ""

        val usarLimite = _limiteActivo.value
        val provocarError = _simularError.value
        val reintentar = _reintentoAutomatico.value

        cargaJob = viewModelScope.launch {
            val inicio = SystemClock.elapsedRealtime()

            try {
                _mensajeComparacion.value =
                    "Midiendo carga secuencial..."

                val tiempoSecuencial = medirCarga(
                    paralelo = false,
                    provocarError = provocarError,
                    usarLimite = usarLimite,
                    reintentar = reintentar
                )

                _mensajeComparacion.value =
                    "Midiendo carga paralela..."

                val tiempoParalelo = medirCarga(
                    paralelo = true,
                    provocarError = provocarError,
                    usarLimite = usarLimite,
                    reintentar = reintentar
                )

                val resultado = ComparacionTiempos(
                    secuencialMs = tiempoSecuencial,
                    paraleloMs = tiempoParalelo
                )

                _comparacion.value = resultado
                _mensajeComparacion.value =
                    "Comparación completada correctamente."

                val porcentaje = String.format(
                    Locale.US,
                    "%.1f",
                    resultado.ahorroPorcentaje
                )

                registrarHistorial(
                    tipo = "Comparación de tiempos",
                    estado = "Exitosa",
                    descripcion =
                        "Secuencial: ${resultado.secuencialMs} ms. " +
                                "Paralela: ${resultado.paraleloMs} ms. " +
                                "Más rápida: ${resultado.masRapido}. " +
                                "Ahorro: $porcentaje%.",
                    duracionMs =
                        SystemClock.elapsedRealtime() - inicio
                )

            } catch (e: TimeoutCancellationException) {
                _comparacion.value = null

                val error =
                    "Comparación interrumpida: se superó " +
                            "el límite de 2 segundos."

                _mensajeComparacion.value = error

                registrarHistorial(
                    tipo = "Comparación de tiempos",
                    estado = "Fallida",
                    descripcion = error,
                    duracionMs =
                        SystemClock.elapsedRealtime() - inicio
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _comparacion.value = null

                val error = "No se pudo comparar: " +
                        (e.message ?: "Error desconocido")

                _mensajeComparacion.value = error

                registrarHistorial(
                    tipo = "Comparación de tiempos",
                    estado = "Fallida",
                    descripcion = error,
                    duracionMs =
                        SystemClock.elapsedRealtime() - inicio
                )

            } finally {
                _comparando.value = false
            }
        }
    }

    private suspend fun registrarHistorial(
        tipo: String,
        estado: String,
        descripcion: String,
        duracionMs: Long?
    ) {
        try {
            historialRepository.registrar(
                tipo = tipo,
                estado = estado,
                descripcion = descripcion,
                duracionMs = duracionMs
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(
                "CentroTareasHistorial",
                "No se pudo guardar el registro",
                e
            )
        }
    }

    private suspend fun medirCarga(
        paralelo: Boolean,
        provocarError: Boolean,
        usarLimite: Boolean,
        reintentar: Boolean
    ): Long {
        val inicio = SystemClock.elapsedRealtime()
        val maxIntentos = if (reintentar) 3 else 1

        for (intento in 1..maxIntentos) {
            try {
                cargarConLimite(
                    paralelo = paralelo,
                    provocarError = provocarError,
                    usarLimite = usarLimite
                )

                return SystemClock.elapsedRealtime() - inicio

            } catch (e: TimeoutCancellationException) {
                throw e
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                if (intento == maxIntentos) throw e

                _mensajeComparacion.value =
                    "Error en intento $intento de $maxIntentos. " +
                            "Reintentando..."

                delay(intento * 1000L)
            }
        }

        error("No se completó la operación")
    }

    private suspend fun cargarConLimite(
        paralelo: Boolean,
        provocarError: Boolean,
        usarLimite: Boolean
    ): Pair<String, List<String>> {
        return if (usarLimite) {
            withTimeout(2000L) {
                obtenerDatos(paralelo, provocarError)
            }
        } else {
            obtenerDatos(paralelo, provocarError)
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
