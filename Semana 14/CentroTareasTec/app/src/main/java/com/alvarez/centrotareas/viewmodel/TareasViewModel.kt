
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
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TareasViewModel(
    private val repository: TareasRepository
) : ViewModel() {

    private val _mensaje = MutableStateFlow("Listo")
    val mensaje: StateFlow<String> = _mensaje.asStateFlow()

    private val _progreso = MutableStateFlow(0f)
    val progreso: StateFlow<Float> = _progreso.asStateFlow()

    private val _corriendo = MutableStateFlow(false)
    val corriendo: StateFlow<Boolean> = _corriendo.asStateFlow()

    private val _primos = MutableStateFlow<PrimosUi>(
        PrimosUi.Inicial
    )
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private val _carga = MutableStateFlow<CargaUi>(
        CargaUi.Inicial
    )
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    private val _simularError = MutableStateFlow(false)
    val simularError: StateFlow<Boolean> =
        _simularError.asStateFlow()

    private var tareaJob: Job? = null
    private var primosJob: Job? = null

    private var ultimoModoParalelo = false

    // TAREA CON PROGRESO - COMMIT ANTERIOR
    fun tareaLarga() {
        if (_corriendo.value) return

        _corriendo.value = true
        _progreso.value = 0f
        _mensaje.value = "Trabajando..."

        tareaJob = viewModelScope.launch {
            Log.d(
                "HILO",
                "Inicio: ${Thread.currentThread().name}"
            )

            try {
                withContext(Dispatchers.Default) {
                    Log.d(
                        "HILO",
                        "Trabajo: ${Thread.currentThread().name}"
                    )

                    for (i in 1..100) {
                        delay(40)
                        _progreso.value = i / 100f
                    }
                }

                Log.d(
                    "HILO",
                    "Final: ${Thread.currentThread().name}"
                )

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

    // NUMEROS PRIMOS - COMMIT ANTERIOR
    fun contarPrimos(hasta: Int) {
        if (hasta < 2 || _primos.value is PrimosUi.Calculando) {
            return
        }

        _primos.value = PrimosUi.Calculando

        primosJob = viewModelScope.launch {
            val inicio = System.currentTimeMillis()

            try {
                val cantidad = repository.contarPrimos(hasta)
                val tiempo =
                    System.currentTimeMillis() - inicio

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

    // COMMIT 8: ACTIVAR O DESACTIVAR ERROR
    fun cambiarSimularError(activar: Boolean) {
        _simularError.value = activar
    }

    // CARGA SECUENCIAL
    fun cargarSecuencial() {
        ejecutarCarga(paralelo = false)
    }

    // CARGA PARALELA
    fun cargarParalelo() {
        ejecutarCarga(paralelo = true)
    }

    // COMMIT 8: REINTENTAR LA ULTIMA CARGA
    fun reintentarCarga() {
        ejecutarCarga(paralelo = ultimoModoParalelo)
    }

    // COMMIT 8: MANEJO DE ERRORES
    private fun ejecutarCarga(paralelo: Boolean) {
        if (_carga.value is CargaUi.Cargando) return

        ultimoModoParalelo = paralelo
        _carga.value = CargaUi.Cargando

        viewModelScope.launch {
            val inicio = System.currentTimeMillis()

            try {
                val perfil: String
                val pedidos: List<String>

                if (paralelo) {
                    val resultado = coroutineScope {
                        val perfilDeferred = async {
                            repository.cargarPerfil()
                        }

                        val pedidosDeferred = async {
                            repository.cargarPedidos(
                                _simularError.value
                            )
                        }

                        Pair(
                            perfilDeferred.await(),
                            pedidosDeferred.await()
                        )
                    }

                    perfil = resultado.first
                    pedidos = resultado.second

                } else {
                    perfil = repository.cargarPerfil()

                    pedidos = repository.cargarPedidos(
                        _simularError.value
                    )
                }

                val tiempo =
                    System.currentTimeMillis() - inicio

                _carga.value = CargaUi.Exito(
                    modo = if (paralelo) {
                        "A la vez"
                    } else {
                        "Una por una"
                    },
                    perfil = perfil,
                    pedidos = pedidos.size,
                    milisegundos = tiempo
                )

            } catch (e: CancellationException) {
                throw e

            } catch (e: Exception) {
                _carga.value = CargaUi.Error(
                    mensaje = "No se pudo cargar: " +
                            (e.message ?: "Error desconocido")
                )
            }
        }
    }
}
