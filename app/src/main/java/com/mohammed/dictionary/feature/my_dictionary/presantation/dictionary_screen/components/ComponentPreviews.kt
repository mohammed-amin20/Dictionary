package com.mohammed.dictionary.feature.my_dictionary.presantation.dictionary_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Definition
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Meaning
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Translation
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Word
import com.mohammed.dictionary.ui.theme.DictionaryTheme
import com.mohammed.dictionary.ui.theme.Spacing
import com.mohammed.dictionary.ui.theme.dictionaryColors

private val sampleWord = Word(
    meanings = listOf(
        Meaning(
            definitions = listOf(
                Definition(
                    definition = "the occurrence and development of events by chance in a happy or beneficial way.",
                    example = "a fortunate stroke of serendipity"
                ),
                Definition(
                    definition = "an aptitude for making desirable discoveries by accident.",
                    example = ""
                )
            ),
            partOfSpeech = "noun"
        ),
        Meaning(
            definitions = listOf(
                Definition(
                    definition = "the quality of being ephemeral; transience.",
                    example = "the ephemeral nature of fame"
                )
            ),
            partOfSpeech = "adjective"
        )
    ),
    phonetic = "/\u02CCs\u025Br\u0259n\u02C8d\u026Ap\u026Ati/",
    audio = "https://example.com/audio.mp3",
    word = "serendipity"
)

private val sampleTranslation = Translation(
    sourceText = "serendipity",
    translatedText = "\u062D\u0633\u0646 \u0627\u0644\u062D\u0638",
    sourceLang = "en",
    targetLang = "ar"
)

@Composable
private fun ThemedPreview(content: @Composable () -> Unit) {
    DictionaryTheme {
        val extras = MaterialTheme.dictionaryColors
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(extras.backgroundGradient))
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.xl),
                verticalArrangement = Arrangement.spacedBy(Spacing.lg)
            ) {
                content()
            }
        }
    }
}

@Preview(name = "Content light", showBackground = true, widthDp = 400, heightDp = 900)
@Composable
private fun ContentPreviewLight() = ThemedPreview {
    WordHeader(word = sampleWord)
    PronunciationButton(
        phonetic = sampleWord.phonetic,
        playing = true,
        enabled = true,
        onClick = {}
    )
    TranslationCard(
        translation = sampleTranslation,
        translating = false,
        error = false,
        onRetry = {}
    )
    sampleWord.meanings.forEach { MeaningCard(meaning = it) }
}

@Preview(name = "Content dark", showBackground = true, widthDp = 400, heightDp = 900, uiMode = 0x20)
@Composable
private fun ContentPreviewDark() = ThemedPreview {
    WordHeader(word = sampleWord)
    PronunciationButton(
        phonetic = sampleWord.phonetic,
        playing = false,
        enabled = true,
        onClick = {}
    )
    TranslationCard(
        translation = sampleTranslation,
        translating = false,
        error = false,
        onRetry = {}
    )
    sampleWord.meanings.forEach { MeaningCard(meaning = it) }
}

@Preview(name = "Idle light", showBackground = true, widthDp = 400, heightDp = 800)
@Composable
private fun IdlePreviewLight() = ThemedPreview {
    IdleState(onSuggestionClick = {})
}

@Preview(name = "Idle dark", showBackground = true, widthDp = 400, heightDp = 800, uiMode = 0x20)
@Composable
private fun IdlePreviewDark() = ThemedPreview {
    IdleState(onSuggestionClick = {})
}

@Preview(name = "States light", showBackground = true, widthDp = 400, heightDp = 900)
@Composable
private fun StatesPreviewLight() = ThemedPreview {
    NotFoundState()
    NoInternetState(onRetry = {})
}

@Preview(name = "Loading dark", showBackground = true, widthDp = 400, heightDp = 400, uiMode = 0x20)
@Composable
private fun LoadingPreviewDark() = ThemedPreview {
    LoadingSkeleton()
}
