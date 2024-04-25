package ru.openunity.hunterhint.ui.personal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.openunity.hunterhint.data.UserRepository
import androidx.lifecycle.ViewModelProvider
import ru.openunity.hunterhint.HunterHintApplication
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewmodel.initializer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PersonalViewModel(val userRepository: UserRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PersonalUiState())
    val uiState = _uiState.asStateFlow()

    init {
        try {
            viewModelScope.launch {
                val user = userRepository.getUser().first()
                _uiState.update {
                    it.copy(
                        user = user
                    )
                }
            }
        } catch (e: NoSuchElementException) {
            _uiState.update {
                it.copy(isLoggedIn = false)
            }
        }
    }
    /*private val userLocalData: StateFlow<User> =
        userRepository.getUser().map { _uiState.update { ui ->
            ui.copy(user = it)
        }
        it
        }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = PersonalUiState()
            )
            сервер - источник достоверности
            данные с сервера загружаются в бд
            данные из бд отображаются на экране
            изменения отправляются на сервер и после в бд

            переменная - uiState
            переменная - LocalData
            пtркменная - RemoteData
*/

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


    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as HunterHintApplication)
                val repository = application.appComponent.getUserRepository()
                PersonalViewModel(userRepository = repository)
            }
        }
    }
}