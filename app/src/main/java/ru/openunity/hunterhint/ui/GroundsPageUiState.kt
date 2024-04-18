package ru.openunity.hunterhint.ui

import ru.openunity.hunterhint.models.Ground

data class GroundsPageUiState(
    val ground: Ground = Ground(),
    val numberOfCurrentImage: Int = 0,
    override val state: StateE = StateE.Loading
) : UiStateE(
    state
)