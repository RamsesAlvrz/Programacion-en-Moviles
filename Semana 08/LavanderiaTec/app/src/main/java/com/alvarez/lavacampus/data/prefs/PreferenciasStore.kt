package com.alvarez.lavacampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Instancia de DataStore para guardar las preferencias
private val Context.dataStore by preferencesDataStore(
    name = "lavacampus_preferencias"
)

// Clase encargada de gestionar la sesión del usuario
class PreferenciasStore(
    private val context: Context
) {

    companion object {
        private val USUARIO_ID = intPreferencesKey("usuario_id")
        private val ROL_USUARIO = stringPreferencesKey("rol_usuario")
    }

    // Observar el identificador del usuario que inició sesión
    val usuarioId: Flow<Int?> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferencias ->
            preferencias[USUARIO_ID]
        }

    // Observar el rol del usuario
    val rolUsuario: Flow<String?> = context.dataStore.data
        .map { preferencias ->
            preferencias[ROL_USUARIO]
        }

    // Guardar la sesión del estudiante o administrador
    suspend fun guardarSesion(
        id: Int,
        rol: String
    ) {
        context.dataStore.edit { preferencias ->
            preferencias[USUARIO_ID] = id
            preferencias[ROL_USUARIO] = rol
        }
    }

    // Eliminar los datos de la sesión al cerrar sesión
    suspend fun cerrarSesion() {
        context.dataStore.edit { preferencias ->
            preferencias.remove(USUARIO_ID)
            preferencias.remove(ROL_USUARIO)
        }
    }
}
