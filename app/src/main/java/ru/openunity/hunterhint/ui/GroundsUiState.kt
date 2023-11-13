package ru.openunity.hunterhint.ui

import ru.openunity.hunterhint.models.Ground
import ru.openunity.hunterhint.models.Photo

data class GroundsUiState(
    val ground: Ground = Ground(),
    val images: List<Photo> = listOf(),
    val numberOfCurrentImage: Int = 0,
)