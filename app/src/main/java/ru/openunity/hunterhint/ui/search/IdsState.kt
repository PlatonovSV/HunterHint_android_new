package ru.openunity.hunterhint.ui.search

import androidx.annotation.StringRes

sealed interface IdsState

class IdsSuccess(
    val ids: List<Int> = listOf()
) : IdsState

data object IdsLoading : IdsState

class IdsError(
    @StringRes val messageId: Int
) : IdsState