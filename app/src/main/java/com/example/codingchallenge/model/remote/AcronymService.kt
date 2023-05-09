package com.example.codingchallenge.model.remote

import com.example.codingchallenge.model.dto.AcronymMeaningDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface AcronymService {
    @GET(DICT_ENDPOINT)
    suspend fun getMeanings(@Query(SF) initials: String): List<AcronymMeaningDTO>

    companion object {
        const val DICT_ENDPOINT = "dictionary.py"
        const val SF = "sf"
    }
}
