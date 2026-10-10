package com.mohammed.dictionary.feature.my_dictionary.data.repository

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.DictionaryAPI
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.FreeDictionaryAPI
import com.mohammed.dictionary.feature.my_dictionary.data.mappers.toDomain
import com.mohammed.dictionary.feature.my_dictionary.data.util.Error
import com.mohammed.dictionary.feature.my_dictionary.data.util.Result
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Word
import kotlinx.coroutines.CancellationException
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class DictionaryRepository @Inject constructor(
    private val api: DictionaryAPI,
    private val freeApi: FreeDictionaryAPI
) {
    suspend fun getWordDefinitions(word: String): Result<Word, Error.Remote> {
        val query = word.trim()
        if (query.isEmpty()) return Result.Failure(Error.Remote.NotFound)

        return when (val primary = fetchPrimary(query)) {
            is Result.Success -> primary

            is Result.Failure -> when (val fallback = fetchFallback(query)) {
                is Result.Success -> fallback
                is Result.Failure -> Result.Failure(
                    selectError(primary.error, fallback.error)
                )
            }
        }
    }

    private suspend fun fetchPrimary(query: String): Result<Word, Error.Remote> = try {
        val dto = api.getWordDefinitions(query).firstOrNull()
            ?: return Result.Failure(Error.Remote.NotFound)
        Result.Success(dto.toDomain())
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        Result.Failure(Error.Remote.NoInternet)
    } catch (e: HttpException) {
        Result.Failure(errorForHttp(e))
    } catch (e: Exception) {
        Result.Failure(Error.Remote.Unknown(e.localizedMessage ?: "Something went wrong."))
    }

    private suspend fun fetchFallback(query: String): Result<Word, Error.Remote> = try {
        val word = freeApi.getEntries(query).toDomain(query)
        if (word == null) Result.Failure(Error.Remote.NotFound) else Result.Success(word)
    } catch (e: CancellationException) {
        throw e
    } catch (e: IOException) {
        Result.Failure(Error.Remote.NoInternet)
    } catch (e: HttpException) {
        Result.Failure(errorForHttp(e))
    } catch (e: Exception) {
        Result.Failure(Error.Remote.Unknown(e.localizedMessage ?: "Something went wrong."))
    }

    private fun errorForHttp(e: HttpException): Error.Remote = when (e.code()) {
        404 -> Error.Remote.NotFound
        in 500..599 -> Error.Remote.Server(e.code())
        else -> Error.Remote.Unknown("Dictionary request failed (HTTP ${e.code()}).")
    }

    private fun selectError(primary: Error.Remote, fallback: Error.Remote): Error.Remote = when {
        primary == Error.Remote.NotFound && fallback == Error.Remote.NotFound -> Error.Remote.NotFound
        primary == Error.Remote.NoInternet || fallback == Error.Remote.NoInternet -> Error.Remote.NoInternet
        primary is Error.Remote.Server -> primary
        fallback is Error.Remote.Server -> fallback
        primary == Error.Remote.NotFound -> fallback
        else -> primary
    }
}
