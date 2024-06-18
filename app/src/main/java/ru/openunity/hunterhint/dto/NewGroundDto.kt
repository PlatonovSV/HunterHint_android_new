package ru.openunity.hunterhint.dto

data class NewGroundDto(
    val token: String = "",
    val groundsName: String= "",
    val companyName: String= "",
    val groundsArea: Double = .0,
    val baseCoordinate: List<Double> = listOf(.0, .0),
    val hotelCapacity: Int = 0,
    val maxNumberHunters: Int = 1,
    val informationBath: Boolean = false,
    val informationHotel: Boolean = false,
    val accommodationCost: Int = 0,
    val regionCode: Int = -1,
    val municipalDistrict: Int = -1,
    val groundsDescription: String = "",
    val photoUrls: List<String> = listOf()
)
