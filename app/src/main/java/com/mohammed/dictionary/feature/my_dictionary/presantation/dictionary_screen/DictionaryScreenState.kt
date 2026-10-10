package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

import com.mohammed.dictionary.feature.my_dictionary.domain.model.Translation
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Word

data class DictionaryScreenState(
    val search: String = "",
    val word: Word? = null,
    val translation: Translation? = null,
    val loading: Boolean = false,
    val translating: Boolean = false,
    val error: Boolean = false,
    val translationError: Boolean = false,
    val noInternet: Boolean = false,
    val serverError: Boolean = false,
    val playing: Boolean = false
)
