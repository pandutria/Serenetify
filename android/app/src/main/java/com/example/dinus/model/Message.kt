package com.example.dinus.model

data class Message(
    val id: Int = 0,
    val sender_id: Int = 0,
    val receiver_id: Int = 0,
    val message: String = "",
    val timestamp: String = ""
)