package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.dictionary.feature.my_dictionary.data.repository.DictionaryRepository
import com.mohammed.dictionary.feature.my_dictionary.data.repository.TranslationRepository
import com.mohammed.dictionary.feature.my_dictionary.data.util.Error
import com.mohammed.dictionary.feature.my_dictionary.data.util.Result
import com.mohammed.dictionary.feature.my_dictionary.domain.player.PronunciationPlayer
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.UiAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryScreenViewModel @Inject constructor(
    private val repository: DictionaryRepository,
    private val translationRepository: TranslationRepository,
    private val player: PronunciationPlayer,
) : ViewModel() {

    private val _state = MutableStateFlow(DictionaryScreenState())
    val state = _state.asStateFlow()

    private val _uiAction = MutableSharedFlow<UiAction>()
    val uiAction = _uiAction.asSharedFlow()

    fun onAction(action: DictionaryScreenAction) {
        when (action) {
            DictionaryScreenAction.OnImeSearch -> search()

            DictionaryScreenAction.OnTranslate -> translateCurrentWord()

            is DictionaryScreenAction.OnSearchChanged -> {
                _state.update { it.copy(search = action.search) }
            }

            is DictionaryScreenAction.OnPlayAudioClicked -> {
                when {
                    action.text.isBlank() && action.audioUrl.isNullOrBlank() -> emit(UiAction.NoAudio)

                    _state.value.playing -> stopAudio()

                    else -> playAudio(action.text, action.audioUrl)
                }
            }
        }
    }

    private fun search() {
        val query = _state.value.search.trim()
        if (query.isEmpty()) {
            emit(UiAction.EmptyQuery)
            return
        }
        stopAudio()
        viewModelScope.launch {
            _state.update {
                it.copy(
                    loading = true,
                    translating = true,
                    error = false,
                    translationError = false,
                    noInternet = false,
                    serverError = false,
                    word = null,
                    translation = null
                )
            }

            val definitions = async { repository.getWordDefinitions(query) }
            val translation = async { translationRepository.translate(query) }

            when (val result = definitions.await()) {
                is Result.Success -> _state.update {
                    it.copy(word = result.data, error = false, noInternet = false)
                }

                is Result.Failure -> when (val error = result.error) {
                    Error.Remote.NoInternet -> _state.update {
                        it.copy(noInternet = true, error = false, serverError = false)
                    }

                    Error.Remote.NotFound -> _state.update {
                        it.copy(error = true, noInternet = false, serverError = false)
                    }

                    is Error.Remote.Server -> _state.update {
                        it.copy(serverError = true, error = false, noInternet = false)
                    }

                    is Error.Remote.Unknown -> {
                        _state.update {
                            it.copy(error = true, noInternet = false, serverError = false)
                        }
                        emit(UiAction.Remote(error.msg))
                    }
                }
            }
            _state.update { it.copy(loading = false) }

            when (val result = translation.await()) {
                is Result.Success -> _state.update {
                    it.copy(translation = result.data, translationError = false)
                }

                is Result.Failure -> _state.update {
                    it.copy(translation = null, translationError = true)
                }
            }
            _state.update { it.copy(translating = false) }
        }
    }

    private fun translateCurrentWord() {
        val query = _state.value.search.trim()
        if (query.isEmpty()) {
            emit(UiAction.EmptyQuery)
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(translating = true, translationError = false) }
            when (val result = translationRepository.translate(query)) {
                is Result.Success -> _state.update {
                    it.copy(translation = result.data, translationError = false)
                }

                is Result.Failure -> _state.update {
                    it.copy(translation = null, translationError = true)
                }
            }
            _state.update { it.copy(translating = false) }
        }
    }

    private fun emit(action: UiAction) {
        viewModelScope.launch { _uiAction.emit(action) }
    }

    private fun playAudio(text: String, audioUrl: String?) {
        player.play(
            text = text,
            audioUrl = audioUrl,
            onStart = { _state.update { state -> state.copy(playing = true) } },
            onComplete = { _state.update { state -> state.copy(playing = false) } },
            onError = {
                _state.update { state -> state.copy(playing = false) }
                emit(UiAction.SoundError)
            }
        )
    }

    private fun stopAudio() {
        player.stop()
        _state.update { state -> state.copy(playing = false) }
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}

sealed interface UiAction {
    data object EmptyQuery : UiAction
    data object NoAudio : UiAction
    data object SoundError : UiAction
    data class Remote(val text: String) : UiAction
}
