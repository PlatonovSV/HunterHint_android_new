package ru.openunity.hunterhint.ui.enums

import androidx.annotation.StringRes
import ru.openunity.hunterhint.R

enum class GuidingPreference(val id: Int, @StringRes val stringResId: Int) {
    GUIDED(0, R.string.guided),
    SEMI_GUIDED(1, R.string.semi_guided),
    SELF_GUIDED(2, R.string.self_guided)
}

fun getGuidingPreferenceById(id: Int) =
    GuidingPreference.entries.find { it.id == id } ?: GuidingPreference.GUIDED