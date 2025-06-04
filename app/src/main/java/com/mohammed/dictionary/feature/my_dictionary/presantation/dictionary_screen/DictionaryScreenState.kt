package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

import com.mohammed.dictionary.feature.my_dictionary.domain.model.Word

data class DictionaryScreenState(
    val search : String = "",
    val word : List<Word> =emptyList()
)
