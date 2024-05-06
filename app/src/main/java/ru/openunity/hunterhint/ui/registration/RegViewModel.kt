package ru.openunity.hunterhint.ui.registration

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.user.UserRepository
import ru.openunity.hunterhint.dto.UserRegDto
import ru.openunity.hunterhint.dto.birthMonth
import ru.openunity.hunterhint.dto.country
import ru.openunity.hunterhint.ui.AppError
import ru.openunity.hunterhint.ui.Loading
import ru.openunity.hunterhint.ui.Success
import java.io.IOException
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class RegViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RegUiState())
    val regUiState: StateFlow<RegUiState> = _uiState.asStateFlow()


    var userMonth by mutableIntStateOf(R.string.month)
        private set
    var userGender by mutableIntStateOf(R.string.gender)
        private set


    fun requestRegistration() {
        viewModelScope.launch(context = Dispatchers.IO) {
            _uiState.update { it.copy(state = Loading(R.string.loading)) }

            try {
                val userId = repository.createUser(_uiState.value.userRegDto)
                _uiState.update {
                    when (userId) {
                        -2L -> {
                            it.copy(
                                state = AppError(R.string.server_error, true),
                            )
                        }

                        -1L -> {
                            it.copy(
                                state = AppError(R.string.server_denied, false),
                            )
                        }

                        else -> {
                            it.copy(
                                state = Success(R.string.registration_completed_successfully),
                                userRegDto = UserRegDto()
                            )
                        }
                    }
                }
            } catch (e: IOException) {
                Log.e("IOException", e.toString())
                _uiState.update { it.copy(state = AppError(R.string.server_error, true)) }
            } catch (e: HttpException) {
                Log.e("HttpException", e.message())
                _uiState.update { it.copy(state = AppError(R.string.no_internet, true)) }
            }
        }
    }

    private fun checkIfPhoneStored() {
        runBlocking {
            _uiState.update {
                it.copy(
                    isPhoneStored = true,
                    state = Loading(R.string.loading)
                )
            }

            try {
                val dto = _uiState.value.userRegDto
                val isStored = repository.isPhoneStored(dto.phoneNumber, dto.countryCode)
                _uiState.update {
                    it.copy(
                        state = Success(R.string.empty),
                        isPhoneStored = isStored
                    )
                }
            } catch (e: IOException) {
                Log.e("IOException", e.toString())
                _uiState.update {
                    it.copy(
                        state = AppError(R.string.no_internet, true)
                    )
                }
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString())
                _uiState.update { it.copy(state = AppError(R.string.server_error, true)) }
            }
        }
    }

    private fun checkIfEmailStored() {
        runBlocking {
            _uiState.update {
                it.copy(
                    isEmailStored = true,
                    state = Loading(R.string.loading)
                )
            }

            try {
                val dto = _uiState.value.userRegDto
                val isStored = repository.isEmailStored(dto.email)
                _uiState.update {
                    it.copy(
                        state = Success(R.string.empty),
                        isEmailStored = isStored
                    )
                }
            } catch (e: IOException) {
                Log.e("IOException", e.toString())
                _uiState.update {
                    it.copy(
                        state = AppError(R.string.no_internet, true)
                    )
                }
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString())
                _uiState.update { it.copy(state = AppError(R.string.server_error, true)) }
            }
        }
    }

    fun updatePhone(userInput: String) {
        val numberLength = _uiState.value.userRegDto.country.numberFormat.count { it == 'X' }
        if (numberLength == 0 || numberLength >= userInput.length || userInput.length <=
            _uiState.value.userRegDto.phoneNumber.length
        ) {
            _uiState.update {
                it.copy(
                    isPhoneStored = false,
                    isPhoneWrong = false,
                    userRegDto = it.userRegDto.copy(
                        phoneNumber = userInput
                    )
                )
            }
        }
    }

    private fun isPhoneCorrect(): Boolean {
        val userInputLength = _uiState.value.userRegDto.phoneNumber.length
        val numberLength = _uiState.value.userRegDto.country.numberFormat.count { it == 'X' }
        val isWrong = (numberLength == 0 && userInputLength <= 3) ||
                (numberLength != userInputLength)
        _uiState.update {
            it.copy(
                isPhoneWrong = isWrong,
            )
        }
        return !isWrong
    }


    fun updateUserName(userInput: String) {
        _uiState.update {
            it.copy(
                isNameCorrect = true,
                userRegDto = it.userRegDto.copy(
                    name = userInput
                )
            )
        }
    }

    fun updateCountry(country: Country) {
        _uiState.update {
            it.copy(
                userRegDto = it.userRegDto.copy(
                    countryCode = country.cCode
                )
            )
        }
    }

    fun updateUserLastName(userInput: String) {
        _uiState.update {
            it.copy(
                isLastNameCorrect = true,
                userRegDto = it.userRegDto.copy(
                    lastName = userInput
                )
            )
        }
    }

    fun updateUserMonth(month: Month) {
        userMonth = month.stringResourceId
        _uiState.update {
            it.copy(
                userRegDto = it.userRegDto.copy(
                    birthMonthCode = month.mCode
                )
            )
        }
        dismissDialogs()
    }

    fun updateUserDay(userInput: String) {
        val day = userInput.toIntOrNull()
        if (day != null && day <= 99) {
            _uiState.update {
                it.copy(
                    userRegDto = it.userRegDto.copy(
                        birthDay = day
                    )
                )
            }
        }
    }

    fun updateUserYear(userInput: String) {
        val year = userInput.toIntOrNull()
        if (year != null && year <= 9999) {
            _uiState.update {
                it.copy(
                    userRegDto = it.userRegDto.copy(
                        birthYear = year
                    )
                )
            }
        }
    }

    fun updateUserGender(gender: Gender) {
        userGender = gender.stringResourceId
        _uiState.update {
            it.copy(
                userRegDto = it.userRegDto.copy(
                    genderCode = gender.gCode
                )
            )
        }
        dismissDialogs()
    }

    fun changeEmail(userInput: String) {
        _uiState.update {
            it.copy(
                isEmailCorrect = true,
                userRegDto = it.userRegDto.copy(
                    email = userInput
                )
            )
        }
    }

    fun updatePassword(userInput: String) {
        _uiState.update {
            it.copy(
                userRegDto = it.userRegDto.copy(
                    password = userInput
                )
            )
        }
    }

    fun updateEmailConfirmationCode(userInput: String) {
        _uiState.update {
            it.copy(
                emailConfirmation = it.emailConfirmation.copy(userInput = userInput)
            )
        }
    }

    fun updatePhoneConfirmationCode(userInput: String) {
        _uiState.update {
            it.copy(
                phoneConfirmation = it.phoneConfirmation.copy(userInput = userInput)
            )
        }
    }

    fun showGenderDialog() {
        _uiState.update {
            it.copy(isGenderDialogShow = true)
        }
    }

    fun showMonthDialog() {
        _uiState.update {
            it.copy(isMonthDialogShow = true)
        }
    }

    fun dismissDialogs() {
        _uiState.update {
            it.copy(isMonthDialogShow = false, isGenderDialogShow = false)
        }
    }

    private fun checkName() {
        _uiState.update {
            it.copy(isNameCorrect = (_uiState.value.userRegDto.name.length > 3))
        }
    }

    private fun checkLastName() {
        _uiState.update {
            it.copy(isLastNameCorrect = (_uiState.value.userRegDto.lastName.length > 3))
        }
    }

    fun isNameCorrect(): Boolean {
        checkLastName()
        checkName()
        return regUiState.value.isLastNameCorrect && regUiState.value.isNameCorrect
    }

    fun checkBirthday(): Boolean {
        val day = _uiState.value.userRegDto.birthDay
        val year = _uiState.value.userRegDto.birthYear
        val month = _uiState.value.userRegDto.birthMonth
        val complete =
            userMonth in Month.entries.map { it.stringResourceId }
        val correct = if (complete) {
            day in 1..month.days && year > 1900
        } else {
            false
        }
        _uiState.update {
            it.copy(
                isBirthdayComplete = complete, isBirthdayCorrect = correct
            )
        }
        return complete && correct
    }

    fun checkGender(): Boolean {
        val isGenderSpecified = userGender in Gender.entries.map { it.stringResourceId }
        _uiState.update { uiState ->
            uiState.copy(
                isGenderSpecified = isGenderSpecified
            )
        }
        return isGenderSpecified
    }

    private fun isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(_uiState.value.userRegDto.email)
            .matches()
    }

    fun requestEmailConfirmation() {
        checkIfEmailStored()
        if (isEmailValid() && !_uiState.value.isEmailStored) {
            val code = createConfirmationCode()
            _uiState.update {
                it.copy(
                    emailConfirmation = it.emailConfirmation.copy(
                        userInput = code,
                        code = code,
                        isRequested = true
                    )
                )
            }
        }
    }

    fun requestPhoneConfirmation() {
        checkIfPhoneStored()
        if (isPhoneCorrect() && !_uiState.value.isPhoneStored) {
            val code = createConfirmationCode()
            _uiState.update {
                it.copy(
                    phoneConfirmation = it.phoneConfirmation.copy(
                        userInput = code,
                        code = code,
                        isRequested = true
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

    fun updateEmail() {
        _uiState.update {
            it.copy(emailConfirmation = Confirmation())
        }
    }

    fun changePhone() {
        _uiState.update {
            it.copy(phoneConfirmation = Confirmation())
        }
    }

    fun showPassword(isPasswordShow: Boolean) {
        _uiState.update {
            it.copy(
                isPasswordShow = isPasswordShow
            )
        }
    }

    fun validatePassword(): Boolean {
        val isMatch =
            Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
                .matcher(_uiState.value.userRegDto.password).find()
        if (regUiState.value.isPasswordStrong != isMatch) {
            _uiState.update {
                it.copy(isPasswordStrong = isMatch)
            }
        }
        return isMatch
    }

    fun onRegCompete() {
        clearData()
    }

    private fun clearData() {
        _uiState.update {
            RegUiState()
        }
    }
}

data class Confirmation(
    val userInput: String = "",
    private val code: String = "",
    val isRequested: Boolean = false
) {
    fun isValid(): Boolean = isRequested && userInput == code
    fun isCompete(): Boolean = isRequested && userInput.length == code.length
}

