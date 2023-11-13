package ru.openunity.hunterhint.models

import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    val id: Int,
    val imgSrc: String
)
