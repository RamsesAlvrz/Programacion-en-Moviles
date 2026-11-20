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

    // TODO: tarea larga - siguientes pasos

    // TODO: progreso y cancelacion - siguientes pasos

    // TODO: calculo de primos - siguientes pasos

    // TODO: cargas secuenciales y paralelas - siguientes pasos

    // TODO: manejo de errores - siguientes pasos
}
