package com.mohammed.dictionary

import com.google.gson.Gson
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto.WordsDto
import com.mohammed.dictionary.feature.my_dictionary.data.mappers.toDomain
import org.junit.Assert.assertEquals
import org.junit.Test

class DictionaryMappersTest {
    private fun words(json: String): WordsDto = Gson().fromJson(json, WordsDto::class.java)

    @Test
    fun missingPronunciationFieldsAreSafe() {
        val word = words("""[{"word":"hello","meanings":[]}]""").first().toDomain()
        assertEquals("", word.audio)
        assertEquals("", word.phonetic)
    }

    @Test
    fun pronunciationSkipsMissingAndBlankAudio() {
        val word = words("""[{
            "word":"hello","meanings":[],"phonetic":null,
            "phonetics":[{}, {"audio":" "}, {"audio":"https://example.com/hello.mp3","text":"hello"}]
        }]""").first().toDomain()
        assertEquals("https://example.com/hello.mp3", word.audio)
        assertEquals("hello", word.phonetic)
    }

    @Test
    fun missingWordAndMeaningsAreSafe() {
        val word = words("""[{}]""").first().toDomain()
        assertEquals("", word.word)
        assertEquals(0, word.meanings.size)
    }

    @Test
    fun missingMeaningFieldsAreSafe() {
        val word = words("""[{"word":"x","meanings":[{"partOfSpeech":null,"definitions":null}]}]""")
            .first().toDomain()
        val meaning = word.meanings.single()
        assertEquals("", meaning.partOfSpeech)
        assertEquals(0, meaning.definitions.size)
    }

    @Test
    fun missingDefinitionFieldsAreSafe() {
        val word = words(
            """[{"word":"x","meanings":[{"partOfSpeech":"noun","definitions":[{}]}]}]"""
        ).first().toDomain()
        val definition = word.meanings.single().definitions.single()
        assertEquals("", definition.definition)
        assertEquals("", definition.example)
    }
}
