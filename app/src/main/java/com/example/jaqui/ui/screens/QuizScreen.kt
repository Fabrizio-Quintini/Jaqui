
package com.example.jaqui.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.jaqui.model.VocabularioTransporte

@Composable
fun QuizScreen() {

    // Lista de palabras que utilizaremos en el cuestionario.
    val palabras = VocabularioTransporte.palabras

    // Guarda el número de la pregunta actual.
    var indicePregunta by remember {
        mutableIntStateOf(0)
    }

    // Guarda la cantidad de respuestas correctas.
    var puntuacion by remember {
        mutableIntStateOf(0)
    }

    // Guarda la respuesta elegida. null significa que aún no respondió.
    var respuestaSeleccionada by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Cuestionario",
            style = MaterialTheme.typography.headlineMedium
        )

        // Comprueba si todavía quedan preguntas.
        if (indicePregunta < palabras.size) {

            val palabraActual = palabras[indicePregunta]

            Text(
                text = "Pregunta ${indicePregunta + 1} de ${palabras.size}"
            )

            Text(
                text = palabraActual.imagen,
                style = MaterialTheme.typography.displayLarge
            )

            Text(
                text = "¿Qué significa \"${palabraActual.turco}\"?",
                style = MaterialTheme.typography.titleLarge
            )

            // Genera dos alternativas incorrectas y una correcta.
            val opciones = remember(indicePregunta) {
                (
                        palabras
                            .filter {
                                it.espanol != palabraActual.espanol
                            }
                            .shuffled()
                            .take(2)
                            .map {
                                it.espanol
                            } + palabraActual.espanol
                        ).shuffled()
            }

            // Muestra las tres alternativas.
            opciones.forEach { opcion ->

                Button(
                    onClick = {
                        // Solo permite responder una vez por pregunta.
                        if (respuestaSeleccionada == null) {

                            respuestaSeleccionada = opcion

                            // Suma un punto si la respuesta es correcta.
                            if (opcion == palabraActual.espanol) {
                                puntuacion++
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = opcion)
                }
            }

            // Muestra el mensaje después de responder.
            if (respuestaSeleccionada != null) {

                Text(
                    text = if (
                        respuestaSeleccionada == palabraActual.espanol
                    ) {
                        "¡Correcto! 🎉"
                    } else {
                        "¡Inténtalo de nuevo en la siguiente pregunta!"
                    },
                    style = MaterialTheme.typography.titleMedium
                )

                // Avanza solo después de seleccionar una alternativa.
                Button(
                    onClick = {
                        indicePregunta++
                        respuestaSeleccionada = null
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (indicePregunta < palabras.lastIndex) {
                            "Siguiente pregunta"
                        } else {
                            "Ver resultado"
                        }
                    )
                }
            }

            Text(
                text = "Puntuación: $puntuacion"
            )

        } else {

            // Pantalla final cuando se terminan todas las preguntas.
            Text(
                text = "¡Cuestionario terminado! 🎉",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Tu puntuación: $puntuacion de ${palabras.size}",
                style = MaterialTheme.typography.titleLarge
            )

            // Reinicia el cuestionario.
            Button(
                onClick = {
                    indicePregunta = 0
                    puntuacion = 0
                    respuestaSeleccionada = null
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Volver a jugar")
            }
        }
    }
}
