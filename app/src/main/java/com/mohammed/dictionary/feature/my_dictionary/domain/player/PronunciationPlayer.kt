package com.mohammed.dictionary.feature.my_dictionary.domain.player

interface PronunciationPlayer {

    fun play(
        text: String,
        audioUrl: String?,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: () -> Unit
    )

    fun stop()

    fun release()
}
