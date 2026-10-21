
package com.example.jaqui.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.json.JSONObject

data class Usuario(
    val nombre: String,
    val password: String
)

private val Context.dataStore by preferencesDataStore(
    name = "jaqui_preferencias"
)

class JaquiDataStore(private val context: Context) {

    companion object {
        private val INDICE_TARJETA =
            intPreferencesKey("indice_tarjeta")
        private val USUARIOS =
            stringPreferencesKey("usuarios")
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

    suspend fun obtenerUsuarios(): Map<String, Usuario> {
        val json = context.dataStore.data.first()[USUARIOS]
        return parsearUsuarios(json)
    }

    suspend fun usuarioExiste(correo: String): Boolean {
        return obtenerUsuarios().containsKey(correo)
    }

    suspend fun guardarUsuario(
        nombre: String,
        correo: String,
        password: String
    ) {
        context.dataStore.edit { preferencias ->
            val usuarios = parsearUsuarios(preferencias[USUARIOS]).toMutableMap()
            usuarios[correo] = Usuario(nombre = nombre, password = password)
            preferencias[USUARIOS] = serializarUsuarios(usuarios)
        }
    }

    suspend fun verificarCredenciales(
        correo: String,
        password: String
    ): Boolean {
        val usuario = obtenerUsuarios()[correo]
        return usuario != null && usuario.password == password
    }

    private fun parsearUsuarios(json: String?): Map<String, Usuario> {
        if (json.isNullOrBlank()) return emptyMap()
        return try {
            val objeto = JSONObject(json)
            val usuarios = mutableMapOf<String, Usuario>()
            val correos = objeto.keys()
            while (correos.hasNext()) {
                val correo = correos.next()
                val datos = objeto.getJSONObject(correo)
                usuarios[correo] = Usuario(
                    nombre = datos.optString("nombre"),
                    password = datos.optString("password")
                )
            }
            usuarios
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private fun serializarUsuarios(usuarios: Map<String, Usuario>): String {
        val objeto = JSONObject()
        usuarios.forEach { (correo, usuario) ->
            objeto.put(
                correo,
                JSONObject().apply {
                    put("nombre", usuario.nombre)
                    put("password", usuario.password)
                }
            )
        }
        return objeto.toString()
    }
}
