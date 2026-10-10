package com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.dto.FreeDictionaryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface FreeDictionaryAPI {

    @GET("api/v1/entries/en/{word}")
    suspend fun getEntries(@Path("word") word: String): FreeDictionaryResponse

    companion object {
        const val BASE_URL = "https://freedictionaryapi.com/"
    }
}
