package ru.openunity.hunterhint.dto

data class AuthResponseDto(
    val userId: Long,
    val jwt: String,
    val isAuthorizationSuccessful: Boolean
)