package ru.openunity.hunterhint.models

data class Ground(
    val id: Int = 0,
    val userId: Int = 0,
    val companyId: Int = 0,
    val regionCode: Int = 0,
    val municipalDistrictId: Int = 0,
    val name: String = "0",
    val baseCoordinate: String = "0",
    val area: Double = .0,
    val isHotel: Boolean = false,
    val isBath: Boolean = false,
    val maxNumberHunters: Int = 0,
    val hotelCapacity: Int = 0,
    val accommodationCost: Int = 0,
    val description: String = "0",
    val regionName: String= "0",
    val municipalDistrictName: String= "0",
    val minCost: Int = 0,
    val companyStr: String= "0",
)

