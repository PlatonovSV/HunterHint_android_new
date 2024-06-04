package ru.openunity.hunterhint.dto

data class BookingCardDto(
    val id: Long,
    val startDate: String,
    val finalDate: String,
    val resourceId: Int,
    val groundsName: String
)

