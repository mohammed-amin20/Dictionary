package com.mohammed.dictionary.feature.my_dictionary.data.mappers

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.translation_api.dto.TranslationResponse
import com.mohammed.dictionary.feature.my_dictionary.data.util.cleanTranslatedText
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Translation

fun TranslationResponse.toDomain(
    query: String,
    sourceLang: String,
    targetLang: String
): Translation? {
    val source = query.trim()
    if (responseStatus != 200) return null

    val primary = responseData?.translatedText?.cleanTranslatedText(source)
    val candidate = primary?.takeIf { it.isUsable(source) }
        ?: matches.orEmpty()
            .sortedByDescending { it.match ?: 0.0 }
            .asSequence()
            .map { it.translation?.cleanTranslatedText(source).orEmpty() }
            .firstOrNull { it.isUsable(source) }
        ?: return null

    return Translation(
        sourceText = source,
        translatedText = candidate,
        sourceLang = sourceLang,
        targetLang = targetLang
    )
}

private fun String.isUsable(query: String): Boolean =
    isNotBlank() && !equals(query, ignoreCase = true)
