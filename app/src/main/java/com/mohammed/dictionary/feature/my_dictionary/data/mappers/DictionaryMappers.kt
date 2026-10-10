package com.mohammed.dictionary.feature.my_dictionary.data.mappers

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto.Definition
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto.Meaning
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto.WordsDtoItem
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Word

fun WordsDtoItem.toDomain(): Word {
    return Word(
        meanings = this.meanings.orEmpty().map { it.toDomain() },
        phonetic = this.phonetic?.takeIf { it.isNotBlank() }
            ?: this.phonetics.orEmpty().firstNotNullOfOrNull {
                it.text?.takeIf { text -> text.isNotBlank() }
            }.orEmpty(),
        audio = this.phonetics.orEmpty()
            .firstNotNullOfOrNull { it.audio?.takeIf { audio -> audio.isNotBlank() } }
            .normalizeAudioUrl(),
        word = this.word.orEmpty()
    )
}

fun Meaning.toDomain(): com.mohammed.dictionary.feature.my_dictionary.domain.model.Meaning {
    return com.mohammed.dictionary.feature.my_dictionary.domain.model.Meaning(
        definitions = this.definitions.orEmpty().map { it.toDomain() },
        partOfSpeech = this.partOfSpeech.orEmpty()
    )
}

fun Definition.toDomain(): com.mohammed.dictionary.feature.my_dictionary.domain.model.Definition {
    return com.mohammed.dictionary.feature.my_dictionary.domain.model.Definition(
        definition = this.definition.orEmpty(),
        example = this.example.orEmpty()
    )
}

private fun String?.normalizeAudioUrl(): String {
    val value = this?.trim().orEmpty()
    return when {
        value.isBlank() -> ""
        value.startsWith("//") -> "https:$value"
        value.startsWith("http://") -> "https://" + value.removePrefix("http://")
        else -> value
    }
}
