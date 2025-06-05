package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen

import androidx.lifecycle.ViewModel
import com.mohammed.dictionary.feature.my_dictionary.data.repository.DictionaryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DictionaryScreenViewModel @Inject constructor(
    private val repository : DictionaryRepository
) : ViewModel(){

    private val _state = MutableStateFlow(DictionaryScreenState())
    val state = _state.asStateFlow()


}