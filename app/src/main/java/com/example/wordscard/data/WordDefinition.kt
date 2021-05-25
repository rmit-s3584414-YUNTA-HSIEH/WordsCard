package com.example.wordscard.data

import com.squareup.moshi.Json


data class WordDefinition(
    @Json(name = "word") val word: String,
    @field:Json(name = "phonetics") val phonetics: List<Phonetics>,
    @field:Json(name = "meanings") val meanings: List<Meanings>
)


data class Phonetics(
    @field:Json(name = "text") val text: String,
    @field:Json(name = "audio") val audio: String
)

data class Meanings(
    @field:Json(name = "partOfSpeech") val partOfSpeech: String,
    @field:Json(name = "definitions") val definitions: List<Definitions>
)

data class Definitions(
    @field:Json(name = "definition") val definition: String,
    @field:Json(name = "example") val example: String?
)
