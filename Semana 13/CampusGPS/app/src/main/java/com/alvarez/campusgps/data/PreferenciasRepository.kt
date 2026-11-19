package com.alvarez.campusgps.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Instancia unica de DataStore para la aplicacion.
private val Context.campusDataStore:
        DataStore<Preferences> by preferencesDataStore(
    name = "campus_preferencias"
)

class PreferenciasRepository(
    context: Context
) {

    private val dataStore =
        context.applicationContext.campusDataStore

    companion object {
        private val ULTIMO_LUGAR_ID =
            intPreferencesKey("ultimo_lugar_id")
    }

    // Leer el ultimo lugar seleccionado.
    val ultimoLugarId: Flow<Int?> =
        dataStore.data.map { preferencias ->
            preferencias[ULTIMO_LUGAR_ID]
        }

    // Guardar la seleccion.
    suspend fun guardarUltimoLugar(id: Int) {
        dataStore.edit { preferencias ->
            preferencias[ULTIMO_LUGAR_ID] = id
        }
    }

    // Borrar la seleccion guardada.
    suspend fun limpiarUltimoLugar() {
        dataStore.edit { preferencias ->
            preferencias.remove(ULTIMO_LUGAR_ID)
        }
    }
}
