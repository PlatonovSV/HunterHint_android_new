package ru.openunity.hunterhint.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroundsPhoto(
    val id: String,
    val imgSrc: String
)
