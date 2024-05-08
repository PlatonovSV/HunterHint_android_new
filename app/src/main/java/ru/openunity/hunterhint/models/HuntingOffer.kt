package ru.openunity.hunterhint.models

import ru.openunity.hunterhint.ui.enums.HuntingMethods
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
) {
    val typeStringRes: Int
        get() = getResourcesById(resourcesTypeId).stringRes

    val guidingStringRes: Int
        get() = getGuidingPreferenceById(guidingPreferenceId).stringResId

    val methodsList: List<HuntingMethods>
        get() = mutableListOf<HuntingMethods>().apply {
            val enumsList = HuntingMethods.entries
            methodIds.forEach { id ->
                enumsList.find { enum ->
                    enum.id == id
                }.let {
                    if (it != null) this.add(it)
                }
            }
        }
}

