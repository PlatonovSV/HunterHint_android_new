package ru.openunity.hunterhint.ui.personal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.openunity.hunterhint.data.user.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(val userRepository: UserRepository) : ViewModel() {
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

}