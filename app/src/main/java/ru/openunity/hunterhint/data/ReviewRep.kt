package ru.openunity.hunterhint.data

import ru.openunity.hunterhint.model.Rating

object ReviewRep {
    fun getRatingById(groundId: Int) = Rating(numberOfReview = (0..300).random(), rating = (1..5).random())
}