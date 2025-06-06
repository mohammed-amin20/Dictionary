package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

import android.media.MediaPlayer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohammed.dictionary.feature.my_dictionary.data.repository.DictionaryRepository
import com.mohammed.dictionary.feature.my_dictionary.data.util.Error
import com.mohammed.dictionary.feature.my_dictionary.data.util.Result
import com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.UiAction.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DictionaryScreenViewModel @Inject constructor(
    private val repository : DictionaryRepository
) : ViewModel(){

    private val _state = MutableStateFlow(DictionaryScreenState())
    val state = _state.asStateFlow()

    private val _uiAction = MutableSharedFlow<UiAction>()
    val uiAction = _uiAction.asSharedFlow()

    fun onAction(action: DictionaryScreenAction){
        when(action){
            DictionaryScreenAction.OnImeSearch -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            loading = true
                        )
                    }
                    val result = repository.getWordDefinitions(_state.value.search)
                    when(result){
                        is Result.Failure -> {
                            when(result.error){
                                Error.Remote.NoInternet -> {
                                    _uiAction.emit(ShowToastMsg("No Internet Connection"))
                                }
                                Error.Remote.NotFound -> {
                                    _state.update {
                                        it.copy(
                                            error = true
                                        )
                                    }
                                }
                                is Error.Remote.Unknown -> {
                                    _uiAction.emit(ShowToastMsg("Something Went Wrong"))
                                }
                            }
                        }
                        is Result.Success -> {
                            val word = result.data
                            _state.update {
                                it.copy(word = word)
                            }
                        }
                    }
                    _state.update {
                        it.copy(
                            loading = false
                        )
                    }
                }
            }
            is DictionaryScreenAction.OnSearchChanged -> {
                _state.update {
                    it.copy(
                        search = action.search
                    )
                }
            }

            is DictionaryScreenAction.OnPlayAudioClicked -> {
                val mediaPlayer = MediaPlayer()
                try {
                    mediaPlayer.reset()
                    mediaPlayer.setDataSource(action.audio)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener {
                        it.start()
                    }
                    mediaPlayer.setOnCompletionListener {
                        it.release()
                    }
                } catch (e : Exception){
                    viewModelScope.launch {
                        _uiAction.emit(
                            UiAction.ShowToastMsg(e.localizedMessage?: "Sound Error")
                        )
                    }
                }
            }
        }
    }
}
sealed interface UiAction{
    data class ShowToastMsg(val msg : String) : UiAction
}