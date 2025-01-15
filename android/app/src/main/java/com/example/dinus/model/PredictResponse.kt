package com.example.dinus.model

data class PredictResponse(
    val mood: Mood,
    val text: TextResponse
)