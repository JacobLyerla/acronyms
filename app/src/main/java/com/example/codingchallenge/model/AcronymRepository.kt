package com.example.codingchallenge.model

import com.example.acronym.data.util.AcronymResult
import com.example.codingchallenge.model.entities.Meaning
import com.example.codingchallenge.model.remote.AcronymService
import javax.inject.Inject

class AcronymRepository @Inject constructor(private val acronymService: AcronymService) {
    suspend fun getWordsFromAcronym(acronym: String): AcronymResult<List<Meaning>> {
        return try {
            val response = acronymService.getMeanings(acronym)
            if (response.isEmpty()) {
                AcronymResult.Error("No meanings found for acronym '$acronym'")
            } else {
                val meanings = response[0].lfs.map {
                    Meaning(
                        lf = it.lf,
                        freq = it.freq,
                        since = it.since
                    )
                }
                if (meanings.isEmpty()) {
                    AcronymResult.Error("No meanings found for acronym '$acronym'")
                } else {
                    AcronymResult.Success(meanings)
                }
            }
        } catch (e: Exception) {
            AcronymResult.Error("Failed to retrieve meanings for acronym '$acronym': ${e.message}")
        }
    }
}
