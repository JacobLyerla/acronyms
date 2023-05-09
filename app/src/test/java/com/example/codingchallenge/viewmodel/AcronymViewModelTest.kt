package com.example.codingchallenge.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.acronym.data.util.AcronymResult
import com.example.codingchallenge.model.AcronymRepository
import com.example.codingchallenge.model.entities.Meaning
import com.example.codingchallenge.view.AcronymScreenState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class AcronymViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var repository: AcronymRepository
    private lateinit var viewmodel: AcronymViewModel
    private lateinit var observer: Observer<AcronymScreenState>

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        viewmodel = AcronymViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        if (::observer.isInitialized) {
            viewmodel.state.removeObserver(observer)
        }
        Dispatchers.resetMain()
    }

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun testInitialConditions() {
        observer = mockk(relaxed = true)
        viewmodel.state.observeForever(observer)
        assert(viewmodel.state.value?.isLoading == false)
        assert(viewmodel.state.value?.showResultText == false)
        assert(viewmodel.state.value?.errorMessage == null)
    }

    @Test
    fun testSuccessfulResponse() = runTest {
        // given
        val acronym = "API"
        val meaning = listOf(Meaning("Application Programming Interface", 1, 1990))
        coEvery { repository.getWordsFromAcronym(acronym) } coAnswers {
            AcronymResult.Success(
                meaning
            )
        }

        // Prepare LiveData observer
        observer = mockk(relaxed = true)
        viewmodel.state.observeForever(observer)

        // when
        viewmodel.input.value = acronym
        launch { viewmodel.getWordsFromAcronym() }
        advanceUntilIdle()

        // then
        val capturedState = viewmodel.state.value
        assert(capturedState?.response is AcronymResult.Success)
        assert((capturedState?.response as AcronymResult.Success).data == meaning)
    }

    @Test
    fun testLoadingStateAfterResponse() = runTest {
        // given
        val acronym = "API"
        val meaning = listOf(Meaning("Application Programming Interface", 1, 1990))
        coEvery { repository.getWordsFromAcronym(acronym) } coAnswers {
            AcronymResult.Success(
                meaning
            )
        }

        // Prepare LiveData observer
        observer = mockk(relaxed = true)
        viewmodel.state.observeForever(observer)

        // when
        viewmodel.input.value = acronym
        launch { viewmodel.getWordsFromAcronym() }
        advanceUntilIdle()

        // then
        assert(viewmodel.state.value?.isLoading == false)
    }

    @Test
    fun testShowResultTextAfterResponse() = runTest {
        // given
        val acronym = "API"
        val meaning = listOf(Meaning("Application Programming Interface", 1, 1990))
        coEvery { repository.getWordsFromAcronym(acronym) } coAnswers {
            AcronymResult.Success(
                meaning
            )
        }

        // Prepare LiveData observer
        observer = mockk(relaxed = true)
        viewmodel.state.observeForever(observer)

        // when
        viewmodel.input.value = acronym
        launch { viewmodel.getWordsFromAcronym() }
        advanceUntilIdle()

        // then
        assert(viewmodel.state.value?.showResultText == true)
    }

    // ...
    @Test
    fun testNoErrorMessageAfterSuccess() = runTest {
        // given
        val acronym = "API"
        val meaning = listOf(Meaning("Application Programming Interface", 1, 1990))
        coEvery { repository.getWordsFromAcronym(acronym) } coAnswers {
            AcronymResult.Success(
                meaning
            )
        }

        // Prepare LiveData observer
        observer = mockk(relaxed = true)
        viewmodel.state.observeForever(observer)

        // when
        viewmodel.input.value = acronym
        launch { viewmodel.getWordsFromAcronym() }
        advanceUntilIdle()

        // then
        assert(viewmodel.state.value?.errorMessage == null)
    }

    @Test
    fun testErrorResponse() = runTest {
        // given
        val acronym = "API"
        val errorMessage = "An error occurred"
        coEvery { repository.getWordsFromAcronym(acronym) } coAnswers {
            AcronymResult.Error(
                errorMessage
            )
        }

        // Prepare LiveData observer
        observer = mockk(relaxed = true)
        viewmodel.state.observeForever(observer)

        // when
        viewmodel.input.value = acronym
        launch { viewmodel.getWordsFromAcronym() }
        advanceUntilIdle()

        // then
        val capturedState = viewmodel.state.value
        assert(capturedState?.response is AcronymResult.Error)
        assert((capturedState?.response as AcronymResult.Error).message == errorMessage)
        assert(capturedState?.isLoading == false)
        assert(capturedState?.showResultText == false)
        assert(capturedState?.errorMessage == errorMessage)
    }

}

