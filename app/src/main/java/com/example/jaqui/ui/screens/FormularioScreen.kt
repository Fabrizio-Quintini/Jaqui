package com.example.jaqui.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaqui.viewmodel.FormularioViewModel

@Composable
fun FormularioScreen(
    viewModel: FormularioViewModel,
    onRegistroExitoso: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsState()

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Crear cuenta",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = estado.nombre,
                onValueChange = { viewModel.onNombreChange(it) },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.errores.nombre != null,
                supportingText = estado.errores.nombre?.let { { Text(it) } }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = estado.correo,
                onValueChange = { viewModel.onCorreoChange(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                isError = estado.errores.correo != null,
                supportingText = estado.errores.correo?.let { { Text(it) } }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = estado.aceptaTerminos,
                    onCheckedChange = { viewModel.onAceptaTerminosChange(it) }
                )
                Text(text = "Acepto los términos y condiciones")
            }
            estado.errores.aceptaTerminos?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    if (viewModel.validarFormulario()) {
                        onRegistroExitoso()
                    }
                },
                modifier = Modifier.width(240.dp)
            ) {
                Text(text = "Registrarse")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "¿Ya tienes cuenta? ")
                TextButton(onClick = onLoginClick) {
                    Text(text = "Inicia sesión")
                }
            }
        }
    }
}