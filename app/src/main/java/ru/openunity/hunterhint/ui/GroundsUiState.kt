package ru.openunity.hunterhint.ui

import ru.openunity.hunterhint.model.Ground
import ru.openunity.hunterhint.model.Image

data class GroundsUiState(
    val ground: Ground = Ground(),
    val images: List<Image> = listOf(),
    val numberOfCurrentImage: Int = 0,
)