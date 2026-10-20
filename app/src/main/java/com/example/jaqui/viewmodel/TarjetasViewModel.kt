package com.example.jaqui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaqui.data.local.JaquiDataStore
import com.example.jaqui.model.TarjetasUiState
import com.example.jaqui.model.VocabularioTransporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TarjetasViewModel(
application: Application
) : AndroidViewModel(application) {

    private val _estado = MutableStateFlow(TarjetasUiState())

    val estado: StateFlow<TarjetasUiState> = _estado.asStateFlow()

    val palabras = VocabularioTransporte.palabras
    private val almacenamiento = JaquiDataStore(application)

    init {
        viewModelScope.launch {
            almacenamiento.indiceTarjeta.collect { indiceGuardado ->
                _estado.update { actual ->
                    actual.copy(
                        indiceActual = indiceGuardado.coerceIn(
                            0,
                            palabras.lastIndex
                        ),
                        mostrarEspanol = false
                    )
                }
            }
        }
    }
    fun voltearTarjeta() {
        _estado.update {
            it.copy(mostrarEspanol = !it.mostrarEspanol)
        }
    }

    fun siguiente() {
        val actual = _estado.value

        if (actual.indiceActual < palabras.lastIndex) {
            val nuevoIndice = actual.indiceActual + 1

            _estado.update {
                it.copy(
                    indiceActual = nuevoIndice,
                    mostrarEspanol = false
                )
            }

            viewModelScope.launch {
                almacenamiento.guardarIndiceTarjeta(nuevoIndice)
            }
        }
    }

    fun anterior() {
        val actual = _estado.value

        if (actual.indiceActual > 0) {
            val nuevoIndice = actual.indiceActual - 1

            _estado.update {
                it.copy(
                    indiceActual = nuevoIndice,
                    mostrarEspanol = false
                )
            }

            viewModelScope.launch {
                almacenamiento.guardarIndiceTarjeta(nuevoIndice)
            }
        }
    }
}