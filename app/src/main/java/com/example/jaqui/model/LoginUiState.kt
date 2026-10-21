package com.example.jaqui.model

data class LoginUiState(
    val correo: String = "",
    val password: String = "",
    val errores: LoginErrores = LoginErrores()
)
