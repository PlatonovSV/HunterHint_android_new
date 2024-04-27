package ru.openunity.hunterhint.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.data.ground.GroundRepository
import ru.openunity.hunterhint.models.GroundsCard
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(private val groundsRepository: GroundRepository) :
    ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    /**
     * Call getGrounds() on init so we can display status immediately.
     */
    init {
        getGrounds()
    }

    fun getGrounds() {
        viewModelScope.launch(context = Dispatchers.IO) {
            _searchUiState.update { it.copy(state = StateE.Loading) }

            try {
                val groundIds = groundsRepository.getIdsOfAllGrounds()
                val idsString = groundIds.joinToString(separator = "-")

                val cards = groundsRepository.getListOfGroundsPreview(idsString)

                _searchUiState.update {
                    it.copy(cards = cards, groundIds = groundIds, state = StateE.Success)
                }
            } catch (e: IOException) {
                Log.e("IOException", e.toString())
                _searchUiState.update { it.copy(state = StateE.Error) }
            } catch (e: HttpException) {
                Log.e("HttpException", e.message())
                _searchUiState.update { it.copy(state = StateE.Error) }
            }
        }
    }

    fun changeImage(groundId: Int, isIncrement: Boolean) {
        val cardsNew = mutableListOf<GroundsCard>()
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
        _searchUiState.value = SearchUiState(
            cards = cardsNew,
            groundIds = _searchUiState.value.groundIds,
            state = _searchUiState.value.state
        )
    }
}