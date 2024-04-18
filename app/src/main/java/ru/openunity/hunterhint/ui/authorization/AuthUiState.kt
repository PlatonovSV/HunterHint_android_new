package ru.openunity.hunterhint.ui.authorization

import ru.openunity.hunterhint.R
import ru.openunity.hunterhint.dto.AuthRequestDto
import ru.openunity.hunterhint.ui.State
import ru.openunity.hunterhint.ui.Success
import ru.openunity.hunterhint.ui.registration.Confirmation

data class AuthUiState(
    val phoneConfirmation: Confirmation = Confirmation(),
    val authRequestDto: AuthRequestDto = AuthRequestDto(),
    val isPhoneStored: Boolean = true,
    val isPasswordShow: Boolean = true,
    val isPasswordGood: Boolean = true,
    val state: State = Success(R.string.empty),
    var isConfirmationValid: Boolean = true,
    val isAuthSuccess: Boolean = false,
)
