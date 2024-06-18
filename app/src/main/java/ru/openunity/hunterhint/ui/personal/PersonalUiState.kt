package ru.openunity.hunterhint.ui.personal

import android.net.Uri
import ru.openunity.hunterhint.dto.UserCardDto
import ru.openunity.hunterhint.models.BookingCard
import ru.openunity.hunterhint.models.database.User
import ru.openunity.hunterhint.ui.components.ComponentLoading
import ru.openunity.hunterhint.ui.components.ComponentState
import ru.openunity.hunterhint.ui.components.ComponentWait
import ru.openunity.hunterhint.ui.enums.AccessLevels

data class PersonalUiState(
    val user: User = User(),
    val currentSection: PersonalSections = PersonalSections.SETTINGS,
    val isLoggedIn: Boolean = true,
    val bookingCards: List<BookingCard> = listOf(),
    val bookingState: ComponentState = ComponentLoading,

    val newUserProfilePicture: Uri? = null,
    val updateUploading: ComponentState = ComponentWait,

    val foundByAdmin: List<UserCard> = listOf(),
    val userSearchState: ComponentState = ComponentWait,

    val showBottomSheet: Boolean = false,
    val bottomSheetUserCard: UserCard = UserCard(),

    val userNameByAdmin: String = "",
    val userLastNameByAdmin: String = "",

    val editStatus: Boolean = true,

    val newName: String = "",
    val newLastName: String = "",
    val newEmail: String = "",
    val newPhoneNumber: String = "",
    val oldPassword: String = "",
    val newPassword: String = "",
    val isOldPasswordShow: Boolean = true,
    val isNewPasswordShow: Boolean = true,
)

data class UserCard(
    val userId: Long = -1,
    val dto: UserCardDto = UserCardDto(),
    val state: ComponentState = ComponentLoading
)


fun getRole(code: Int) = AccessLevels.entries.find { it.code == code } ?: AccessLevels.HUNTER

