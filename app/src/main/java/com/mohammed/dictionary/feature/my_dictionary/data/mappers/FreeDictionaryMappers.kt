package com.mohammed.dictionary.feature.my_dictionary.data.mappers

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.dto.FreeDictionaryResponse
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.dto.FreeEntry
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.dto.FreeSense
import com.mohammed.dictionary.feature.my_dictionary.data.util.cleanText
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Definition
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Meaning
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Word

private const val MAX_MEANINGS = 5
private const val MAX_DEFINITIONS_PER_MEANING = 5

fun FreeDictionaryResponse.toDomain(query: String): Word? {
    val meanings = entries.orEmpty()
        .mapNotNull { it.toMeaning() }
        .take(MAX_MEANINGS)
    if (meanings.isEmpty()) return null

    val pronunciations = entries.orEmpty()
        .asSequence()
        .flatMap { it.pronunciations.orEmpty().asSequence() }

    val phonetic = pronunciations
        .mapNotNull { it.text?.cleanText()?.takeIf(String::isNotBlank) }
        .firstOrNull()
        .orEmpty()

    val audio = pronunciations
        .mapNotNull { it.audio?.trim()?.takeIf(String::isNotBlank) }
        .firstOrNull()
        .orEmpty()

    return Word(
        meanings = meanings,
        phonetic = phonetic,
        audio = audio,
        word = word?.cleanText()?.takeIf(String::isNotBlank) ?: query.trim()
    )
}

private fun FreeEntry.toMeaning(): Meaning? {
    val definitions = senses.orEmpty()
        .mapNotNull { it.toDefinition() }
        .take(MAX_DEFINITIONS_PER_MEANING)
    if (definitions.isEmpty()) return null
    return Meaning(
        definitions = definitions,
        partOfSpeech = partOfSpeech?.cleanText().orEmpty()
    )
}

private fun FreeSense.toDefinition(): Definition? {
    val text = definition?.cleanText().orEmpty()
    if (text.isBlank()) return null
    val example = examples.orEmpty()
        .firstNotNullOfOrNull { it.cleanText().takeIf(String::isNotBlank) }
        .orEmpty()
    return Definition(definition = text, example = example)
}
