package com.example.codingchallenge.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.acronym.data.util.AcronymResult
import com.example.codingchallenge.R
import com.example.codingchallenge.model.AcronymRepository
import com.example.codingchallenge.view.AcronymScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AcronymViewModel @Inject constructor(private val repo: AcronymRepository) : ViewModel() {
    private val _state = MutableLiveData(AcronymScreenState())
    val state: LiveData<AcronymScreenState> get() = _state

    var input = MutableLiveData("")

    private val handler = CoroutineExceptionHandler { _, throwable ->
        val errorTextId = when (throwable) {
            is SerializationException -> R.string.serialization
            is IOException -> R.string.io
            is HttpException -> R.string.http
            else -> R.string.general_exception
        }
        val errorMessage = throwable.localizedMessage
        _state.value = _state.value?.copy(
            isLoading = false,
            showResultText = false,
            errorTextId = errorTextId,
            errorMessage = errorMessage
        )
    }

    fun getWordsFromAcronym() = viewModelScope.launch(handler) {
        _state.value = AcronymScreenState()
        _state.value = AcronymScreenState(isLoading = true)
        val result = repo.getWordsFromAcronym(input.value!!.replace(" ", "").trim())

        _state.value = when (result) {
            is AcronymResult.Success -> {
                _state.value?.copy(isLoading = false, response = result, showResultText = true)
            }

            is AcronymResult.Error -> {
                _state.value?.copy(
                    isLoading = false,
                    response = result,
                    showResultText = false,
                    errorMessage = result.message
                )
            }

            is AcronymResult.Empty,
            is AcronymResult.Initial,
            is AcronymResult.Loading -> {
                _state.value?.copy(isLoading = false, response = result, showResultText = false)
            }
        }
        input.value = ""
    }

    fun resultCount(): String {
        val count = (_state.value?.response as? AcronymResult.Success)?.data?.size ?: 0
        return if (count > 0) {
            "$count results"
        } else {
            "No results"
        }
    }
}

