package com.example.dinus.model

data class CommunityMessage(
    val sender_id: Int,
    val community_id: Int,
    val message: String,
    val timestamp: String,
    val sender: User = User()
)
