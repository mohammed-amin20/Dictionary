package com.mohammed.dictionary.feature.my_dictionary.data.repository

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.translation_api.TranslationAPI
import com.mohammed.dictionary.feature.my_dictionary.data.mappers.toDomain
import com.mohammed.dictionary.feature.my_dictionary.data.util.Error
import com.mohammed.dictionary.feature.my_dictionary.data.util.Result
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Translation
import kotlinx.coroutines.CancellationException
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class TranslationRepository @Inject constructor(
    private val api: TranslationAPI
) {
    suspend fun translate(
        text: String,
        sourceLang: String = TranslationAPI.ENGLISH,
        targetLang: String = TranslationAPI.ARABIC
    ): Result<Translation, Error.Remote> {
        val query = text.trim()
        if (query.isEmpty()) return Result.Failure(Error.Remote.NotFound)
        return try {
            val response = api.translate(query, "$sourceLang|$targetLang")
            val translation = response.toDomain(query, sourceLang, targetLang)
            if (translation == null) {
                Result.Failure(
                    Error.Remote.Unknown(
                        response.responseDetails?.takeIf { it.isNotBlank() }
                            ?: "No translation available for \"$query\"."
                    )
                )
            } else {
                Result.Success(translation)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: IOException) {
            Result.Failure(Error.Remote.NoInternet)
        } catch (e: HttpException) {
            Result.Failure(
                when (e.code()) {
                    429 -> Error.Remote.Unknown("Translation rate limit reached. Try again later.")
                    else -> Error.Remote.Unknown("Translation failed (HTTP ${e.code()}).")
                }
            )
        } catch (e: Exception) {
            Result.Failure(Error.Remote.Unknown(e.localizedMessage ?: "Translation failed."))
        }
    }
}
