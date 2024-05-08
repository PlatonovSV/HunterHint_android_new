package ru.openunity.hunterhint.ui.registration

import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.dto.UserRegDto
import ru.openunity.hunterhint.ui.State
import ru.openunity.hunterhint.ui.Success

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
    val isPhoneWrong: Boolean = false,
    val isPasswordShow: Boolean = true,
    val isPasswordStrong: Boolean = true,
    val userRegDto: UserRegDto = UserRegDto(),
    val isPhoneStored: Boolean = false,
    val isEmailStored: Boolean = false,
    val state: State = Success(R.string.empty),

    val phoneConfirmation: Confirmation = Confirmation(),
    val emailConfirmation: Confirmation = Confirmation()
)

