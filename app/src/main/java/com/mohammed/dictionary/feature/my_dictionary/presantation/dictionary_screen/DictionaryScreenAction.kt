package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

import android.bluetooth.BluetoothLeAudio

sealed interface DictionaryScreenAction {
    data object OnImeSearch : DictionaryScreenAction
    data class OnSearchChanged(val search : String) : DictionaryScreenAction
    data class OnPlayAudioClicked(val audio: String) : DictionaryScreenAction
}