package com.example.dinus.model

data class TestDetection(
    val question: String = "",
    val answers: Map<String, Answer> = emptyMap()
)