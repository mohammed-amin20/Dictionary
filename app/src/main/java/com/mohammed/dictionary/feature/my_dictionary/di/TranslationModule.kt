package com.mohammed.dictionary.feature.my_dictionary.di

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.translation_api.TranslationAPI
import com.mohammed.dictionary.feature.my_dictionary.data.repository.TranslationRepository
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
object TranslationModule {

    @Provides
    @Singleton
    fun provideTranslationApi(): TranslationAPI {
        return Retrofit.Builder()
            .baseUrl(TranslationAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .callTimeout(20, TimeUnit.SECONDS)
                    .addInterceptor(
                        HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BASIC)
                    )
                    .build()
            )
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideTranslationRepository(api: TranslationAPI): TranslationRepository {
        return TranslationRepository(api)
    }
}
