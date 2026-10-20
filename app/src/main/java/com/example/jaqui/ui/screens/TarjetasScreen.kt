package com.example.jaqui.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jaqui.viewmodel.TarjetasViewModel
import java.util.Locale

@Composable
fun TarjetasScreen(
    modifier: Modifier = Modifier,
    viewModel: TarjetasViewModel = viewModel()
) {
    val estado by viewModel.estado.collectAsState()
    val palabraActual = viewModel.palabras[estado.indiceActual]
    val context = LocalContext.current

    val rotacionTarjeta by animateFloatAsState(
        targetValue = if (estado.mostrarEspanol) 180f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "rotacionTarjeta"
    )

    var motorTts by remember {
        mutableStateOf<TextToSpeech?>(null)
    }

    var ttsListo by remember {
        mutableStateOf(false)
    }

    DisposableEffect(context) {
        val motor = TextToSpeech(context) { resultado ->
            ttsListo = resultado == TextToSpeech.SUCCESS
        }

        motorTts = motor

        onDispose {
            motor.stop()
            motor.shutdown()
            motorTts = null
            ttsListo = false
        }
    }

    fun reproducir(texto: String, idioma: Locale) {
        val motor = motorTts

        if (ttsListo && motor != null) {
            motor.setLanguage(idioma)

            motor.speak(
                texto,
                TextToSpeech.QUEUE_FLUSH,
                null,
                "jaqui-$texto"
            )
        }
    }

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
            text = "Tarjeta ${estado.indiceActual + 1} de ${viewModel.palabras.size}",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Vocabulario: transporte",
            style = MaterialTheme.typography.headlineSmall
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    rotationY = rotacionTarjeta
                    cameraDistance = 12 * density
                }
                .clickable {
                    viewModel.voltearTarjeta()
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .graphicsLayer {
                        rotationY = if(estado.mostrarEspanol) 180f else 0f
                    },
            ) {
                Text(
                    text = palabraActual.imagen,
                    fontSize = 64.sp
                )

                Text(
                    text = if (estado.mostrarEspanol) {
                        palabraActual.espanol
                    } else {
                        palabraActual.turco
                    },
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = if (estado.mostrarEspanol) {
                        "Español"
                    } else {
                        "Turco"
                    }
                )
            }
        }

        Text(text = "Toca la tarjeta para ver la traducción")
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    reproducir(palabraActual.turco, Locale("tr", "TR"))
                },
                enabled = ttsListo
            ) {
                Text("🔊 Turco")
            }

            Button(
                onClick = {
                    reproducir(palabraActual.espanol, Locale("es", "CL"))
                },
                enabled = ttsListo
            ) {
                Text("🔊 Español")
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { viewModel.anterior() },
                enabled = estado.indiceActual > 0
            ) {
                Text("Anterior")
            }

            Button(
                onClick = { viewModel.siguiente() },
                enabled = estado.indiceActual < viewModel.palabras.lastIndex
            ) {
                Text("Siguiente")
            }
        }
    }
}