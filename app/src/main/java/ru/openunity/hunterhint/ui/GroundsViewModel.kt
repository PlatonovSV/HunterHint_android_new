package ru.openunity.hunterhint.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.openunity.hunterhint.data.GroundRep
import ru.openunity.hunterhint.data.ImageRep

class GroundsViewModel : ViewModel() {

    private val _groundsUiState = MutableStateFlow(GroundsUiState())
    val groundsUiState: StateFlow<GroundsUiState> = _groundsUiState.asStateFlow()

    fun onGroundsCardClick(groundId: Int) {
        val ground = GroundRep.findGroundById(groundId = groundId)
        val images = ImageRep.getImagesById(groundId = groundId)
        _groundsUiState.update {
            it.copy(
                ground = ground,
                images = images,
                numberOfCurrentImage = 0
            )
        }
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