package ru.openunity.hunterhint.dto

import ru.openunity.hunterhint.models.Photo

data class ReviewDto(
    val id: Long = 0,
    val dateOfCreation: String = "",
    val userName: String = "",
    val userLastName: String = "",
    val startDate: String = "",
    val review: String = "",
    val starsQuantity: Int = 5,
    val usersPhoto: Photo = Photo(),
    val images: List<Photo> = listOf()
)
