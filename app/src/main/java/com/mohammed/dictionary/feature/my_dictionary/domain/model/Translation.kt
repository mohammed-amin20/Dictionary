package com.mohammed.dictionary.feature.my_dictionary.domain.model

data class Translation(
    val sourceText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String
)
