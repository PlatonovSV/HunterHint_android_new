package ru.openunity.hunterhint.ui.registration

data class RegUiState(
    val isNameCorrect: Boolean = true,
    val isLastNameCorrect: Boolean = true,
    val isDataCorrect: Boolean = true,
    val isBirthdayComplete: Boolean = true,
    val isBirthdayCorrect: Boolean = true,
    val isMonthDialogShow: Boolean = false,
    val isGenderDialogShow: Boolean = false,
    val isGenderSpecified: Boolean = true,
    val isEmailCorrect: Boolean = true,
    val isConfirmationRequested: Boolean = false,
    val isCodeCorrect: Boolean = false,
    val isPhoneWrong: Boolean = false
)