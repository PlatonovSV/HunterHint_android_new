package ru.openunity.hunterhint.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.openunity.hunterhint.dto.UserDto
import ru.openunity.hunterhint.ui.enums.AccessLevels

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val jwt: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = "",
    val gender: Int = 0,
    val countryCode: Int = 0,
    val photoUrl: String = "",
    val roleCode: Int = 0
) {
    companion object {
        fun updateWithDto(user: User, dto: UserDto) = user.copy(
            name = dto.name,
            lastName = dto.lastName,
            email = dto.email,
            phoneNumber = dto.phoneNumber,
            dateOfBirth = dto.dateOfBirth,
            gender = dto.gender,
            countryCode = dto.countryCode,
            photoUrl = dto.profilePhotoUrl,
            roleCode = dto.accessLevel
        )


    }

    val isHunter: Boolean
        get() = roleCode == AccessLevels.HUNTER.code

    val isOwner: Boolean
        get() = roleCode == AccessLevels.OWNER.code

    val isAdmin: Boolean
        get() = roleCode == AccessLevels.ADMIN.code

    val role: AccessLevels
        get() {
            return AccessLevels.entries.find { it.code == this.roleCode } ?: AccessLevels.HUNTER
        }
}





