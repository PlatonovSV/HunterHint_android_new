package ru.openunity.hunterhint.models

import ru.openunity.hunterhint.dto.BookingDataDto
import ru.openunity.hunterhint.ui.enums.HuntingMethods
import ru.openunity.hunterhint.ui.enums.getMethodsById
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class BookingData(
    val id: Long = -1,
    val offerId: Long = -1,
    val bookingTime: OffsetDateTime = OffsetDateTime.MIN,
    val startDate: LocalDateTime = LocalDateTime.MIN,
    val finalDate: LocalDateTime  = LocalDateTime.MIN,
    val huntingMethod: HuntingMethods = HuntingMethods.BY_PEN,
    val bookingInfo: String = ""
) {
    companion object {
        fun fromDto(dto: BookingDataDto) =
            BookingData(
                id = dto.id,
                offerId = dto.offerId,
                bookingTime = OffsetDateTime.parse(dto.bookingTime),
                startDate = LocalDateTime.parse(dto.startDate),
                finalDate = LocalDateTime.parse(dto.finalDate),
                huntingMethod = getMethodsById(dto.huntingMethodId),
                bookingInfo = dto.bookingInfo
            )
    }
}
