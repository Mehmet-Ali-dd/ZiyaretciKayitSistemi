package com.example.ziyaretcikayitsistemi.data.model

data class Visitor(
    var id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val hostEmployeeId: String = "",
    val reasonId: String = "",
    val status: String = "Bekliyor",
    var firmId: String = "",
    val registeredAt: Long = System.currentTimeMillis(),
    val checkInTime: Long? = null,
    val checkOutTime: Long? = null
)