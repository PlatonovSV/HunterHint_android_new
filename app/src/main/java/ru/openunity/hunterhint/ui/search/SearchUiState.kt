package ru.openunity.hunterhint.ui.search

import ru.openunity.hunterhint.models.GroundsCard
import ru.openunity.hunterhint.ui.StateE
import ru.openunity.hunterhint.ui.UiStateE

/**
 * UI state for the Search screen
 */
data class SearchUiState(
    val cards: List<GroundsCard> = listOf(),
    val groundIds: List<Int> = listOf(),
    override val state: StateE = StateE.Loading
) : UiStateE(
    state
)

