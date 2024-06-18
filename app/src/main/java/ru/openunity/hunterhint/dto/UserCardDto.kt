package ru.openunity.hunterhint.dto

data class UserCardDto(
    val name: String = "",
    val lastName: String = "",
    val phoneNumber: String = "",
    val email: String = "",
    val accessLevel: Int = 0,
    val photoUrl: String = "",
    val status: Int = 0
)