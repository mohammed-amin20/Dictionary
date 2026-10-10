package com.mohammed.dictionary.feature.my_dictionary.di

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.DictionaryAPI
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.FreeDictionaryAPI
import com.mohammed.dictionary.feature.my_dictionary.data.repository.DictionaryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DictionaryModule {

    private fun client(): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .callTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        )
        .build()

    @Provides
    @Singleton
    fun provideDictionaryApi(): DictionaryAPI = Retrofit.Builder()
        .baseUrl(DictionaryAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client())
        .build()
        .create()

    @Provides
    @Singleton
    fun provideFreeDictionaryApi(): FreeDictionaryAPI = Retrofit.Builder()
        .baseUrl(FreeDictionaryAPI.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client())
        .build()
        .create()

    @Provides
    @Singleton
    fun provideRepository(
        api: DictionaryAPI,
        freeApi: FreeDictionaryAPI
    ): DictionaryRepository = DictionaryRepository(api, freeApi)
}
