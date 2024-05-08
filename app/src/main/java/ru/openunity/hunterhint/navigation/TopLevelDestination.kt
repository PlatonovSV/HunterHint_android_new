package ru.openunity.hunterhint.navigation

/**
 * enum values that represent the screens in the app
 */
enum class AppScreen(val route: String) {
    Search(Search.name),
    GroundsPage("${GroundsPage.name}/{groundsId}"),
    RegName(RegName.name),
    RegDate(RegDate.name),
    RegEmail(RegEmail.name),
    RegPhone("${RegPhone.name}/{countryId}"),
    RegPhoneCode(RegPhoneCode.name),
    RegPassword(RegPassword.name),
    RegCompletion(RegCompletion.name),
    AuthPhone("${AuthPhone.name}/{countryId}"),
    AuthPhoneCode(AuthPhoneCode.name),
    AuthPassword(AuthPassword.name),
    Personal(Personal.name),
    Booking("${Booking.name}/{offersId}")
}