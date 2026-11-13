package com.tecsup.smartcatalog.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.smartcatalog.data.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val rol: String) : LoginUiState
    data class Error(val mensaje: String) : LoginUiState
}

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<LoginUiState>(LoginUiState.Idle)

    val uiState: StateFlow<LoginUiState> =
        _uiState.asStateFlow()

    fun iniciarSesion(correo: String, password: String) {

        if (correo.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState.Error(
                "Completa el correo y la contraseña"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val resultado = repository.iniciarSesion(
                correo.trim(),
                password
            )

            if (resultado.isSuccess) {
                try {
                    val rol = repository.obtenerRol()

                    if (rol == "admin" || rol == "usuario") {
                        _uiState.value = LoginUiState.Success(rol)
                    } else {
                        repository.cerrarSesion()
                        _uiState.value = LoginUiState.Error(
                            "Tu cuenta no tiene un rol válido"
                        )
                    }
                } catch (e: Exception) {
                    repository.cerrarSesion()
                    _uiState.value = LoginUiState.Error(
                        e.message ?: "No se pudo verificar el rol"
                    )
                }
            } else {
                _uiState.value = LoginUiState.Error(
                    resultado.exceptionOrNull()?.localizedMessage
                        ?: "Credenciales incorrectas"
                )
            }
        }
    }

    fun registrar(
        nombre: String,
        correo: String,
        password: String
    ) {

        if (nombre.isBlank() ||
            correo.isBlank() ||
            password.isBlank()
        ) {
            _uiState.value = LoginUiState.Error(
                "Completa todos los campos"
            )
            return
        }

        if (password.length < 6) {
            _uiState.value = LoginUiState.Error(
                "La contraseña debe tener al menos 6 caracteres"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val resultado = repository.registrar(
                nombre.trim(),
                correo.trim(),
                password
            )

            if (resultado.isSuccess) {
                _uiState.value = LoginUiState.Success("usuario")
            } else {
                _uiState.value = LoginUiState.Error(
                    resultado.exceptionOrNull()?.localizedMessage
                        ?: "No se pudo registrar el usuario"
                )
            }
        }
    }

    fun reiniciarEstado() {
        _uiState.value = LoginUiState.Idle
    }
}
