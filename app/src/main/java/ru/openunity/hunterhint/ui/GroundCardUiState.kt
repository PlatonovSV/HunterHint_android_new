package ru.openunity.hunterhint.ui

import ru.openunity.hunterhint.data.ImageRep
import ru.openunity.hunterhint.data.ReviewRep
import ru.openunity.hunterhint.model.Ground
import ru.openunity.hunterhint.model.Image
import ru.openunity.hunterhint.model.Rating


data class GroundCardUiState(
    val id: Int,
    val name: String,
    val area: Double,
    val isHotel: Boolean,
    val isBath: Boolean,
    val regionName: String,
    val municipalDistrictName: String,
    val minCost: Int,
    val images: List<Image>,
    val numberOfCurrentImage: Int,
    val rating: Rating
)

fun Ground.toGroundCardUiState() = GroundCardUiState(
    id = id,
    name = name,
    area = area,
    isHotel = isHotel,
    isBath = isBath,
    regionName = regionName,
    municipalDistrictName = municipalDistrictName,
    minCost = minCost,
    images = ImageRep.getPreviewImagesById(id),
    numberOfCurrentImage = 0,
    rating = ReviewRep.getRatingById(id)
)