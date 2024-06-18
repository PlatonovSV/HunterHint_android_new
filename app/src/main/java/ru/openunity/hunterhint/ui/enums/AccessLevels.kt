package ru.openunity.hunterhint.ui.enums

import androidx.annotation.StringRes
import ru.openunity.hunterhint.R

enum class AccessLevels(val code: Int, @StringRes val strResId: Int) {
    HUNTER(0, R.string.hunter),
    OWNER(1, R.string.owner),
    ADMIN(2, R.string.admin)
}