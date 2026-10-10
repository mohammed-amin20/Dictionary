package com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.dto

import com.google.gson.annotations.SerializedName

data class FreeDictionaryResponse(
    @SerializedName("word") val word: String? = null,
    @SerializedName("entries") val entries: List<FreeEntry>? = null
)

data class FreeEntry(
    @SerializedName("partOfSpeech") val partOfSpeech: String? = null,
    @SerializedName("pronunciations") val pronunciations: List<FreePronunciation>? = null,
    @SerializedName("senses") val senses: List<FreeSense>? = null
)

data class FreePronunciation(
    @SerializedName("text") val text: String? = null,
    @SerializedName("audio") val audio: String? = null
)

data class FreeSense(
    @SerializedName("definition") val definition: String? = null,
    @SerializedName("examples") val examples: List<String>? = null
)
