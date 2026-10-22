package com.alvarez.lavacampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvarez.lavacampus.data.local.entity.Usuario
import com.alvarez.lavacampus.data.prefs.PreferenciasStore
import com.alvarez.lavacampus.data.repository.LavaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// Gestiona la autenticación y la sesión del usuario
class SesionViewModel(
    private val repository: LavaRepository,
    private val preferenciasStore: PreferenciasStore
) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<Usuario?>(null)
    val usuarioActual: StateFlow<Usuario?> = _usuarioActual.asStateFlow()

    private val _cargando = MutableStateFlow(false)
    val cargando: StateFlow<Boolean> = _cargando.asStateFlow()

    private val _mensajeError = MutableStateFlow<String?>(null)
    val mensajeError: StateFlow<String?> = _mensajeError.asStateFlow()

    // Recuperar la sesión almacenada en DataStore
    init {
        viewModelScope.launch {
            val usuarioId = preferenciasStore.usuarioId.first()

            if (usuarioId != null) {
                _usuarioActual.value = repository.buscarUsuarioPorId(usuarioId)
            }
        }
    }

    // Iniciar sesión con correo, contraseña y rol seleccionado
    fun iniciarSesion(
        correo: String,
        clave: String,
        rol: String,
        onResultado: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            _cargando.value = true
            _mensajeError.value = null

            try {
                val usuario = repository.buscarUsuarioPorCorreo(
                    correo.trim()
                )

                if (
                    usuario != null &&
                    usuario.clave == clave &&
                    usuario.rol == rol
                ) {
                    preferenciasStore.guardarSesion(
                        usuario.id,
                        usuario.rol
                    )

                    _usuarioActual.value = usuario
                    onResultado(true)
                } else {
                    _mensajeError.value =
                        "Correo, contraseña o rol incorrectos"

                    onResultado(false)
                }

            } catch (e: Exception) {
                _mensajeError.value =
                    "No se pudo iniciar sesión: ${e.message}"

                onResultado(false)

            } finally {
                _cargando.value = false
            }
        }
    }

    // Cerrar la sesión actual
    fun cerrarSesion(onFinalizado: () -> Unit) {
        viewModelScope.launch {
            preferenciasStore.cerrarSesion()
            _usuarioActual.value = null
            _mensajeError.value = null
            onFinalizado()
        }
    }

    // Limpiar mensajes de error
    fun limpiarError() {
        _mensajeError.value = null
    }
}
