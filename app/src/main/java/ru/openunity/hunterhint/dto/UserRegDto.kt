package ru.openunity.hunterhint.dto

import ru.openunity.hunterhint.ui.registration.Country
import ru.openunity.hunterhint.ui.registration.Gender
import ru.openunity.hunterhint.ui.registration.Month

data class UserRegDto(
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val birthMonthCode: Int = Month.JANUARY.mCode,
    val birthDay: Int = 0,
    val birthYear: Int = 0,
    val genderCode: Int = Gender.MALE.gCode,
    val password: String = "",
    val countryCode: Int = Country.RUSSIAN_FEDERATION.cCode,
    val phoneNumber: String = "",
    val accessLevel: Int = -1
)

val UserRegDto.country: Country
    get() {
        Country.entries.forEach {
            if (it.cCode == countryCode) return it
        }
        return Country.RUSSIAN_FEDERATION
    }

val UserRegDto.birthMonth: Month
    get() {
        Month.entries.forEach {
            if (it.mCode == birthMonthCode) return it
        }
        return Month.JANUARY
    }

val UserRegDto.gender: Gender
    get() = if (genderCode == 0) Gender.MALE else Gender.FEMALE