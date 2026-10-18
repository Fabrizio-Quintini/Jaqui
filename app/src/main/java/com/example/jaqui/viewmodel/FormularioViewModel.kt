package com.example.jaqui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.example.jaqui.model.FormularioErrores
import com.example.jaqui.model.FormularioUiState




class FormularioViewModel : ViewModel() {

    private val _estado = MutableStateFlow(FormularioUiState())
    val estado: StateFlow<FormularioUiState> = _estado.asStateFlow()

    fun onNombreChange(nombre: String) {
        _estado.update { it.copy(nombre = nombre) }
    }

    fun onCorreoChange(correo: String) {
        _estado.update { it.copy(correo = correo) }
    }

    fun onAceptaTerminosChange(acepta: Boolean) {
        _estado.update { it.copy(aceptaTerminos = acepta) }
    }

    fun onLoginClick() {}

    fun onRegistroClick() {}

    fun validarFormulario(): Boolean {
        var valido = true
        var errores = FormularioErrores()

        if (_estado.value.nombre.isBlank()) {
            errores = errores.copy(nombre = "El nombre es obligatorio")
            valido = false
        }

        if (_estado.value.correo.isBlank()) {
            errores = errores.copy(correo = "El correo es obligatorio")
            valido = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(_estado.value.correo).matches()) {
            errores = errores.copy(correo = "Correo inválido")
            valido = false
        }

        if (!_estado.value.aceptaTerminos) {
            errores = errores.copy(aceptaTerminos = "Debes aceptar los términos y condiciones")
            valido = false
        }

        _estado.update { it.copy(errores = errores) }
        return valido
    }

}