
package com.tecsup.smartcatalog.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.smartcatalog.data.repository.ItemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class AdminStats(
    val totalProductos: Int = 0,
    val totalUsuarios: Int = 0,
    val totalFavoritos: Int = 0
)

data class UsuarioAdmin(
    val uid: String,
    val nombre: String,
    val correo: String,
    val rol: String
)

sealed interface AdminUiState {
    data object Loading : AdminUiState

    data class Success(
        val stats: AdminStats,
        val usuarios: List<UsuarioAdmin>
    ) : AdminUiState

    data class Error(
        val mensaje: String
    ) : AdminUiState
}

class AdminViewModel(
    private val repository: ItemRepository
) : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState =
        MutableStateFlow<AdminUiState>(AdminUiState.Loading)

    val uiState: StateFlow<AdminUiState> =
        _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    fun cargarDatos() {
        viewModelScope.launch {
            _uiState.value = AdminUiState.Loading

            try {
                val totalProductos =
                    repository.contarProductos().first()

                val totalFavoritos =
                    repository.contarFavoritos().first()

                val documentos = firestore
                    .collection("usuarios")
                    .get()
                    .await()

                val usuarios = documentos.documents
                    .map { doc ->
                        UsuarioAdmin(
                            uid = doc.id,
                            nombre = doc.getString("nombre") ?: "",
                            correo = doc.getString("correo") ?: "",
                            rol = doc.getString("rol") ?: "usuario"
                        )
                    }
                    .sortedBy { it.nombre.lowercase() }

                _uiState.value = AdminUiState.Success(
                    stats = AdminStats(
                        totalProductos = totalProductos,
                        totalUsuarios = usuarios.size,
                        totalFavoritos = totalFavoritos
                    ),
                    usuarios = usuarios
                )

            } catch (e: Exception) {
                _uiState.value = AdminUiState.Error(
                    mensaje = e.message
                        ?: "No se pudieron cargar los datos"
                )
            }
        }
    }
}
