package com.mohammed.dictionary

import com.google.gson.Gson
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.dto.FreeDictionaryResponse
import com.mohammed.dictionary.feature.my_dictionary.data.mappers.toDomain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FreeDictionaryMappersTest {

    private fun response(json: String): FreeDictionaryResponse =
        Gson().fromJson(json, FreeDictionaryResponse::class.java)

    @Test
    fun mapsEntriesToDomain() {
        val word = response(
            """
            {
              "word":"hello",
              "entries":[
                {
                  "partOfSpeech":"interjection",
                  "pronunciations":[{"type":"ipa","text":"/həˈloʊ/"}],
                  "senses":[{"definition":"A greeting.","examples":["Hello, everyone."]}]
                }
              ]
            }
            """.trimIndent()
        ).toDomain("hello")!!

        assertEquals("hello", word.word)
        assertEquals("/həˈloʊ/", word.phonetic)
        assertEquals(1, word.meanings.size)
        val meaning = word.meanings.single()
        assertEquals("interjection", meaning.partOfSpeech)
        assertEquals("A greeting.", meaning.definitions.single().definition)
        assertEquals("Hello, everyone.", meaning.definitions.single().example)
    }

    @Test
    fun emptyEntriesMapToNull() {
        assertNull(response("""{"word":"asdf","entries":[]}""").toDomain("asdf"))
    }

    @Test
    fun htmlAndEntitiesAreCleaned() {
        val word = response(
            """
            {
              "word":"x",
              "entries":[
                {"partOfSpeech":"noun","senses":[{"definition":"a &amp; b <b>bold</b>"}]}
              ]
            }
            """.trimIndent()
        ).toDomain("x")!!

        assertEquals("a & b bold", word.meanings.single().definitions.single().definition)
    }

    @Test
    fun missingWordFallsBackToQuery() {
        val word = response(
            """{"entries":[{"partOfSpeech":"noun","senses":[{"definition":"Something."}]}]}"""
        ).toDomain("something")!!
        assertEquals("something", word.word)
    }
}
