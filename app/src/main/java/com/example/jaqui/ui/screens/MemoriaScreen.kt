package com.example.jaqui.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jaqui.model.CartaMemoria
import com.example.jaqui.viewmodel.MemoriaViewModel

private const val COLUMNAS = 5
private const val FILAS = 4
private val ESPACIO = 8.dp

@Composable
fun MemoriaScreen(
    viewModel: MemoriaViewModel,
    modifier: Modifier = Modifier,
    onVolver: () -> Unit = {}
) {
    val estado by viewModel.estado.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var segundosTranscurridos by remember { mutableStateOf(0L) }

    // Temporizador en vivo; se detiene al terminar la partida.
    LaunchedEffect(estado.inicioMillis, estado.terminado) {
        if (estado.terminado) {
            estado.tiempoFinalSegundos?.let { segundosTranscurridos = it }
        } else {
            while (true) {
                segundosTranscurridos = if (estado.inicioMillis > 0L) {
                    (System.currentTimeMillis() - estado.inicioMillis) / 1000
                } else {
                    0L
                }
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    LaunchedEffect(estado.mensajeId) {
        estado.mensaje?.let { texto ->
            snackbarHostState.showSnackbar(texto)
            viewModel.onMensajeMostrado()
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton(onClick = onVolver) {
                    Text(text = "← Volver")
                }
                Text(
                    text = "Memorice",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Aciertos: ${estado.aciertos} / ${estado.totalPares}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Tiempo: ${formatearTiempo(segundosTranscurridos)}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Button(onClick = { viewModel.reiniciar() }) {
                    Text(text = "Reiniciar")
                }
            }

            if (estado.terminado) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "¡Ganaste! 🎉",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Tiempo final: ${formatearTiempo(estado.tiempoFinalSegundos ?: segundosTranscurridos)}",
                            fontSize = 16.sp
                        )
                        Button(
                            onClick = { viewModel.reiniciar() },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(text = "Jugar de nuevo")
                        }
                    }
                }
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val anchoCarta = (maxWidth - ESPACIO * (COLUMNAS - 1)) / COLUMNAS
                val altoCarta = (maxHeight - ESPACIO * (FILAS - 1)) / FILAS

                Column(verticalArrangement = Arrangement.spacedBy(ESPACIO)) {
                    for (fila in 0 until FILAS) {
                        Row(horizontalArrangement = Arrangement.spacedBy(ESPACIO)) {
                            for (col in 0 until COLUMNAS) {
                                val indice = fila * COLUMNAS + col
                                if (indice < estado.cartas.size) {
                                    val carta = estado.cartas[indice]
                                    CartaMemoriaItem(
                                        carta = carta,
                                        modifier = Modifier.size(anchoCarta, altoCarta),
                                        onClick = { viewModel.onCartaClick(carta.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun formatearTiempo(segundos: Long): String {
    val minutos = segundos / 60
    val resto = segundos % 60
    return "%02d:%02d".format(minutos, resto)
}

@Composable
private fun CartaMemoriaItem(
    carta: CartaMemoria,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    if (carta.emparejada) {
        Box(modifier = modifier)
        return
    }

    Card(
        modifier = modifier.clickable(enabled = !carta.revelada) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (carta.revelada) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.secondaryContainer
            }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            if (carta.revelada) {
                Text(
                    text = carta.texto,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "?",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
    }
}
