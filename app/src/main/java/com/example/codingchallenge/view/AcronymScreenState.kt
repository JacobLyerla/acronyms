package com.example.codingchallenge.view

import com.example.acronym.data.util.AcronymResult
import com.example.codingchallenge.model.entities.Meaning

data class AcronymScreenState(
    val isLoading: Boolean = false,
    val response: AcronymResult<List<Meaning>> = AcronymResult.Empty,
    val showResultText: Boolean = false,
    val errorTextId: Int? = null,
    val errorMessage: String? = null

)
