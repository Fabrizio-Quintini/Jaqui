package com.example.jaqui.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TraductorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp,
            Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            text = "Jaqui ",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold)
        Text(
            text = "Aprende turco jugando  "
        )
        Button(
            modifier = Modifier.width(240.dp),
            onClick = { }
        ) {
            Text(
                text = "Jugar como invitado"
            )
        }
        Button(
            modifier = Modifier.width(240.dp),
            onClick = { }
        ) {
            Text(
                text = "Iniciar sesión"
            )
        }
    }

}