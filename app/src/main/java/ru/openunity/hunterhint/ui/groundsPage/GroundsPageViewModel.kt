package ru.openunity.hunterhint.ui.groundsPage

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
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.ground.GroundRepository
import ru.openunity.hunterhint.data.offer.FindOffersParams
import ru.openunity.hunterhint.data.offer.OfferRepository
import ru.openunity.hunterhint.ui.StateE
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class GroundsPageViewModel @Inject constructor(
    private val groundsRepository: GroundRepository,
    private val offerRepository: OfferRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GroundsPageUiState())
    val uiState: StateFlow<GroundsPageUiState> = _uiState.asStateFlow()

    fun onGroundsCardClick(groundId: Int) {
        if (_uiState.value.ground.id != groundId) {
            getGround(groundId)
        } else {
            if (_uiState.value.state != StateE.Success) {
                _uiState.update {
                    it.copy(state = StateE.Success)
                }
            }
        }
    }

    private fun getGround(groundsId: Int) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    state = StateE.Loading,
                    offersParams = FindOffersParams(groundsId = groundsId)
                )
            }
            _uiState.update {
                try {
                    it.copy(
                        ground = groundsRepository.getGround(groundsId),
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
            if (_uiState.value.state == StateE.Success) {
                getOffers()
            }
        }
    }

    fun getOffers() {
        _uiState.update {
            it.copy(
                offers = OffersLoading
            )
        }
        viewModelScope.launch {
            val params = _uiState.value.offersParams
            var result: OffersState
            try {
                val offers = offerRepository.findOffers(params.getParams())
                result = OffersSuccess(offers)
            } catch (e: IOException) {
                result = OffersError(R.string.server_error)
            } catch (e: HttpException) {
                result = OffersError(R.string.no_internet)
            }
            _uiState.update {
                it.copy(
                    offers = result
                )
            }
        }
    }


    fun changeImage(increment: Boolean) {
        _uiState.update {
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

    /*

   получение оферов - get c jgwbjyfkmysvb gfhfvtnhfv
     */

    fun addToFavorite() {
        TODO("Not yet implemented")
    }

}