package com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto

data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String
)