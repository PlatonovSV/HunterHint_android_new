package ru.openunity.hunterhint.ui.bottomAppBar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.openunity.hunterhint.data.user.UserRepository
import javax.inject.Inject

@HiltViewModel
class BottomAppBarViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private var _uiState = MutableStateFlow(BottomAppBarUiState())

    init {
        viewModelScope.launch {
            userRepository.isAuthorized.collect {isAuthorized ->
                _uiState.update {
                    it.copy(
                        isAuthorized = isAuthorized
                    )
                }
            }
        }
    }

    val uiState = _uiState.asStateFlow()
}