package com.example.ziyaretcikayitsistemi.data.model

data class VisitorWithDetails(
    val visitorId: String,
    val visitorFirstName: String,
    val visitorLastName: String,
    val status: String,
    val hostFirstName: String,
    val hostLastName: String,
    val reasonName: String,
    val registeredAt: Long
)