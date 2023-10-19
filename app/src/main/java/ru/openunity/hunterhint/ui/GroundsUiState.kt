package ru.openunity.hunterhint.ui

import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.models.Image

data class GroundsUiState(
    val ground: Ground = Ground(),
    val images: List<Image> = listOf(),
    val numberOfCurrentImage: Int = 0,
)