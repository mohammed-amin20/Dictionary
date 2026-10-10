package com.mohammed.dictionary.feature.my_dictionary.data.data_source.player

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.mohammed.dictionary.feature.my_dictionary.domain.player.PronunciationPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidPronunciationPlayer @Inject constructor(
    @ApplicationContext private val context: Context
) : PronunciationPlayer {

    private var mediaPlayer: MediaPlayer? = null
    private var textToSpeech: TextToSpeech? = null
    private var ttsReady = false
    private var ttsFailed = false
    private var pendingSpeech: String? = null

    private var onStart: (() -> Unit)? = null
    private var onComplete: (() -> Unit)? = null
    private var onError: (() -> Unit)? = null

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.apply {
                    language = Locale.US
                    setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) {
                            onStart?.invoke()
                        }

                        override fun onDone(utteranceId: String?) {
                            onComplete?.invoke()
                        }

                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) {
                            handleError()
                        }

                        override fun onError(utteranceId: String?, errorCode: Int) {
                            Log.w(TAG, "TTS error code=$errorCode")
                            handleError()
                        }
                    })
                }
                ttsReady = true
                pendingSpeech?.let { speak(it) }
            } else {
                Log.w(TAG, "TTS initialization failed with status=$status")
                ttsFailed = true
                if (pendingSpeech != null) handleError()
            }
            pendingSpeech = null
        }
    }

    override fun play(
        text: String,
        audioUrl: String?,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: () -> Unit
    ) {
        stop()
        this.onStart = onStart
        this.onComplete = onComplete
        this.onError = onError

        val url = audioUrl?.trim()?.takeIf { it.isNotBlank() }
        if (url != null && (url.startsWith("https://") || url.startsWith("http://"))) {
            playUrl(url)
        } else {
            speak(text)
        }
    }

    private fun playUrl(url: String) {
        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener {
                    it.start()
                    onStart?.invoke()
                }
                setOnCompletionListener { handleComplete() }
                setOnErrorListener { _, what, extra ->
                    Log.w(TAG, "MediaPlayer error what=$what extra=$extra")
                    handleError()
                    true
                }
                prepareAsync()
            }
        } catch (e: Exception) {
            Log.w(TAG, "MediaPlayer failed to play $url", e)
            handleError()
        }
    }

    private fun speak(text: String) {
        if (text.isBlank()) {
            handleError()
            return
        }
        if (ttsFailed) {
            handleError()
            return
        }
        if (!ttsReady) {
            pendingSpeech = text
            return
        }
        val result = textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, Bundle(), UTTERANCE_ID)
        if (result == TextToSpeech.ERROR || result == null) {
            handleError()
        }
    }

    override fun stop() {
        mediaPlayer?.let { player ->
            try {
                player.stop()
            } catch (_: IllegalStateException) {
            }
            try {
                player.release()
            } catch (_: Exception) {
            }
        }
        mediaPlayer = null
        textToSpeech?.stop()
        onStart = null
        onComplete = null
        onError = null
    }

    override fun release() {
        stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        ttsReady = false
        ttsFailed = false
    }

    private fun handleComplete() {
        releaseMediaPlayer()
        onComplete?.invoke()
    }

    private fun handleError() {
        releaseMediaPlayer()
        onError?.invoke()
    }

    private fun releaseMediaPlayer() {
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {
        }
        mediaPlayer = null
    }

    private companion object {
        const val TAG = "PronunciationPlayer"
        const val UTTERANCE_ID = "dictionary_word"
    }
}
