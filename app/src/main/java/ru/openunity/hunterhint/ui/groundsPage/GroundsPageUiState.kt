package ru.openunity.hunterhint.ui.groundsPage

import ru.openunity.hunterhint.data.offer.FindOffersParams
import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.ui.StateE
import ru.openunity.hunterhint.ui.UiStateE

data class GroundsPageUiState(
    val ground: Ground = Ground(),
    val offers: OffersState = OffersLoading,
    val offersParams: FindOffersParams = FindOffersParams(),
    val numberOfCurrentImage: Int = 0,
    val isAuthorized: Boolean = false,
    override val state: StateE = StateE.Loading
) : UiStateE(
    state
)