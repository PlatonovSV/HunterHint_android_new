package ru.openunity.hunterhint.ui.registration

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.openunity.hunterhint.HunterHintApplication
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.data.GroundsRepository

class RegViewModel(private val repository: GroundsRepository) : ViewModel() {

    private val _regUiState = MutableStateFlow(RegUiState())
    private var confirmationCode: String = ""
    val regUiState: StateFlow<RegUiState> = _regUiState.asStateFlow()

    var userName by mutableStateOf("")
        private set

    var userLastName by mutableStateOf("")
        private set

    var userMonth by mutableIntStateOf(R.string.month)
        private set
    private var month: Month = Month.JANUARY
    var userGender by mutableIntStateOf(R.string.gender)
        private set
    var userDay by mutableStateOf("")
        private set
    var userYear by mutableStateOf("")
        private set
    var userEmail by mutableStateOf("")
        private set
    var userConfirmationCode by mutableStateOf("")
        private set
    var userCountry by mutableStateOf(Country.RUSSIAN_FEDERATION)
        private set

    var userPhone by mutableStateOf("")

    fun updateUserPhone(userInput: String) {
        val numberLength = userCountry.numberFormat.count { it == 'X' }
        if (numberLength == 0 || numberLength >= userInput.length) {
            userPhone = userInput
        }
        if (regUiState.value.isPhoneWrong) {
            _regUiState.update {
                it.copy(
                    isPhoneWrong = false
                )
            }
        }
    }

    fun isPhoneCorrect(): Boolean {
        val numberLength = userCountry.numberFormat.count { it == 'X' }
        if ((numberLength == 0 && userPhone.length < 3) || (numberLength > 0 && numberLength != userPhone.length)) {
            _regUiState.update {
                it.copy(
                    isPhoneWrong = true
                )
            }
        }
        return !regUiState.value.isPhoneWrong
    }

    fun updateUserName(userInput: String) {
        userName = userInput
        if (!regUiState.value.isNameCorrect) {
            _regUiState.update {
                it.copy(
                    isNameCorrect = true
                )
            }
        }
    }

    fun updateCountry(country: Country) {
        userCountry = country
    }

    fun updateUserLastName(userInput: String) {
        userLastName = userInput
        if (!regUiState.value.isLastNameCorrect) {
            _regUiState.update {
                it.copy(
                    isLastNameCorrect = true
                )
            }
        }
    }

    fun updateUserMonth(month: Month) {
        userMonth = month.stringResourceId
        this.month = month
        dismissDialogs()
    }

    fun updateUserDay(userInput: String) {
        if (userInput.length <= 2) {
            userDay = userInput
        }
    }

    fun updateUserYear(userInput: String) {
        if (userInput.length <= 4) {
            userYear = userInput
        }
    }

    fun updateUserGender(gender: Gender) {
        userGender = gender.stringResourceId
        dismissDialogs()
    }

    fun updateEmail(userInput: String) {
        userEmail = userInput
    }

    fun updateConfirmationCode(userInput: String) {
        userConfirmationCode = userInput
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
            it.copy(isNameCorrect = (userName.length > 3))
        }
    }

    private fun checkLastName() {
        _regUiState.update {
            it.copy(isLastNameCorrect = (userLastName.length > 3))
        }
    }

    fun isNameCorrect(): Boolean {
        checkLastName()
        checkName()
        return regUiState.value.isLastNameCorrect && regUiState.value.isNameCorrect
    }

    fun checkBirthday(): Boolean {
        val day = userDay.toIntOrNull()
        val year = userYear.toIntOrNull()
        val complete =
            userMonth in Month.entries.map { it.stringResourceId } && day != null && year != null
        val correct = if (complete) {
            day in 1..month.days && year!! > 1900
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

    fun isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
    }

    fun reqEmailConf() {
        confirmationCode = createConfirmationCode()
        _regUiState.update {
            it.copy(
                isConfirmationRequested = true
            )
        }
        userConfirmationCode = confirmationCode
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

    fun changeEmail() {
        userConfirmationCode = ""
        confirmationCode = ""
        _regUiState.update {
            it.copy(isConfirmationRequested = false)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HunterHintApplication)
                val repository = application.container.repository
                RegViewModel(repository)
            }
        }
    }
}

