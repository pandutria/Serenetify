package com.example.dinus.model

data class DoctorMessege(
    val sender_id: Int = 0,
    val receiver_id: Int,
    val message: String,
    val timestamp: String

)
