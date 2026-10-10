package com.mohammed.dictionary

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.DictionaryAPI
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.dto.WordsDto
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.FreeDictionaryAPI
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.dto.FreeDictionaryResponse
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.dto.FreeEntry
import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.free_dictionary_api.dto.FreeSense
import com.mohammed.dictionary.feature.my_dictionary.data.repository.DictionaryRepository
import com.mohammed.dictionary.feature.my_dictionary.data.util.Error
import com.mohammed.dictionary.feature.my_dictionary.data.util.Result
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class DictionaryRepositoryTest {

    private fun repository(
        response: suspend (String) -> WordsDto,
        fallback: suspend (String) -> FreeDictionaryResponse = {
            FreeDictionaryResponse(word = it, entries = emptyList())
        }
    ) = DictionaryRepository(
        object : DictionaryAPI {
            override suspend fun getWordDefinitions(word: String) = response(word)
        },
        object : FreeDictionaryAPI {
            override suspend fun getEntries(word: String) = fallback(word)
        }
    )

    private fun fallbackWord(word: String) = FreeDictionaryResponse(
        word = word,
        entries = listOf(
            FreeEntry(
                partOfSpeech = "noun",
                senses = listOf(FreeSense(definition = "A fallback definition."))
            )
        )
    )

    @Test
    fun emptyResponseIsNotFound() = runBlocking {
        val result = repository(response = { WordsDto() }).getWordDefinitions("missing")
        assertEquals(Error.Remote.NotFound, (result as Result.Failure).error)
    }

    @Test
    fun queryIsTrimmed() = runBlocking {
        var requested = ""
        repository(response = { requested = it; WordsDto() }).getWordDefinitions(" hello ")
        assertEquals("hello", requested)
    }

    @Test
    fun blankQueryDoesNotCallApi() = runBlocking {
        val result = repository(response = { throw AssertionError("Unexpected API call") })
            .getWordDefinitions("  ")
        assertEquals(Error.Remote.NotFound, (result as Result.Failure).error)
    }

    @Test
    fun cancellationIsRethrown() = runBlocking {
        val cancellation = CancellationException("Cancelled search")
        try {
            repository(response = { throw cancellation }).getWordDefinitions("hello")
            fail("Cancellation must propagate")
        } catch (actual: CancellationException) {
            assertSame(cancellation, actual)
        }
    }

    @Test
    fun serverFailureIsReportedAsServerError() = runBlocking {
        val result = repository(
            response = { throw HttpException(Response.error<WordsDto>(500, "".toResponseBody())) }
        ).getWordDefinitions("hello")
        assertTrue((result as Result.Failure).error is Error.Remote.Server)
    }

    @Test
    fun primaryFailureFallsBackToSecondaryProvider() = runBlocking {
        val result = repository(
            response = { throw HttpException(Response.error<WordsDto>(522, "".toResponseBody())) },
            fallback = { fallbackWord(it) }
        ).getWordDefinitions("hello")

        when (result) {
            is Result.Success -> assertEquals("hello", result.data.word)
            is Result.Failure -> fail("Expected success but got ${result.error}")
        }
    }

    @Test
    fun http404IsNotFound() = runBlocking {
        val result = repository(
            response = { throw HttpException(Response.error<WordsDto>(404, "".toResponseBody())) }
        ).getWordDefinitions("hello")
        assertEquals(Error.Remote.NotFound, (result as Result.Failure).error)
    }

    @Test
    fun connectionFailureIsNoInternet() = runBlocking {
        val result = repository(response = { throw IOException("Offline") })
            .getWordDefinitions("hello")
        assertEquals(Error.Remote.NoInternet, (result as Result.Failure).error)
    }
}
