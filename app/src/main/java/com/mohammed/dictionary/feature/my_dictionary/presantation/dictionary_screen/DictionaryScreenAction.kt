package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

sealed interface DictionaryScreenAction {
    data object OnImeSearch : DictionaryScreenAction
}