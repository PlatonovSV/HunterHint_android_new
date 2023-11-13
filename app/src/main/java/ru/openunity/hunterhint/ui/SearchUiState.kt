package ru.openunity.hunterhint.ui

import ru.openunity.hunterhint.models.GroundsCard

/**
 * UI state for the Search screen
 */
data class SearchUiState(
    val cards: List<GroundsCard> = listOf(),
    val groundIds: List<Int> = listOf(),
    override val state: State = State.Loading
) : UiState(
    state
)

