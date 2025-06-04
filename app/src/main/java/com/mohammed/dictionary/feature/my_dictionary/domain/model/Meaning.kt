package com.mohammed.dictionary.feature.my_dictionary.domain.model


data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String
)
