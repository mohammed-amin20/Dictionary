package com.mohammed.dictionary.feature.my_dictionary.data.mappers

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto.Definition
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto.Meaning
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto.WordsDtoItem
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Word

fun WordsDtoItem.toDomain(): Word{
    return Word(
        meanings = this.meanings.map { it.toDomain() },
        phonetic = this.phonetic ?: "",
        audio = if (this.phonetics.any{ it.audio != "" })
                    this.phonetics.filter { it.audio != "" }[0].audio
                else "",
        word = this.word
    )
}

fun Meaning.toDomain() : com.mohammed.dictionary.feature.my_dictionary.domain.model.Meaning{
    return com.mohammed.dictionary.feature.my_dictionary.domain.model.Meaning(
        definitions = this.definitions.map { it.toDomain() },
        partOfSpeech = this.partOfSpeech
    )
}

fun Definition.toDomain(): com.mohammed.dictionary.feature.my_dictionary.domain.model.Definition{
    return com.mohammed.dictionary.feature.my_dictionary.domain.model.Definition(
        definition = this.definition,
        example = this.example
    )
}
