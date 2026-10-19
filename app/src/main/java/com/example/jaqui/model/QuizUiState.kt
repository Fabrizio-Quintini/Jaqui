package com.example.jaqui.model

data class QuizUiState(
    val indiceActual: Int = 0,
    val opciones: List<String> = emptyList(),
    val respuestaSeleccionada: String? = null,
    val mensaje: String? = null,
    val puntuacion: Int = 0,
    val preguntasRespondidas: Int = 0,
    val terminado: Boolean = false
)