package com.example.dinus.model

data class ChatMessage(
    val sender_id: Int = 0,
    val receiver_id: Int = 0,
    val message: String = ""
)
