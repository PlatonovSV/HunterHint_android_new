package ru.openunity.hunterhint.ui.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.offer.OfferRepository
import ru.openunity.hunterhint.ui.registration.Month
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val offerRepository: OfferRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState = _uiState.asStateFlow()


    fun updateStartDay(userInput: String) {
        _uiState.update {
            it.copy(
                startDay = it.startDay.updateDay(userInput),
                isShowStartError = false
            )
        }
    }

    fun updateStartMonth(userInput: Month) {
        _uiState.update {
            it.copy(
                startDay = it.startDay.updateMonth(userInput),
                isShowStartError = false
            )
        }
        dismissDialogs()
    }

    fun updateStartYear(userInput: String) {
        _uiState.update {
            it.copy(
                startDay = it.startDay.updateYear(userInput),
                isShowStartError = false
            )
        }
    }


    fun updateFinalDay(userInput: String) {
        _uiState.update {
            it.copy(
                finalDay = it.finalDay.updateDay(userInput),
                isShowFinalError = false
            )
        }
    }

    fun updateFinalMonth(userInput: Month) {
        _uiState.update {
            it.copy(
                finalDay = it.finalDay.updateMonth(userInput),
                isShowFinalError = false
            )
        }
        dismissDialogs()
    }


    fun updateFinalYear(userInput: String) {
        _uiState.update {
            it.copy(
                finalDay = it.finalDay.updateYear(userInput),
                isShowFinalError = false
            )
        }
    }

    fun showStartMonthDialog() {
        _uiState.update {
            it.copy(
                isStartMonthDialogShow = true,
            )
        }
    }

    fun showFinalMonthDialog() {
        _uiState.update {
            it.copy(
                isFinalMonthDialogShow = true,
            )
        }
    }

    fun dismissDialogs() {
        _uiState.update {
            it.copy(isStartMonthDialogShow = false, isFinalMonthDialogShow = false)
        }
    }

    fun nextSection() {
        val next =
            if (_uiState.value.currentSection.ordinal == BookingSections.entries.lastIndex) {
                0
            } else {
                _uiState.value.currentSection.ordinal.inc()
            }
        _uiState.update {
            it.copy(
                currentSection = BookingSections.entries[next]
            )
        }
    }

    fun changeSection(bookingSections: BookingSections) {
        _uiState.update {
            it.copy(
                currentSection = bookingSections
            )
        }
    }

    fun getOffer() {
        getOffer(_uiState.value.offerId)
    }

    fun getOffer(id: Long) {
        _uiState.update {
            it.copy(
                offerId = id,
                offer = OfferLoading
            )
        }
        viewModelScope.launch {
            var result: OfferState
            try {
                val offer = offerRepository.getOffer(id)
                result = OfferSuccess(offer)
            } catch (e: IOException) {
                result = OfferError(R.string.server_error)
            } catch (e: HttpException) {
                result = OfferError(R.string.no_internet)
            }
            _uiState.update {
                it.copy(
                    offer = result
                )
            }
        }
    }


}

