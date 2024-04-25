package ru.openunity.hunterhint.ui.personal

import ru.openunity.hunterhint.models.User

data class PersonalUiState(
    val user: User = User(),
    val currentSection: PersonalSections = PersonalSections.SETTINGS,
    val isLoggedIn: Boolean = true
) {

}
