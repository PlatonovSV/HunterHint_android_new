package ru.openunity.hunterhint.models

import ru.openunity.hunterhint.ui.enums.getGuidingPreferenceById
import ru.openunity.hunterhint.ui.enums.getResourcesById
import java.time.LocalDateTime

data class HuntingOffer(
    val id: Long = 0L,
    val groundsId: Int = 0,
    val resourcesTypeId: Int = 0,
    val openingDate: LocalDateTime = LocalDateTime.MIN,
    val closingDate: LocalDateTime = LocalDateTime.MAX,
    val methodIds: List<Int> = listOf(),
    val eventCost: Int = 0,
    val guidingPreferenceId: Int = 0,
    val description: String = ""
)

val HuntingOffer.typeStringRes: Int
    get() = getResourcesById(this.resourcesTypeId).stringRes

val HuntingOffer.guidingStringRes: Int
    get() = getGuidingPreferenceById(this.guidingPreferenceId).stringResId

