package ru.openunity.hunterhint.models

import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.dto.BookingCardDto
import ru.openunity.hunterhint.ui.enums.getResourcesById

data class BookingCard(
    val id: Long,
    val groundsName: String = "",
    val typeStrRes: Int = R.string.bears,
    val startDate: String,
    val finalDate: String,
) {
    companion object {
        fun fromDtoList(dtoList: List<BookingCardDto>): List<BookingCard> {
            val cards = mutableListOf<BookingCard>()
            dtoList.forEach {
                cards.add(fromDto(it))
            }
            return cards
        }

        private fun fromDto(dto: BookingCardDto): BookingCard {
            return BookingCard(
                id = dto.id,
                groundsName = dto.groundsName,
                typeStrRes = getResourcesById(dto.resourceId).stringRes,
                startDate = dto.startDate,
                finalDate = dto.finalDate
            )
        }
    }
}



