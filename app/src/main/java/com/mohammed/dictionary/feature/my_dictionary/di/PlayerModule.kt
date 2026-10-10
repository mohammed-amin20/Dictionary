package com.mohammed.dictionary.feature.my_dictionary.di

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.player.AndroidPronunciationPlayer
import com.mohammed.dictionary.feature.my_dictionary.domain.player.PronunciationPlayer
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PlayerModule {

    @Binds
    @Singleton
    abstract fun bindPronunciationPlayer(
        impl: AndroidPronunciationPlayer
    ): PronunciationPlayer
}
