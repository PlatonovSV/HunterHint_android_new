package ru.openunity.hunterhint.ui.booking

import androidx.annotation.StringRes
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.ui.registration.Month
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

data class BookingUiState(
    val startDay: UserDate = UserDate(),
    val finalDay: UserDate = UserDate(),
    val currentSection: BookingSections = BookingSections.DATE,
    val isStartMonthDialogShow: Boolean = false,
    val isFinalMonthDialogShow: Boolean = false,
    val isShowStartError: Boolean = false,
    val isShowFinalError: Boolean = false,
    val offer: OfferState = OfferLoading,
    val offerId: Long = -1L,
    val huntingMethodId: Int = -1
) {

    val isDateWrong: Boolean
        get() = !(startDay.isActual && finalDay.date.isAfter(startDay.date))

    val isMethodNotSelected: Boolean
        get() = huntingMethodId == -1
}

enum class BookingSections(@StringRes val sectionNameId: Int) {
    DATE(R.string.date),
    HUNTING_METHODS(R.string.hunting_method)
}

data class UserDate(
    val day: Int = 0, val monthCode: Int = Month.JANUARY.mCode, val year: Int = 0
) {
    val month: Month
        get() {
            Month.entries.forEach {
                if (it.mCode == this.monthCode) {
                    return it
                }
            }
            return Month.MARCH
        }

    val isDayCorrect: Boolean
        get() = checkDay(day)

    val isMonthCorrect: Boolean
        get() = checkMonth(monthCode)

    val isYearCorrect: Boolean
        get() = checkYear(year)

    val isCorrect: Boolean
        get() {
            val currentYear = LocalDateTime.now().year.minus(1)
            return year in currentYear..currentYear.plus(3) && isMonthCorrect && isDayCorrect
        }
    val isActual: Boolean
        get() = isCorrect && LocalDateTime.now().isBefore(date)

    val date: LocalDateTime
        get()
        = try {
            LocalDateTime.parse("${year}-${month.mCode}-${day}T00:00:00")
        } catch (e: DateTimeParseException) {
            LocalDateTime.now()
        }


    private fun checkDay(day: Int) = day in 1..month.days

    private fun checkMonth(month: Int) = month in Month.entries.map { it.mCode }

    private fun checkYear(year: Int) = year in 1..2032

    private fun checkDay(day: String): Boolean {
        if (day.isBlank()) {
            return true
        }
        val intDay = day.toIntOrNull()
        if (intDay != null) {
            return checkDay(intDay)
        }
        return false
    }

    private fun checkYear(year: String): Boolean {
        if (year.isBlank()) {
            return true
        }
        val intYear = year.toIntOrNull()
        return if (intYear != null) {
            checkYear(intYear)
        } else {
            false
        }
    }

    fun updateYear(userInput: String) = if (checkYear(userInput)) {
        this.copy(
            year = if (userInput.isBlank()) 0 else userInput.toInt()
        )
    } else {
        this
    }

    fun updateMonth(userInput: Month) = this.copy(
        monthCode = userInput.mCode
    )

    fun updateDay(userInput: String) = if (checkDay(userInput)) {
        this.copy(
            day = if (userInput.isBlank()) 0 else userInput.toInt()
        )
    } else {
        this
    }

}



