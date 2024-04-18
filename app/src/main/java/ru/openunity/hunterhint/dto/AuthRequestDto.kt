package ru.openunity.hunterhint.dto

import ru.openunity.hunterhint.ui.registration.Country

data class AuthRequestDto(
    val countryCode: Int = Country.RUSSIAN_FEDERATION.cCode,
    val phoneNumber: String = "",
    val password: String = ""
)

val AuthRequestDto.country: Country
    get() {
        Country.entries.forEach {
            if (it.cCode == countryCode) return it
        }
        return Country.RUSSIAN_FEDERATION
    }