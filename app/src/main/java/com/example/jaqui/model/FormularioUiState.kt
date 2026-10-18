package com.example.jaqui.model

data class FormularioUiState(
    val nombre: String = "",
    val correo: String = "",
    val aceptaTerminos: Boolean = false,
    val errores: FormularioErrores = FormularioErrores()
)