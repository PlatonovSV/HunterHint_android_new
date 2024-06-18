package ru.openunity.hunterhint.dto

import ru.openunity.hunterhint.ui.registration.Country
import ru.openunity.hunterhint.ui.registration.Gender


data class UserDto(
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val gender: Int = 0,
    val countryCode: Int = 0,
    val profilePhotoUrl: String = "",
    val accessLevel: Int = 0
)

fun getGenderByCode(gCode: Int) = if (gCode == 0) Gender.MALE else Gender.FEMALE

fun getCountryByCode(cCode: Int): Country {
    for (country in Country.entries) {
        if (country.cCode == cCode) {
            return country
        }
    }
    return Country.RUSSIAN_FEDERATION
}