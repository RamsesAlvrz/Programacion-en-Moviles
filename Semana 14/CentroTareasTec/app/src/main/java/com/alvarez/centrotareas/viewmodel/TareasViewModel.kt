package com.alvarez.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.centrotareas.data.TareasRepository
import com.alvarez.centrotareas.model.CargaUi
import com.alvarez.centrotareas.model.PrimosUi
import kotlinx.coroutines.Dispatchers
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

    private val _primos = MutableStateFlow<PrimosUi>(
        PrimosUi.Inicial
    )
    val primos: StateFlow<PrimosUi> = _primos.asStateFlow()

    private val _carga = MutableStateFlow<CargaUi>(
        CargaUi.Inicial
    )
    val carga: StateFlow<CargaUi> = _carga.asStateFlow()

    // COMMIT 3: tarea larga con corrutina
    fun tareaLarga() {
        viewModelScope.launch {
            _mensaje.value = "Trabajando..."

            Log.d(
                "HILO",
                "Inicio: ${Thread.currentThread().name}"
            )

            withContext(Dispatchers.Default) {
                Log.d(
                    "HILO",
                    "Trabajo: ${Thread.currentThread().name}"
                )

                delay(4000)
            }

            Log.d(
                "HILO",
                "Final: ${Thread.currentThread().name}"
            )

            _mensaje.value = "Listo"
        }
    }

    // TODO: progreso y cancelacion

    // TODO: calculo de primos

    // TODO: cargas secuenciales y paralelas

    // TODO: manejo de errores
}
