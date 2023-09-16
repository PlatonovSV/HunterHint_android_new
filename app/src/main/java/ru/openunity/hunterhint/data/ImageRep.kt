package ru.openunity.hunterhint.data

import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.model.Image

object ImageRep {
    fun getPreviewImagesById(groundId: Int): List<Image> {
        val numberOfImage = (0..5).random()
        val allImages = mutableListOf(
            R.drawable.forest_1,
            R.drawable.forest_2,
            R.drawable.forest_3,
            R.drawable.forest_4,
            R.drawable.forest_5,
            R.drawable.forest_6,
            R.drawable.forest_7,
            R.drawable.forest_8,
            R.drawable.forest_9,
            R.drawable.forest_10,
            R.drawable.forest_11,
            R.drawable.forest_12,
            R.drawable.forest_13,
            R.drawable.forest_14,
            R.drawable.forest_15,
            R.drawable.forest_16,
            R.drawable.forest_17,
            R.drawable.forest_18,
            R.drawable.forest_19,
            R.drawable.forest_20,
        )
        allImages.shuffle()
        val images = mutableListOf<Image>()
        if (numberOfImage > 0) {
            repeat(numberOfImage) {
                images.add(Image(allImages[it]))
            }
        }
        return images
    }

    fun getImagesById(groundId: Int): List<Image> {
        val numberOfImage = (0..10).random()
        val allImages = mutableListOf(
            R.drawable.forest_1,
            R.drawable.forest_2,
            R.drawable.forest_3,
            R.drawable.forest_4,
            R.drawable.forest_5,
            R.drawable.forest_6,
            R.drawable.forest_7,
            R.drawable.forest_8,
            R.drawable.forest_9,
            R.drawable.forest_10,
            R.drawable.forest_11,
            R.drawable.forest_12,
            R.drawable.forest_13,
            R.drawable.forest_14,
            R.drawable.forest_15,
            R.drawable.forest_16,
            R.drawable.forest_17,
            R.drawable.forest_18,
            R.drawable.forest_19,
            R.drawable.forest_20,
        )
        allImages.shuffle()
        val images = mutableListOf<Image>()
        if (numberOfImage > 0) {
            repeat(numberOfImage) {
                images.add(Image(allImages[it]))
            }
        }
        return images
    }
}