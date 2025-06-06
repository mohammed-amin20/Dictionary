package com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto.WordsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryAPI {

    @GET("/api/v2/entries/en/{word}")
    suspend fun getWordDefinitions( @Path("word") word : String) : WordsDto

    companion object{
        const val BASE_URL = "https://api.dictionaryapi.dev/"
    }
}