package com.mohammed.dictionary.feature.my_dictionary.data.repository

import com.mohammed.dictionary.feature.my_dictionary.data.data_source.remote.dictionary_api.DictionaryAPI
import com.mohammed.dictionary.feature.my_dictionary.data.mappers.toDomain
import com.mohammed.dictionary.feature.my_dictionary.data.util.Error
import com.mohammed.dictionary.feature.my_dictionary.data.util.Result
import com.mohammed.dictionary.feature.my_dictionary.domain.model.Word
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class DictionaryRepository @Inject constructor(
    private val api : DictionaryAPI
){
    suspend fun getWordDefinitions(word : String) : Result<List<Word>, Error.Remote>{
        try {
            val wordDto = api.getWordDefinitions(word)
            val words = wordDto.map { it.toDomain() }
            return Result.Success(words)
        }catch(e: IOException){
            return Result.Failure(Error.Remote.NoInternet)
        }catch(e: HttpException){
            return Result.Failure(Error.Remote.NotFound)
        }
        catch(e: Exception){
            return Result.Failure(Error.Remote.Unknown(e.localizedMessage?:"Something went wrong."))
        }
    }
}