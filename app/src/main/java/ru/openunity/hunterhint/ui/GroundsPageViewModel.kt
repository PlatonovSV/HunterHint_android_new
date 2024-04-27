package ru.openunity.hunterhint.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.data.ground.GroundRepository
import ru.openunity.hunterhint.network.GroundRetrofitService
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GroundsPageViewModel @Inject constructor(private val groundsRepository: GroundRepository) : ViewModel() {

    private val _groundsPageUiState = MutableStateFlow(GroundsPageUiState())
    val groundsPageUiState: StateFlow<GroundsPageUiState> = _groundsPageUiState.asStateFlow()

    /*fun onGroundsCardClick(groundId: Int) {
        if (_groundsPageUiState.value.ground.id != groundId) {
            getGround(groundId)
        } else {
            if (_groundsPageUiState.value.state != StateE.Success) {
                _groundsPageUiState.update {
                    it.copy(state = StateE.Success)
                }
            }
        }
    }*/

    private fun getGround(groundId: Int) {
        viewModelScope.launch {
            _groundsPageUiState.update {
                it.copy(state = StateE.Loading)
            }
            _groundsPageUiState.update {
                try {
                    it.copy(
                        ground = groundsRepository.getGround(groundId),
                        numberOfCurrentImage = 0,
                        state = StateE.Success
                    )
                } catch (e: IOException) {
                    Log.e("IOException", e.toString())
                    it.copy(state = StateE.Error)
                } catch (e: HttpException) {
                    Log.e("HttpException", e.message())
                    it.copy(state = StateE.Error)
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

}