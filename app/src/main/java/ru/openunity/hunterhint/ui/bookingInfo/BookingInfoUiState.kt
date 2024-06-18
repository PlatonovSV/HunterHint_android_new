package ru.openunity.hunterhint.ui.bookingInfo

import ru.openunity.hunterhint.dto.UserCardDto
import ru.openunity.hunterhint.models.BookingData
import ru.openunity.hunterhint.models.GroundsCard
import ru.openunity.hunterhint.models.HuntingOffer
import ru.openunity.hunterhint.ui.components.ComponentLoading
import ru.openunity.hunterhint.ui.components.ComponentState

data class BookingInfoUiState(
    val groundsCardState: ComponentState = ComponentLoading,
    val groundsCard: GroundsCard = GroundsCard(),
    val groundsOwner: UserCardDto = UserCardDto(),

    val offersCardState: ComponentState = ComponentLoading,
    val offer: HuntingOffer = HuntingOffer(),

    val bookingCardState: ComponentState = ComponentLoading,
    val booking: BookingData = BookingData(),

    val bookingId: Long = -1
)
