package com.alvarez.centrotareas.viewmodel

import androidx.lifecycle.ViewModel
import com.alvarez.centrotareas.data.TareasRepository
import com.alvarez.centrotareas.model.CargaUi
import com.alvarez.centrotareas.model.PrimosUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

    // COMMIT 2: bloqueo intencional del hilo principal.
    fun tareaLargaMal() {
        _mensaje.value = "Trabajando..."

        Thread.sleep(4000)

        _mensaje.value = "Listo"
    }

    // TODO: progreso y cancelacion

    // TODO: calculo de primos

    // TODO: cargas secuenciales y paralelas

    // TODO: manejo de errores
}
