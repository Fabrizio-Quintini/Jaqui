
package com.example.jaqui.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Palabra(
    val imagen: String,
    val turco: String,
    val espanol: String
)

private val palabrasTransporte = listOf(
    Palabra("🚗", "Araba", "Auto"),
    Palabra("🚌", "Otobüs", "Autobús"),
    Palabra("🚆", "Tren", "Tren"),
    Palabra("✈️", "Uçak", "Avión"),
    Palabra("🚲", "Bisiklet", "Bicicleta"),
    Palabra("🚢", "Gemi", "Barco")
)

@Composable
fun TarjetasScreen(modifier: Modifier = Modifier) {

    var mostrarEspanol by remember {
        mutableStateOf(false)
    }

    var indiceActual by remember {
        mutableStateOf(0)
    }

    val palabraActual = palabrasTransporte[indiceActual]


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(
            20.dp,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tarjeta ${indiceActual + 1} de ${palabrasTransporte.size}",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Vocabulario: transporte",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    mostrarEspanol = !mostrarEspanol
                }
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = palabraActual.imagen,
                    fontSize = 64.sp
                )

                Text(
                    text = if (mostrarEspanol) {
                        palabraActual.espanol
                    } else {
                        palabraActual.turco
                    },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (mostrarEspanol) "Español" else "Turco"
                )
            }
        }

        Text(
            text = "Toca la tarjeta para ver la traducción"
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (indiceActual > 0) {
                        indiceActual--
                        mostrarEspanol = false
                    }
                }
            ) {
                Text(text = "Anterior")
            }

            Button(
                onClick = {
                    if (indiceActual < palabrasTransporte.lastIndex) {
                        indiceActual++
                        mostrarEspanol = false
                    }
                }
            ) {
                Text(text = "Siguiente")
            }
        }
    }
}
