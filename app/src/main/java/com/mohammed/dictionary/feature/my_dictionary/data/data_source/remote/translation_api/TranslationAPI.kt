package com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.translation_api

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.translation_api.dto.TranslationResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationAPI {

    @GET("get")
    suspend fun translate(
        @Query("q") query: String,
        @Query("langpair") langPair: String
    ): TranslationResponse

    companion object {
        const val BASE_URL = "https://api.mymemory.translated.net/"
        const val ENGLISH = "en"
        const val ARABIC = "ar"
    }
}
