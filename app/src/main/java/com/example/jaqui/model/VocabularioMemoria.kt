package com.example.jaqui.model

data class ParVocabulario(
    val turco: String,
    val espanol: String
)

/**
 * Vocabulario de 10 pares (20 tarjetas) con temas variados:
 * transporte, objetos, comida, animales, familia, lugares y naturaleza.
 */
object VocabularioMemoria {
    val pares: List<ParVocabulario> = listOf(
        ParVocabulario(turco = "Araba", espanol = "Auto"),
        ParVocabulario(turco = "Kitap", espanol = "Libro"),
        ParVocabulario(turco = "Elma", espanol = "Manzana"),
        ParVocabulario(turco = "Su", espanol = "Agua"),
        ParVocabulario(turco = "Kedi", espanol = "Gato"),
        ParVocabulario(turco = "Köpek", espanol = "Perro"),
        ParVocabulario(turco = "Anne", espanol = "Madre"),
        ParVocabulario(turco = "Baba", espanol = "Padre"),
        ParVocabulario(turco = "Ev", espanol = "Casa"),
        ParVocabulario(turco = "Güneş", espanol = "Sol")
    )
}
