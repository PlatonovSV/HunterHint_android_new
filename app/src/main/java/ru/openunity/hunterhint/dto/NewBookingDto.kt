package ru.openunity.hunterhint.dto

data class NewBookingDto(
    val offerId: Long,
    val bookingData: String,
    val startDate: String,
    val finalDate: String,
    val methodId: Int
)