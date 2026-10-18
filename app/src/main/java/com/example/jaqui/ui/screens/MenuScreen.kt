package com.example.jaqui.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(modifier: Modifier = Modifier,
               onTarjetasClick:() -> Unit = {},
               onQuizClick:() -> Unit = {},
               onMemoriaClick:() -> Unit = {}
    ) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(
            12.dp,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Menu Principal",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "¿Que quieres practicar?"
        )
        Button(
            onClick = onTarjetasClick
        ) {
            Text(text = "Tarjetas de vocabulario")
        }

        Button(
            onClick = onQuizClick
        ) {
            Text(text = "Cuestionario")
        }

        Button(
            onClick = onMemoriaClick
        ) {
            Text(text = "Memorice")
        }
    }
}
