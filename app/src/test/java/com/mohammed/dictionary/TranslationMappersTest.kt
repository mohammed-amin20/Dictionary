package com.mohammed.dictionary

import com.google.gson.Gson
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.translation_api.dto.TranslationResponse
import com.mohammed.dictionary.feature.my_dictionary.data.mappers.toDomain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class TranslationMappersTest {

    private fun response(json: String): TranslationResponse =
        Gson().fromJson(json, TranslationResponse::class.java)

    private fun translate(json: String) =
        response(json).toDomain(query = "hello", sourceLang = "en", targetLang = "ar")

    @Test
    fun validResponseMapsToDomain() {
        val translation = translate(
            """{"responseData":{"translatedText":"مرحبا"},"responseStatus":200}"""
        )
        assertEquals("hello", translation?.sourceText)
        assertEquals("مرحبا", translation?.translatedText)
        assertEquals("en", translation?.sourceLang)
        assertEquals("ar", translation?.targetLang)
    }

    @Test
    fun missingResponseDataIsNull() {
        assertNull(translate("""{"responseStatus":200}"""))
    }

    @Test
    fun blankTranslationIsNull() {
        assertNull(
            translate("""{"responseData":{"translatedText":"   "},"responseStatus":200}""")
        )
    }

    @Test
    fun nonSuccessStatusIsNull() {
        assertNull(
            translate("""{"responseData":{"translatedText":"مرحبا"},"responseStatus":403}""")
        )
    }

    @Test
    fun unchangedTextFallsBackToBestMatch() {
        val translation = translate(
            """
            {
              "responseData":{"translatedText":"hello"},
              "responseStatus":200,
              "matches":[
                {"translation":"hello","match":1},
                {"translation":"مرحبا","match":0.9}
              ]
            }
            """.trimIndent()
        )
        assertEquals("مرحبا", translation?.translatedText)
    }

    @Test
    fun arabicPresentationFormsAreNormalized() {
        val translation = translate(
            """{"responseData":{"translatedText":"\uFEFC\uFEEB\uFE8D"},"responseStatus":200}"""
        )
        assertEquals("\u0644\u0627\u0647\u0627", translation?.translatedText)
    }

    @Test
    fun trailingReferenceArtifactIsStripped() {
        val translation = response(
            """{"responseData":{"translatedText":"كتاب/book"},"responseStatus":200}"""
        ).toDomain(query = "book", sourceLang = "en", targetLang = "ar")
        assertEquals("كتاب", translation?.translatedText)
    }

    @Test
    fun htmlEntitiesAreDecoded() {
        val translation = translate(
            """{"responseData":{"translatedText":"caf&#233;"},"responseStatus":200}"""
        )
        assertEquals("café", translation?.translatedText)
    }
}
