package com.example.codingchallenge.model.dto

@kotlinx.serialization.Serializable
data class MeaningDTO(
    val lf: String,
    val freq: Int,
    val since: Int
)
