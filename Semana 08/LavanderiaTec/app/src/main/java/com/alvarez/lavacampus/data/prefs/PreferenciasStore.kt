package com.alvarez.lavacampus.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Instancia única de DataStore para las preferencias de LavaCampus.
private val Context.dataStore by preferencesDataStore(
    name = "lavacampus_preferencias"
)

class PreferenciasStore(
    private val context: Context
) {

    companion object {

        // Datos de la sesión.
        private val USUARIO_ID = intPreferencesKey("usuario_id")
        private val ROL_USUARIO = stringPreferencesKey("rol_usuario")

        // COMMIT 14: Configuraciones de la aplicación.
        private val MODO_OSCURO = booleanPreferencesKey("modo_oscuro")
        private val NOTIFICACIONES = booleanPreferencesKey("notificaciones")
        private val RECIBIR_PEDIDOS = booleanPreferencesKey("recibir_pedidos")
    }

    // =====================================
    // LECTURA SEGURA DE PREFERENCIAS
    // =====================================

    private val preferencias = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

    // =====================================
    // SESIÓN DEL USUARIO
    // =====================================

    // Identificador del usuario que inició sesión.
    val usuarioId: Flow<Int?> = preferencias.map {
        it[USUARIO_ID]
    }

    // Rol del usuario.
    val rolUsuario: Flow<String?> = preferencias.map {
        it[ROL_USUARIO]
    }

    // Guardar la sesión del estudiante o administrador.
    suspend fun guardarSesion(
        id: Int,
        rol: String
    ) {
        context.dataStore.edit { preferencias ->
            preferencias[USUARIO_ID] = id
            preferencias[ROL_USUARIO] = rol
        }
    }

    // Cerrar sesión sin eliminar las configuraciones.
    suspend fun cerrarSesion() {
        context.dataStore.edit { preferencias ->
            preferencias.remove(USUARIO_ID)
            preferencias.remove(ROL_USUARIO)
        }
    }

    // =====================================
    // COMMIT 14: MODO OSCURO
    // =====================================

    // Por defecto, el modo oscuro está desactivado.
    val modoOscuro: Flow<Boolean> = preferencias.map {
        it[MODO_OSCURO] ?: false
    }

    suspend fun guardarModoOscuro(activado: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[MODO_OSCURO] = activado
        }
    }

    // =====================================
    // COMMIT 14: NOTIFICACIONES
    // =====================================

    // Por defecto, las notificaciones están activadas.
    val notificaciones: Flow<Boolean> = preferencias.map {
        it[NOTIFICACIONES] ?: true
    }

    suspend fun guardarNotificaciones(activadas: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[NOTIFICACIONES] = activadas
        }
    }

    // =====================================
    // COMMIT 14: RECEPCIÓN DE PEDIDOS
    // =====================================

    // Por defecto, la lavandería recibe pedidos.
    val recibirPedidos: Flow<Boolean> = preferencias.map {
        it[RECIBIR_PEDIDOS] ?: true
    }

    suspend fun guardarRecibirPedidos(activado: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[RECIBIR_PEDIDOS] = activado
        }
    }
}