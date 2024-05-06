package ru.openunity.hunterhint.ui.search

import androidx.annotation.StringRes
import ru.openunity.hunterhint.models.GroundsCard

sealed interface CardsState

class CardsSuccess(
    val cards: List<GroundsCard> = listOf()
) : CardsState

class CardsLoading(
    val cards: List<GroundsCard> = listOf()
) : CardsState

class CardsError(
    val cards: List<GroundsCard> = listOf(),
    @StringRes val messageId: Int
) : CardsState