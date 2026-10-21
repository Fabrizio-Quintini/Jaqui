package com.example.jaqui.viewmodel

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaqui.data.local.JaquiDataStore
import com.example.jaqui.model.LoginErrores
import com.example.jaqui.model.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val almacenamiento = JaquiDataStore(application)

    private val _estado = MutableStateFlow(LoginUiState())
    val estado: StateFlow<LoginUiState> = _estado.asStateFlow()

    fun onCorreoChange(correo: String) {
        _estado.update {
            it.copy(
                correo = correo,
                errores = it.errores.copy(correo = null, general = null)
            )
        }
    }

    fun onPasswordChange(password: String) {
        _estado.update {
            it.copy(
                password = password,
                errores = it.errores.copy(password = null, general = null)
            )
        }
    }

    fun iniciarSesion(onExitoso: () -> Unit) {
        if (!validarFormulario()) return

        val correo = _estado.value.correo.trim().lowercase()

        viewModelScope.launch {
            if (almacenamiento.verificarCredenciales(
                    correo = correo,
                    password = _estado.value.password
                )
            ) {
                onExitoso()
            } else {
                _estado.update {
                    it.copy(
                        errores = it.errores.copy(
                            general = "El correo o la contraseña no coinciden con ninguna cuenta"
                        )
                    )
                }
            }
        }
    }

    private fun validarFormulario(): Boolean {
        var valido = true
        var errores = LoginErrores()
        val estadoActual = _estado.value

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
        }

        _estado.update { it.copy(errores = errores) }
        return valido
    }
}
