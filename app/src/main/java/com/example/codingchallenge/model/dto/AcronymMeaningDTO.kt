package com.example.codingchallenge.model.dto

@kotlinx.serialization.Serializable
data class AcronymMeaningDTO(
    val sf: String = "",
    val lfs: List<MeaningDTO> = emptyList()
)
