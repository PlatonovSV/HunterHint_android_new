package ru.openunity.hunterhint.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GroundsPageViewModel : ViewModel() {

    private val _groundsUiState = MutableStateFlow(GroundsUiState())
    val groundsUiState: StateFlow<GroundsUiState> = _groundsUiState.asStateFlow()

    fun onGroundsCardClick(groundId: Int) {

    }

    fun changeImage(increment: Boolean) {
        if (increment) {
            if (_groundsUiState.value.numberOfCurrentImage == _groundsUiState.value.images.lastIndex) {
                _groundsUiState.update {
                    it.copy(
                        numberOfCurrentImage = 0
                    )
                }
            } else {
                _groundsUiState.update {
                    it.copy(
                        numberOfCurrentImage = it.numberOfCurrentImage.inc()
                    )
                }
            }
        } else {
            if (_groundsUiState.value.numberOfCurrentImage == 0) {
                _groundsUiState.update {
                    it.copy(
                        numberOfCurrentImage = it.images.lastIndex
                    )
                }
            } else {
                _groundsUiState.update {
                    it.copy(
                        numberOfCurrentImage = it.numberOfCurrentImage.dec()
                    )
                }
            }
        }
    }
}