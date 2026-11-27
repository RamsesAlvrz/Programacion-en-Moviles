
package com.alvarez.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.avisoscampus.data.AvisosRepository
import com.alvarez.avisoscampus.model.Aviso
import com.alvarez.avisoscampus.model.Origen
import com.alvarez.avisoscampus.model.PermisoUi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AvisosViewModel(
    private val repo: AvisosRepository
) : ViewModel() {

    val avisos: StateFlow<List<Aviso>> = repo.avisos

    private val _eventos = MutableSharedFlow<Aviso>()

    val eventos: SharedFlow<Aviso> =
        _eventos.asSharedFlow()

    private val _permiso =
        MutableStateFlow<PermisoUi>(PermisoUi.SinPedir)

    val permiso: StateFlow<PermisoUi> =
        _permiso.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)

    val mensaje: StateFlow<String?> =
        _mensaje.asStateFlow()

    private val _faltan = MutableStateFlow<Int?>(null)

    val faltan: StateFlow<Int?> =
        _faltan.asStateFlow()

    private var recordatorioJob: Job? = null

    private val _escuchandoAvion = MutableStateFlow(false)

    val escuchandoAvion: StateFlow<Boolean> =
        _escuchandoAvion.asStateFlow()

    fun permisoRespondido(concedido: Boolean) {

        _permiso.value = if (concedido) {
            PermisoUi.Concedido
        } else {
            PermisoUi.Denegado
        }
    }

    fun avisarAhora() {

        viewModelScope.launch {

            val aviso = repo.registrar(
                titulo = "Tarea lista",
                texto = "Tu entrega fue registrada",
                origen = Origen.BOTON
            )

            _eventos.emit(aviso)
        }
    }

    fun recordarEn(segundos: Int = 10) {

        if (recordatorioJob?.isActive == true) {
            return
        }

        recordatorioJob = viewModelScope.launch {

            try {

                for (restantes in segundos downTo 1) {

                    _faltan.value = restantes

                    delay(1000)
                }

                val aviso = repo.registrar(
                    titulo = "Recordatorio",
                    texto = "Revisa tu laboratorio pendiente",
                    origen = Origen.RECORDATORIO
                )

                _eventos.emit(aviso)

            } finally {

                _faltan.value = null
            }
        }
    }

    fun cancelarRecordatorio() {

        recordatorioJob?.cancel()
    }

    fun cambiarEscucha(activado: Boolean) {

        _escuchandoAvion.value = activado
    }

    fun alCambiarModoAvion(activado: Boolean) {

        if (!_escuchandoAvion.value) {
            return
        }

        viewModelScope.launch {

            val aviso = repo.registrar(
                titulo = "Modo avión",
                texto = if (activado) {
                    "Activado"
                } else {
                    "Desactivado"
                },
                origen = Origen.MODO_AVION
            )

            _eventos.emit(aviso)
        }
    }

    // TODO Paso 8: manejar permiso denegado
}
