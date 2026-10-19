package com.example.jaqui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.jaqui.model.TarjetasUiState
import com.example.jaqui.model.VocabularioTransporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TarjetasViewModel : ViewModel() {

    private val _estado = MutableStateFlow(TarjetasUiState())

    val estado: StateFlow<TarjetasUiState> = _estado.asStateFlow()

    val palabras = VocabularioTransporte.palabras

    fun voltearTarjeta() {
        _estado.update {
            it.copy(mostrarEspanol = !it.mostrarEspanol)
        }
    }

    fun siguiente() {
        _estado.update { actual ->
            if (actual.indiceActual < palabras.lastIndex) {
                actual.copy(
                    indiceActual = actual.indiceActual + 1,
                    mostrarEspanol = false
                )
            } else {
                actual
            }
        }
    }

    fun anterior() {
        _estado.update { actual ->
            if (actual.indiceActual > 0) {
                actual.copy(
                    indiceActual = actual.indiceActual - 1,
                    mostrarEspanol = false
                )
            } else {
                actual
            }
        }
    }
}