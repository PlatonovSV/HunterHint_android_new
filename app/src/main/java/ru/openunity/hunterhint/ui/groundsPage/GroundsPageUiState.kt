package ru.openunity.hunterhint.ui.groundsPage

import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.ui.StateE
import ru.openunity.hunterhint.ui.UiStateE

data class GroundsPageUiState(
    val ground: Ground = Ground(),
    val numberOfCurrentImage: Int = 0,
    override val state: StateE = StateE.Loading
) : UiStateE(
    state
)