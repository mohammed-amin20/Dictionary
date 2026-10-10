package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen


sealed interface DictionaryScreenAction {
    data object OnImeSearch : DictionaryScreenAction
    data object OnTranslate : DictionaryScreenAction
    data class OnSearchChanged(val search: String) : DictionaryScreenAction
    data class OnPlayAudioClicked(val text: String, val audioUrl: String?) : DictionaryScreenAction
}
