package ru.openunity.hunterhint.dto

import java.time.LocalDateTime;

data class BookingDto(
    val id: Long,
    val offerId: Long,
    val userId: Long,
    val bookingData: String,
    val startDate: LocalDateTime,
    val finalDate: LocalDateTime
)