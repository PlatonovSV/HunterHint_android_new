package ru.openunity.hunterhint.ui.search

/**
 * UI state for the Search screen
 */
data class SearchUiState(
    val cards: CardsState = CardsLoading(),
    val groundIds: IdsState = IdsLoading
)

