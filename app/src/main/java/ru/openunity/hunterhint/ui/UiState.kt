package ru.openunity.hunterhint.ui
open class UiState(open val state: State)
enum class State {
    Loading,
    Error,
    Success
}