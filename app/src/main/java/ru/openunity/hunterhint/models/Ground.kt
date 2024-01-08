package ru.openunity.hunterhint.models

data class Ground(
    val id: Int = 0,
    val name: String = "",
    val companyName: String = "",
    val area: Double = .0,
    val isHotel: Boolean = false,
    val isBath: Boolean = false,
    val maxHunters: Int = 0,
    val hotelCapacity: Int = 0,
    val accommodationCost: Int = 0,
    val regionName: String = "",
    val municipalDistrictName: String = "",
    val baseCoordinate: String = "",
    val minCost: Int = 0,
    val images: List<Photo> = listOf(),
    val rating: Double = .0,
    val reviewsQuantity: Int = 0,
    val description: String = "",
)