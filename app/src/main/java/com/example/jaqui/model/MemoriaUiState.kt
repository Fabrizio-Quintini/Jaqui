package com.example.jaqui.model

data class MemoriaUiState(
    val cartas: List<CartaMemoria> = emptyList(),
    val primeraId: Int? = null,
    val bloqueado: Boolean = false,
    val aciertos: Int = 0,
    val totalPares: Int = 0,
    val terminado: Boolean = false,
    val mensaje: String? = null,
    val mensajeId: Int = 0,
    val inicioMillis: Long = 0L,
    val tiempoFinalSegundos: Long? = null
)
