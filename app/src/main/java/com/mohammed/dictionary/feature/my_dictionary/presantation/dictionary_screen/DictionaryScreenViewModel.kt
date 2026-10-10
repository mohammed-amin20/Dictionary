package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.dictionary.feature.my_dictionary.data.repository.DictionaryRepository
import com.mohammed.dictionary.feature.my_dictionary.data.repository.TranslationRepository
import com.mohammed.dictionary.feature.my_dictionary.data.util.Error
import com.mohammed.dictionary.feature.my_dictionary.data.util.Result
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
) : ViewModel() {

    private val _state = MutableStateFlow(DictionaryScreenState())
    val state = _state.asStateFlow()

    private val _uiAction = MutableSharedFlow<UiAction>()
    val uiAction = _uiAction.asSharedFlow()

    private var mediaPlayer: MediaPlayer? = null

    fun onAction(action: DictionaryScreenAction) {
        when (action) {
            DictionaryScreenAction.OnImeSearch -> search()

            DictionaryScreenAction.OnTranslate -> translateCurrentWord()

            is DictionaryScreenAction.OnSearchChanged -> {
                _state.update { it.copy(search = action.search) }
            }

            is DictionaryScreenAction.OnPlayAudioClicked -> {
                when {
                    action.audio.isBlank() -> emit(UiAction.NoAudio)

                    _state.value.playing -> stopAudio()

                    else -> playAudio(action.audio)
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

    private fun playAudio(url: String) {
        stopAudio()
        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener {
                    it.start()
                    _state.update { state -> state.copy(playing = true) }
                }
                setOnCompletionListener { stopAudio() }
                setOnErrorListener { _, _, _ ->
                    stopAudio()
                    emit(UiAction.SoundError)
                    true
                }
                prepareAsync()
            }
        } catch (_: Exception) {
            stopAudio()
            emit(UiAction.SoundError)
        }
    }

    private fun stopAudio() {
        mediaPlayer?.run {
            try {
                if (isPlaying) pause()
            } catch (_: IllegalStateException) {
            }
            release()
        }
        mediaPlayer = null
        _state.update { state -> state.copy(playing = false) }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}

sealed interface UiAction {
    data object EmptyQuery : UiAction
    data object NoAudio : UiAction
    data object SoundError : UiAction
    data class Remote(val text: String) : UiAction
}
