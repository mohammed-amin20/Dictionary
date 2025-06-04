package com.mohammed.dictionary.feature.my_dictionary.data.util

sealed interface Error {
    sealed interface Remote: Error{
        data object NoInternet: Remote
        data object NotFound : Remote
        data class Unknown (val msg: String) : Remote
    }
}