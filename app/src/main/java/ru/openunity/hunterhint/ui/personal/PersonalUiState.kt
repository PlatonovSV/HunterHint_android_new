package ru.openunity.hunterhint.ui.personal

import ru.openunity.hunterhint.dto.BookingCardDto
import ru.openunity.hunterhint.models.BookingCard
import ru.openunity.hunterhint.models.database.User
import ru.openunity.hunterhint.ui.components.ComponentLoading
import ru.openunity.hunterhint.ui.components.ComponentState

data class PersonalUiState(
    val user: User = User(),
    val currentSection: PersonalSections = PersonalSections.BOOKINGS,
    val isLoggedIn: Boolean = true,
    val bookingCards: List<BookingCard> = listOf(),
    val bookingState: ComponentState = ComponentLoading
)