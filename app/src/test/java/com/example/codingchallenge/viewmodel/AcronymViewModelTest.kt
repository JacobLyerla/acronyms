package com.example.codingchallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.acronym.data.util.AcronymResult
import com.example.codingchallenge.model.AcronymRepository
import com.example.codingchallenge.model.entities.Meaning
import com.example.codingchallenge.view.AcronymScreenState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.DisplayName
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AcronymViewModelTest {

    private val repo = mockk<AcronymRepository>()
    private val viewModel = AcronymViewModel(repo)

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    @DisplayName("Test retrieving the meaning of an acronym")
    fun getWordsFromAcronym() = runTest {
        // given
        val acronym = "API"
        val expected = listOf(
            "Application Programming Interface",
            "American Petroleum Institute"
        )
        val result = AcronymResult.Success(expected.map { Meaning(it, 1, 1990) })
        coEvery { repo.getWordsFromAcronym(acronym) } coAnswers { result }

        // when
        viewModel.input.value = acronym
        viewModel.getWordsFromAcronym()

        // then
        val expectedScreenState = AcronymScreenState(
            isLoading = false,
            response = result,
            showResultText = true
        )
        assert(viewModel.state.value == expectedScreenState)
    }
}
