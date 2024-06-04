package ru.openunity.hunterhint.dto

data class BookingDataDto(
    val id: Long,
    val offerId: Long,
    val bookingTime: String,
    val startDate: String,
    val finalDate: String,
    val huntingMethodId: Int,
    val bookingInfo: String
)