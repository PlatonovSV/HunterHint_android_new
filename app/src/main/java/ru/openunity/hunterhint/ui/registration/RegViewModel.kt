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


    fun updateUserName(userInput: String) {
        userName = userInput
    }

    fun updateUserLastName(userInput: String) {
        userLastName = userInput
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
            userMonth in Month.values().map { it.stringResourceId } && day != null && year != null
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
        val isGenderSpecified = userGender in Gender.values().map { it.stringResourceId }
        _regUiState.update { uiState ->
            uiState.copy(
                isGenderSpecified = isGenderSpecified
            )
        }
        return isGenderSpecified
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

enum class Gender(val stringResourceId: Int) {
    MALE(R.string.male), FEMALE(R.string.female)
}

enum class Month(val days: Int, val stringResourceId: Int) {
    JANUARY(31, R.string.january), FEBRUARY(29, R.string.february), MARCH(
        31,
        R.string.march
    ),
    APRIL(30, R.string.april), MAY(31, R.string.may), JUNE(30, R.string.june), JULY(
        31,
        R.string.july
    ),
    AUGUST(31, R.string.august), SEPTEMBER(30, R.string.september), OCTOBER(
        31,
        R.string.october
    ),
    NOVEMBER(30, R.string.november), DECEMBER(31, R.string.december)
}