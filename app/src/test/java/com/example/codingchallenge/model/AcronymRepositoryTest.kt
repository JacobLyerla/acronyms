package com.example.codingchallenge.model

import com.example.acronym.data.util.AcronymResult
import com.example.codingchallenge.model.dto.AcronymMeaningDTO
import com.example.codingchallenge.model.dto.MeaningDTO
import com.example.codingchallenge.model.entities.Meaning
import com.example.codingchallenge.model.remote.AcronymService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AcronymRepositoryTest {

    private val service = mockk<AcronymService>()
    private val repo = AcronymRepository(service)

    @Test
    @DisplayName("Test retrieving the meaning of an acronym from the Acronym API")
    fun getWordsFromAcronym() = runTest {
        // given
        val acronym = "lol"
        val expected = listOf(Meaning("Application Programming Interface", 1, 1990))
        val response = listOf(
            AcronymMeaningDTO("API",listOf(MeaningDTO("Application Programming Interface", 1, 1990)))
        )
        coEvery { service.getMeanings(acronym) } coAnswers { response }

        // when
        val result = repo.getWordsFromAcronym(acronym)

        // then
        assertEquals(AcronymResult.Success(expected), result)
    }
}
