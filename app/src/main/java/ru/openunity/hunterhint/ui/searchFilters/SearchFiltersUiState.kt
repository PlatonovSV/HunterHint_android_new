package ru.openunity.hunterhint.ui.searchFilters

import ru.openunity.hunterhint.ui.booking.UserDate
import java.time.LocalDateTime

data class SearchFiltersUiState(
    val resourcesHint: Hint = Hint(key = SearchFiltersHints.RESOURCES),
    val regionHint: Hint = Hint(key = SearchFiltersHints.REGION),
    val methodHint: Hint = Hint(key = SearchFiltersHints.METHOD),
    val districtHint: Hint = Hint(key = SearchFiltersHints.DISTRICT),
    val groundsNameHint: Hint = Hint(key = SearchFiltersHints.GROUNDS_NAME),

    val guestsNumber: Int = 0,
    val huntersNumber: Int = 1,

    val guidingPreferenceId: Int = -1,


    val startDay: UserDate = UserDate(),
    val finalDay: UserDate = UserDate(),
    val isStartMonthDialogShow: Boolean = false,
    val isFinalMonthDialogShow: Boolean = false,
    val isShowStartError: Boolean = false,
    val isShowFinalError: Boolean = false,
    val isNeedsBath: Boolean = false,
    val isNeedsHotel: Boolean = false,

    val minPrice: Int = 0,
    val maxPrice: Int = Int.MAX_VALUE,
) {


    val isDateWrong: Boolean
        get() {
            return !(LocalDateTime.now()
                .isBefore(startDay.date) && startDay.date.isBefore(finalDay.date))
        }
}

data class Hint(
    val allLocal: List<Pair<Int, String>> = listOf(),
    val hints: List<Pair<Int, String>> = listOf(),
    val current: Pair<Int, String> = Pair(-1, ""),
    val isError: Boolean = false,
    val key: Any? = null,
    val getAllLocal: (String) -> Unit = {}
) {
    fun updateHints(userInput: String, keyQuery: Any): Hint {
        if (keyQuery == key) {
            if (userInput.isBlank()) {
                return this.copy(
                    isError = false, hints = listOf(), current = Pair(-1, "")
                )
            }
            getAllLocal(userInput)
            val newHint: Hint = this.copy(
                hints = this.allLocal.filter {
                    it.second.contains(userInput, ignoreCase = true)
                }
            )

            return newHint.copy(
                current = this.current.copy(
                    first = -1,
                    second = userInput
                ),
                isError = newHint.hints.isEmpty()
            )
        }
        return this
    }

    fun selectHint(hint: Pair<Int, String>, keyQuery: SearchFiltersHints): Hint {
        if (keyQuery == key) {
            return this.copy(
                hints = listOf(), current = hint.copy(
                    first = Int.MAX_VALUE
                )
            )
        }
        return this
    }
}

enum class SearchFiltersHints {
    REGION, DISTRICT, RESOURCES, METHOD, GROUNDS_NAME
}