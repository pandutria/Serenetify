package com.example.dinus.model

data class Event(
    val id: Int = 0,
    val name: String = "",
    val organizer: String = "",
    val location: String = "",
    val price: String = "",
    val admin_id: Int = 0,
    val image_url: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val city: String = "",
    val admin: User = User()
)

