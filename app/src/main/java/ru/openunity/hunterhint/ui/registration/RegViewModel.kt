package ru.openunity.hunterhint.ui.registration

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException
import ru.openunity.hunterhint.HunterHintApplication
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.UserRepository
import ru.openunity.hunterhint.dto.UserRegDto
import ru.openunity.hunterhint.dto.birthMonth
import ru.openunity.hunterhint.dto.country
import ru.openunity.hunterhint.network.UserRemoteDataSource
import ru.openunity.hunterhint.ui.AppError
import ru.openunity.hunterhint.ui.Loading
import ru.openunity.hunterhint.ui.Success
import java.io.IOException
import java.util.regex.Pattern

class RegViewModel(private val repository: UserRepository) : ViewModel() {

    private val _regUiState = MutableStateFlow(RegUiState())
    val regUiState: StateFlow<RegUiState> = _regUiState.asStateFlow()


    var userMonth by mutableIntStateOf(R.string.month)
        private set
    var userGender by mutableIntStateOf(R.string.gender)
        private set


    fun requestRegistration() {
        viewModelScope.launch(context = Dispatchers.IO) {
            _regUiState.update { it.copy(state = Loading(R.string.loading)) }

            try {
                val userId = repository.createUser(_regUiState.value.userRegDto)
                _regUiState.update {
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
                _regUiState.update { it.copy(state = AppError(R.string.server_error, true)) }
            } catch (e: HttpException) {
                Log.e("HttpException", e.message())
                _regUiState.update { it.copy(state = AppError(R.string.no_internet, true)) }
            }
        }
    }

    private fun checkIfPhoneStored() {
        runBlocking {
            _regUiState.update {
                it.copy(
                    isPhoneStored = true,
                    state = Loading(R.string.loading)
                )
            }

            try {
                val dto = _regUiState.value.userRegDto
                val isStored = repository.isPhoneStored(dto.phoneNumber, dto.countryCode)
                _regUiState.update {
                    it.copy(
                        state = Success(R.string.empty),
                        isPhoneStored = isStored
                    )
                }
            } catch (e: IOException) {
                Log.e("IOException", e.toString())
                _regUiState.update {
                    it.copy(
                        state = AppError(R.string.no_internet, true)
                    )
                }
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString())
                _regUiState.update { it.copy(state = AppError(R.string.server_error, true)) }
            }
        }
    }

    private fun checkIfEmailStored() {
        runBlocking {
            _regUiState.update {
                it.copy(
                    isEmailStored = true,
                    state = Loading(R.string.loading)
                )
            }

            try {
                val dto = _regUiState.value.userRegDto
                val isStored = repository.isEmailStored(dto.email)
                _regUiState.update {
                    it.copy(
                        state = Success(R.string.empty),
                        isEmailStored = isStored
                    )
                }
            } catch (e: IOException) {
                Log.e("IOException", e.toString())
                _regUiState.update {
                    it.copy(
                        state = AppError(R.string.no_internet, true)
                    )
                }
            } catch (e: HttpException) {
                Log.e("HttpException", e.toString())
                _regUiState.update { it.copy(state = AppError(R.string.server_error, true)) }
            }
        }
    }

