package com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto.WordsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryAPI {

    @GET("/word")
    suspend fun getWordDefinitions( @Path("word") word : String) : WordsDto

    companion object{
        const val BASE_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/"
    }
}