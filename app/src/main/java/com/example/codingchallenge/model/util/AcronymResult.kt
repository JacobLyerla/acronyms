package com.example.acronym.data.util

sealed class AcronymResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : AcronymResult<T>()
    data class Error(val message: String?) : AcronymResult<Nothing>()
    object Empty : AcronymResult<Nothing>()
    object Loading : AcronymResult<Nothing>()
    object Initial : AcronymResult<Nothing>()
}

