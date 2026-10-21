package com.example.jaqui.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaqui.data.local.JaquiDataStore
import com.example.jaqui.model.FormularioErrores
import com.example.jaqui.model.FormularioUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FormularioViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val almacenamiento = JaquiDataStore(application)

    private val _estado = MutableStateFlow(FormularioUiState())
    val estado: StateFlow<FormularioUiState> = _estado.asStateFlow()

    fun onNombreChange(nombre: String) {
        _estado.update {
            it.copy(
                nombre = nombre,
                errores = it.errores.copy(nombre = null)
            )
        }
    }

    fun onCorreoChange(correo: String) {
        _estado.update {
            it.copy(
                correo = correo,
                errores = it.errores.copy(correo = null)
            )
        }
    }

    fun onPasswordChange(password: String) {
        _estado.update {
            it.copy(
                password = password,
                errores = it.errores.copy(password = null)
            )
        }
    }

    fun onConfirmarPasswordChange(confirmarPassword: String) {
        _estado.update {
            it.copy(
                confirmarPassword = confirmarPassword,
                errores = it.errores.copy(confirmarPassword = null)
            )
        }
    }

    fun onAceptaTerminosChange(acepta: Boolean) {
        _estado.update {
            it.copy(
                aceptaTerminos = acepta,
                errores = it.errores.copy(aceptaTerminos = null)
            )
        }
    }

    fun registrar(onExitoso: () -> Unit) {
        if (!validarFormulario()) return

        val estadoActual = _estado.value
        val correo = estadoActual.correo.trim().lowercase()

        viewModelScope.launch {
            if (almacenamiento.usuarioExiste(correo)) {
                _estado.update {
                    it.copy(
                        errores = it.errores.copy(
                            correo = "El correo ya está registrado. Inicia sesión."
                        )
                    )
                }
                return@launch
            }

            almacenamiento.guardarUsuario(
                nombre = estadoActual.nombre.trim(),
                correo = correo,
                password = estadoActual.password
            )
            onExitoso()
        }
    }

    private fun validarFormulario(): Boolean {
        var valido = true
        var errores = FormularioErrores()
        val estadoActual = _estado.value

        if (estadoActual.nombre.isBlank()) {
            errores = errores.copy(nombre = "El nombre es obligatorio")
            valido = false
        }

        if (estadoActual.correo.isBlank()) {
            errores = errores.copy(correo = "El correo es obligatorio")
            valido = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(estadoActual.correo).matches()) {
            errores = errores.copy(correo = "Correo inválido")
            valido = false
        }

        if (estadoActual.password.isBlank()) {
            errores = errores.copy(password = "La contraseña es obligatoria")
            valido = false
        } else if (estadoActual.password.length < MINIMO_PASSWORD) {
            errores = errores.copy(
                password = "Debe tener al menos $MINIMO_PASSWORD caracteres"
            )
            valido = false
        }

        if (estadoActual.confirmarPassword.isBlank()) {
            errores = errores.copy(
                confirmarPassword = "Confirma tu contraseña"
            )
            valido = false
        } else if (estadoActual.confirmarPassword != estadoActual.password) {
            errores = errores.copy(
                confirmarPassword = "Las contraseñas no coinciden"
            )
            valido = false
        }

        if (!estadoActual.aceptaTerminos) {
            errores = errores.copy(
                aceptaTerminos = "Debes aceptar los términos y condiciones"
            )
            valido = false
        }

        _estado.update { it.copy(errores = errores) }
        return valido
    }

    companion object {
        private const val MINIMO_PASSWORD = 8
    }
}
