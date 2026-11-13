package com.tecsup.smartcatalog.ui

sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(
        val data: T,
        val offline: Boolean = false
    ) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}
