package ru.openunity.hunterhint.ui.booking

import androidx.annotation.StringRes
import ru.openunity.hunterhint.models.HuntingOffer

sealed interface OfferState

class OfferSuccess(
    val offer: HuntingOffer
) : OfferState {

}

data object OfferLoading : OfferState

class OfferError(
    @StringRes val messageId: Int
) : OfferState {}