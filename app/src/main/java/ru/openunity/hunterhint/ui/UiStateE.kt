package ru.openunity.hunterhint.ui

import ru.openunity.hunterhint.R

open class UiStateE(open val state: StateE)
enum class StateE {
    Loading,
    Error,
    Success
}

sealed class State(val message: Int)

class Loading(message: Int): State(message)

class AppError(message: Int, val isRepeatPossible: Boolean, val imageId: Int = R.drawable.ic_connection_error): State(message)

class Success(message: Int = R.string.empty, val result: Boolean = false): State(message)

open class UiState(val state: State)