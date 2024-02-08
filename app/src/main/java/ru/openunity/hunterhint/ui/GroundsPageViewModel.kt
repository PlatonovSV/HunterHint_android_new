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
import java.io.IOException

class GroundsPageViewModel(private val groundsRepository: GroundsRepository) : ViewModel() {

    private val _groundsPageUiState = MutableStateFlow(GroundsPageUiState())
    val groundsPageUiState: StateFlow<GroundsPageUiState> = _groundsPageUiState.asStateFlow()

    fun onGroundsCardClick(groundId: Int) {
        if (_groundsPageUiState.value.ground.id != groundId) {
            getGround(groundId)
        } else {
            if (_groundsPageUiState.value.state != State.Success) {
                _groundsPageUiState.update {
                    it.copy(state = State.Success)
                }
            }
        }
    }

    private fun getGround(groundId: Int) {
        viewModelScope.launch {
            _groundsPageUiState.update {
                it.copy(state = State.Loading)
            }
            _groundsPageUiState.update {
                try {
                    it.copy(
                        ground = groundsRepository.getGround(groundId),
                        numberOfCurrentImage = 0,
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


    fun changeImage(increment: Boolean) {
        _groundsPageUiState.update {
            val lastImage = it.ground.images.lastIndex
            val current = it.numberOfCurrentImage
            it.copy(
                numberOfCurrentImage = when {
                    current < lastImage && increment -> current.inc()
                    current == lastImage && increment -> 0
                    current == 0 -> lastImage
                    else -> current.dec()
                }
            )
        }
    }

    fun addToFavorite() {
        TODO("Not yet implemented")
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HunterHintApplication)
                val groundsRepository = application.container.repository
                GroundsPageViewModel(groundsRepository = groundsRepository)
            }
        }
    }
}