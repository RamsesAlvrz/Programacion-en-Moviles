
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

    private var tareaJob: Job? = null
    private var primosJob: Job? = null

    // TAREA CON PROGRESO
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

    // CONTAR NUMEROS PRIMOS
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

    // COMMIT 7: CARGA SECUENCIAL
    fun cargarSecuencial() {
        if (_carga.value is CargaUi.Cargando) return

        _carga.value = CargaUi.Cargando

        viewModelScope.launch {
            val inicio = System.currentTimeMillis()

            val perfil = repository.cargarPerfil()
            val pedidos = repository.cargarPedidos(false)

            val tiempo = System.currentTimeMillis() - inicio

            _carga.value = CargaUi.Exito(
                modo = "Una por una",
                perfil = perfil,
                pedidos = pedidos.size,
                milisegundos = tiempo
            )
        }
    }

    // COMMIT 7: CARGA PARALELA CON ASYNC
    fun cargarParalelo() {
        if (_carga.value is CargaUi.Cargando) return

        _carga.value = CargaUi.Cargando

        viewModelScope.launch {
            val inicio = System.currentTimeMillis()

            val resultado = coroutineScope {
                val perfilDeferred = async {
                    repository.cargarPerfil()
                }

                val pedidosDeferred = async {
                    repository.cargarPedidos(false)
                }

                Pair(
                    perfilDeferred.await(),
                    pedidosDeferred.await()
                )
            }

            val tiempo = System.currentTimeMillis() - inicio

            _carga.value = CargaUi.Exito(
                modo = "A la vez",
                perfil = resultado.first,
                pedidos = resultado.second.size,
                milisegundos = tiempo
            )
        }
    }

    // TODO: manejo de errores - siguiente commit
}
