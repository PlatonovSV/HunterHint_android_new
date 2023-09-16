package ru.openunity.hunterhint.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.openunity.hunterhint.data.GroundRep

class SearchViewModel : ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()
    init {
        findGrounds()
    }

    private fun findGrounds() {
        val grounds = GroundRep.getGroundList()
        val cards = mutableListOf<GroundCardUiState>()
        grounds.forEach {
            cards.add(it.toGroundCardUiState())
        }
        _searchUiState.value = SearchUiState(cards = cards)
    }

    fun changeImage(groundId: Int, isIncrement: Boolean) {
        val cardsNew = mutableListOf<GroundCardUiState>()
        _searchUiState.value.cards.forEach {
            if (it.id == groundId) {
                var numberOfCurrentImage = it.numberOfCurrentImage
                if (isIncrement) {
                    if (numberOfCurrentImage == it.images.size - 1) {
                        numberOfCurrentImage = 0
                    } else {
                        numberOfCurrentImage++
                    }
                } else {
                    if (numberOfCurrentImage == 0) {
                        numberOfCurrentImage = it.images.size - 1
                    } else {
                        numberOfCurrentImage--
                    }
                }
                cardsNew.add(it.copy(numberOfCurrentImage = numberOfCurrentImage))
            } else {
                cardsNew.add(it)
            }
        }
        _searchUiState.value = SearchUiState(cards = cardsNew)
    }
}