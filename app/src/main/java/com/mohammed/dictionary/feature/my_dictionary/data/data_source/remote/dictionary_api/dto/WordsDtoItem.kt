package com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto

data class WordsDtoItem(
    val meanings: List<Meaning>,
    val origin: String,
    val phonetic: String,
    val phonetics: List<Phonetic>,
    val word: String
)