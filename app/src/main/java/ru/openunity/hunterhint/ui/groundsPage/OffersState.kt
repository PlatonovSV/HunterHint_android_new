package ru.openunity.hunterhint.ui.groundsPage

import androidx.annotation.StringRes
import ru.openunity.hunterhint.models.HuntingOffer

sealed interface OffersState

class OffersSuccess(
    val offers: List<HuntingOffer>
) : OffersState {

}

data object OffersLoading : OffersState

class OffersError(
    @StringRes val messageId: Int
) : OffersState {}