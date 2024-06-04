package ru.openunity.hunterhint.models

import kotlinx.serialization.Serializable

@Serializable
data class Photo(
    val id: Long = -1,
    val imgSrc: String = ""
)
