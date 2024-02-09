package ru.openunity.hunterhint.ui.registration

import androidx.compose.runtime.getValue
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
import ru.openunity.hunterhint.data.GroundsRepository

class RegViewModel(private val repository: GroundsRepository) : ViewModel() {

    private val _regUiState = MutableStateFlow(RegUiState())
    val regUiState: StateFlow<RegUiState> = _regUiState.asStateFlow()

    var userName by mutableStateOf("")
        private set

    var userLastName by mutableStateOf("")
        private set


    fun updateUserName(userInput: String) {
        userName = userInput
    }

    fun updateUserLastName(userInput: String) {
        userLastName = userInput
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

    fun isAllowedGo(): Boolean {
        checkLastName()
        checkName()
        return regUiState.value.isLastNameCorrect && regUiState.value.isNameCorrect
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

enum class Gender {
    MALE,
    FEMALE
}

enum class Month {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER
}