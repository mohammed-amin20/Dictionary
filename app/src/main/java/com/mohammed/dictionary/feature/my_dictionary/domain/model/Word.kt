package com.mohammed.dictionary.feature.my_dictionary.domain.model


data class Word(
    val meanings: List<Meaning>,
    val phonetic: String,
    val audio: String?,
    val word: String
)
