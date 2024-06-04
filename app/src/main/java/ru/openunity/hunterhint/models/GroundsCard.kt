package ru.openunity.hunterhint.models

import kotlinx.serialization.Serializable

@Serializable
data class GroundsCard(
    val id: Int = -1,
    val name: String = "",
    val area: Double = .0,
    val isHotel: Boolean = false,
    val isBath: Boolean = false,
    val regionName: String = "",
    val municipalDistrictName: String = "",
    val minCost: Int = 0,
    val images: List<Photo> = listOf(),
    @kotlinx.serialization.Transient
    val numberOfCurrentImage: Int = 0,
    val rating: Double = .0,
    val reviewsQuantity: Int = 0
)