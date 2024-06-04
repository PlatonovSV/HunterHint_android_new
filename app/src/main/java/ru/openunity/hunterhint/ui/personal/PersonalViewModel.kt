package ru.openunity.hunterhint.ui.personal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.openunity.hunterhint.data.booking.BookingRepository
import ru.openunity.hunterhint.data.user.UserRepository
import ru.openunity.hunterhint.models.BookingCard
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(
    private val userRepository: UserRepository, private val bookingRepository: BookingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PersonalUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getUser().collect {
                _uiState.update { ui ->
                    if (it == null) {
                        ui.copy(
                            isLoggedIn = false
                        )
                    } else {
                        ui.copy(
                            user = it
                        )
                    }
                }
            }
        }
        viewModelScope.launch {
            while (true) {
                val user = _uiState.value.user
                if (user.jwt.isNotEmpty()) {
                    val bookingCards = try {
                        bookingRepository.getUsersBooking(
                            token = user.jwt
                        ).sortedBy { it.id }
                    } catch (e: IOException) {
                        listOf()
                    } catch (e: HttpException) {
                        listOf()
                    }
                    if (bookingCards.isNotEmpty() && _uiState.value.bookingCards != bookingCards) {
                        _uiState.update {
                            it.copy(
                                bookingCards = BookingCard.fromDtoList(bookingCards)
                            )
                        }
                    }
                }
                delay(3000)
            }
        }
    }

    fun changeSection(personalSections: PersonalSections) {
        _uiState.update {
            it.copy(
                currentSection = personalSections
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.delete()
        }
    }

}