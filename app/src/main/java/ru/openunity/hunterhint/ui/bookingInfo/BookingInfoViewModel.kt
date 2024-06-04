package ru.openunity.hunterhint.ui.bookingInfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.booking.BookingRepository
import ru.openunity.hunterhint.data.ground.GroundRepository
import ru.openunity.hunterhint.data.offer.OfferRepository
import ru.openunity.hunterhint.data.user.UserRepository
import ru.openunity.hunterhint.models.BookingData
import ru.openunity.hunterhint.ui.components.ComponentError
import ru.openunity.hunterhint.ui.components.ComponentLoading
import ru.openunity.hunterhint.ui.components.ComponentSuccess
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class BookingInfoViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val groundRepository: GroundRepository,
    private val offerRepository: OfferRepository,
    private val bookingRepository: BookingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BookingInfoUiState())
    val uiState = _uiState.asStateFlow()

    fun setBooking(id: Long) {
        _uiState.update {
            BookingInfoUiState(bookingId = id)
        }
        downloadBooking()
    }

    fun downloadBooking() {
        val id = _uiState.value.bookingId
        _uiState.update {
            it.copy(
                bookingCardState = ComponentLoading
            )
        }
        viewModelScope.launch {
            val jwt = userRepository.getUser().first()?.jwt ?: ""
            try {
                val bookingDto = bookingRepository.getBooking(jwt, id)
                val booking = BookingData.fromDto(bookingDto)
                _uiState.update {
                    it.copy(
                        booking = booking, bookingCardState = ComponentSuccess
                    )
                }
                downloadOffer(booking.offerId)
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        bookingCardState = ComponentError(R.string.server_error)
                    )
                }
            } catch (e: HttpException) {
                _uiState.update {
                    it.copy(
                        bookingCardState = ComponentError(R.string.no_internet)
                    )
                }
            }
        }
    }

    private fun downloadOffer(id: Long) {
        _uiState.update {
            it.copy(
                offersCardState = ComponentLoading
            )
        }
        viewModelScope.launch {
            try {
                val dto = offerRepository.getOffer(id)
                _uiState.update {
                    it.copy(
                        offer = dto,
                        offersCardState = ComponentSuccess
                    )
                }
                downloadGroundsCard(dto.groundsId)
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        offersCardState = ComponentError(R.string.server_error)
                    )
                }
            } catch (e: HttpException) {
                _uiState.update {
                    it.copy(
                        offersCardState = ComponentError(R.string.no_internet)
                    )
                }
            }
        }
    }

    private fun downloadGroundsCard(id: Int) {
        _uiState.update {
            it.copy(
                groundsCardState = ComponentLoading
            )
        }
        viewModelScope.launch {
            try {
                val dto = groundRepository.getListOfGroundsPreview(listOf(id)).first()
                _uiState.update {
                    it.copy(
                        groundsCardState = ComponentSuccess,
                        groundsCard = dto
                    )
                }
            } catch (e: IOException) {
                _uiState.update {
                    it.copy(
                        groundsCardState = ComponentError(R.string.server_error)
                    )
                }
            } catch (e: HttpException) {
                _uiState.update {
                    it.copy(
                        groundsCardState = ComponentError(R.string.no_internet)
                    )
                }
            }
        }
    }

    fun changeImage(isIncrement: Boolean) {
        val images = uiState.value.groundsCard.images
        var numberOfCurrentImage = uiState.value.groundsCard.numberOfCurrentImage
        if (isIncrement) {
            if (numberOfCurrentImage == images.size - 1) {
                numberOfCurrentImage = 0
            } else {
                numberOfCurrentImage++
            }
        } else {
            if (numberOfCurrentImage == 0) {
                numberOfCurrentImage = images.size - 1
            } else {
                numberOfCurrentImage--
            }
        }
        _uiState.update {
            it.copy(
                groundsCard = it.groundsCard.copy(
                    numberOfCurrentImage = numberOfCurrentImage
                )
            )
        }

    }
}
