
package com.example.jaqui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.jaqui.model.Palabra
import com.example.jaqui.model.QuizUiState
import com.example.jaqui.model.VocabularioMemoria
import com.example.jaqui.model.VocabularioTransporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class QuizViewModel : ViewModel() {

    private val palabras = (
            VocabularioTransporte.palabras +
                    VocabularioMemoria.pares.map { par ->
                        Palabra(
                            imagen = imagenPara(par.turco),
                            turco = par.turco,
                            espanol = par.espanol
                        )
                    }
            ).distinctBy { it.espanol.lowercase() }

    private val totalPreguntas = palabras.size

    private val _estado = MutableStateFlow(QuizUiState())
    val estado: StateFlow<QuizUiState> = _estado.asStateFlow()

    init {
        prepararPregunta()
    }

    private fun imagenPara(turco: String): String {
        return when (turco) {
            "Araba" -> "🚗"
            "Kitap" -> "📚"
            "Elma" -> "🍎"
            "Su" -> "💧"
            "Kedi" -> "🐱"
            "Köpek" -> "🐶"
            "Anne" -> "👩"
            "Baba" -> "👨"
            "Ev" -> "🏠"
            "Güneş" -> "☀️"
            else -> "🧩"
        }
    }

    private fun prepararPregunta() {
        val actual = _estado.value

        if (actual.preguntasRespondidas >= totalPreguntas) {
            _estado.update { it.copy(terminado = true) }
            return
        }

        val palabraCorrecta = palabras[actual.preguntasRespondidas]

        val incorrectas = palabras
            .filter { it.espanol != palabraCorrecta.espanol }
            .shuffled()
            .take(2)
            .map { it.espanol }

        val opciones = (incorrectas + palabraCorrecta.espanol).shuffled()

        _estado.update {
            it.copy(
                indiceActual = actual.preguntasRespondidas,
                opciones = opciones,
                respuestaSeleccionada = null,
                mensaje = null,
                terminado = false
            )
        }
    }

    fun responder(opcion: String) {
        val actual = _estado.value

        if (actual.terminado || actual.respuestaSeleccionada != null) {
            return
        }

        val palabraCorrecta = palabras[actual.indiceActual]
        val acerto = opcion == palabraCorrecta.espanol

        _estado.update {
            it.copy(
                respuestaSeleccionada = opcion,
                mensaje = if (acerto) {
                    "¡Correcto!"
                } else {
                    "Casi. La respuesta era ${palabraCorrecta.espanol}."
                },
                puntuacion = if (acerto) {
                    it.puntuacion + 1
                } else {
                    it.puntuacion
                }
            )
        }
    }

    fun siguientePregunta() {
        val actual = _estado.value

        if (actual.respuestaSeleccionada == null || actual.terminado) {
            return
        }

        _estado.update {
            it.copy(
                preguntasRespondidas = it.preguntasRespondidas + 1
            )
        }

        prepararPregunta()
    }

    fun reiniciar() {
        _estado.value = QuizUiState()
        prepararPregunta()
    }

    fun obtenerImagen(): String {
        return palabras[_estado.value.indiceActual].imagen
    }

    fun obtenerPalabraTurca(): String {
        return palabras[_estado.value.indiceActual].turco
    }

    fun obtenerTotalPreguntas(): Int {
        return totalPreguntas
    }
}
