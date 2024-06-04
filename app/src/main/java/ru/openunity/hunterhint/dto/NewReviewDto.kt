package ru.openunity.hunterhint.dto

data class NewReviewDto(
    val images: List<String> = listOf(),
    val bookingId: Long = -1L,
    val rating: Int = 5,
    val feedback: String = ""
)