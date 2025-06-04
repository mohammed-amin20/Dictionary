package com.mohammed.dictionary.feature.my_dictionary.di

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.DictionaryAPI
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
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DictionaryModule {
    @Provides
    @Singleton
    fun provideDictionaryApi() : DictionaryAPI{
        return Retrofit.Builder()
            .baseUrl(DictionaryAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)
                    )
                    .build()
            )
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideRepository(api : DictionaryAPI) : DictionaryRepository{
        return DictionaryRepository(api)
    }
}