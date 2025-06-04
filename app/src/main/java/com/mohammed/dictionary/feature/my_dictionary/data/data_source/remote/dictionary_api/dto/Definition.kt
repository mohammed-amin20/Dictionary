package com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto

data class Definition(
    val antonyms: List<Any>,
    val definition: String,
    val example: String,
    val synonyms: List<Any>
)