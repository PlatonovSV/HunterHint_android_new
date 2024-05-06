package ru.openunity.hunterhint.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.ground.GroundRepository
import ru.openunity.hunterhint.models.GroundsCard
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(private val groundsRepository: GroundRepository) :
    ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    /**
     * Call getGrounds() on init so we can display status immediately.
     */
    init {
        getGroundIds()
    }

    fun getGroundIds() {
        _uiState.update {
            it.copy(
                groundIds = IdsLoading,
                cards = CardsLoading()
            )
        }
        viewModelScope.launch {
            var result: IdsState
            try {
                val groundIds = groundsRepository.getIdsOfAllGrounds()
                result = IdsSuccess(groundIds)
            } catch (e: IOException) {
                result = IdsError(R.string.server_error)
            } catch (e: HttpException) {
                result = IdsError(R.string.no_internet)
            }
            _uiState.update {
                it.copy(
                    groundIds = result
                )
            }
            getGrounds()
        }
    }

    fun getGrounds() {
        if (_uiState.value.groundIds is IdsSuccess) {
            getGrounds((_uiState.value.groundIds as IdsSuccess).ids)
        }
    }

    private fun getGrounds(groundsId: List<Int>) {
        _uiState.update {
            it.copy(
                cards = CardsLoading()
            )
        }
        viewModelScope.launch {
            var result: CardsState
            try {
                val cards = groundsRepository.getListOfGroundsPreview(groundsId)
                result = CardsSuccess(cards)
            } catch (e: IOException) {
                result = CardsError(messageId = R.string.server_error)
            } catch (e: HttpException) {
                result = CardsError(messageId = R.string.no_internet)
            }
            _uiState.update {
                it.copy(
                    cards = result
                )
            }
        }
    }

    fun changeImage(groundId: Int, isIncrement: Boolean) {
        val cards = _uiState.value.cards
        val cardsNew = mutableListOf<GroundsCard>()
        if (cards is CardsSuccess) {
            cards.cards.forEach {
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
        }
        if (_uiState.value.cards is CardsSuccess) {
            _uiState.update {
                it.copy(
                    cards = CardsSuccess(cardsNew)
                )
            }
        }
    }
}