
package com.example.jaqui.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(
    name = "jaqui_preferencias"
)

class JaquiDataStore(private val context: Context) {

    companion object {
        private val INDICE_TARJETA =
            intPreferencesKey("indice_tarjeta")
    }

    val indiceTarjeta: Flow<Int> =
        context.dataStore.data.map { preferencias ->
            preferencias[INDICE_TARJETA] ?: 0
        }

    suspend fun guardarIndiceTarjeta(indice: Int) {
        context.dataStore.edit { preferencias ->
            preferencias[INDICE_TARJETA] = indice
        }
    }
}
