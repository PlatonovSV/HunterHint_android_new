package ru.openunity.hunterhint.ui.enums

import androidx.annotation.StringRes
import ru.openunity.hunterhint.R

enum class Status(val code: Int, @StringRes val strResId: Int) {
    ACTIVE(0, R.string.active),
    BLOCKED(1, R.string.blocked),
    DELETED(2, R.string.deleted),
}

fun getStatusByCode(code: Int) = Status.entries.find { it.code == code } ?: Status.DELETED