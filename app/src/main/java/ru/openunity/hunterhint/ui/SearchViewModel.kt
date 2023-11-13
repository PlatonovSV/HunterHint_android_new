package ru.openunity.hunterhint.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.HunterHintApplication
import ru.openunity.hunterhint.data.GroundsRepository
import ru.openunity.hunterhint.models.GroundsCard
import java.io.IOException


class SearchViewModel(private val groundsRepository: GroundsRepository) : ViewModel() {
    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState: StateFlow<SearchUiState> = _searchUiState.asStateFlow()

    /**
     * Call getGrounds() on init so we can display status immediately.
     */
    init {
        getGrounds()
    }


    fun getGrounds() {
        viewModelScope.launch {
            _searchUiState.update {
                it.copy(state = State.Loading)
            }
            _searchUiState.update {
                try {
                    val groundIds = groundsRepository.getIdsOfAllGrounds()
                    val stringBuffer = StringBuffer()
                    groundIds.forEach { id ->
                        stringBuffer.append(id)
                        stringBuffer.append('-')
                    }
                    stringBuffer.deleteCharAt(stringBuffer.lastIndex)
                    val cards = groundsRepository.getListOfGroundsPreview(stringBuffer.toString())
                    it.copy(
                        cards = cards,
                        groundIds = groundIds,
                        state = State.Success
                    )
                } catch (e: IOException) {
                    Log.e("IOException", e.toString())
                    it.copy(state = State.Error)
                } catch (e: HttpException) {
                    Log.e("HttpException", e.message())
                    it.copy(state = State.Error)
                }
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

    /**
     * Factory for [SearchViewModel] that takes [GroundsRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HunterHintApplication)
                val groundsRepository = application.container.repository
                SearchViewModel(groundsRepository = groundsRepository)
            }
        }
    }
}