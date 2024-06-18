package ru.openunity.hunterhint.dto

data class NewGroundDto(
    val token: String,
    val groundsName: String,
    val companyName: String,
    val groundsArea: Double,
    val baseCoordinate: List<Double>,
    val hotelCapacity: Int,
    val maxNumberHunters: Int,
    val informationBath: Boolean,
    val informationHotel: Boolean,
    val accommodationCost: Int,
    val regionCode: Int,
    val municipalDistrict: Int,
    val groundsDescription: String,
    val photoUrls: List<String>
)
