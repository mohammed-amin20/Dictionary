package com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto

data class WordsDtoItem(
    val meanings: List<Meaning>? = null,
    val origin: String? = null,
    val phonetic: String? = null,
    val phonetics: List<Phonetic>? = null,
    val word: String? = null
)
