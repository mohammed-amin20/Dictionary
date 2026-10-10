package com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.translation_api.dto

import com.google.gson.annotations.SerializedName

data class TranslationResponse(
    @SerializedName("responseData") val responseData: ResponseData? = null,
    @SerializedName("responseStatus") val responseStatus: Int? = null,
    @SerializedName("responseDetails") val responseDetails: String? = null,
    @SerializedName("matches") val matches: List<TranslationMatch>? = null
)

data class ResponseData(
    @SerializedName("translatedText") val translatedText: String? = null,
    @SerializedName("match") val match: Double? = null
)

data class TranslationMatch(
    @SerializedName("translation") val translation: String? = null,
    @SerializedName("match") val match: Double? = null,
    @SerializedName("quality") val quality: String? = null
)
