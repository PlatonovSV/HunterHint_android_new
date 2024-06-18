package ru.openunity.hunterhint.ui.authorization

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.user.UserRepository
import ru.openunity.hunterhint.dto.AuthResponseDto
import ru.openunity.hunterhint.dto.country
import ru.openunity.hunterhint.dto.getCountryByCode
import ru.openunity.hunterhint.models.database.User
import ru.openunity.hunterhint.ui.AppError
import ru.openunity.hunterhint.ui.Loading
import ru.openunity.hunterhint.ui.Success
import ru.openunity.hunterhint.ui.components.ComponentError
import ru.openunity.hunterhint.ui.registration.Confirmation
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val minPasswordLength = 8

    private val _authUiState = MutableStateFlow(AuthUiState())
    val authUiState = _authUiState.asStateFlow()

    fun updatePhoneConfirmationCode(userInput: String) {
        _authUiState.update {
            it.copy(
                isConfirmationValid = true,
                phoneConfirmation = it.phoneConfirmation.copy(userInput = userInput)
            )
        }
    }

    fun changePhone() {
        _authUiState.update {
            it.copy(phoneConfirmation = Confirmation())
        }
    }

    fun updateCountry(countryCode: Int) {
        if (countryCode > 0) {
            _authUiState.update {
                it.copy(
                    authRequestDto = it.authRequestDto.copy(
                        countryCode = getCountryByCode(countryCode).cCode
                    )
                )
            }
        }

    }

    private fun auth() {
        val dto = _authUiState.value.authRequestDto
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = userRepository.authorization(dto)
                processAuthorizationResponse(response)
            } catch (e: IOException) {
                setErrorState(R.string.no_internet)
            } catch (e: HttpException) {
                setErrorState(R.string.server_error)
            }
        }
    }

    private fun processAuthorizationResponse(response: AuthResponseDto) {
        viewModelScope.launch(Dispatchers.IO) {
            if (response.isAuthorizationSuccessful) {

                try {
                    userRepository.insert(
                        User(
                            id = response.userId, jwt = response.jwt
                        )
                    )
                    val dto = userRepository.getUsersData(response.jwt)
                    val user = userRepository.getUser().first()!!
                    userRepository.update(User.updateWithDto(user, dto))
                    _authUiState.update {
                        AuthUiState(state = Success(result = true))
                    }
                } catch (e: IOException) {
                    _authUiState.update { it.copy(state = AppError(R.string.server_error, true)) }
                    ComponentError(R.string.server_error)
                } catch (e: HttpException) {
                    _authUiState.update { it.copy(state = AppError(R.string.no_internet, true)) }
                }
            } else {
                _authUiState.update {
                    it.copy(
                        state = AppError(R.string.wrong_password, true), isPasswordGood = false
                    )
                }
            }
        }
    }


    fun changePassword(userInput: String) {
        _authUiState.update {
            it.copy(
                authRequestDto = it.authRequestDto.copy(
                    password = userInput
                )
            )
        }
    }

    private fun setErrorState(errorCode: Int, isRepeatPossible: Boolean = true) {
        _authUiState.update {
            it.copy(
                state = AppError(errorCode, isRepeatPossible)
            )
        }
    }

    fun updatePhone(userInput: String) {
        val numberLength =
            _authUiState.value.authRequestDto.country.numberFormat.count { it == 'X' }
        if (numberLength == 0 || numberLength >= userInput.length || userInput.length <= _authUiState.value.authRequestDto.phoneNumber.length) {
            _authUiState.update {
                it.copy(
                    isPhoneStored = true, authRequestDto = it.authRequestDto.copy(
                        phoneNumber = userInput
                    )
                )
            }
        }
    }

    private fun checkIfPhoneStored() {
        runBlocking {
            _authUiState.update {
                it.copy(
                    isPhoneStored = true, state = Loading(R.string.loading)
                )
            }

            try {
                val dto = _authUiState.value.authRequestDto
                val isStored = userRepository.isPhoneStored(dto.phoneNumber, dto.countryCode)
                _authUiState.update {
                    it.copy(
                        state = Success(R.string.empty), isPhoneStored = isStored
                    )
                }
            } catch (e: IOException) {
                Log.e("IOException", e.toString())
                _authUiState.update {
                    it.copy(
                        state = AppError(R.string.no_internet, true)
                    )
                }
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString())
                _authUiState.update { it.copy(state = AppError(R.string.server_error, true)) }
            }
        }
    }

    fun requestPhoneConfirmation() {
        checkIfPhoneStored()
        if (_authUiState.value.isPhoneStored) {
            val code = createConfirmationCode()
            _authUiState.update {
                it.copy(
                    phoneConfirmation = it.phoneConfirmation.copy(
                        userInput = code, code = code, isRequested = true
                    )
                )
            }
        }
    }

    private fun createConfirmationCode(): String {
        val digit = setOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        val length = 7
        var code = ""
        repeat(length) {
            code += digit.random()
        }
        return code
    }

    fun showPassword(isPasswordShow: Boolean) {
        _authUiState.update {
            it.copy(
                isPasswordShow = isPasswordShow
            )
        }
    }

    fun onClickAuth() {
        val dto = _authUiState.value.authRequestDto
        if (dto.password.length >= minPasswordLength) {
            auth()
        } else {
            _authUiState.update {
                it.copy(
                    isPasswordGood = false
                )
            }
        }
    }

    fun checkConfirmation(): Boolean {
        val isValid = _authUiState.value.phoneConfirmation.isValid()
        _authUiState.update {
            it.copy(
                isConfirmationValid = isValid
            )
        }
        return isValid
    }

}