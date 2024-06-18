package ru.openunity.hunterhint.ui.groundsPage

import ru.openunity.hunterhint.data.offer.FindOffersParams
import ru.openunity.hunterhint.dto.UserCardDto
import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.models.Review
import ru.openunity.hunterhint.ui.StateE
import ru.openunity.hunterhint.ui.UiStateE
import ru.openunity.hunterhint.ui.components.ComponentLoading
import ru.openunity.hunterhint.ui.components.ComponentState

data class GroundsPageUiState(
    val ground: Ground = Ground(),
    val offers: OffersState = OffersLoading,
    val offersParams: FindOffersParams = FindOffersParams(),
    val numberOfCurrentImage: Int = 0,
    val isAuthorized: Boolean = false,
    val reviewsListState: ComponentState = ComponentLoading,
    val reviews: List<Review> = listOf(),
    val groundsOwner: UserCardDto = UserCardDto(),
    override val state: StateE = StateE.Loading
) : UiStateE(
    state
)