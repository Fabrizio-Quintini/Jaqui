package com.example.jaqui.model

enum class Idioma {
    ESPANOL,
    TURCO
}

data class CartaMemoria(
    val id: Int,
    val parId: Int,
    val texto: String,
    val idioma: Idioma,
    val revelada: Boolean = false,
    val emparejada: Boolean = false
)
