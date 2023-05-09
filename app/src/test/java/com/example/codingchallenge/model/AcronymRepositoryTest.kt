package com.example.codingchallenge.model

import com.example.acronym.data.util.AcronymResult
import com.example.codingchallenge.model.dto.AcronymMeaningDTO
import com.example.codingchallenge.model.dto.MeaningDTO
import com.example.codingchallenge.model.entities.Meaning
import com.example.codingchallenge.model.remote.AcronymService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class AcronymRepositoryTest {

    private lateinit var service: AcronymService
    private lateinit var repo: AcronymRepository

    @Before
    fun setup() {
        service = mockk()
        repo = AcronymRepository(service)
    }

    @Test
    fun `getWordsFromAcronym returns successful result when service provides meanings`() = runTest {
        // given
        val acronym = "API"
        lateinit var result: AcronymResult<List<Meaning>>
        val expected = listOf(Meaning("Application Programming Interface", 1, 1990))
        val response = listOf(
            AcronymMeaningDTO(
                "API",
                listOf(MeaningDTO("Application Programming Interface", 1, 1990))
            )
        )
        coEvery { service.getMeanings(acronym) } coAnswers { response }
        launch { result = repo.getWordsFromAcronym(acronym) }
        advanceUntilIdle()

        // then
        coVerify { service.getMeanings(acronym) }
        assertTrue(result is AcronymResult.Success)
        assertTrue((result as AcronymResult.Success).data == expected)
        assertEquals(AcronymResult.Success(expected), result)
    }

    @Test
    fun `getWordsFromAcronym returns error result when service provides no meanings`() = runTest {
        // given
        val acronym = "API"
        lateinit var result: AcronymResult<List<Meaning>>
        coEvery { service.getMeanings(acronym) } coAnswers { listOf<AcronymMeaningDTO>() }
        launch { result = repo.getWordsFromAcronym(acronym) }
        advanceUntilIdle()

        // then
        assertTrue(result is AcronymResult.Error)
        assertEquals("No meanings found for acronym '$acronym'", (result as AcronymResult.Error).message)
    }

    @Test
    fun `getWordsFromAcronym returns error result when service throws exception`() = runTest {
        // given
        val acronym = "API"
        lateinit var result: AcronymResult<List<Meaning>>
        val exceptionMessage = "Failed to retrieve meanings"
        coEvery { service.getMeanings(acronym) } coAnswers { throw Exception(exceptionMessage) }
        launch { result = repo.getWordsFromAcronym(acronym) }
        advanceUntilIdle()

        // then
        assertTrue(result is AcronymResult.Error)
    }
    @Test
    fun `getWordsFromAcronym returns error result when service provides no meanings for non-standard acronym`() = runTest {
        // given
        val acronym = "NONONO"
        lateinit var result: AcronymResult<List<Meaning>>
        coEvery { service.getMeanings(acronym) } coAnswers { listOf<AcronymMeaningDTO>() }
        launch { result = repo.getWordsFromAcronym(acronym) }
        advanceUntilIdle()

        // then
        assertTrue(result is AcronymResult.Error)
        assertEquals("No meanings found for acronym '$acronym'", (result as AcronymResult.Error).message)
    }
}

