package ru.openunity.hunterhint.models

import ru.openunity.hunterhint.dto.ReviewDto
import ru.openunity.hunterhint.ui.components.ComponentLoading
import ru.openunity.hunterhint.ui.components.ComponentState
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

data class Review(
    val id: Long = 0,
    val dateOfCreation: String = "",
    val userName: String = "",
    val userLastName: String = "",
    val startDate: String = "",
    val review: String = "",
    val starsQuantity: Int = 5,
    val isExpanded: Boolean = false,
    val usersPhoto: Photo = Photo(),
    val state: ComponentState = ComponentLoading,
    val images: List<Photo> = listOf(),
    val numberOfCurrentImage: Int = 0
) {
    companion object {
        fun fromDto(dto: ReviewDto): Review {
            return Review(
                id = dto.id,
                dateOfCreation = OffsetDateTime.parse(dto.dateOfCreation)
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")),
                userName = dto.userName,
                userLastName = dto.userLastName,
                startDate = dto.startDate,
                review = dto.review,
                starsQuantity = dto.starsQuantity,
                usersPhoto = dto.usersPhoto,
                images = dto.images
            )
        }
    }
}
