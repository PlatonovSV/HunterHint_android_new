package ru.openunity.hunterhint.models

import kotlinx.serialization.Serializable

@Serializable
data class GroundsCard(
    val id: Int,
    val name: String,
    val area: Double,
    val isHotel: Boolean,
    val isBath: Boolean,
    val regionName: String,
    val municipalDistrictName: String,
    val minCost: Int,
    val images: List<Photo>,
    @kotlinx.serialization.Transient
    val numberOfCurrentImage: Int = 0,
    val rating: Double,
    val reviewsQuantity: Int
)