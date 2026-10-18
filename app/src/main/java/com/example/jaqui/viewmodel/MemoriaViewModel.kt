package com.example.jaqui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jaqui.model.CartaMemoria
import com.example.jaqui.model.Idioma
import com.example.jaqui.model.MemoriaUiState
import com.example.jaqui.model.VocabularioMemoria
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MemoriaViewModel : ViewModel() {

    private val _estado = MutableStateFlow(MemoriaUiState())
    val estado: StateFlow<MemoriaUiState> = _estado.asStateFlow()

    init {
        reiniciar()
    }

    fun reiniciar() {
        val cartas = mutableListOf<CartaMemoria>()
        var id = 0
        VocabularioMemoria.pares.forEachIndexed { indice, par ->
            cartas += CartaMemoria(id = id++, parId = indice, texto = par.turco, idioma = Idioma.TURCO)
            cartas += CartaMemoria(id = id++, parId = indice, texto = par.espanol, idioma = Idioma.ESPANOL)
        }
        _estado.value = MemoriaUiState(
            cartas = cartas.shuffled(),
            totalPares = VocabularioMemoria.pares.size,
            inicioMillis = System.currentTimeMillis()
        )
    }

    fun onCartaClick(cartaId: Int) {
        val actual = _estado.value
        if (actual.bloqueado) return

        val carta = actual.cartas.firstOrNull { it.id == cartaId } ?: return
        if (carta.revelada || carta.emparejada) return

        val cartasConRevelada = actual.cartas.map {
            if (it.id == cartaId) it.copy(revelada = true) else it
        }

        val primeraId = actual.primeraId
        if (primeraId == null) {
            _estado.value = actual.copy(cartas = cartasConRevelada, primeraId = cartaId)
            return
        }

        val primera = cartasConRevelada.first { it.id == primeraId }
        if (primera.parId == carta.parId) {
            val nuevas = cartasConRevelada.map {
                if (it.id == primeraId || it.id == cartaId) {
                    it.copy(revelada = false, emparejada = true)
                } else {
                    it
                }
            }
            val aciertos = actual.aciertos + 1
            val terminado = aciertos == actual.totalPares
            val tiempoFinal = if (terminado) {
                (System.currentTimeMillis() - actual.inicioMillis) / 1000
            } else {
                actual.tiempoFinalSegundos
            }
            _estado.value = actual.copy(
                cartas = nuevas,
                primeraId = null,
                aciertos = aciertos,
                terminado = terminado,
                tiempoFinalSegundos = tiempoFinal,
                mensaje = if (terminado) {
                    "¡Ganaste! Emparejaste todas las tarjetas."
                } else {
                    "¡Correcto! ${primera.texto} = ${carta.texto}"
                },
                mensajeId = actual.mensajeId + 1
            )
        } else {
            _estado.value = actual.copy(
                cartas = cartasConRevelada,
                bloqueado = true
            )
            viewModelScope.launch {
                kotlinx.coroutines.delay(900)
                _estado.update { estado ->
                    estado.copy(
                        cartas = estado.cartas.map {
                            if (it.id == primeraId || it.id == cartaId) {
                                it.copy(revelada = false)
                            } else {
                                it
                            }
                        },
                        primeraId = null,
                        bloqueado = false
                    )
                }
            }
        }
    }

    fun onMensajeMostrado() {
        _estado.update { it.copy(mensaje = null) }
    }
}
