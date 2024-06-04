package ru.openunity.hunterhint.ui.booking

import androidx.annotation.StringRes
import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.models.database.User
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
    val huntingMethodId: Int = -1,
    val additionalRequests: String = "",
    val user: User? = null,
    val bookRequestState: BookRequestState = BookRequestWait(R.string.empty)
) {


    val isDateWrong: Boolean
        get() {
            return !(LocalDateTime.now().isBefore(startDay.date) && startDay.date.isBefore(finalDay.date))
        }

    val isMethodNotSelected: Boolean
        get() = huntingMethodId == -1
}

enum class BookingSections(@StringRes val sectionNameId: Int) {
    DATE(R.string.date),
    HUNTING_METHODS(R.string.hunting_method),
    ADDITIONAL(R.string.additional_information)
}

data class UserDate(
    val day: Int = LocalDateTime.now().dayOfMonth, val monthCode: Int = LocalDateTime.now().monthValue, val year: Int = LocalDateTime.now().year
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
            LocalDateTime.parse("${year}-${if(month.mCode < 10) "0" else ""}${month.mCode}-${day}T00:00:00")
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

sealed interface BookRequestState

class BookRequestWait(@StringRes val resId: Int = R.string.empty) : BookRequestState

data object BookRequestLoading : BookRequestState

data object BookRequestSuccess : BookRequestState



