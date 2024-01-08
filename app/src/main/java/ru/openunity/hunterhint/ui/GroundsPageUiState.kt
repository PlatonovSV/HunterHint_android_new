package ru.openunity.hunterhint.ui

import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.models.Photo

data class GroundsPageUiState(
    val ground: Ground = Ground(),
    val numberOfCurrentImage: Int = 0,
    override val state: State = State.Loading
) : UiState(
    state
)