    fun updatePhone(userInput: String) {
        val numberLength = _regUiState.value.userRegDto.country.numberFormat.count { it == 'X' }
        if (numberLength == 0 || numberLength >= userInput.length || userInput.length <=
            _regUiState.value.userRegDto.phoneNumber.length
        ) {
            _regUiState.update {
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
        val userInputLength = _regUiState.value.userRegDto.phoneNumber.length
        val numberLength = _regUiState.value.userRegDto.country.numberFormat.count { it == 'X' }
        val isWrong = (numberLength == 0 && userInputLength <= 3) ||
                (numberLength != userInputLength)
        _regUiState.update {
            it.copy(
                isPhoneWrong = isWrong,
            )
        }
        return !isWrong
    }


    fun updateUserName(userInput: String) {
        _regUiState.update {
            it.copy(
                isNameCorrect = true,
                userRegDto = it.userRegDto.copy(
                    name = userInput
                )
            )
        }
    }

    fun updateCountry(country: Country) {
        _regUiState.update {
            it.copy(
                userRegDto = it.userRegDto.copy(
                    countryCode = country.cCode
                )
            )
        }
    }

    fun updateUserLastName(userInput: String) {
        _regUiState.update {
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
        _regUiState.update {
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
            _regUiState.update {
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
            _regUiState.update {
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
        _regUiState.update {
            it.copy(
                userRegDto = it.userRegDto.copy(
                    genderCode = gender.gCode
                )
            )
        }
        dismissDialogs()
    }

    fun changeEmail(userInput: String) {
        _regUiState.update {
            it.copy(
                isEmailCorrect = true,
                userRegDto = it.userRegDto.copy(
                    email = userInput
                )
            )
        }
    }

    fun updatePassword(userInput: String) {
        _regUiState.update {
            it.copy(
                userRegDto = it.userRegDto.copy(
                    password = userInput
                )
            )
        }
    }

    fun updateEmailConfirmationCode(userInput: String) {
        _regUiState.update {
            it.copy(
                emailConfirmation = it.emailConfirmation.copy(userInput = userInput)
            )
        }
    }

    fun updatePhoneConfirmationCode(userInput: String) {
        _regUiState.update {
            it.copy(
                phoneConfirmation = it.phoneConfirmation.copy(userInput = userInput)
            )
        }
    }

    fun showGenderDialog() {
        _regUiState.update {
            it.copy(isGenderDialogShow = true)
        }
    }

    fun showMonthDialog() {
        _regUiState.update {
            it.copy(isMonthDialogShow = true)
        }
    }

    fun dismissDialogs() {
        _regUiState.update {
            it.copy(isMonthDialogShow = false, isGenderDialogShow = false)
        }
    }

    private fun checkName() {
        _regUiState.update {
            it.copy(isNameCorrect = (_regUiState.value.userRegDto.name.length > 3))
        }
    }

    private fun checkLastName() {
        _regUiState.update {
            it.copy(isLastNameCorrect = (_regUiState.value.userRegDto.lastName.length > 3))
        }
    }

    fun isNameCorrect(): Boolean {
        checkLastName()
        checkName()
        return regUiState.value.isLastNameCorrect && regUiState.value.isNameCorrect
    }

    fun checkBirthday(): Boolean {
        val day = _regUiState.value.userRegDto.birthDay
        val year = _regUiState.value.userRegDto.birthYear
        val month = _regUiState.value.userRegDto.birthMonth
        val complete =
            userMonth in Month.entries.map { it.stringResourceId }
        val correct = if (complete) {
            day in 1..month.days && year > 1900
        } else {
            false
        }
        _regUiState.update {
            it.copy(
                isBirthdayComplete = complete, isBirthdayCorrect = correct
            )
        }
        return complete && correct
    }

    fun checkGender(): Boolean {
        val isGenderSpecified = userGender in Gender.entries.map { it.stringResourceId }
        _regUiState.update { uiState ->
            uiState.copy(
                isGenderSpecified = isGenderSpecified
            )
        }
        return isGenderSpecified
    }

    private fun isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(_regUiState.value.userRegDto.email)
            .matches()
    }

    fun requestEmailConfirmation() {
        checkIfEmailStored()
        if (isEmailValid() && !_regUiState.value.isEmailStored) {
            val code = createConfirmationCode()
            _regUiState.update {
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
        if (isPhoneCorrect() && !_regUiState.value.isPhoneStored) {
            val code = createConfirmationCode()
            _regUiState.update {
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
        _regUiState.update {
            it.copy(emailConfirmation = Confirmation())
        }
    }

    fun changePhone() {
        _regUiState.update {
            it.copy(phoneConfirmation = Confirmation())
        }
    }

    fun showPassword(isPasswordShow: Boolean) {
        _regUiState.update {
            it.copy(
                isPasswordShow = isPasswordShow
            )
        }
    }

    fun validatePassword(): Boolean {
        val isMatch =
            Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$")
                .matcher(_regUiState.value.userRegDto.password).find()
        if (regUiState.value.isPasswordStrong != isMatch) {
            _regUiState.update {
                it.copy(isPasswordStrong = isMatch)
            }
        }
        return isMatch
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HunterHintApplication)
                val repository = application.appComponent.getUserRepository()
                RegViewModel(repository)
            }
        }
    }

    fun onRegCompete() {
        clearData()
    }

    private fun clearData() {
        _regUiState.update {
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

